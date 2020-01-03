package lionel.meethere.site.controller;

import com.alibaba.fastjson.JSON;
import lionel.meethere.paging.PageParam;
import lionel.meethere.result.CommonResult;
import lionel.meethere.result.Result;
import lionel.meethere.site.entity.Site;
import lionel.meethere.site.param.SiteUpdateParam;
import lionel.meethere.site.service.SiteService;
import lionel.meethere.user.session.UserSessionInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(SiteController.class)
class SiteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SiteService siteService;

    @Test
    void when_request_to_get_a_site_then_dispatch_to_service_and_return_suceccess() throws Exception {
        MvcResult result = mockMvc.perform(
                post("/site/get")
                        .param("id","1")
        ).andReturn();

        verify(siteService,times(1)).getSiteById(1);
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void when_request_to_get_Site_List_then_dispatch_to_serve_and_return_success() throws Exception{
        MvcResult result = mockMvc.perform(
                post("/site/list")
                        .param("pageNum","1")
                        .param("pageSize","2")
        ).andReturn();

        PageParam pageParam = new PageParam(1,2);
        verify(siteService,times(1)).getSites(pageParam);
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void when_request_to_get_Site_List_by_stadium_then_dispatch_to_serve_and_return_success() throws Exception{
        MvcResult result = mockMvc.perform(
                post("/site/listByStadium")
                        .param("pageNum","1")
                        .param("pageSize","2")
                        .param("id","1")
        ).andReturn();

        PageParam pageParam = new PageParam(1,2);
        verify(siteService,times(1)).getSiteCountByStadium(1);
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void when_request_to_get_a_site_by_site_then_dispatch_to_service_and_return_suceccess() throws Exception{
        MvcResult result = mockMvc.perform(
                post("/site/get")
                        .param("id","1")
        ).andReturn();

        verify(siteService,times(1)).getSiteById(1);
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }


    @Test
    void when_create_site_by_administer_then_dispatch_to_service_and_return_success_result() throws Exception{
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",1);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/site/create")
                        .param("name", "篮球场")
                        .param("stadiumId","1")
                        .param("location","中山北路")
                        .param("description","设施齐全")
                        .param("rent","20")
                        .param("image","null")
                        .session(session)
        ).andReturn();

        Site site = new Site(0, "篮球场", 1, "中山北路", "设施齐全", new BigDecimal(20), null);
        verify(siteService,times(1)).createSite(site);
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void when_create_site_by_common_user_then_dispatch_to_service_and_return_denied_result() throws Exception{
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",0);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/site/create")
                        .param("name", "篮球场")
                        .param("stadiumId","1")
                        .param("location","中山北路")
                        .param("description","设施齐全")
                        .param("rent","20.00")
                        .param("image","null")
                        .session(session)
        ).andReturn();

        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.ACCESS_DENIED, res.getCode());
    }

    @Test
    void when_delete_site_by_administrator_then_dispatch_to_service_and_return_success_result()throws Exception {
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",1);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/site/delete")
                        .param("id", "1")
                        .session(session)
        ).andReturn();

        verify(siteService,times(1)).deleteSite(1);
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void when_delete_site_by_common_user_then_dispatch_to_service_and_return_denied_result()throws Exception {
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",0);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/site/delete")
                        .param("id", "1")
                        .session(session)
        ).andReturn();

        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.ACCESS_DENIED, res.getCode());
    }

    @Test
    void when_update_site_by_administrator_then_dispatch_to_service_and_return_success_result() throws Exception{
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",1);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/site/update")
                        .session(session)
                        .param("id","1")
                        .param("name", "篮球场")
                        .param("stadiumId","1")
                        .param("location","中山北路")
                        .param("description","设施齐全")
                        .param("rent","20.00")
                        .param("image","null")
        ).andReturn();

        SiteUpdateParam siteUpdateParam = new SiteUpdateParam(1, "篮球场", 1, "中山北路", "设施齐全", new BigDecimal(20), null);
        //verify(siteService,times(1)).updateSite(any());
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void when_update_site_by_common_user_then_dispatch_to_service_and_return_denied_result() throws Exception{
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",0);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/site/update")
                        .session(session)
                        .param("id","1")
                        .param("name", "篮球场")
                        .param("stadiumId","1")
                        .param("location","中山北路")
                        .param("description","设施齐全")
                        .param("rent","20.00")
                        .param("image","null")
        ).andReturn();

        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.ACCESS_DENIED, res.getCode());
    }
}