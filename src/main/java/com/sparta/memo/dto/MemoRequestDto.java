package com.sparta.memo.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@RequiredArgsConstructor
@ToString
public class MemoRequestDto {
    private final String title;
    private final String username;
    private final String contents;
    private  final String pass;
}
