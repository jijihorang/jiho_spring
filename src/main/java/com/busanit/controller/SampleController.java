package com.busanit.controller;

import com.busanit.domain.SampleVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController /* @ResponseBody 계속 붙이기 힘들면 @RestController 사용하면 된다 ! */
@RequestMapping("/sample")
public class SampleController {

    @GetMapping(value = "/getText", produces = "text/plain; charset=UTF-8") /* uri = getText, produces(서버에서 클라이언트에게 보내는 형식)는 텍스트 형식으로 보냄  */
    // @ResponseBody  /* 서버에서 응답할 때 body 안의 내용을 출력할 때 타입으로 변환해줄 때 사용하는 어노테이션 방식 (여기서는 text/plain 방식으로 변환해서 리턴 시킴) */
    public String getText() {

        return "안녕하세요?"; /* json을 사용하여 String 바로 출력 */
    }
    // http://localhost:8080/sample/getText2/3/2 -> 이름 지정 안하고 값만 넣어서 출력 가능 !
    @GetMapping("/getText2/{bno}/{page}")
    // @ResponseBody
    public String getText2(@PathVariable("bno") int bno, @PathVariable("page") int page) { /* (@PathVariable 사용 시 {bno}, {page} 값들을 받아올 수 있음 */

        return "안녕하세요? bno =" + bno + " page =" + page;
    }

    @GetMapping(value = "/getSample", produces = { MediaType.APPLICATION_JSON_VALUE,
                                                      MediaType.APPLICATION_XML_VALUE })
    public SampleVO getSample() {
        return new SampleVO(112, "민또", "허");
    }

    @GetMapping(value = "/check", params = {"height", "weight"})
    public ResponseEntity<SampleVO> check(Double height, Double weight) {
        SampleVO vo = new SampleVO(0, "" + height, "" + weight);

        ResponseEntity<SampleVO> result = null;

        if(height < 150) {
            // BAD_GATEWAY - 502
            result = ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(vo);
        } else {
            // OK - 200
            result = ResponseEntity.status(HttpStatus.OK).body(vo);
        }

        return result;
    }

    @PostMapping("/getSample2")
    public SampleVO getSample2(@RequestBody SampleVO sampleVO) {

        return sampleVO;
    }
}





