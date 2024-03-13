package com.busanit.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor /* 빈생성자 만들어줌 */
@AllArgsConstructor /* 매개변수에 따른 모든 경우의 생성자를 만들어줌 */
public class SampleVO {
    private Integer mno;
    private String firstName;
    private String lastName;
}
