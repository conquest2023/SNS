package com.snsService.sns.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snsService.sns.controller.request.UserJoinRequest;
import com.snsService.sns.controller.request.UserLoginRequest;
import com.snsService.sns.exception.ErrorCode;
import com.snsService.sns.exception.SnsApplicationException;
import com.snsService.sns.model.User;
import com.snsService.sns.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserService userService;
    @Test
    public  void 회원가입() throws Exception {
        String userName="userName";
        String password="password";

        when(userService.join(userName,password)).thenReturn(mock(User.class));

        mvc.perform(post("/api/v1/users/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName,password))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public  void 회원가입시_이미_회원가입된_아이디() throws  Exception{
        String userName="userName";
        String password="password";

        when(userService.join(userName,password)).thenThrow(new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME,""));


        mvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName,password))))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public  void 로그인시_회원가입_안된_유저이름_입력() throws Exception {
        String userName="userName";
        String password="password";

        when(userService.login(userName,password)).thenThrow(new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME,""));

        mvc.perform(post("/api/v1/users/join/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName,password))))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    @Test
    public  void 로그인시_비밀번호_틀리게_입력() throws Exception {
        String userName="userName";
        String password="password";

        when(userService.login(userName,password)).thenThrow(new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME,""));


        mvc.perform(post("/api/v1/users/join/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName,password))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


    @Test
    public  void 로그인() throws Exception {
        String userName="userName";
        String password="password";

        when(userService.login(userName,password)).thenReturn("testToken");

        mvc.perform(post("/api/v1/users/join/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName,password))))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser
    void 알람기능() throws  Exception{
        when(userService.alarmList(any(),any())).thenReturn(Page.empty());
        mvc.perform(get("/api/v1/users/alarm")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 알람리스트요청시_로그인하지_않은경우() throws  Exception{
        when(userService.alarmList(any(),any())).thenReturn(Page.empty());
        mvc.perform(get("/api/v1/users/alarm")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
