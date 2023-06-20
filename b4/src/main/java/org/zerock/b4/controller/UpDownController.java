package org.zerock.b4.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.b4.dto.RequestFileRemoveDTO;
import org.zerock.b4.dto.UploadResultDTO;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;

@RestController
@Log4j2
public class UpDownController {

    // Upload는 springboot로 조회는  webServer(nginx)로 
    @Value("${org.zerock.upload.path}") // import 시에 springframework으로 시작하는 Value
    private String uploadPath; // uploadPath => C:\\webserver\\nginx-1.24.0\\html => value 어노테이션으로 가져온다.

    @PostMapping("/upload")
    public List<UploadResultDTO> upload(MultipartFile[] files) {

        //length list라 사용가능하다.
        // 만약 파일이 null값이거나 길이가 0이면 null 반환한다.
        if(files ==null || files.length == 0){
            return null;
        }

        // resultList이름으로 배열리스트 구현
        List<UploadResultDTO> resultList = new ArrayList<>();



        // 들어오는 모든 파일들을 루프를 돌린다.
        // 배열이 매개변수 => 메개변수 한계 정하지 않음 => for : 메서드 용이
        // 이 for loop는 들어온거 다 돌리는 루프이다. while과 같다.
        for (MultipartFile file : files) {

            // 그냥 초기값 넣어줬다. 안전하게 하기 위해서
            UploadResultDTO result = null;

            // file의 메서드 사용가능한 이유는 for : => 이름 체인지해서
            // getOriginalFilename => 현재 파일의 이름과 확장자 모두 가져온다. 
            String fileName = file.getOriginalFilename();
            log.info("fileName: " + fileName);

            // 업로드한 파일의 크기를 구한다.
            // long값으로 받아야 한다.
            // byte 단위로 얻는다.
            long size = file.getSize(); 
            log.info("size: " + size);
            
            // uuid => uuid Class => 들어오는 값의 중복을 피하기 위해 사용한다.
            // ramdomUUID => uuid형태로 바꾼다.
            // toString => uuid 형태를 문자열로 바꾼다.
            // for 이니까 들어오는 첨부파일 하나 당 하나 생성한다.
            String uuidStr = UUID.randomUUID().toString();

            // uuid+_+파일이름(확장자) => 우리가 원하는 upload server 파일명
            String saveFileName = uuidStr + "_" + fileName;

            // File => 생성자를 통해서 File의 값을 넣어준느데
            // (String (주소), String(파일이름)) => 하나의 객체가 주소와 이름을 갖는다.
            // saveFile은 주소와 파일이름_uuid를 얻는다.
            // save 파일은 uploadPath saveFileName 객체, 들어오는 많은 파일의...
            // 생성자가 파일을 만들어 내는 것이다.
            File saveFile = new File(uploadPath, saveFileName);

        try {
            // 파일 확장자 체크 (File extension check)

            // save 파일을 복사한다.
            FileCopyUtils.copy(file.getBytes(), saveFile);

            // 빌더 방식으로 result변수에 업로드된 uuid filename을 넣는다.
            result = UploadResultDTO.builder()
                    .uuid(uuidStr)
                    .fileName(fileName)
                    .build();

            // 이미지 파일 여부 확인
            // toPath => saveFile의 경로를 반환한다.
            // files/probe.. => mineType 확인하지 못하면 null 값을 반환한다.
            // Mine 타입 => 클라이언트에게 전송된 문서의 다양성을 알려주기 위한 매커니즘(text/plain)
            // files.pro... mine타입을 얻어온다.
            String mimeType = Files.probeContentType(saveFile.toPath());

            // 마인 타입확인 완료
            log.info("mimeType: " + mimeType);

            // string.startWith => 비교대상 문자열과 체크할 문자열을 비교하고 boolean값 반환한다.
            if (mimeType.startsWith("image")) {

                // upload success 섬네일
                // 중복을 피하기 위해서 s_추가
                // 섬네일의 이름은? s_uuid_filename
                File thumbFile = new File(uploadPath, "s_" + saveFileName);
                // 썸네일을 추가한다.
                Thumbnailator.createThumbnail(saveFile, thumbFile, 100, 100);
                // boolean은 기본값이 false이다 true로 변환한다.
                result.setImg(true);
            } // end if

            // 1. 이미지일때만 섬네일만들기
            // 2. DTO 생성

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        

        resultList.add(result);
        }// end for
        //여기서 파일 두개가 저장된다.
        return resultList;
    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName){

        Resource resource = new FileSystemResource(uploadPath+File.separator + fileName);
        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();

        try{
            headers.add("Content-Type", Files.probeContentType( resource.getFile().toPath() ));
        } catch(Exception e){
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    
    //@RequestBody = JSON 데이터를 RequestFileRemoveDTO로 변환하라고 지칭해줌
    @DeleteMapping("/removeFile/{fileName}")
    public Map<String,String> removeFile(@PathVariable("fileName") String fileName){

        log.info("delete file....");
        log.info(fileName);

        File originFile = new File(uploadPath,fileName);

        // 자바 외부에서 JVM접근시 checkedException
        try {
            String mimeType = Files.probeContentType(originFile.toPath());

            if(mimeType.startsWith("image")){
                File thumbFile = new File(uploadPath, "s_"+fileName);
                thumbFile.delete();
            }
            originFile.delete();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // JSON 형태로 나가게하기위해서 
        return Map.of("result","success");
    }

}
