package TASK_FLOW.SecurityConfig;

import TASK_FLOW.Service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .userDetailsService(userDetailsService) // ✅ NOW VALID

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/tasks/register", "/index.html").permitAll()
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .defaultSuccessUrl("/index.html", true)
                        .permitAll()
                )

                .logout(logout -> logout.permitAll());

        return http.build();
    }
}