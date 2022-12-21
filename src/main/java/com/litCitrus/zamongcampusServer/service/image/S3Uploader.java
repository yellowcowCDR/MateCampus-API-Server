package com.litCitrus.zamongcampusServer.service.image;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;  // S3 버킷 이름

    public List<String> upload(List<MultipartFile> multipartFiles, String dirName) throws IOException {
        // 1. 이미지로 변환
        List<File> uploadFiles = convert(multipartFiles, dirName)  // 파일 변환할 수 없으면 에러
                .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));

        // 2. 이미지 변환 가능한 이미지들을 보낸다. (File 타입)
        // ** 만약 file명, size 등 그 외 정보들이 필요하다면
        // ** https://ksabs.tistory.com/152 참고해서 변경할 것.
        // ** 혹은 convert함수 리턴값부터 Map으로 File, MultipartFile을 묶어서 진행
        List<String> uploadImageUrls = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String current_date = simpleDateFormat.format(new Date());
        for (File uploadFile : uploadFiles){
            String fileName = dirName + "/"  + current_date + "/"+ UUID.randomUUID() + uploadFile.getName();   // S3에 저장된 파일 이름
            uploadImageUrls.add(putS3(uploadFile, fileName)); // s3로 업로드 후 주소 반환
            removeNewFile(uploadFile);    // local 이미지 삭제
        }

        return uploadImageUrls;
    }

    public String uploadOne(MultipartFile multipartFile, String dirName) throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String current_date = simpleDateFormat.format(new Date());
        String path = new File("").getAbsolutePath() + "/images/";
        String new_file_name = Long.toString(System.nanoTime()) + multipartFile.getOriginalFilename();
        File convertFile = new File(path + new_file_name);
        if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
            try (FileOutputStream fos = new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                fos.write(multipartFile.getBytes());
            }
        }
        String fileName = dirName + "/"  + current_date + "/"+ UUID.randomUUID() + convertFile.getName();   // S3에 저장된 파일 이름
        return putS3(convertFile, fileName); // s3로 업로드 후 주소 반환
    }

    // S3로 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return "https://d1cy8kjxuu1lsp.cloudfront.net/" + fileName;
//        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("File delete fail");
    }

    // 로컬에 파일 업로드 하기
    private Optional<List<File>> convert(List<MultipartFile> multipartFiles, String dirname) throws IOException {
        List<File> files = new ArrayList<>();
        String path = new File("").getAbsolutePath() + "/images/";
        log.debug("[@S3Uploader, convert] path: "+ path);

        for (MultipartFile file : multipartFiles) {
            String new_file_name = Long.toString(System.nanoTime()) + file.getOriginalFilename();
            File convertFile = new File(path + new_file_name);
            if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
                try (FileOutputStream fos = new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                    fos.write(file.getBytes());
                    files.add(convertFile);
                }
            }
        }
        return Optional.of(files);
    }

}

