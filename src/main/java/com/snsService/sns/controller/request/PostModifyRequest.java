package com.snsService.sns.controller.request;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class PostModifyRequest {

    private  String title;
    private  String  body;

    public PostModifyRequest(String title, String body) {
        this.title = title;
        this.body = body;
    }
}
