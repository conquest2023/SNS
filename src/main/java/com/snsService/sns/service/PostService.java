package com.snsService.sns.service;

import com.snsService.sns.exception.ErrorCode;
import com.snsService.sns.exception.SnsApplicationException;
import com.snsService.sns.model.Post;
import com.snsService.sns.model.entity.LikeEntity;
import com.snsService.sns.model.entity.PostEntity;
import com.snsService.sns.model.entity.UserEntity;
import com.snsService.sns.repository.LikeEntityRepository;
import com.snsService.sns.repository.PostEntityRepository;
import com.snsService.sns.repository.UserEntityRepository;
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


    @Transactional
    public  void create(String title,String body,String userName){

       UserEntity userEntity=
               userEntityRepository.findByUserName(userName).orElseThrow(()->
                       new SnsApplicationException(ErrorCode.USER_NOT_FOUND,String.format("%s not founded",userName)));

      postEntityRepository.save(PostEntity.of(title,body,userEntity));
    }

    public Post modify(String  title, String  body, String userName, Integer postId){
        UserEntity userEntity=
                userEntityRepository.findByUserName(userName).orElseThrow(()->
                        new SnsApplicationException(ErrorCode.USER_NOT_FOUND,String.format("%s not founded",userName)));


        //post exist
       PostEntity postEntity=  postEntityRepository.findById(postId).orElseThrow(()->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND,String.format("%s not founded",postId)));
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
        UserEntity userEntity=userEntityRepository.findByUserName(userName).orElseThrow(()->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND,String.format("%s not founded",userName)));

        PostEntity postEntity=  postEntityRepository.findById(postId).orElseThrow(()->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND,String.format("%s not founded",postId)));

        if (postEntity.getUser()!=userEntity){
            throw  new SnsApplicationException(ErrorCode.INVALID_PERMISSION,String.format("%s has no permission with %s",userName,postId));

        }

        postEntityRepository.delete(postEntity);
    }

    public  Page<Post> list(Pageable pageable){
        return  postEntityRepository.findAll(pageable).map(Post::fromEntity);
    }

    public Page<Post> my(String userName,Pageable pageable){
        UserEntity userEntity=userEntityRepository.findByUserName(userName).orElseThrow(()->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND,String.format("%s not founded",userName)));


      return   postEntityRepository.findAllByUser(userEntity,pageable).map(Post::fromEntity);
    }


    @Transactional
    public  void like(Integer postId,String userName){
        PostEntity postEntity=  postEntityRepository.findById(postId).orElseThrow(()->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND,String.format("%s not founded",postId)));

        UserEntity userEntity=userEntityRepository.findByUserName(userName).orElseThrow(()->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND,String.format("%s not founded",userName)));

        // check liked->throw
        likeEntityRepository.findByUserAndPost(userEntity,postEntity).ifPresent(it->{
            throw  new SnsApplicationException(ErrorCode.ALREADY_LIKED,String.format("userName %s already like post %d",userName,postId));
        });


        likeEntityRepository.save(LikeEntity.of(userEntity,postEntity));
        // likeEntityRepository
    }
    public  int likeCount(Integer postId){
        PostEntity postEntity=  postEntityRepository.findById(postId).orElseThrow(()->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND,String.format("%s not founded",postId)));


        // count like->throw
       List<LikeEntity> likeEntities= likeEntityRepository.findAllByPost(postEntity);

       return likeEntityRepository.countByPost(postEntity);
    }
}

