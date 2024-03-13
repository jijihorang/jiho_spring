package com.busanit.controller;

import com.busanit.domain.AttachFileDTO;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Controller
public class UploadController {

    // 오늘 날짜로 폴더명 가져오기
    private String getFolder() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  // yyyy-MM-dd로 만들어줌
        Date date = new Date(); // 오늘 날짜 가져옴
        String str = sdf.format(date);

        return str.replace("-", File.separator); // - 대신 파일 구분자로 치환시킴
    }

    // 이미지 여부 check (이미지일 경우 썸네일)
    private boolean checkImageType(File file) {
        try {
            // 파일의 Content-type 조회(text/plain, image/png, image/jpeg, ...)
            String contentType = Files.probeContentType(file.toPath());

            // image로 시작하는지 check해서 true/false로 반환
            return contentType.startsWith("image"); /* true */

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @GetMapping("/uploadAjax")
    public void uploadAjax() {  /* void 반환 타입 시 upload ajax를 찾아줌 */
        System.out.println("upload ajax");
    }

    @PostMapping(value = "/uploadAjaxAction", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<AttachFileDTO>> uploadAjaxPost(MultipartFile[] uploadFile) {
        List<AttachFileDTO> list = new ArrayList<>();
        String uploadFolder = "C:\\upload";
        String uploadFolderPath = getFolder();

        // make folder ---------------
        File uploadPath = new File(uploadFolder, getFolder());

        // 폴더가 존재하지 않을 경우 mkdirs()로 폴더 생성
        // make yyy/MM/dd folder
        if(uploadPath.exists() == false) {
            uploadPath.mkdirs();
        }


        for (MultipartFile multipartFile : uploadFile) {
            System.out.println("-----------------------------------------------------------------");
            System.out.println("Upload File Name : " + multipartFile.getOriginalFilename());
            System.out.println("Upload File Size: " + multipartFile.getSize());

            AttachFileDTO attachDTO = new AttachFileDTO();

            String uploadFileName = multipartFile.getOriginalFilename();

            // IE has file path
            uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
            System.out.println("only file name : " + uploadFileName);

            attachDTO.setFileName(uploadFileName);

            // 파일 중복 시 이름 다르게 (같은 사진이지만 이름만 다름)
            UUID uuid = UUID.randomUUID();
            uploadFileName = uuid.toString() + "_" + uploadFileName;

            try {

                File saveFile = new File(uploadPath, uploadFileName);

                // transferTo() : 파일을 실제로 저장
                multipartFile.transferTo(saveFile);

                attachDTO.setUuid(uuid.toString());
                attachDTO.setUploadPath(uploadFolderPath);

                // check image type file
                if (checkImageType(saveFile)) { /* saveFile 타입이 image일 경우 */
                    attachDTO.setImage(true);
                    FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_" + uploadFileName)); /* s_로 시작하는 이름을 만들어 */

                    Thumbnailator.createThumbnail(multipartFile.getInputStream(),thumbnail, 100,100); /* 썸네일 생성 */
                    thumbnail.close();
                }

                // add to List
                list.add(attachDTO);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/display")
    @ResponseBody
    public ResponseEntity<byte[]>getFile(String fileName) {

        File file = new File("c:\\upload\\" + fileName);

        ResponseEntity<byte[]> result = null;

        try {
            HttpHeaders header = new HttpHeaders();
            header.add("Content-type", Files.probeContentType(file.toPath()));
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    // 첨부 파일 다운로드
    // MIME 타입 -> application/octet-stream
    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@RequestHeader("User-Agent") String userAgent, String fileName) {
        Resource resource = new FileSystemResource("c:\\upload\\" + fileName);

        if(resource.exists() == false) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String resourceName = resource.getFilename();

        // remove UUID - 파일 다운 시 UUID 제거
        String resourceOriginalName = resourceName.substring(resourceName.indexOf("_") + 1);

        HttpHeaders headers = new HttpHeaders();

        try {
            String downloadName = null;

            // 한글 처리
            if(userAgent.contains("Trident")) {
                // IE browser
                downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8").replaceAll("\\+", " ");
            } else if (userAgent.contains("Edge browser")) {
                // Edge browser
                downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8");
            } else {
                // Chrome browser
                downloadName = new String(resourceOriginalName.getBytes("UTF-8"), "ISO-8859-1");
            }
            System.out.println("downloadName : " + downloadName);

            headers.add("content-Disposition", "attachment; filename=" + downloadName);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
    }

    //첨부 파일 삭제 버튼 클릭 이벤트 (3월 13일 추가 2)
    @PostMapping("/deleteFile")
    @ResponseBody
    public ResponseEntity<String> deleteFile(String fileName, String type) {
        File file;

       try {
           file = new File("C:\\upload\\" + URLDecoder.decode(fileName, "UTF-8"));
           file.delete();

           // 이미지인 경우 원본 이미지도 같이 삭제
           if (type.equals("image")) {
               String largeFileName = file.getAbsolutePath().replace("s_", "");

               file = new File(largeFileName);

               file.delete();
           }
       } catch (UnsupportedEncodingException e) {
           e.printStackTrace();
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
       return new ResponseEntity<String>("deleted", HttpStatus.OK);
    }
}
