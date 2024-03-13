package com.busanit.mapper;

import com.busanit.domain.BoardVO;
import com.busanit.domain.PagingHandler;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BoardMapper {

    // 게시글 리스트 (목록 가져오기)
    public List<BoardVO> getList();

    // 게시글 리스트 (페이징) -- 3월 7일 추가
    public List<BoardVO> getListWithPaging(PagingHandler page);

    // 게시글 등록
    public void insert(BoardVO board); /* insert 한 값을 BoardVO에 받아서 보내기 */

    // 게시글 등록 후 pk 값 얻기
    public void insertSelectKey(BoardVO board);

    // 상세보기
    public BoardVO read(Long bno);

    // 게시글 삭제
    public int delete(Long bno); /* 반환값 int로 설정 시 변화가 생기는 행의 개수를 int 타입으로 반환 */

    // 게시글 수정
    public int update(BoardVO board); /* 반환값 int로 설정 시 변화가 생기는 행의 개수를 int 타입으로 반환 */

    // 게시글 총 갯수
    public int getTotalCount(PagingHandler paging);  /* BoardMapper.xml에 추가 */

    // 댓글 새로 등록 및 삭제 갯수
    public void updateReplyCnt(@Param("bno") int bno,
                               @Param("amount") int amount);

}
