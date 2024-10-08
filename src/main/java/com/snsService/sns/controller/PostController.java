package com.snsService.sns.controller;

import com.snsService.sns.controller.request.PostCommentRequest;
import com.snsService.sns.controller.request.PostCreateRequest;
import com.snsService.sns.controller.request.PostModifyRequest;
import com.snsService.sns.controller.response.CommentResponse;
import com.snsService.sns.controller.response.PostResponse;
import com.snsService.sns.controller.response.Response;
import com.snsService.sns.model.Post;
import com.snsService.sns.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private  final PostService postService;
    @PostMapping
    public Response<Void> create(@RequestBody PostCreateRequest request, Authentication authentication){


        postService.create(request.getTitle(),request.getBody(),authentication.getName());

        return  Response.success();

    }
    @PutMapping("/{postId}")
    public Response<PostResponse> modify(@PathVariable Integer postId, @RequestBody PostModifyRequest request, Authentication authentication){


       Post post= postService.modify(request.getTitle(),request.getBody(),authentication.getName(),postId);
        return  Response.success(PostResponse.fromPost(post));

    }

    @DeleteMapping("/{postId}")
    public  Response<Void> delete(@PathVariable Integer postId,Authentication authentication){
        postService.delete(authentication.getName(),postId);

        return  Response.success();
    }

    @GetMapping
    public Response<Page<PostResponse>> list(Pageable pageable, Authentication authentication){
       return Response.success(postService.list(pageable).map(PostResponse::fromPost));
    }


    @GetMapping
    public Response<Page<PostResponse>> my(Pageable pageable, Authentication authentication){
        return Response.success(postService.my(authentication.getName(),pageable).map(PostResponse::fromPost));
    }

    @PostMapping("/{postId}/likes")
    public  Response<Void>like(@PathVariable Integer postId,Authentication authentication){

        postService.like(postId,authentication.getName());
        return Response.success();
    }

    @PostMapping("/{postId}/likes")
    public  Response<Void> likeCount(@PathVariable Integer postId,Authentication authentication){

        postService.like(postId,authentication.getName());
        return Response.success();
    }
    @PostMapping("/{postId}/comments")
    public  Response<Void> comment(@PathVariable Integer postId, @RequestBody PostCommentRequest request, Authentication authentication){
        postService.comment(postId,authentication.getName(),request.getComment());


        return  Response.success();
    }

    @GetMapping("/{postId}/comments")
    public  Response<Page<CommentResponse>> comment(@PathVariable Integer postId, Pageable pageable, @RequestBody PostCommentRequest request, Authentication authentication){
        postService.getComments(postId,pageable);

        return  Response.success(postService.getComments(postId,pageable).map(CommentResponse::fromComment));
    }

}
