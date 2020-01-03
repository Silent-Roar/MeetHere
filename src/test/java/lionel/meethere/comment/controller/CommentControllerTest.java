package lionel.meethere.comment.controller;

import com.alibaba.fastjson.JSON;
import lionel.meethere.comment.dto.CommentDTO;
import lionel.meethere.comment.entity.Comment;
import lionel.meethere.comment.param.CommentPublishParam;
import lionel.meethere.comment.service.CommentService;
import lionel.meethere.comment.status.CommentStatus;
import lionel.meethere.paging.PageParam;
import lionel.meethere.result.CommonResult;
import lionel.meethere.result.Result;
import lionel.meethere.user.session.UserSessionInfo;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Test
    void when_publish_a_comment_on_site_then_dispatch_to_service_and_return_success() throws Exception{
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lionel",1);
        session.setAttribute("userSessionInfo",userSessionInfo);
        CommentPublishParam publishParam = new CommentPublishParam(1,"不错");
        CommentDTO commentDTO = new CommentDTO();
        BeanUtils.copyProperties(publishParam,commentDTO);
        commentDTO.setReviewerId(userSessionInfo.getId());

        MvcResult result = mockMvc.perform(
                post("/site/comment/publish")
                .param("siteId","1")
                .param("content","不错")
                .session(session)
        ).andReturn();

        verify(commentService,times(1)).publishComment(commentDTO);
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void when_admin_request_to_delete_comment_then_dispatch_to_service_return_success() throws Exception {
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",1);
        session.setAttribute("userSessionInfo",userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/site/comment/delete")
                        .param("commentId","1")
                        .param("reviewerId","1")
                        .session(session)
        ).andReturn();

        verify(commentService,times(1)).deleteComment(1);
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void when_comment_owner_request_to_delete_comment_then_dispatch_to_service_return_success() throws Exception {
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(2,"lyb",0);
        session.setAttribute("userSessionInfo",userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/site/comment/delete")
                        .param("commentId","1")
                        .param("reviewerId","2")
                        .session(session)
        ).andReturn();

        verify(commentService,times(1)).deleteComment(1);
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void when_no_admin_request_to_delete_comment_then_dispatch_to_service_return_success() throws Exception {
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(2,"lyb",0);
        session.setAttribute("userSessionInfo",userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/site/comment/delete")
                        .param("commentId","1")
                        .param("reviewerId","1")
                        .session(session)
        ).andReturn();

        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.FAILED, res.getCode());
    }

    @Test
    void when_admin_request_to_audit_comment_with_success_status_then_dispatch_to_service_return_success() throws Exception {
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",1);
        session.setAttribute("userSessionInfo",userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/site/comment/audit")
                        .param("commentId","1")
                        .param("auditOption","1")
                        .session(session)
        ).andReturn();

        verify(commentService,times(1)).auditComment(1,1);
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void when_admin_request_to_audit_comment_with_failed_status_then_dispatch_to_service_return_success() throws Exception {
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",1);
        session.setAttribute("userSessionInfo",userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/site/comment/audit")
                        .param("commentId","1")
                        .param("auditOption","0")
                        .session(session)
        ).andReturn();

        verify(commentService,times(1)).auditComment(1,2);
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void when_no_admin_request_to_audit_comment_then_dispatch_to_service_return_success() throws Exception {
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",0);
        session.setAttribute("userSessionInfo",userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/site/comment/audit")
                        .param("commentId","1")
                        .param("auditOption","1")
                        .session(session)
        ).andReturn();

        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.ACCESS_DENIED, res.getCode());
    }

    @Test
    void when_request_to_getCommentsBySite_then_dispatch_to_service_return_success() throws Exception{
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lionel",1);
        session.setAttribute("userSessionInfo",userSessionInfo);
        PageParam pageParam = new PageParam( 1,2);

        MvcResult result = mockMvc.perform(
                post("/site/comment/getcomments")
                        .param("siteId","1")
                        .param("pageNum","1")
                        .param("pageSize","2")
                        .session(session)
        ).andReturn();

        verify(commentService,times(1)).getCommentsBySite(1);
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void when_request_to_get_Commnet_By_Id_then_dispatch_to_service_and_return_success() throws Exception{
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lionel",1);
        session.setAttribute("userSessionInfo",userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/site/comment/get")
                        .param("commentId","1")
                        .session(session)
        ).andReturn();

        verify(commentService,times(1)).getCommentById(1);
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void when_request_to_get_CommnetList_By_Id_then_dispatch_to_service_and_return_success() throws Exception{
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lionel",1);
        session.setAttribute("userSessionInfo",userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/site/comment/listNewComments")
                        .param("status", "1")
                        .param("pageNum","1")
                        .param("pageSize","2")
                        .session(session)
        ).andReturn();

        PageParam pageParam = new PageParam(1,2);
        verify(commentService,times(1)).getCommentsByStatus(pageParam,1);
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }
}