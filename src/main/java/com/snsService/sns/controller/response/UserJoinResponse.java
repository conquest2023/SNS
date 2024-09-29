package com.snsService.sns.controller.response;

import com.snsService.sns.model.User;
import com.snsService.sns.model.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@AllArgsConstructor
public class UserJoinResponse {

    private  String userName;

    private Integer id;

    private UserRole role;


    public  static   UserJoinResponse fromUser(User user){

        return  new UserJoinResponse(
                user.getUsername(),
                user.getId(),
                user.getUserRole());
    }
}
