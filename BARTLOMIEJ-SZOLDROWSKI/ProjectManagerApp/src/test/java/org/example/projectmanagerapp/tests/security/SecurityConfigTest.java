package org.example.projectmanagerapp.tests.security;

import org.example.projectmanagerapp.security.SecurityConfig;
import org.example.projectmanagerapp.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SecurityConfigTest {

    @Test
    void shouldRegisterSecurityBeans() throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class);

        assertNotNull(context.getBean(PasswordEncoder.class));
        assertNotNull(context.getBean(DaoAuthenticationProvider.class));
        assertNotNull(context.getBean(AuthenticationManager.class));

        PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
        assertTrue(encoder.matches("test123", encoder.encode("test123")));

        context.close();
    }

    @Configuration
    @Import(SecurityConfig.class)
    static class TestConfig {

        @Bean
        public UserDetailsServiceImpl userDetailsService() {
            return mock(UserDetailsServiceImpl.class);
        }
    }
}
