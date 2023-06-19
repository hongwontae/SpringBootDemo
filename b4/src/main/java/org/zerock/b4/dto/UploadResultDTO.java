package org.zerock.b4.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadResultDTO {
    
    private String uuid;
    private String fileName;
    private boolean img;

    // 이미지가 아닐때 default image를 위해서 만든다
    public String getLink(){

        if(img){
            return "s_"+ uuid +"_"+fileName;
        }else{
            return "default.jpg";
        }

    }
}
