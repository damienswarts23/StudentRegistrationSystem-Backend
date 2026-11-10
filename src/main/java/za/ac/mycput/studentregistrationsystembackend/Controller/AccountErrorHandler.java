package za.ac.mycput.studentregistrationsystembackend.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.ResponseEntity;
@RestControllerAdvice(assignableTypes={AccountController.class,LecturerController.class,StudentController.class})
public class AccountErrorHandler {
 @ExceptionHandler(ResponseStatusException.class)
 public ResponseEntity<String> handle(ResponseStatusException e) {return ResponseEntity.status(e.getStatusCode()).body(e.getReason());}
}
