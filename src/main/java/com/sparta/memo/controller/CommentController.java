package com.sparta.memo.controller;

import com.sparta.memo.domain.Comment;
import com.sparta.memo.dto.CommentRequestDto;
import com.sparta.memo.repository.CommentRepository;
import com.sparta.memo.service.CommentService;
import com.sparta.memo.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentRepository commentRepository;

    private final CommentService commentService;

    @PostMapping("/api/comment/{memoId}")
    public Comment createComment(@RequestBody CommentRequestDto requestDto,@PathVariable Long memoId) {
        Optional<String> currentUsername = SecurityUtil.getCurrentUsername();
        if (currentUsername.isPresent() && currentUsername.get().equals("anonymousUser")) {
            throw new IllegalArgumentException("로그인이 필요한 기능입니다.");
        }
        Comment comment = new Comment(requestDto,memoId,requestDto.getAuthor());
        return commentRepository.save(comment);
    }

    @GetMapping("/api/{memoId}/comment")
    public  List<Comment> getComment(@PathVariable Long memoId) {
        return commentRepository.findAllByMemoIdOrderByModifiedAtDesc(memoId);

    }

    @PutMapping("/api/comment/{id}")
    public Long updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto){
        commentService.update(id,requestDto);
        return id;
    }

    @DeleteMapping("/api/comment/{id}")
    public Long deleteComment(@PathVariable Long id,@RequestBody CommentRequestDto requestDto) {
        commentService.delete(id,requestDto);
        return id;
    }


}

