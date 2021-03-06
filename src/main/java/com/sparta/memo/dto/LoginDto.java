package com.sparta.memo.dto;

import com.sun.istack.NotNull;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    private String username;
    private String password;
}
