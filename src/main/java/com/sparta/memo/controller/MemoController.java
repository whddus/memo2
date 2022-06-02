package com.sparta.memo.controller;

import com.sparta.memo.domain.Memo;
import com.sparta.memo.domain.Comment;
import com.sparta.memo.dto.CommentRequestDto;
import com.sparta.memo.repository.CommentRepository;
import com.sparta.memo.repository.MemoRepository;
import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.service.CommentService;
import com.sparta.memo.service.MemoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequiredArgsConstructor
@Slf4j
public class MemoController {
    private final MemoRepository memoRepository;
    private final CommentRepository commentRepository;
    private final MemoService memoService;


    @PostMapping("/api/memos")
    public Memo createMemo(@RequestBody MemoRequestDto requestDto) {
        Memo memo = new Memo(requestDto);
        return memoRepository.save(memo);
    }
    @GetMapping("/api/memos")
    public List<Memo> getMemos() {
        return memoRepository.findAllByOrderByModifiedAtDesc();
    }

    @GetMapping("/api/memos/{id}")
    public  Memo getMemo(@PathVariable Long id) {
        Memo memo = memoRepository.findById(id).orElseThrow(
                ()->new IllegalArgumentException("메모가 존재하지 않습니다.")
        );
        return memo;
    }

    @DeleteMapping("/api/memos/{id}")
    public Long deleteMemo(@PathVariable Long id,@RequestBody MemoRequestDto requestDto) {
        memoService.delete(id,requestDto);
        return id;
    }
    @PutMapping("/api/memos/{id}")
    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto){
        memoService.update(id,requestDto);
        return id;
    }


}
