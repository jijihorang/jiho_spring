package com.busanit.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.util.UriComponentsBuilder;

@Getter
@Setter
@ToString
public class PagingHandler {

    // 페이징 기능
    private int pageNum; // 페이지 번호
    private int amount; // 한 페이지에 보여질 데이터 수

    // 검색 기능 추가
    private String type;       // 검색 유형
    private String keyword;    // 검색어

    public int getOffset() {  // 1페이지 시작하면 amount 까지 출력 (1 페이지 10개 출력, 2페이지 11부터 10개 출력)
        return (this.pageNum -1) * this.amount;
    }

    // 페이징 기능
    public PagingHandler() {  // 기본 생성자
        this(1, 10); /* 페이지 넘버는 1부터 시작 한 페이지에 10개 데이터만 보여지도록 설정 */
    }

    // 페이징 기능
    public PagingHandler(int pageNum, int amount) {
        this.pageNum = pageNum;
        this.amount = amount;
    }

    // [ 검색 기능 구분 ]
    // T - Title 검색
    // C - Content  검색
    // TC - Title + Content 검색
    public String[] getTypeArr() { /* 배열 사용 */
        // TC -> {"T", "C"}
        return type == null ? new String[] {} : type.split(""); /* split 사용하여 T, C 따로 저장 */
    }


    // web.util.UriComponentsBuilder
    // 여러 개의 파라미터들을 연결해서 URL 형태로 만들어줌
    // GET 방ㅅ기에 적합한 URL을 인코딩된 결과로 만들어줌 (한글 처리에 신경 X)
    public String getListLink() { /* 검색 항목 들어갔다 나와도 그대로 검색 유지되게 해주는 방식 */

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
                .queryParam("pageNum", this.pageNum)
                .queryParam("amount", this.getAmount())
                .queryParam("type", this.getType())
                .queryParam("keyword", this.getKeyword());

        return builder.toUriString();
    }

}
