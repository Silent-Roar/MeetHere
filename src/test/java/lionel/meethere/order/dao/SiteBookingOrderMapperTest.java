package lionel.meethere.order.dao;


import lionel.meethere.order.entity.SiteBookingOrder;
import lionel.meethere.order.param.SiteBookingOrderUpdateParam;
import lionel.meethere.order.status.OrderStatus;
import lionel.meethere.order.vo.SiteBookingOrderUserVO;
import lionel.meethere.paging.PageParam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;


@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SiteBookingOrderMapperTest {

    @Autowired
    private SiteBookingOrderMapper orderMapper;
    private LocalDateTime startTime = LocalDateTime.now();
    private LocalDateTime endTime = LocalDateTime.now().plus(Duration.ofHours(1));

    @BeforeEach
    void setup(){
        this.orderMapper.insertOrder(new SiteBookingOrder(2,1,2,"场地1",new BigDecimal(20),0, startTime,endTime));
        this.orderMapper.insertOrder(new SiteBookingOrder(3,1,3,"场地1",new BigDecimal(20),1, startTime,endTime));
        this.orderMapper.insertOrder(new SiteBookingOrder(4,1,3,"场地1",new BigDecimal(20),0, startTime,endTime));
        this.orderMapper.insertOrder(new SiteBookingOrder(5,2,3,"场地1",new BigDecimal(20),0,startTime,endTime));
    }

    @Test
    @Transactional
    void when_insert_a_order_should_insert_success(){

        SiteBookingOrder order = new SiteBookingOrder(1,1,2,"场地1",new BigDecimal(20),0, startTime,endTime);
        this.orderMapper.insertOrder(order);

        SiteBookingOrder order_inserted = orderMapper.getOrderById(1);
        Assertions.assertAll                                                                                                                                                                                                                                                                                                                                          (
                () -> assertEquals(1,order_inserted.getId()),
                () -> assertEquals(1,order_inserted.getUserId()),
                () -> assertEquals(2,order_inserted.getSiteId()),
                () -> assertEquals("场地1",order_inserted.getSiteName()),
                () -> assertEquals(new BigDecimal(20).setScale(2),order_inserted.getRent()),
                () -> assertEquals(0,order_inserted.getStatus()),
                () -> assertEquals(startTime,order_inserted.getStartTime()),
                () -> assertEquals(endTime,order_inserted.getEndTime())
        );
    }


    @ParameterizedTest
    @MethodSource("orderStatusProvider")
    void when_update_order_status_should_update_success(Integer orderStatus, Integer orderId){

        orderMapper.updateOrderStatus(orderId,orderStatus);
        assertEquals(orderStatus,orderMapper.getOrderById(orderId).getStatus());
    }
    static Stream<Arguments>  orderStatusProvider() {
        return Stream.of(
                arguments(OrderStatus.AUDITED_FAILED, 2),
                arguments(OrderStatus.AUDITED, 2),
                arguments(OrderStatus.UNAUDITED, 2),
                arguments(OrderStatus.CANCEL, 2)
        );
    }
    @Test
    void when_update_a_order_with_updateParam_should_update_the_property(){
         LocalDateTime newStartTime = LocalDateTime.now();
         LocalDateTime newEndTime = LocalDateTime.now().plus(Duration.ofHours(1));

        orderMapper.updateOrderBookTime(
                new SiteBookingOrderUpdateParam(2,1,startTime,newStartTime,newEndTime));

        SiteBookingOrder order = orderMapper.getOrderById(2);

        Assertions.assertAll(
                () -> assertEquals(newStartTime,order.getStartTime()),
                () -> assertEquals(newEndTime,order.getEndTime())
        );
    }

    @Test
    void when_get_order_by_valid_Id_should_return_a_order(){
        assertNotNull(this.orderMapper.getOrderById(2));
        SiteBookingOrder order = orderMapper.getOrderById(2);
        Assertions.assertAll(
                () -> assertEquals(2,order.getId()),
                () -> assertEquals(1,order.getUserId()),
                () -> assertEquals(2,order.getSiteId()),
                () -> assertEquals("场地1",order.getSiteName()),
                () -> assertEquals(new BigDecimal(20).setScale(2),order.getRent()),
                () -> assertEquals(0,order.getStatus()),
                () -> assertEquals(startTime,order.getStartTime()),
                () -> assertEquals(endTime,order.getEndTime())
        );
    }

    @Test
    void when_get_order_by_invalid_Id_should_return_null(){
        assertNull(this.orderMapper.getOrderById(10));
    }

    @ParameterizedTest
    @MethodSource("orderByUserSourceProvider")
    void when_get_order_by_userId_then_return_the_order_list_of_user(PageParam pageParam,int wsize,int userId,int status){
        List<SiteBookingOrderUserVO> orderUserVOS = this.orderMapper.getOrderByUser(userId,status,pageParam);
        assertEquals(wsize,orderUserVOS.size());
        if(!orderUserVOS.isEmpty())
            assertEquals(status,orderUserVOS.get(0).getStatus());
    }

    static Stream<Arguments>  orderByUserSourceProvider(){
        return Stream.of(
                arguments(new PageParam(1,1),1,1,OrderStatus.UNAUDITED),
                arguments(new PageParam(1,3),2,1,OrderStatus.UNAUDITED),
                arguments(new PageParam(1,3),1,1,OrderStatus.AUDITED),
                arguments(new PageParam(2,1),1,1,OrderStatus.UNAUDITED),
                arguments(new PageParam(2,1),0,1,OrderStatus.AUDITED),
                arguments(new PageParam(1,1),0,1,OrderStatus.CANCEL)
        );
    }

    @ParameterizedTest
    @MethodSource("orderSourceProvider")
    void when_list_orders_then_return_the_list_of_orders(PageParam pageParam,int wsize,int status){
        List<SiteBookingOrder>  orderAdminVOS = this.orderMapper.listOrders(status,pageParam);
        assertEquals(wsize,orderAdminVOS.size());
        if(!orderAdminVOS.isEmpty())
            assertEquals(status,orderAdminVOS.get(0).getStatus());
    }

    static Stream<Arguments>  orderSourceProvider(){
        return Stream.of(
                arguments(new PageParam(1,1),1,OrderStatus.UNAUDITED),
                arguments(new PageParam(1,3),3,OrderStatus.UNAUDITED),
                arguments(new PageParam(1,6),1,OrderStatus.AUDITED),
                arguments(new PageParam(2,2),1,OrderStatus.UNAUDITED),
                arguments(new PageParam(2,1),0,OrderStatus.AUDITED),
                arguments(new PageParam(1,1),0,OrderStatus.CANCEL)
        );
    }

    @Test
    void when_get_order_count_should_return_count(){
        assertEquals(3,orderMapper.getOrderCount(0));
    }

}
