package com.snsService.sns.fixture;

import com.snsService.sns.model.entity.PostEntity;
import com.snsService.sns.model.entity.UserEntity;
import org.springframework.security.core.parameters.P;

public class PostEntityFixture {

    public  static PostEntity get(String userName,Integer postId,Integer userId) {
        UserEntity result = new UserEntity();
        result.setId(userId);
        result.setUserName(userName);

        PostEntity postResult=new PostEntity();
        postResult.setUser(result);
        postResult.setId(postId);

        return postResult;

    }
}
