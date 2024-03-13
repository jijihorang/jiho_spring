package com.busanit.domain;

import lombok.Data;

import java.util.Date;

@Data
public class ReplyVO {  /* mysql에서 테이블 생성 후 domain에서 RelpyVO 생성해야 함  */
    private int rno;
    private int bno;

    private String reply;
    private String replyer;

    private Date regDate;
    private Date updateDate;
}
