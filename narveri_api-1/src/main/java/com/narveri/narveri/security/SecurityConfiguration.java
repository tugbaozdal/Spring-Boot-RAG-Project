package com.narveri.narveri.security;

import  com.narveri.narveri.constant.PrivilegeConstant;
import com.narveri.narveri.constant.SecurityConstants;
import com.narveri.narveri.security.jwt.JwtAuthenticationEntryPoint;
import com.narveri.narveri.security.jwt.JwtAuthorizationFilter;
import com.narveri.narveri.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

//fronend için eklenenler

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {


    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private AuthenticationConfiguration configuration;

    @Autowired
    private TokenProvider tokenProvider;



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.cors(withDefaults()).csrf(csrf->csrf.disable());
        http.csrf(csrf -> csrf.disable());//yeni

        http .authorizeHttpRequests(
                        auth->{
                            auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();//yeni
                            auth.requestMatchers("/api/auth/login","/api/auth/register","/api/public/**").permitAll();
                            auth.requestMatchers("/api/admin/**").hasAuthority(PrivilegeConstant.ADMIN);
                            auth.anyRequest().authenticated();
                        })
                .exceptionHandling(exception->exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .addFilterBefore(new JwtAuthorizationFilter(configuration.getAuthenticationManager(),tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session ->session.sessionCreationPolicy(STATELESS));

        return  http.build();
    }




    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> {
         //   web.ignoring().requestMatchers(HttpMethod.OPTIONS, "/**");
            web.ignoring().requestMatchers(SecurityConstants.IGNORE_SWAGGER);
            web.ignoring().requestMatchers( SecurityConstants.IGNORE_OTHERS);
        });
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManagerProvider(UserDetailsService detailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(detailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    //frontend için eklenenler
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:5173"); // VITE
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }


}