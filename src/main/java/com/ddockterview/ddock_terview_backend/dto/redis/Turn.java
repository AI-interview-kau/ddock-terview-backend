package com.ddockterview.ddock_terview_backend.dto.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Turn implements Serializable {

    private String speaker; // "AI" or "User"
    private String content; // 질문 텍스트 or 답변 S3 URL

}
