package za.ac.mycput.studentregistrationsystembackend.SecurityTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import za.ac.mycput.studentregistrationsystembackend.Repository.AppUserRepository;
import za.ac.mycput.studentregistrationsystembackend.Domain.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
@SpringBootTest(properties={
 "spring.datasource.url=jdbc:h2:mem:security;MODE=MySQL;DB_CLOSE_DELAY=-1",
 "spring.datasource.driver-class-name=org.h2.Driver", "spring.datasource.username=sa",
 "spring.datasource.password=", "spring.jpa.hibernate.ddl-auto=create-drop",
 "app.admin.username=admin@cput.ac.za", "app.admin.password=Admin@2026!"})
@AutoConfigureMockMvc @Transactional
class AccountSecurityTest {
 @Autowired MockMvc mvc; @Autowired AppUserRepository users; @Autowired PasswordEncoder encoder;
 @Test void storedPasswordIsBcrypt() {
  var u=users.findByUsernameIgnoreCase("admin@cput.ac.za").orElseThrow();
  assertNotEquals("Admin@2026!",u.getPassword());assertTrue(encoder.matches("Admin@2026!",u.getPassword()));
 }
 @Test void wrongPasswordFails() throws Exception {
  mvc.perform(post("/api/auth/login").with(csrf()).param("username","admin@cput.ac.za").param("password","wrong")).andExpect(status().isUnauthorized());
 }
 @Test void loginReturnsOwnNameAndLogoutInvalidatesSession() throws Exception {
  var result=mvc.perform(post("/api/auth/login").with(csrf()).param("username","admin@cput.ac.za").param("password","Admin@2026!")).andExpect(status().isNoContent()).andReturn();
  var session=(MockHttpSession)result.getRequest().getSession(false);
  assertNotNull(session);
  mvc.perform(get("/api/auth/me").session(session)).andExpect(status().isOk()).andExpect(content().string(org.hamcrest.Matchers.containsString("Damien Swarts")));
  mvc.perform(post("/api/auth/logout").session(session).with(csrf())).andExpect(status().isNoContent());
  assertTrue(session.isInvalid());
 }
 @Test void anonymousCannotReadStudents() throws Exception {mvc.perform(get("/api/students/student-list")).andExpect(status().isUnauthorized());}
 @Test void studentCannotReadAdminList() throws Exception {mvc.perform(get("/api/students/student-list").with(user("student").roles("STUDENT"))).andExpect(status().isForbidden());}
 @Test void lecturerCannotDeleteStudent() throws Exception {mvc.perform(delete("/api/students/1").with(user("lecturer").roles("LECTURER")).with(csrf())).andExpect(status().isForbidden());}
 @Test void studentCannotReadAnotherStudent() throws Exception {
  users.save(new AppUser("student@test.co.za",encoder.encode("Student@2026"),Role.STUDENT,"Student",1));
  mvc.perform(get("/api/students/2/personal-details-text").with(user("student@test.co.za").roles("STUDENT"))).andExpect(status().isForbidden());
 }
 @Test void profileChangeNeedsCsrf() throws Exception {mvc.perform(post("/api/auth/profile").with(user("admin@cput.ac.za").roles("ADMIN")).param("displayName","Updated Name")).andExpect(status().isForbidden());}
 @Test void namePersistsOnOwnAccountOnly() throws Exception {
  users.save(new AppUser("second@test.co.za",encoder.encode("Admin@2026!"),Role.ADMIN,"Second Admin",null));
  mvc.perform(post("/api/auth/profile").with(user("admin@cput.ac.za").roles("ADMIN")).with(csrf()).param("displayName","Updated Name")).andExpect(status().isOk());
  assertEquals("Updated Name",users.findByUsernameIgnoreCase("admin@cput.ac.za").orElseThrow().getDisplayName());
  assertEquals("Second Admin",users.findByUsernameIgnoreCase("second@test.co.za").orElseThrow().getDisplayName());
 }
 @Test void photoPersistsAndMalformedPhotoRejected() throws Exception {
  var image=new java.awt.image.BufferedImage(24,24,java.awt.image.BufferedImage.TYPE_INT_RGB);
  var output=new java.io.ByteArrayOutputStream();javax.imageio.ImageIO.write(image,"png",output);
  mvc.perform(post("/api/auth/photo").with(user("admin@cput.ac.za").roles("ADMIN")).with(csrf()).contentType("application/octet-stream").content(output.toByteArray())).andExpect(status().isOk());
  assertNotNull(users.findByUsernameIgnoreCase("admin@cput.ac.za").orElseThrow().getPhoto());
  mvc.perform(post("/api/auth/photo").with(user("admin@cput.ac.za").roles("ADMIN")).with(csrf()).content("not an image")).andExpect(status().isBadRequest());
 }
 @Test void passwordRequiresCurrentPassword() throws Exception {
  mvc.perform(post("/api/auth/password").with(user("admin@cput.ac.za").roles("ADMIN")).with(csrf()).param("currentPassword","wrong").param("newPassword","Different@2026")).andExpect(status().isBadRequest());
 }
 @Test void studentCannotCreateAdminAccount() throws Exception {
  mvc.perform(post("/api/auth/accounts").with(user("student").roles("STUDENT")).with(csrf()).param("role","ADMIN").param("username","new@test.co.za").param("displayName","New Admin").param("password","Other@2026!")).andExpect(status().isForbidden());
 }
 @Test void applicationResultCannotBeLookedUpAnonymouslyByEmail() throws Exception {
  mvc.perform(get("/api/applications/result-text").param("email","someone@test.co.za")).andExpect(status().isUnauthorized());
 }

