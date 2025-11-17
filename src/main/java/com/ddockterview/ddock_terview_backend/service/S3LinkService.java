//package com.ddockterview.ddock_terview_backend.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import software.amazon.awssdk.services.s3.model.GetObjectRequest;
//import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//import software.amazon.awssdk.services.s3.presigner.S3Presigner;
//import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
//import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
//
//import java.time.Duration;
//
//@Service
//@RequiredArgsConstructor
//public class S3LinkService {
//
//    private final S3Presigner s3Presigner;
//
//    @Value("${cloud.aws.s3.bucket}")
//    private String bucketName;
//
//    /**
//     * S3에 파일을 업로드하기 위한 Presigned URL을 생성합니다.
//     * @param s3Key S3에 저장될 파일의 경로 (예: videos/userId/uuid.webm)
//     * @return 10분간 유효한 PUT 권한의 Presigned URL
//     */
//    public String getUploadUrl(String s3Key) {
//        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
//                .bucket(bucketName)
//                .key(s3Key)
//                .contentType("video/webm") // 프론트엔드에서 webm으로 녹화한다고 가정
//                .build();
//
//        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
//                .signatureDuration(Duration.ofMinutes(10))
//                .putObjectRequest(putObjectRequest)
//                .build();
//
//        return s3Presigner.presignPutObject(presignRequest).url().toString();
//    }
//
//    /**
//     * S3에 저장된 파일을 보기 위한 Presigned URL을 생성합니다.
//     * @param s3Key S3에 저장된 파일의 경로
//     * @return 1시간 동안 유효한 GET 권한의 Presigned URL
//     */
//    public String getDownloadUrl(String s3Key) {
//        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
//                .bucket(bucketName)
//                .key(s3Key)
//                .build();
//
//        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
//                .signatureDuration(Duration.ofHours(1))
//                .getObjectRequest(getObjectRequest)
//                .build();
//
//        return s3Presigner.presignGetObject(presignRequest).url().toString();
//    }
//}
