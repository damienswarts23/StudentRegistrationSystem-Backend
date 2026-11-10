package za.ac.mycput.studentregistrationsystembackend.Controller;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import za.ac.mycput.studentregistrationsystembackend.Repository.AdminLogRepository;

@RestController
@PreAuthorize("hasRole('ADMIN')")
public class AdminLogController {
    private final AdminLogRepository logs;
    public AdminLogController(AdminLogRepository logs){this.logs=logs;}
    @GetMapping(value="/api/admin-log",produces="text/plain")
    public String recent() {
        StringBuilder text=new StringBuilder();
        for(var entry:logs.findTop200ByOrderByOccurredAtDescIdDesc()) {
            text.append(entry.getOccurredAt()).append('\t').append(clean(entry.getAdminName())).append('\t')
                .append(clean(entry.getAdminUsername())).append('\t').append(clean(entry.getAction())).append('\t')
                .append(clean(entry.getResource())).append('\n');
        }
        return text.toString();
    }
    private static String clean(String value){return value.replaceAll("[\\p{Cntrl}]"," ");}
}
