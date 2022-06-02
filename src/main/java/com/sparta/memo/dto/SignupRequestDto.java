package com.sparta.memo.dto;


import com.sparta.memo.domain.User;
import com.sun.istack.NotNull;
import lombok.*;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDto {

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String confirmPassword;

    private Set<AuthorityDto> authorityDtoSet;

    public static SignupRequestDto from(User user) {
        if(user == null) return null;

        return SignupRequestDto.builder()
                .username(user.getUsername())
                .authorityDtoSet(user.getAuthorities().stream()
                        .map(authority -> AuthorityDto.builder().authorityName(authority.getAuthorityName()).build())
                        .collect(Collectors.toSet()))
                .build();
    }
}

