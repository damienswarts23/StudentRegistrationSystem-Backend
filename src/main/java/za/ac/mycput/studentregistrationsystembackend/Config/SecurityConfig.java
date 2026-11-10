package za.ac.mycput.studentregistrationsystembackend.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpMethod;
import za.ac.mycput.studentregistrationsystembackend.Service.CustomUserDetailsService;
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
 private final CustomUserDetailsService userDetailsService;
 public SecurityConfig(CustomUserDetailsService userDetailsService) {
  this.userDetailsService=userDetailsService;
 }
 @Bean public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
 // Swing opens the role-specific dashboard after reading /api/auth/me.
 // An HTTP 204 replaces the HTML redirect used by the lecturer's web client.
 @Bean public org.springframework.security.web.authentication.AuthenticationSuccessHandler loginSuccessHandler() {
  return (request,response,authentication)->response.setStatus(204);
 }
 @Bean public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
  // CSRF remains enabled. Swing obtains a token and retains its session cookie.
  http.userDetailsService(userDetailsService)
   .authorizeHttpRequests(a->a.requestMatchers("/api/auth/csrf", "/api/auth/login", "/api/auth/logout", "/error").permitAll()
    .requestMatchers(HttpMethod.GET, "/api/courses/course-list", "/api/courses/department/*/course-list",
        "/api/departments/department-list", "/api/classes/course/*/class-list", "/api/applications/result-text").permitAll()
    .requestMatchers(HttpMethod.POST, "/api/applicants", "/api/applications", "/api/students/*/login-account").permitAll()
    // Like the lecturer's /admin/** matcher, these routes are admin-only.
    .requestMatchers("/api/admin-log", "/api/auth/accounts", "/api/auth/profile").hasRole("ADMIN")
    .requestMatchers("/api/departments/**", "/api/courses/**", "/api/classes/**").hasRole("ADMIN")
    // Shared account endpoints are available to each signed-in role.
    .requestMatchers("/api/auth/**").authenticated()
    // Controller method checks further restrict each role to authorized records.
    .requestMatchers("/api/**").authenticated().anyRequest().denyAll())
   .formLogin(l->l.loginProcessingUrl("/api/auth/login")
    .successHandler(loginSuccessHandler())
    .failureHandler((q,s,e)->{s.setStatus(401);s.setContentType("text/plain");s.getWriter().write("Incorrect username or password.");}))
   .logout(l->l.logoutUrl("/api/auth/logout").invalidateHttpSession(true).deleteCookies("JSESSIONID")
    .logoutSuccessHandler((q,s,a)->s.setStatus(204)))
   .exceptionHandling(e->e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
    .accessDeniedHandler((q,s,x)->{s.setStatus(403);s.setContentType("text/plain");s.getWriter().write("You do not have permission, or your session has expired. Sign in again if needed.");}));
  return http.build();
 }
}
