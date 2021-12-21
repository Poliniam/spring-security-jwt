package com.javamaster.springsecurityjwt.config;

import com.javamaster.springsecurityjwt.config.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // класс является классом настроек Spring Security
public class SecurityConfig extends WebSecurityConfigurerAdapter { //данный класс позволяет настроить всю систему секюрити и авторизации под свои нужды.

    @Autowired
    private JwtFilter jwtFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .cors().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        //которая будет управлять сессией юзера в системе спринг секюрити. Так как я буду авторизировать пользователя по токену, мне не нужно создавать и хранить для него сессию. Поэтому я указал STATELESS.
                .and()
                .authorizeRequests()
                .antMatchers("/admin/*").hasRole("ADMIN")
                .antMatchers("/user/*").hasRole("USER")
                .antMatchers("/register", "/auth", "/articles/{id}/comments",
                        "/auth/confirm/{hash_code}", "games", "/object", "/object/{id}",
                        "/users/{id}/comments", "/users/{id}/comments/{idComm}", "/filterByGames", "/rating/{id}",
                        "/forgetPassword", "/forgetPassword/confirm/{hash_code}", "/newPassword", "/filterByGames/{gameName}", "/fuser/object"

                        ).permitAll()
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
