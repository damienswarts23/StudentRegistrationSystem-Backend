package za.ac.mycput.studentregistrationsystembackend.Config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.method.HandlerMethod;
import za.ac.mycput.studentregistrationsystembackend.Domain.AdminLog;
import za.ac.mycput.studentregistrationsystembackend.Domain.Role;
import za.ac.mycput.studentregistrationsystembackend.Repository.AdminLogRepository;
import za.ac.mycput.studentregistrationsystembackend.Security.AccountAccess;

import java.util.Set;

@Configuration
public class AdminLogConfig implements WebMvcConfigurer {
    private final AccountAccess access;
    private final AdminLogRepository logs;

    public AdminLogConfig(AccountAccess access, AdminLogRepository logs) {
        this.access = access;
        this.logs = logs;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                if (handler instanceof HandlerMethod && Set.of("POST", "PUT", "PATCH", "DELETE").contains(request.getMethod())) {
                    var admin = access.current();
                    if (admin != null && admin.getRole() == Role.ADMIN) {
                        // Snapshot before a name/password change; never copy credentials or request bodies.
                        request.setAttribute("auditAdminName", admin.getDisplayName());
                        request.setAttribute("auditAdminUsername", admin.getUsername());
                    }
                }
                return true;
            }

            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView view) {
                Object username = request.getAttribute("auditAdminUsername");
                if (username == null || response.getStatus() < 200 || response.getStatus() >= 300 || !(handler instanceof HandlerMethod method))
                    return;
                String resource = request.getRequestURI();
                String area = method.getBeanType().getSimpleName().replace("Controller", "");
                String operation = method.getMethod().getName().replaceAll("([a-z])([A-Z])", "$1 $2");
                String verb = switch (request.getMethod()) {
                    case "DELETE" -> "Deleted";
                    case "PUT", "PATCH" -> "Updated";
                    default -> operation.startsWith("create") ? "Created" : "Updated";
                };
                logs.save(new AdminLog(String.valueOf(request.getAttribute("auditAdminName")), username.toString(),
                        verb + " · " + area + " · " + operation, resource));
            }
        }).addPathPatterns("/api/**").excludePathPatterns("/api/auth/login", "/api/auth/logout", "/api/admin-log");
    }
}
