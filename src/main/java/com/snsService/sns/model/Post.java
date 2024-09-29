package com.snsService.sns.model;

import com.snsService.sns.model.entity.PostEntity;
import com.snsService.sns.model.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.Instant;
@Getter
@AllArgsConstructor
public class Post {
    private Integer id;

    private  String title;

    private  String body;

    private User user;

    private Timestamp registeredAt;

    private Timestamp updatedAt;

    private Timestamp deletedAt;




    public  static Post fromEntity(PostEntity entity){

      return  new Post(
              entity.getId(),
              entity.getTitle(),
              entity.getBody(),
              User.fromEntity(entity.getUser()),
              entity.getRegisteredAt(),
              entity.getUpdatedAt(),
              entity.getDeletedAt()
      );
    }


}
