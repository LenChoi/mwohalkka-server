package com.wedo.mwohalkka.server.admin.config

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
class SecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/login", "/actuator/health").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .defaultSuccessUrl("/")
            .and()
            .exceptionHandling().accessDeniedPage("/error-forbidden")
    }

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers(
            "/favicon.ico", "/js/**", "/css/**", "/font-awesome/**",
            "/img/**", "/fonts/**", "/vendor/**", "/error"
        )
    }
}
