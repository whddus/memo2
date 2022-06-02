package com.sparta.memo.repository;

import com.sparta.memo.domain.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    List<Memo> findAllByOrderByModifiedAtDesc();

    Memo findByIdAndPass(Long id, String pass);



}
