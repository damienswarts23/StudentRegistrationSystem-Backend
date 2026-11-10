package za.ac.mycput.studentregistrationsystembackend.SecurityTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import za.ac.mycput.studentregistrationsystembackend.Domain.*;
import za.ac.mycput.studentregistrationsystembackend.Service.*;
import za.ac.mycput.studentregistrationsystembackend.Repository.*;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
class LecturerAccountServiceTest {
 private final LecturerService lecturers=mock(LecturerService.class);
 private final LecturerRepository records=mock(LecturerRepository.class);
 private final AppUserRepository users=mock(AppUserRepository.class);
 private final BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
 private final LecturerAccountService service=new LecturerAccountService(lecturers,records,users,encoder);
 private Lecturer lecturer() {
  Lecturer l=mock(Lecturer.class);when(l.getPersonId()).thenReturn(14);
  when(l.getLecturerEmail()).thenReturn("mbalib@cput.ac.za");when(l.getFirstName()).thenReturn("B");when(l.getLastName()).thenReturn("Mbali");return l;
 }
 @Test void createsLinkedHashedLoginTogetherWithLecturer() {
  Lecturer input=lecturer();when(lecturers.create(input)).thenReturn(input);
  when(users.findByUsernameIgnoreCase("mbalib@cput.ac.za")).thenReturn(Optional.empty());
  assertSame(input,service.create(input,"Lecturer@2026!"));
  var saved=org.mockito.ArgumentCaptor.forClass(AppUser.class);verify(users).saveAndFlush(saved.capture());
  assertEquals(Role.LECTURER,saved.getValue().getRole());assertEquals(14,saved.getValue().getLinkedId());
  assertEquals("mbalib@cput.ac.za",saved.getValue().getUsername());
  assertTrue(encoder.matches("Lecturer@2026!",saved.getValue().getPassword()));
 }
 @Test void enablesExistingRecordWithoutDuplicatingLecturer() {
  doReturn(Optional.of(lecturer())).when(records).findById(14);
  when(users.findByUsernameIgnoreCase("mbalib@cput.ac.za")).thenReturn(Optional.empty());
  assertEquals("mbalib@cput.ac.za",service.enable(14,"Lecturer@2026!"));
  verifyNoInteractions(lecturers);verify(users).saveAndFlush(any(AppUser.class));
 }
 @Test void existingLoginIsNeverOverwritten() {
  doReturn(Optional.of(lecturer())).when(records).findById(14);
  when(users.findByUsernameIgnoreCase("mbalib@cput.ac.za")).thenReturn(Optional.of(new AppUser()));
  assertThrows(ResponseStatusException.class,()->service.enable(14,"Lecturer@2026!"));
  verify(users,never()).saveAndFlush(any());
 }
 @Test void weakPasswordCannotCreateRecord() {
  assertThrows(ResponseStatusException.class,()->service.create(lecturer(),"123"));verifyNoInteractions(lecturers,users);
 }
}
