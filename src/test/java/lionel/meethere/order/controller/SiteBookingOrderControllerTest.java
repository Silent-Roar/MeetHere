package lionel.meethere.order.controller;

import com.alibaba.fastjson.JSON;
import lionel.meethere.booking.service.SiteBookTimeService;
import lionel.meethere.order.entity.SiteBookingOrder;
import lionel.meethere.order.exception.BookingTimeConflictException;
import lionel.meethere.order.param.SiteBookingOrderAuditParam;
import lionel.meethere.order.param.SiteBookingOrderCreateParam;
import lionel.meethere.order.param.SiteBookingOrderUpdateParam;
import lionel.meethere.order.service.SiteBookingOrderService;
import lionel.meethere.order.status.AuditStatus;
import lionel.meethere.paging.PageParam;
import lionel.meethere.result.CommonResult;
import lionel.meethere.result.OrderResult;
import lionel.meethere.result.Result;
import lionel.meethere.user.session.UserSessionInfo;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(SiteBookingOrderController.class)
class SiteBookingOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SiteBookingOrderService orderService;

    @MockBean
    private SiteBookTimeService siteBookTimeService;

    @Test
    void when_do_createOrder_then_dispatch_to_service_create_and_without_exception_return_success() throws Exception {
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",0);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/order/create")
                        .param("siteId", "1")
                        .param("siteName","篮球场")
                        .param("rent","20.00")
                        .param("startTime","2019-12-25 12:00:00")
                        .param("endTime","2019-12-25 13:00:00")
                        .session(session)
        ).andReturn();

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse("2019-12-25 12:00:00",df);
        LocalDateTime end = LocalDateTime.parse("2019-12-25 13:00:00",df);
        SiteBookingOrderCreateParam createParam = new SiteBookingOrderCreateParam(1,"篮球场",new BigDecimal(20).setScale(2),start,end);
        verify(orderService,times(1)).createSiteBookingOrder(userSessionInfo.getId(),createParam);
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }


    @Test
    void when_audit_order_by_administrator_then_dispatch_to_service_and_return_success() throws  Exception{
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",1);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/order/audit")
                        .param("orderId", "1")
                        .param("auditStatus","1")
                        .session(session)
        ).andReturn();


        verify(orderService,times(1)).auditOrder(anyInt(),anyInt());
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void when_audit_order_by_not_administrator_then_dispatch_to_service_and_return_success() throws  Exception{
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",0);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/order/audit")
                        .param("orderId", "1")
                        .param("auditStatus","1")
                        .session(session)
        ).andReturn();

        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.ACCESS_DENIED, res.getCode());
    }

    @Test
    void when_administrator_cancel_order_then_dispatch_to_service_and_return_success() throws Exception {
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",1);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/order/cancel")
                        .param("orderId", "1")
                        .session(session)
        ).andReturn();

        verify(orderService,times(1)).cancelOrderByAdmin(1);
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void when_not_administrator_cancel_order_then_dispatch_to_service_and_return_success() throws Exception {
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",0);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/order/cancel")
                        .param("orderId", "1")
                        .session(session)
        ).andReturn();

        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.ACCESS_DENIED, res.getCode());
    }

    @Test
    void when_user_cancel_order_then_dispatch_to_service_and_return_success() throws Exception{
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",0);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/order/user/cancel")
                        .param("orderId", "1")
                        .session(session)
        ).andReturn();

        verify(orderService,times(1)).cancelOrderByUser(userSessionInfo.getId(),1);
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }
    @Test
    void when_not_a_user_cancel_order_by_userId_then_deny_and_return_fail() throws Exception{
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",1);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/order/user/cancel")
                        .param("orderId", "1")
                        .session(session)
        ).andReturn();

        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.FAILED, res.getCode());
    }

    @Test
    void when_user_update_order_BookingTime_then_dispatch_to_service_and_return_success() throws Exception {
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",0);
        session.setAttribute("userSessionInfo", userSessionInfo);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime oldStart = LocalDateTime.parse("2019-12-25 12:00:00",df);
        LocalDateTime start = LocalDateTime.parse("2019-12-26 12:00:00",df);
        LocalDateTime end = LocalDateTime.parse("2019-12-26 13:00:00",df);
        SiteBookingOrderUpdateParam updateParam = new SiteBookingOrderUpdateParam(1,1,oldStart,start,end);

        MvcResult result = mockMvc.perform(
                post("/order/user/update")
                        .param("orderId", "1")
                        .param("siteId","1")
                        .param("oldStartTime","2019-12-25 12:00:00")
                        .param("startTime","2019-12-26 12:00:00")
                        .param("endTime","2019-12-26 13:00:00")
                        .session(session)
        ).andReturn();

        verify(orderService,times(1)).updateOrderBookTime(updateParam);
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void when_admin_list_all_orders_then_dispatch_to_service_and_return_success() throws Exception  {
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",1);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/order/list")
                        .param("status", "1")
                        .param("pageNum","1")
                        .param("pageSize","2")
                        .session(session)
        ).andReturn();

        verify(orderService,times(1)).listOrders(1,new PageParam(1,2));
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void when_user_list_all_orders_then_dispatch_to_service_and_return_success() throws Exception  {
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",0);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/order/list")
                        .param("status", "1")
                        .param("pageNum","1")
                        .param("pageSize","2")
                        .session(session)
        ).andReturn();

        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.ACCESS_DENIED, res.getCode());
    }

    @Test
    void when_user_list_his_orders_then_dispatch_to_service_and_return_success() throws Exception {
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",0);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/order/user/list")
                        .param("status", "1")
                        .param("pageNum","1")
                        .param("pageSize","2")
                        .session(session)
        ).andReturn();

        verify(orderService,times(1)).getOrderByUser(1,1,new PageParam(1,2));
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void when_get_order_by_id_then_dispatch_to_service_and_return_success() throws Exception {
        MockHttpSession session = new MockHttpSession();
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",0);
        session.setAttribute("userSessionInfo", userSessionInfo);

        MvcResult result = mockMvc.perform(
                post("/order/get")
                        .param("orderId","1")
                        .session(session)
        ).andReturn();

        verify(orderService,times(1)).getOrderById(1);
        Result<Object> res = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }
}