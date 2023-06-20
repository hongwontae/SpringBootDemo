package org.zerock.b4.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProductRegisterDTO {

    private Integer pno;
    private String pname;
    private int price;
    private boolean status;

    // uuid와 파일네임이 필요하다.
    private List<String>fileNames;
    
}
