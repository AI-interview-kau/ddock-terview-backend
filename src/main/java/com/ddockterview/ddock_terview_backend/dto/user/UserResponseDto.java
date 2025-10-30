package com.ddockterview.ddock_terview_backend.dto.user;

import com.ddockterview.ddock_terview_backend.entity.User;
import com.ddockterview.ddock_terview_backend.entity.enums.Status;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private String name;
    private String depart;
    private Status status;

    public UserResponseDto(User user) {
        this.name = user.getName();
        this.depart = user.getDepart();
        this.status = user.getStatus();
    }

}
