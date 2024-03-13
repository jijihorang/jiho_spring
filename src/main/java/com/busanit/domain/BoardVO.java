package com.busanit.domain;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BoardVO {
    private int bno;
    private String title;
    private String content;
    private String writer;
    private Date regdate;
    private Date updateDate;

    private int replyCnt;  // 추가
    private List<BoardAttachVO> attachList;

}
