package com.snsService.sns.controller;

import com.snsService.sns.controller.request.UserJoinRequest;
import com.snsService.sns.controller.request.UserLoginRequest;
import com.snsService.sns.controller.response.AlarmResponse;
import com.snsService.sns.controller.response.Response;
import com.snsService.sns.controller.response.UserJoinResponse;
import com.snsService.sns.controller.response.UserLoginResponse;
import com.snsService.sns.model.User;
import com.snsService.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

    private  final UserService userService;


    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request){

      User user= userService.join(request.getName(),request.getPassword());

        UserJoinResponse response=UserJoinResponse.fromUser(user);

        return  Response.success(UserJoinResponse.fromUser(user));
    }


    @PostMapping("/login")
    public  Response<UserLoginResponse> login(@RequestBody UserLoginRequest request){
       String token= userService.login(request.getUserName(),request.getPassword());
       return  Response.success(new UserLoginResponse(token));
    }

    @GetMapping("/alarm")
    public  Response<Page<AlarmResponse>> alarm(Pageable pageable, Authentication authentication){
       return Response.success(userService.alarmList(authentication.getName(),pageable).map(AlarmResponse::fromAlarm));
    }
}
