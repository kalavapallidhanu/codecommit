package com.impactsure.artnook.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.impactsure.artnook.configs.CognitoAuthenticationEntryPoint;
import com.impactsure.artnook.configs.jwt.JWTAuthenticationFilter;
import com.impactsure.artnook.configs.jwt.JWTLoginFilter;
import com.impactsure.artnook.configs.jwt.JwtAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {	

	@Autowired
	private JwtAuthenticationProvider jwtAuthenticationProvider;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() 
        .authorizeRequests()
        .antMatchers("/artnook/","/","/artnook","/artnook/index","/artnook/login","/artnook/new-pwd", "/artnook/fp", "/artnook/privacy",  "/artnook/terms", "/artnook/cfp", "/artnook/fph","/artnook/terms-conditions").permitAll()
        .anyRequest().authenticated()
        .and().exceptionHandling() .accessDeniedPage("/artnook/login").and().logout().logoutSuccessUrl("/login").permitAll().and().exceptionHandling().authenticationEntryPoint(new CognitoAuthenticationEntryPoint()).and()
        .addFilterBefore(new JWTLoginFilter("/artnook/doLogin", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

  @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
	  auth.authenticationProvider(jwtAuthenticationProvider);
    }

    @Override
	public void configure(WebSecurity web) throws Exception {
	    web
	       .ignoring()
	       .antMatchers("/artnook/jwks.json","/jwks.json","/artnook.pdf","/artnook/artnook.pdf", "/artnook/terms-conditions","/resources/**", "/static/**", "/css/**", "/js/**", "/images/**","/artnook/resources/**", "/artnook/static/**", "/artnook/css/**", "/artnook/js/**","/artnook/img/**","/artnook/vendor/**");
	}


}
