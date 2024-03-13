package com.busanit.service;

import com.busanit.domain.PagingHandler;
import com.busanit.domain.ReplyPageDTO;
import com.busanit.domain.ReplyVO;

import java.util.List;

public interface ReplyService {

    // 등록
    public int register(ReplyVO vo);

    // 게시글 가져오기
    public ReplyVO get(int rno);

    // 수정
    public int modify(ReplyVO vo);

    // 삭제
    public int remove(int rno);

    // 게시글 리스트
    public List<ReplyVO> getList(PagingHandler paging, int bno);

    // 게시글 리스트 (바뀐 버전)
    public ReplyPageDTO getListPage(PagingHandler paging, int bno);


}
