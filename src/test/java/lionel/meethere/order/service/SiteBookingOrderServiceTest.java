package lionel.meethere.order.service;

import lionel.meethere.booking.service.SiteBookTimeService;
import lionel.meethere.order.dao.SiteBookingOrderMapper;
import lionel.meethere.order.entity.SiteBookingOrder;
import lionel.meethere.order.exception.BookingTimeConflictException;
import lionel.meethere.order.exception.UserIdNotMatchOrderException;
import lionel.meethere.order.exception.WrongOrderStatusException;
import lionel.meethere.order.param.SiteBookingOrderCreateParam;
import lionel.meethere.order.param.SiteBookingOrderUpdateParam;
import lionel.meethere.order.status.AuditStatus;
import lionel.meethere.order.status.OrderStatus;
import lionel.meethere.paging.PageParam;
import lionel.meethere.site.entity.Site;
import lionel.meethere.site.service.SiteService;
import lionel.meethere.stadium.entity.Stadium;
import lionel.meethere.stadium.service.StadiumService;
import lionel.meethere.stadium.vo.StadiumVO;
import lionel.meethere.user.service.UserService;
import lionel.meethere.user.vo.UserVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class SiteBookingOrderServiceTest {

    @Mock
    private SiteBookingOrderMapper orderMapper;

    @Mock
    private SiteBookTimeService bookTimeService;

    @Mock
    private SiteService siteService;

    @Mock
    private StadiumService stadiumService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SiteBookingOrderService orderService;

    @Test
    void when_do_crateSiteBookingOrder_and_not_time_conflict_then_dispatch_to_mapper_create_order() {
        LocalDateTime localDateTime = LocalDateTime.now();
        SiteBookingOrderCreateParam createParam = new SiteBookingOrderCreateParam(1,"site1",new BigDecimal(20),localDateTime, localDateTime);
        SiteBookingOrder order = new SiteBookingOrder(null,1,1,"site1",new BigDecimal(20), OrderStatus.UNAUDITED,localDateTime, localDateTime);
        when(bookTimeService.tryBooking(createParam.getSiteId(),createParam.getStartTime(),createParam.getEndTime())).thenReturn(true);

        orderService.createSiteBookingOrder(1,createParam);
        verify(bookTimeService).tryBooking(createParam.getSiteId(),createParam.getStartTime(),createParam.getEndTime());
        verify(orderMapper).insertOrder(order);
    }

    @Test
    void when_do_crateSiteBookingOrder_and_have_time_conflict_then_throw_BookingTimeConflictException() {
        LocalDateTime localDateTime = LocalDateTime.now();
        SiteBookingOrderCreateParam createParam = new SiteBookingOrderCreateParam(1,"site1",new BigDecimal(20),localDateTime, localDateTime);
        when(bookTimeService.tryBooking(createParam.getSiteId(),createParam.getStartTime(),createParam.getEndTime())).thenReturn(false);

        assertThrows(BookingTimeConflictException.class,()->orderService.createSiteBookingOrder(1,createParam));
    }

    @ParameterizedTest
    @MethodSource("wrongStatusProvider")
    void when_do_auditOrder_with_a_wrong_status_order_then_throw_WrongOrderStatusException(Integer orderStatus) {
        SiteBookingOrder order = new SiteBookingOrder();
        order.setStatus(orderStatus);
        order.setId(1);

        when(orderMapper.getOrderById(1)).thenReturn(order);

        assertThrows(WrongOrderStatusException.class,()->orderService.auditOrder(1, AuditStatus.SUCCESS));
    }

    static Stream<Integer> wrongStatusProvider(){
        return Stream.of(OrderStatus.CANCEL,OrderStatus.AUDITED,OrderStatus.AUDITED_FAILED);
    }
    @Test
    void when_do_auditOrder_with_unaudited_status_order_and_success_audit_option_then_mapper_update_order_status_as_AUDITED() {
        SiteBookingOrder order = new SiteBookingOrder();
        order.setStatus(OrderStatus.UNAUDITED);
        order.setId(1);

        when(orderMapper.getOrderById(1)).thenReturn(order);

        orderService.auditOrder(1,AuditStatus.SUCCESS);
        verify(orderMapper).updateOrderStatus(1,OrderStatus.AUDITED);
    }

    @Test
    void when_do_auditOrder_with_unaudited_status_order_and_fail_audit_option_then_mapper_update_order_status_as_AUDITED_FAILED() {
        SiteBookingOrder order = new SiteBookingOrder();
        order.setStatus(OrderStatus.UNAUDITED);
        order.setId(1);

        when(orderMapper.getOrderById(1)).thenReturn(order);

        orderService.auditOrder(1,AuditStatus.FAILED);
        verify(orderMapper).updateOrderStatus(1,OrderStatus.AUDITED_FAILED);
    }

    @Test
    void when_do_cancelOrderByUser_and_userId_unequal_orderId_then_throw_UserIdNotMatchOrderException() {
        Integer orderId = 1;
        Integer userId = 2;
        SiteBookingOrder order = new SiteBookingOrder();
        order.setId(orderId);
        order.setUserId(userId);

        when(orderMapper.getOrderById(orderId)).thenReturn(order);

        assertThrows(UserIdNotMatchOrderException.class,()->orderService.cancelOrderByUser(3,orderId));
        verify(orderMapper).getOrderById(orderId);

    }

    @ParameterizedTest
    @MethodSource("orderStatusProvider")
    void when_do_cancelOrderByUser_and_userId_equal_orderId_with_right_order_status_then_mapper_delete_order_and_booking_time(Integer orderStatus) {

        Integer userId =2;
        Integer orderId =1;
        SiteBookingOrder order = new SiteBookingOrder();
        order.setId(orderId);
        order.setUserId(userId);
        order.setStatus(orderStatus);

        when(orderMapper.getOrderById(orderId)).thenReturn(order);

        orderService.cancelOrderByUser(userId,orderId);

        verify(orderMapper).updateOrderStatus(orderId,OrderStatus.CANCEL);
        verify(bookTimeService).cancelSiteBookTime(order.getSiteId(),order.getStartTime());
    }

    static Stream<Integer> orderStatusProvider(){
        return Stream.of(OrderStatus.AUDITED,OrderStatus.UNAUDITED,OrderStatus.AUDITED_FAILED);
    }

    @ParameterizedTest
    @MethodSource("orderStatusProvider")
    void when_do_cancelOrderByAdmin_with_right_order_status_then_mapper_delete_order_and_booking_time(Integer orderStatus) {
        Integer orderId =1;
        SiteBookingOrder order = new SiteBookingOrder();
        order.setId(orderId);
        order.setStatus(orderStatus);

        when(orderMapper.getOrderById(orderId)).thenReturn(order);

        orderService.cancelOrderByAdmin(orderId);

        verify(orderMapper).updateOrderStatus(orderId,OrderStatus.CANCEL);
        verify(bookTimeService).cancelSiteBookTime(order.getSiteId(),order.getStartTime());
    }

    @Test
    void when_do_updateOrderBookTime_and_have_time_conflict_then_throw_BookingTimeConflictException() {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime oldStartTime = localDateTime;
        LocalDateTime newStartTime = localDateTime;
        LocalDateTime newEndTime = localDateTime;
        SiteBookingOrderUpdateParam updateParam = new SiteBookingOrderUpdateParam(1,1,oldStartTime,newStartTime,newEndTime);
        when(bookTimeService.tryUpdateBookingTime(updateParam.getSiteId(),updateParam.getOldStartTime(),updateParam.getStartTime(),updateParam.getEndTime())).thenReturn(false);
        assertThrows(BookingTimeConflictException.class,()->orderService.updateOrderBookTime(updateParam));
        verify(bookTimeService).tryUpdateBookingTime(updateParam.getSiteId(),updateParam.getOldStartTime(),updateParam.getStartTime(),updateParam.getEndTime());
    }

    @Test
    void when_do_updateOrderBookTime_and_no_conflict_then_update_order_and_booking_time() {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime oldStartTime = localDateTime;
        LocalDateTime newStartTime = localDateTime;
        LocalDateTime newEndTime = localDateTime;
        SiteBookingOrderUpdateParam updateParam = new SiteBookingOrderUpdateParam(1,1,oldStartTime,newStartTime,newEndTime);
        when(bookTimeService.tryUpdateBookingTime(updateParam.getSiteId(),updateParam.getOldStartTime(),updateParam.getStartTime(),updateParam.getEndTime())).thenReturn(true);

        orderService.updateOrderBookTime(updateParam);

        verify(bookTimeService).updateSiteBookTime(updateParam);
        verify(orderMapper).updateOrderBookTime(updateParam);
        verify(orderMapper).updateOrderStatus(updateParam.getSiteId(),OrderStatus.UNAUDITED);

    }

    @Test
    void when_do_getOrderById_then_return_OrderAdminVO() {
        Integer orderId = 1;
        SiteBookingOrder order = new SiteBookingOrder();
        order.setId(1);
        order.setSiteId(2);
        order.setUserId(4);
        Site site = new Site();
        site.setStadiumId(3);
        when(orderMapper.getOrderById(1)).thenReturn(order);
        when(siteService.getSiteById(2)).thenReturn(site);
        when(stadiumService.getStadiumById(3)).thenReturn(new StadiumVO());
        when(userService.getUserById(4)).thenReturn(new UserVO());

        orderService.getOrderById(1);
        verify(orderMapper).getOrderById(1);
    }

    @Test
    void when_do_getOrderByUser_then_mapper_get_user_Order() {
        PageParam pageParam = new PageParam(1,1);

        orderService.getOrderByUser(1,OrderStatus.AUDITED,pageParam);
        verify(orderMapper).getOrderByUser(1,OrderStatus.AUDITED,pageParam);
    }

    @Test
    void when_do_list_orders_then_mapper_get_list_of_site_of_Order() {
        PageParam pageParam = new PageParam(1,3);

        orderService.listOrders(OrderStatus.AUDITED,pageParam);
        verify(orderMapper).listOrders(OrderStatus.AUDITED,pageParam);
    }

    @Test
    void getOrderCount() {
        orderService.getOrderCount(1);
        verify(orderMapper).getOrderCount(1);
    }
}