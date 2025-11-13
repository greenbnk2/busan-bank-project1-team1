package kr.co.api.flobankapi.dto;

import lombok.Data;

@Data
public class TermsDTO {

    private Integer termNo;        // 약관 번호 (PK)
    private String termTitle;      // 약관 제목
    private String termFile;       // 약관 파일 경로
    private String termRegDy;      // 약관 등록일자 (CHAR(8))

}
