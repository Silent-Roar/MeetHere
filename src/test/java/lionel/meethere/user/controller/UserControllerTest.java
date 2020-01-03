package lionel.meethere.user.controller;

import com.alibaba.fastjson.JSON;
import lionel.meethere.paging.PageParam;
import lionel.meethere.result.CommonResult;
import lionel.meethere.result.Result;
import lionel.meethere.user.service.UserService;
import lionel.meethere.user.session.UserSessionInfo;
import lionel.meethere.user.vo.UserVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void when_user_update_username_should_dispatcher_to_service_and_return_success_result() throws Exception {
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",0);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/user/update/username")
                        .param("newName", "xyl")
                        .session(session)
        ).andReturn();

        verify(userService,times(1)).updateUsername(1,"xyl");
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void when_user_update_password_should_dispatcher_to_service_and_return_success_result() throws Exception {
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",0);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/user/update/password")
                        .param("oldPassword", "123")
                        .param("newPassword","321")
                        .session(session)
        ).andReturn();

        verify(userService,times(1)).updatePassword(userSessionInfo,"123","321");
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());

    }


    @Test
    void when_user_update_telephone_should_dispatcher_to_service_and_return_success_result() throws Exception {MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",0);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/user/update/telephone")
                        .param("telephone", "1232131546")
                        .session(session)
        ).andReturn();

        verify(userService,times(1)).updateTelephone(userSessionInfo.getId(),"1232131546");
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());

    }

    @Test
    void when_an_administrator_request_to_update_user_permission_should_dispatcher_to_service_and_return_success_result() throws Exception {
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",1);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/user/update/permission")
                        .param("userId","2")
                        .param("permission", "1")
                        .session(session)
        ).andReturn();

        verify(userService,times(1)).updatePermission(2,1);
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void when_no_administrator_request_to_update_user_permission_should_return_denied_result() throws Exception {
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",0);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/user/update/permission")
                        .param("userId","2")
                        .param("permission", "1")
                        .session(session)
        ).andReturn();

        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.ACCESS_DENIED, res.getCode());
    }


    @Test
    void when_an_administrator_request_to_list_user_should_dispatcher_to_service_and_return_success_result() throws Exception {

        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo();
        userSessionInfo.setAdmin(1);
        userSessionInfo.setId(1);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                get("/user/list")
                        .param("pageNum", "1")
                        .param("pageSize", "1")
                        .session(session)
        ).andReturn();

        verify(userService).getUserList(new PageParam(1,1));
        verify(userService).getCountOfUser();

        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void when_no_administrator_request_to_list_user_should_return_denied_result() throws Exception {

        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo();
        userSessionInfo.setAdmin(0);
        userSessionInfo.setId(1);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/user/list")
                        .param("pageNum", "1")
                        .param("pageSize", "1")
                        .session(session)
        ).andReturn();

        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.ACCESS_DENIED, res.getCode());
    }

    @Test
    void when_request_to_get_user_by_id_should_dispatcher_service_to_return_success_result() throws Exception{

        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",1);
        session.setAttribute("userSessionInfo", userSessionInfo);
        when(userService.getUserById(1)).thenReturn(new UserVO(1,"lyb","1234312431"));
        MvcResult result = mockMvc.perform(
                post("/user/get")
                        .param("id", "1")
                        .session(session)
        ).andReturn();

        verify(userService).getUserById(1);
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void when_an_administrator_request_to_delete_user_should_dispatch_service_to_return_success_result() throws Exception{

        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",1);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/user/delete")
                        .param("userId", "2")
                        .session(session)
        ).andReturn();

        verify(userService,times(1)).deleteUserById(2);
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void  when_not_administrator_request_to_delete_user_should_return_denied_result() throws Exception {

        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",0);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/user/delete")
                        .param("userId", "2")
                        .session(session)
        ).andReturn();

        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.ACCESS_DENIED, res.getCode());
    }
}