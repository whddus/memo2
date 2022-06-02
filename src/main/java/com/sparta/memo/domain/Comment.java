package com.sparta.memo.domain;

import com.sparta.memo.dto.CommentRequestDto;
import com.sparta.memo.dto.MemoRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Comment extends Timestamped {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false)
    private Long memoId;

    private String author;

    @Column(nullable = false)
    private String comment;


    public Comment(CommentRequestDto requestDto,Long memoId,String author) {
        this.author = author;
        this.memoId = memoId;
        this.comment = requestDto.getComment();
    }

    public void update(CommentRequestDto requestDto) {
        this.memoId = requestDto.getMemoId();
        this.comment = requestDto.getComment();
    }


}
