package com.snsService.sns.controller.response;

import com.snsService.sns.model.User;
import com.snsService.sns.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {

    private  String userName;

    private Integer id;

    private UserRole role;


    public  static   UserResponse fromUser(User user){

        return  new UserResponse(
                user.getUsername(),
                user.getId(),
                user.getUserRole());
    }
}
