package lionel.meethere.user.controller;

import com.alibaba.fastjson.JSON;
import lionel.meethere.result.CommonResult;
import lionel.meethere.result.Result;
import lionel.meethere.result.UserResult;
import lionel.meethere.user.entity.User;
import lionel.meethere.user.exception.IncorrectUsernameOrPasswordException;
import lionel.meethere.user.exception.UsernameAlreadyExistException;
import lionel.meethere.user.exception.UsernameNotExistsException;
import lionel.meethere.user.param.LoginParam;
import lionel.meethere.user.param.RegisterParam;
import lionel.meethere.user.service.UserService;
import lionel.meethere.user.session.UserSessionInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;


    @Test
    void when_matched_user_request_to_login_then_dispatch_to_service_and_return_success() throws Exception{
        LoginParam loginParam = new LoginParam("lyb","123456789");
        when(userService.login(loginParam)).thenReturn(new User(1,"lyb","18982170688","123456789",0));
        MvcResult result = mockMvc.perform(
                post("/login")
                        .param("username","lyb")
                        .param("password","123456789")
        ).andReturn();

        verify(userService,times(1)).login(loginParam);
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void when_invalid_username_request_to_login_then_dispatch_to_service_and_return_exception()throws Exception{

        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lu",0);
        session.setAttribute("userSessionInfo",userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/login")
                        .param("username","lu")
                        .param("password","1234567891011121314151617181920")
                        .session(session)
        ).andReturn();

        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(UserResult.INVALID_USERNAME_OR_PASSWORD,res.getCode());
    }

    @Test
    void when_unmatched_user_request_to_login_then_dispatch_to_service_and_return_incorrect() throws Exception{
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",0);
        session.setAttribute("userSessionInfo",userSessionInfo);
        LoginParam loginParam = new LoginParam("lyb","123456789");
        when(userService.login(loginParam)).thenThrow(IncorrectUsernameOrPasswordException.class);
        MvcResult result = mockMvc.perform(
                post("/login")
                        .param("username","lyb")
                        .param("password","123456789")
                        .session(session)
        ).andReturn();

        verify(userService,times(1)).login(loginParam);
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(UserResult.INCORRECT_USERNAME_OR_PASSWORD, res.getCode());
    }

    @Test
    void when_unregisterd_user_request_to_login_then_dispatch_to_service_and_return_incorrect() throws Exception{
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",0);
        session.setAttribute("userSessionInfo",userSessionInfo);
        LoginParam loginParam = new LoginParam("lyb","123456789");
        when(userService.login(loginParam)).thenThrow(UsernameNotExistsException.class);
        MvcResult result = mockMvc.perform(
                post("/login")
                        .param("username","lyb")
                        .param("password","123456789")
                        .session(session)
        ).andReturn();

        verify(userService,times(1)).login(loginParam);
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(UserResult.USERNAME_NOT_EXISTS, res.getCode());
    }


    @Test
    void when_unregistered_user_request_to_regist_then_dispatch_to_service_and_return_success() throws Exception {
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",0);
        session.setAttribute("userSessionInfo",userSessionInfo);
        RegisterParam registerParam = new RegisterParam("lyb","123456789","18982170688");

        MvcResult result = mockMvc.perform(
                post("/register")
                        .param("username","lyb")
                        .param("password","123456789")
                        .param("telephone","18982170688")
                        .session(session)
        ).andReturn();

        verify(userService,times(1)).register(registerParam);
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void when_registered_user_request_to_regist_then_return_user_already_exist() throws Exception {
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",0);
        session.setAttribute("userSessionInfo",userSessionInfo);
        RegisterParam registerParam = new RegisterParam("lyb","123456789","18982170688");
        when(userService.register(registerParam)).thenThrow(UsernameAlreadyExistException.class);
        MvcResult result = mockMvc.perform(
                post("/register")
                .param("username","lyb")
                .param("password","123456789")
                .param("telephone","18982170688")
                .session(session)
                        /*.content(JSON.toJSONString(registerParam))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .session(session)*/
        ).andReturn();

        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(UserResult.USERNAME_ALREADY_EXISTS, res.getCode());
    }

    @Test
    void when_invalid_username_request_to_regist_then_dispatch_to_service_and_return_exception()throws Exception{

        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lu",0);
        session.setAttribute("userSessionInfo",userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/register")
                        .param("username","lu")
                        .param("password","1234567891011121314151617181920")
                        .param("telephone","18982170688")
                        .session(session)
        ).andReturn();

        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(UserResult.INVALID_USERNAME_OR_PASSWORD,res.getCode());
    }

    @Test
    void when_request_to_logout_then_return_sucess() throws Exception {
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",0);
        session.setAttribute("userSessionInfo",userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/logout")
                    .session(session)
        ).andReturn();
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

}