package net.serfozo.fibonacci.config;

import net.serfozo.fibonacci.core.security.CustomJdbcDaoImpl;
import net.serfozo.fibonacci.core.security.FibonacciAuthenticationFailureHandler;
import net.serfozo.fibonacci.core.security.FibonacciAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import javax.inject.Inject;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Inject
    private DataSource dataSource;

    //for sake of simplicity
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public UserDetailsManager userDetailsManager() {
        final CustomJdbcDaoImpl customJdbcDao = new CustomJdbcDaoImpl();
        customJdbcDao.setDataSource(dataSource);
        return customJdbcDao;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            .and()
                .formLogin()
                    .loginProcessingUrl("/login")
                    .successHandler(new FibonacciAuthenticationSuccessHandler())
                    .failureHandler(new FibonacciAuthenticationFailureHandler())
            .and()
                .logout()
                    .logoutUrl("/logout")
            .and()
                .authorizeRequests()
                    .anyRequest()
                        .permitAll()
            .and()
                .csrf()
                    .disable();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsManager());
    }
}
