package com.sparta.memo.controller;

import com.sparta.memo.domain.User;
import com.sparta.memo.dto.LoginDto;
import com.sparta.memo.dto.TokenDto;
import com.sparta.memo.jwt.JwtFilter;
import com.sparta.memo.jwt.TokenProvider;
import com.sparta.memo.service.UserService;
import com.sparta.memo.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginController {

    private final TokenProvider tokenProvider;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> authorize(@RequestBody LoginDto loginDto) {

        String username = loginDto.getUsername();
        String password = loginDto.getPassword();

        Optional<String> currentUsername = SecurityUtil.getCurrentUsername();
        if (currentUsername.isPresent() && !currentUsername.get().equals("anonymousUser")) {
            throw new IllegalArgumentException("이미 로그인이 되어있습니다.");
        }

        Optional<User> found = userService.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("닉네임 또는 패스워드를 확인해주세요.");
        }

        if (!passwordEncoder.matches(password, found.get().getPassword())) {
            throw new IllegalArgumentException("닉네임 또는 패스워드를 확인해주세요.");
        }
        //====================================================================

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        // authenticationToken 을 이용해서 authenticate() 메소드를 호출하면
        // UserDetailsService 의 loadUserByUsername() 이 호출되고
        // 그 결과를 가지고 authentication 객체를 생성한다.
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 시큐리티 컨텍스트에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 인증 정보를 가지고 JWT Token 을 생성한다.
        String jwt = tokenProvider.createToken(authentication);

        // 토큰을 헤더에 저장
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }
}
