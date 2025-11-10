package kr.co.ap.flobankap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
    private String custCode;
    private String custId;
    private String custPw;
    private String custName;
    private String custJumin;
    private String custEmail;
    private String custHp;
    private LocalDate custBirth;
    private String custGen;
    private String custEngName;
    private LocalDate custRegDt;
    private Integer custStatus;
    private String custZip;
    private String custAddr1;
    private String custAddr2;
    private Integer custTransLimit;
    private LocalDate custLastLoginDt;
    private String custNation;
    private LocalDate custUpdDt;
}
