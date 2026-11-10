package za.ac.mycput.studentregistrationsystembackend.SecurityTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import za.ac.mycput.studentregistrationsystembackend.Domain.*;
import za.ac.mycput.studentregistrationsystembackend.Service.StudentAccountService;
import za.ac.mycput.studentregistrationsystembackend.Repository.*;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
class StudentAccountServiceTest {
 private final StudentRepository records=mock(StudentRepository.class);
 private final AppUserRepository users=mock(AppUserRepository.class);
 private final BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
 private final StudentAccountService service=new StudentAccountService(records,users,encoder);
 private Student student() {
  Student s=mock(Student.class);Applicant a=mock(Applicant.class);
  when(s.getStudentId()).thenReturn(12);when(s.getStudentEmail()).thenReturn("202600012@mycput.ac.za");
  when(s.getApplicant()).thenReturn(a);when(a.getPersonId()).thenReturn(37);
  when(a.getFirstName()).thenReturn("Linton");when(a.getLastName()).thenReturn("Cloete");return s;
 }
 @Test void activationStoresHashedPasswordAndCorrectStudentId() {
  doReturn(Optional.of(student())).when(records).findById(12);
  when(users.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.empty());
  assertEquals("202600012@mycput.ac.za",service.enable(12,"Student@2026!"));
  var saved=org.mockito.ArgumentCaptor.forClass(AppUser.class);verify(users).saveAndFlush(saved.capture());
  assertEquals(Role.STUDENT,saved.getValue().getRole());assertEquals(Integer.valueOf(12),saved.getValue().getLinkedId());
  assertTrue(encoder.matches("Student@2026!",saved.getValue().getPassword()));
  verify(records,never()).save(any());
 }
 @Test void existingPasswordCannotBeOverwritten() {
  doReturn(Optional.of(student())).when(records).findById(12);
  when(users.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.of(new AppUser()));
  assertThrows(ResponseStatusException.class,()->service.enable(12,"Student@2026!"));
  verify(users,never()).saveAndFlush(any());
 }
 @Test void anonymousActivationRequiresMatchingApplicationSession() {
  var access=new za.ac.mycput.studentregistrationsystembackend.Security.AccountAccess(users,records,
   mock(LecturerRepository.class),mock(RegistrationRepository.class),mock(ClassRepository.class));
  doReturn(Optional.of(student())).when(records).findById(12);
  var request=new org.springframework.mock.web.MockHttpServletRequest();
  org.springframework.web.context.request.RequestContextHolder.setRequestAttributes(new org.springframework.web.context.request.ServletRequestAttributes(request));
  try {
   assertFalse(access.canActivateStudent(12));
   request.getSession().setAttribute("newApplicantId",99);assertFalse(access.canActivateStudent(12));
   request.getSession().setAttribute("newApplicantId",37);assertTrue(access.canActivateStudent(12));
  } finally {org.springframework.web.context.request.RequestContextHolder.resetRequestAttributes();}
 }
 @Test void adminResetUpdatesOnlyMatchingStudentAccount() {
  var account=new AppUser("202600012@mycput.ac.za",encoder.encode("OldPassword1"),Role.STUDENT,"Linton",12);
  doReturn(Optional.of(student())).when(records).findById(12);
  when(users.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.of(account));
  service.reset(12,"NewPassword2026!");
  assertTrue(encoder.matches("NewPassword2026!",account.getPassword()));verify(records,never()).save(any());
 }
 @Test void resetRejectsEmailBelongingToDifferentAccount() {
  doReturn(Optional.of(student())).when(records).findById(12);
  when(users.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.of(new AppUser("202600012@mycput.ac.za","hash",Role.ADMIN,"Admin",null)));
  assertThrows(ResponseStatusException.class,()->service.reset(12,"NewPassword2026!"));verify(users,never()).saveAndFlush(any());
 }
}
