package com.ddockterview.ddock_terview_backend.service;

import com.ddockterview.ddock_terview_backend.entity.User;
import com.ddockterview.ddock_terview_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserId(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> 해당하는 회원을 찾을 수 없습니다."));
    }

    // DB의 사용자 정보(User 엔티티)를 UserDetails 객체로 변환하는 메서드
    private UserDetails createUserDetails(User user) {

        return User.builder()
                .userId(user.getUsername())
                .password(user.getPassword())
                .depart(user.getDepart())
                .status(user.getStatus())
                .roles(List.of(user.getRoles().toArray(new String[0])))
                .build();
    }
}