package com.sparta.memo.service;

import com.sparta.memo.domain.User;
import com.sparta.memo.dto.SignupRequestDto;
import com.sparta.memo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

import java.util.Optional;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public boolean isUserValid(String str) {return Pattern.matches("^[A-Za-z0-9]{3,}$", str);}
    public boolean isPassValid(String str) {return Pattern.matches("^.{4,}$", str);}


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    public void registerUser(SignupRequestDto requestDto) {

        // 회원 ID 중복 확인
        String username = requestDto.getUsername();
        String pass = requestDto.getPassword();
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 닉네임입니다.");
        }
        if(!isUserValid(username)){
            throw new IllegalArgumentException("닉네임은 최소 3자 이상, 알파벳 대소문자(a~z, A~Z), 숫자(0~9)이어야 합니다.");
        }
        if(pass.contains(username)){
            throw new IllegalArgumentException("비밀번호에는 닉네임이 포함되면 안됩니다.");
        }
        if(!isPassValid(pass)){
            throw new IllegalArgumentException("비밀번호는 4글자이상이어야합니다.");
        }
        if(!pass.equals(requestDto.getConfirmPassword())){
            throw new IllegalArgumentException("비밀번호확인이 틀렸습니다.");
        }

        // 패스워드 암호화
        String password = passwordEncoder.encode(requestDto.getPassword());


        User user = new User(username, password);
        userRepository.save(user);

    }
}