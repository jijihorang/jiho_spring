package com.busanit.controller;

import com.busanit.domain.PagingHandler;
import com.busanit.domain.ReplyPageDTO;
import com.busanit.domain.ReplyVO;
import com.busanit.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController   /* 댓글으 전부 비동기 방식으로 REST 방식 사용 */
@RequiredArgsConstructor
@RequestMapping("/replies")
public class ReplyController {

    /*
        작업     |URL                     | HTTP 전송방식
        등록     |  /replies/new          | POST
        조회     |  /replies/:rno         | GET
        삭제     |  /replies/:rno         | DELETE
        수정     |  /replies/:rno         | PUT or PATCH
        페이징   | /replies/pages/:bno    | GET
    */

    private final ReplyService replyService;

    // consumes, produces를 이용해서 JSON 방식의 데이터만 처리하도록 함
    // consumes - 클라이언트가 서버에게 보내느 데이터 타입을 명시
    // produces - 서버가 클라이언트에게 보내는 데이터 타입을 명시

    // @RequestBody를 적용해서 JSON 데이터를 ReplyVo 타입으로 변환하도록 지정
    @PostMapping(value = "/new",
            consumes = "application/json",
            produces = {MediaType.TEXT_PLAIN_VALUE })
    public ResponseEntity<String> create(@RequestBody ReplyVO vo) {
        int insertCount = replyService.register(vo);

        // 삼항연산자 사용 (1일 시 ok, 1이 아닐시 에러)
        // HttpStatus.OK => 200 정상
        // HttpStatus.INTERNAL_SERVER_ERROR =? 500 에러
        return insertCount == 1 ?
                new ResponseEntity<>("success", HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // @PathVariable - URI 경로 중간에 들어간 값을 얻기 위해서 사용 ({bno}, {page})
    @GetMapping(value = "/page/{bno}/{page}",
            produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ReplyPageDTO> getList(
            @PathVariable("bno") int bno,
            @PathVariable("page") int page) {

        PagingHandler paging = new PagingHandler(page, 5);

        return new ResponseEntity<>(replyService.getListPage(paging, bno),HttpStatus.OK);
    }

    // 댓글 조회 (객체 그대로 보내기 위해 APPLICATION_JSON_VALUE 사용 )
    @GetMapping(value = "/{rno}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ReplyVO> get(@PathVariable("rno") int rno) {
        return new ResponseEntity<>(replyService.get(rno), HttpStatus.OK);
    }

    // 댓글 삭제
    @DeleteMapping(value = "/{rno}", produces = { MediaType.TEXT_PLAIN_VALUE })
    public ResponseEntity<String> remove(@PathVariable("rno") int rno) {
        return replyService.remove(rno) == 1
                ? new ResponseEntity<>("success", HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 댓글 수정 (put, patch 모두 대응 위해 @RequestMapping 사용 -> 사용자 요청 받는 것은 json 받음)
    @RequestMapping(method = {RequestMethod.PUT, RequestMethod.PATCH },
            value = "/{rno}",
            consumes = "application/json",
            produces = { MediaType.TEXT_PLAIN_VALUE })

    public ResponseEntity<String> modify(
            @PathVariable("rno") int rno,
            @RequestBody ReplyVO vo) {

        vo.setRno(rno);

        return replyService.modify(vo) == 1
                ? new ResponseEntity<>("success", HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}