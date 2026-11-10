package za.ac.mycput.studentregistrationsystembackend.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.mycput.studentregistrationsystembackend.Domain.AdminLog;
import java.util.List;
public interface AdminLogRepository extends JpaRepository<AdminLog,Long> {
    List<AdminLog> findTop200ByOrderByOccurredAtDescIdDesc();
}
