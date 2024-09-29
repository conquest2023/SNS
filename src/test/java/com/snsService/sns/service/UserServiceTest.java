package com.snsService.sns.service;

import com.snsService.sns.exception.SnsApplicationException;
import com.snsService.sns.fixture.UserEntityFixture;
import com.snsService.sns.model.entity.UserEntity;
import com.snsService.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest

public class UserServiceTest {


    @Autowired
    private  UserService userService;



    @MockBean
    private UserEntityRepository userEntityRepository;

    @MockBean
    private BCryptPasswordEncoder encoder;

    @Test
    void 회원가입_정삭적으로_동작하는_경우(){

     String userName="userName";
     String password="password";


      //mocking
      when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
      when(encoder.encode(password)).thenReturn("encrypt_password");
      when(userEntityRepository.save(any())).thenReturn(Optional.of(UserEntityFixture.get(userName,password,1)));


      Assertions.assertDoesNotThrow(()-> userService.join(userName,password));

    }

    @Test
    void 회원가입시_아이디가_있는경우(){

        String userName="userName";
        String password="password";
        UserEntity fixture= UserEntityFixture.get(userName,password,1);


        //mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(mock(fixture)));

        Assertions.assertThrows(SnsApplicationException.class,()->userService.join(userName,password));


    }

    @Test
    void 회원가입시_비밀번호가_틀리는경우(){

        String userName="userName";

        String password="password";
        String wrongPassword="WrongPassword";
        UserEntity fixture= UserEntityFixture.get(userName,password,1);


        //mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(mock(fixture)));

        Assertions.assertThrows(SnsApplicationException.class,()->userService.login(userName,wrongPassword));


    }

    @Test
    void 로그인이_정삭적으로_동작하는_경우(){

        String userName="userName";
        String password="password";

        UserEntity fixture= UserEntityFixture.get(userName,password,1);

        //mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());


        Assertions.assertDoesNotThrow(()-> userService.login(userName,password));

    }


}
