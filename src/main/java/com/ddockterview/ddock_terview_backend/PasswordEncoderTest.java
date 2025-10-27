package com.ddockterview.ddock_terview_backend;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderTest {
    public static void main(String[] args) {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String rawPassword = "1111"; // 테스트하고 싶은 비밀번호
        String encodedPassword = encoder.encode(rawPassword);

        System.out.println("암호화된 비밀번호: " + encodedPassword);
    }
}
