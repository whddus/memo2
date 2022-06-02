package com.sparta.memo.repository;

import com.sparta.memo.domain.Comment;
import com.sparta.memo.domain.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByMemoIdOrderByModifiedAtDesc(Long memoId);

    Comment findByIdAndMemoId(Long id,Long memoId);



}