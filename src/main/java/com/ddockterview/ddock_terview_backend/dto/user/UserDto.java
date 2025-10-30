package com.ddockterview.ddock_terview_backend.dto.user;

import com.ddockterview.ddock_terview_backend.entity.User;
import com.ddockterview.ddock_terview_backend.entity.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class UserDto {

    private String name;
    private String depart;
    private Status status;

    public UserDto(User user) {
        this.name = user.getName();
        this.depart = user.getDepart();
        this.status = user.getStatus();
    }

}
