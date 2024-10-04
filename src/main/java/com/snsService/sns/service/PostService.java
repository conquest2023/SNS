package com.snsService.sns.service;

import com.snsService.sns.exception.ErrorCode;
import com.snsService.sns.exception.SnsApplicationException;
import com.snsService.sns.model.AlarmArgs;
import com.snsService.sns.model.AlarmType;
import com.snsService.sns.model.Comment;
import com.snsService.sns.model.Post;
import com.snsService.sns.model.entity.*;
import com.snsService.sns.repository.*;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private  final PostEntityRepository postEntityRepository;

    private  final UserEntityRepository userEntityRepository;

    private  final LikeEntityRepository likeEntityRepository;

    private  final CommentEntityRepository commentEntityRepository;

    private  final AlarmEntityRepository alarmEntityRepository;

    @Transactional
    public  void create(String title,String body,String userName){

        UserEntity userEntity = getUserEntityOrException(userName);

        postEntityRepository.save(PostEntity.of(title,body,userEntity));
    }

    public Post modify(String  title, String  body, String userName, Integer postId){
        UserEntity userEntity = getUserEntityOrException(userName);


        //post exist
        PostEntity postEntity = getPostOrException(postId);
        // post permission

        if(postEntity.getUser()!=userEntity){
            throw  new SnsApplicationException(ErrorCode.INVALID_PERMISSION,String.format("%s has no permission with %s",userName,postId));
        }

        postEntity.setTitle(title);
        postEntity.setBody(body);

       return Post.fromEntity(postEntityRepository.saveAndFlush(postEntity));
    }



    @Transactional
    public  void delete(String userName,Integer postId){
        UserEntity userEntity = getUserEntityOrException(userName);

        PostEntity postEntity = getPostOrException(postId);

        if (postEntity.getUser()!=userEntity){
            throw  new SnsApplicationException(ErrorCode.INVALID_PERMISSION,String.format("%s has no permission with %s",userName,postId));

        }

        postEntityRepository.delete(postEntity);
    }
    public  Page<Post> list(Pageable pageable){
        return  postEntityRepository.findAll(pageable).map(Post::fromEntity);
    }

    public Page<Post> my(String userName,Pageable pageable){
        UserEntity userEntity = getUserEntityOrException(userName);


        return   postEntityRepository.findAllByUser(userEntity,pageable).map(Post::fromEntity);
    }


    @Transactional
    public  void like(Integer postId,String userName){
        PostEntity postEntity = getPostOrException(postId);

        UserEntity userEntity = getUserEntityOrException(userName);

        // check liked->throw
        likeEntityRepository.findByUserAndPost(userEntity,postEntity).ifPresent(it->{
            throw  new SnsApplicationException(ErrorCode.ALREADY_LIKED,String.format("userName %s already like post %d",userName,postId));
        });


        likeEntityRepository.save(LikeEntity.of(userEntity,postEntity));
        // likeEntityRepository
        alarmEntityRepository.save(AlarmEntity.of(postEntity.getUser(),AlarmType.NEW_COMMENT_ON_POST,new AlarmArgs(userEntity.getId(),postEntity.getId())));

    }
    public  int likeCount(Integer postId){
        PostEntity postEntity = getPostOrException(postId);


        // count like->throw
       List<LikeEntity> likeEntities= likeEntityRepository.findAllByPost(postEntity);

       return likeEntityRepository.countByPost(postEntity);
    }

    @Transactional
    public  void comment(Integer postId,String userName,String comment){

        PostEntity postEntity=getPostOrException(postId);
        UserEntity userEntity=getUserEntityOrException(userName);

        //comment save
        commentEntityRepository.save(CommentEntity.of(userEntity,postEntity,comment));

        alarmEntityRepository.save(AlarmEntity.of(postEntity.getUser(),AlarmType.NEW_COMMENT_ON_POST,new AlarmArgs(userEntity.getId(),postEntity.getId())));



    }
    public Page<Comment> getComments(Integer postId, Pageable pageable){
            PostEntity postEntity=getPostOrException(postId);
          return   commentEntityRepository.findAllByPost(postEntity,pageable).map(Comment::fromEntity);
    }


    private PostEntity getPostOrException(Integer postId) {
        PostEntity postEntity=  postEntityRepository.findById(postId).orElseThrow(()->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND,String.format("%s not founded", postId)));
        return postEntity;
    }

    private UserEntity getUserEntityOrException(String userName) {
        UserEntity userEntity=userEntityRepository.findByUserName(userName).orElseThrow(()->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND,String.format("%s not founded", userName)));
        return userEntity;
    }
}

