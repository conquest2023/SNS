package com.snsService.sns.fixture;

import com.snsService.sns.model.entity.UserEntity;

public class UserEntityFixture {

    public  static UserEntity get(String userName,String password,Integer userId) {
        UserEntity result = new UserEntity();
        result.setId(1);
        result.setUserName(userName);
        result.setPassword(password);
        return  result;
    }
}
