package com.busanit.mapper;

import com.busanit.domain.PagingHandler;
import com.busanit.domain.ReplyVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReplyMapper {

    // insert 사용하여 댓글
    public int insert(ReplyVO vo);

    // read 사용하여 특정 댓글 불러오기
    public ReplyVO read(int rno);

    // 댓글 삭제
    public int delete(int rno);

    // 전체 댓글 삭제
    public int deleteAll(int bno);

    // 댓글 수정
    public int update(ReplyVO vo);

    // 댓글 전체 리스트 가져오기
    // mybatis에서 두 개 이상의 데이터를 파라미터로 전달하는 방식 3가지
    // 1. 별도의 객체로 구성
    // 2. Map을 이용하는 방식
    // 3. @Param을 이용해서 이름 사용하는 방식
    public List<ReplyVO> getListWithPaging (
            @Param("paging")PagingHandler paging,  /* paging 기능 필요 */
            @Param("bno") int bno); /* 게시글 번호 값을 매개변수로 넘겨줌 */

    // 댓글 총 갯수
    public int getCountByBno(int bno);
}


