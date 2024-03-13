package com.busanit.service;

import com.busanit.domain.BoardAttachVO;
import com.busanit.domain.BoardVO;
import com.busanit.domain.PagingHandler;

import java.util.List;

public interface BoardService {

    // 게시글 리스트 (목록 가져오기)
    // public List<BoardVO> getList();

    // 게시글 리스트 (목록 가져오기)
    public List<BoardVO> getList(PagingHandler paging);

    // 게시글 상세
    public BoardVO get(Long bno);

    // 게시글 등록
    public void register(BoardVO board);

    // 게시글 수정
    public boolean modify(BoardVO board);

    // 게시글 삭제
    public boolean remove(Long bno);

    // 게시글 총 갯수
    public int getTotal (PagingHandler paging);

    // 첨부 파일 리스트 조회
    public List<BoardAttachVO> getAttachList(int bno);
}

