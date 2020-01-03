package lionel.meethere.stadium.controller;

import com.alibaba.fastjson.JSON;
import lionel.meethere.paging.PageParam;
import lionel.meethere.result.CommonResult;
import lionel.meethere.result.Result;
import lionel.meethere.stadium.entity.Stadium;
import lionel.meethere.stadium.service.StadiumService;
import lionel.meethere.user.session.UserSessionInfo;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
@WebMvcTest(StadiumController.class)
class StadiumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StadiumService stadiumService;

    @Test
    void when_list_stadium_then_dispatcher_to_service_and_return_success_result() throws Exception {
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",1);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/stadium/list")
                        .param("pageNum", "1")
                        .param("pageSize","10")
                        .session(session)
        ).andReturn();

        verify(stadiumService,times(1)).getStadiums(new PageParam(1,10));
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void when_get_stadium_by_id_then_dispatcher_to_service_and_return_success_result() throws Exception {
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",1);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/stadium/get")
                        .param("id", "1")
                        .session(session)
        ).andReturn();

        verify(stadiumService,times(1)).getStadiumById(1);
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void when_create_stadium_by_administer_then_dispatch_to_service_and_return_success_result() throws Exception{
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",1);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/stadium/create")
                        .param("name", "软测体育馆")
                        .param("location","中山北路")
                        .param("image","null")
                        .session(session)
        ).andReturn();

        Stadium stadium = new Stadium(null,"软测体育馆","中山北路",null);
        verify(stadiumService,times(1)).createStadium(any());
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void when_create_stadium_by_common_user_then_dispatch_to_service_and_return_denied_result() throws Exception{
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",0);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/stadium/create")
                        .param("name", "软测体育馆")
                        .param("location","中山北路")
                        .param("image","null")
                        .session(session)
        ).andReturn();

        Stadium stadium = new Stadium(1,"软测体育馆","中山北路","null");
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.ACCESS_DENIED, res.getCode());
    }

    @Test
    void when_delete_Stadium_by_administrator_then_dispatch_to_service_and_return_success_result()throws Exception {
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",1);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/stadium/delete")
                        .param("id", "1")
                        .session(session)
        ).andReturn();

        verify(stadiumService,times(1)).delteStadium(1);
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void when_delete_Stadium_by_common_user_then_dispatch_to_service_and_return_denied_result()throws Exception {
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",0);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/stadium/delete")
                        .param("id", "1")
                        .session(session)
        ).andReturn();

        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.ACCESS_DENIED, res.getCode());
    }

    @Test
    void when_update_Stadium_by_administrator_then_dispatch_to_service_and_return_success_result() throws Exception{
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",1);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/stadium/update")
                        .param("id", "1")
                        .param("name", "软测体育馆")
                        .param("location","中山北路")
                        .param("image","null")
                        .session(session)
        ).andReturn();

        Stadium stadium = new Stadium(1,"软测体育馆","中山北路","null");
        verify(stadiumService,times(1)).updateStadium(stadium);
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void when_update_Stadium_by_common_user_then_dispatch_to_service_and_return_denied_result() throws Exception{
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",0);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/stadium/update")
                        .param("id", "1")
                        .param("name", "软测体育馆")
                        .param("location","中山北路")
                        .param("image","null")
                        .session(session)
        ).andReturn();

        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.ACCESS_DENIED, res.getCode());
    }
}