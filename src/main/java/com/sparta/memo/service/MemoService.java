package com.sparta.memo.service;

import com.sparta.memo.domain.Memo;
import com.sparta.memo.repository.MemoRepository;
import com.sparta.memo.dto.MemoRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class MemoService {

    private final MemoRepository memoRepository;

    @Transactional
    public Long update(Long id, MemoRequestDto requestDto){

        Memo memo = memoRepository.findByIdAndPass(id,requestDto.getPass());
        if(memo == null){
            throw new IllegalArgumentException("비밀번호가 맞지 않습니다.");
        }

        memo.update(requestDto);
        return id;
    }

    @Transactional
    public Long delete(Long id, MemoRequestDto requestDto){

        Memo memo = memoRepository.findByIdAndPass(id,requestDto.getPass());
        if(memo == null){
            throw new IllegalArgumentException("비밀번호가 맞지 않습니다.");
        }else{memoRepository.deleteById(id);}

        return id;
    }

}