 @Autowired za.ac.mycput.studentregistrationsystembackend.Repository.AdminLogRepository logs;
 @Test void adminCannotCreateStudentAccountInSettings() throws Exception {
  mvc.perform(post("/api/auth/accounts").with(user("admin@cput.ac.za").roles("ADMIN")).with(csrf())
   .param("role","STUDENT").param("password","Student@2026!"))
   .andExpect(status().isBadRequest());
 }
 @Test void successfulAdminChangeIsSharedWithOtherAdmins() throws Exception {
  users.save(new AppUser("other@test.co.za",encoder.encode("Other@2026!"),Role.ADMIN,"Other Admin",null));
  mvc.perform(post("/api/auth/profile").with(user("admin@cput.ac.za").roles("ADMIN")).with(csrf())
   .param("displayName","Updated Admin")).andExpect(status().isOk());
  mvc.perform(get("/api/admin-log").with(user("other@test.co.za").roles("ADMIN")))
   .andExpect(status().isOk()).andExpect(content().string(org.hamcrest.Matchers.containsString("admin@cput.ac.za")))
   .andExpect(content().string(org.hamcrest.Matchers.containsString("/api/auth/profile")));
 }
 @Test void failedChangeDoesNotAddLogEntry() throws Exception {
  long before=logs.count();
  mvc.perform(post("/api/auth/profile").with(user("admin@cput.ac.za").roles("ADMIN")).with(csrf())
   .param("displayName", "")).andExpect(status().isBadRequest());
  assertEquals(before,logs.count());
 }
 @Test void studentCannotReadAdminHistory() throws Exception {
  mvc.perform(get("/api/admin-log").with(user("student").roles("STUDENT"))).andExpect(status().isForbidden());
 }
 @Test void adminCreationIsLoggedWithoutPassword() throws Exception {
  mvc.perform(post("/api/auth/accounts").with(user("admin@cput.ac.za").roles("ADMIN")).with(csrf())
   .param("role","ADMIN").param("username","newadmin@test.co.za").param("displayName","New Admin")
   .param("password","Private@2026!")).andExpect(status().isOk());
  mvc.perform(get("/api/admin-log").with(user("admin@cput.ac.za").roles("ADMIN")))
   .andExpect(status().isOk()).andExpect(content().string(org.hamcrest.Matchers.containsString("Created")))
   .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Private@2026!"))));
 }
 @Test void studentCannotEnableLecturerLogin() throws Exception {
  mvc.perform(post("/api/lecturers/14/login-account").with(user("student").roles("STUDENT")).with(csrf())
   .param("password","Lecturer@2026!")).andExpect(status().isForbidden());
 }
 @Test void anonymousCannotEnableStudentWithoutApplicationSession() throws Exception {
  mvc.perform(post("/api/students/12/login-account").with(csrf()).param("password","Student@2026!"))
   .andExpect(status().isUnauthorized());
 }
 @Test void unrelatedStudentCannotEnableAnotherStudent() throws Exception {
  mvc.perform(post("/api/students/12/login-account").with(user("other").roles("STUDENT")).with(csrf())
   .param("password","Student@2026!")).andExpect(status().isForbidden());
 }
 @Test void studentCannotResetAnotherStudentPassword() throws Exception {
  mvc.perform(post("/api/students/12/reset-login-password").with(user("other").roles("STUDENT")).with(csrf())
   .param("password","OtherPassword2026!")).andExpect(status().isForbidden());
 }
 @Test void studentPhotoChangesOnlyTheirOwnAccount() throws Exception {
  var account=users.save(new AppUser("photo@student.test",encoder.encode("Student@2026!"),Role.STUDENT,"Student",12));
  var pic=new java.awt.image.BufferedImage(16,16,java.awt.image.BufferedImage.TYPE_INT_RGB);
  var bytes=new java.io.ByteArrayOutputStream();javax.imageio.ImageIO.write(pic,"png",bytes);
  byte[] adminBefore=users.findByUsernameIgnoreCase("admin@cput.ac.za").orElseThrow().getPhoto();
  mvc.perform(post("/api/auth/photo").with(user(account.getUsername()).roles("STUDENT")).with(csrf())
   .contentType("application/octet-stream").content(bytes.toByteArray())).andExpect(status().isOk());
  assertNotNull(users.findByUsernameIgnoreCase(account.getUsername()).orElseThrow().getPhoto());
  assertArrayEquals(adminBefore,users.findByUsernameIgnoreCase("admin@cput.ac.za").orElseThrow().getPhoto());
 }
 @Test void publicCourseListStillWorksWithoutLogin() throws Exception {
  mvc.perform(get("/api/courses/course-list")).andExpect(status().isOk());
 }
 @Test void lecturerCannotModifyDepartmentThroughFilterChain() throws Exception {
  mvc.perform(delete("/api/departments/1").with(user("lecturer").roles("LECTURER")).with(csrf()))
   .andExpect(status().isForbidden());
 }
 @Test void successfulPasswordChangeStoresHashAndExpiresSession() throws Exception {
  var session=new MockHttpSession();
  mvc.perform(post("/api/auth/password").session(session)
   .with(user("admin@cput.ac.za").roles("ADMIN")).with(csrf())
   .param("currentPassword","Admin@2026!").param("newPassword","Changed@2026!"))
   .andExpect(status().isOk());
  assertTrue(session.isInvalid());
  var hash=users.findByUsernameIgnoreCase("admin@cput.ac.za").orElseThrow().getPassword();
  assertTrue(encoder.matches("Changed@2026!",hash));
  assertFalse(encoder.matches("Admin@2026!",hash));
 }
}
