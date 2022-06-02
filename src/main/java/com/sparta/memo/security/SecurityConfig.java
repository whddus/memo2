package com.sparta.memo.security;

import com.sparta.memo.jwt.JwtSecurityConfig;
import com.sparta.memo.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

//===================================================
// @EnableWebSecurity: 기본적인 Web 보안을 활성화 하는 애노테이션이다.
// 추가적인 설정을 하려면 WebSecurityConfigurer 를 implements 하거나
// WebSecurityConfigurerAdapter 를 extends 하여 사용해야 한다.
// debug = true 로 설정하면 시큐리티 디버깅이 가능하다. ( chain 확인 가능 ) -> 확인해보기
@EnableWebSecurity
//===================================================

//===================================================
// @PreAuthorize 애노테이션을 메소드 단위로 추가하기 위햐서 사용
@EnableGlobalMethodSecurity(prePostEnabled = true)
//===================================================
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 토큰을 생성하고 유효성 검사하는 객체
    private final TokenProvider tokenProvider;
    private final CorsFilter corsFilter;
    // 로그인 안 하고 접근하면 401 에러를 반환하는 객체

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        // h2-console 사용에 대한 허용 (CSRF, FrameOptions 무시)
        web
                .ignoring()
                .antMatchers("/h2-console/**");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        // 서버에서 인증은 JWT로 인증하기 때문에 Session의 생성을 막습니다.
        http
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class);

        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                //=============================================================
                // enable h2-console
          http
                .headers()
                .frameOptions()
                .sameOrigin()

                //===================================================

                //===================================================
                // authorizeRequests: HttpServletRequest 를 사용하는 요청들에 대한 접근제한 설정
                // permitAll(): 인증(로그인)을 받지 않아도 접근 가능하도록 설정
                .and()
                .authorizeRequests()
                .antMatchers("/api/login").permitAll() // 로그인 API 요청 허용
                .antMatchers("/user/signup").permitAll() // 회원가입 API 요청 허용

                //===================================================

                //===================================================
                // anyRequest(): 이외의 모든 요청
                // authenticated(): 인증(로그인)을 받아야 접근할 수 있도록 설정
                .anyRequest().authenticated()
                //===================================================

                //===================================================
                // JwtFilter 를 addFilterBefore 로 등록했던 JwtSecurityConfig 클래스 적용
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));

        //===================================================
    }
}
