package lionel.meethere.order.controller;

import lionel.meethere.order.exception.BookingTimeConflictException;
import lionel.meethere.order.param.SiteBookingOrderAuditParam;
import lionel.meethere.order.param.SiteBookingOrderCreateParam;
import lionel.meethere.order.param.SiteBookingOrderUpdateParam;
import lionel.meethere.order.service.SiteBookingOrderService;
import lionel.meethere.paging.PageParam;
import lionel.meethere.result.CommonResult;
import lionel.meethere.result.OrderResult;
import lionel.meethere.result.Result;
import lionel.meethere.user.session.UserSessionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@CrossOrigin
@RequestMapping("/order/")
public class SiteBookingOrderController {

    @Autowired
    private SiteBookingOrderService orderService;

    //OK
    @PostMapping("create")
    public Result<?> createOrder(@SessionAttribute UserSessionInfo userSessionInfo,
                                 @RequestParam Integer siteId,
                                 @RequestParam String siteName,
                                 @RequestParam BigDecimal rent,
                                 @RequestParam  String startTime,
                                 @RequestParam  String endTime){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(startTime,df);
        LocalDateTime end = LocalDateTime.parse(endTime,df);
        SiteBookingOrderCreateParam createParam = new SiteBookingOrderCreateParam(siteId,siteName,rent,start,end);
        try {
            orderService.createSiteBookingOrder(userSessionInfo.getId(),createParam);
        }catch (BookingTimeConflictException e){
            return OrderResult.reserveTimeConflict();
        }
        return CommonResult.success();
    }

    @PostMapping("audit")
    public Result<?> auditOrder(@SessionAttribute UserSessionInfo userSessionInfo,
                                @RequestParam  Integer orderId,
                                @RequestParam Integer auditStatus) {

        SiteBookingOrderAuditParam auditParam = new SiteBookingOrderAuditParam(orderId,auditStatus);
        if(userSessionInfo.getAdmin() != 1) {
            return CommonResult.accessDenied();
        }

        orderService.auditOrder(auditParam.getOrderId(),auditParam.getAuditStatus());
        return CommonResult.success();
    }

    @PostMapping("cancel")
    public Result<?> cancelOrderByAdmin(@SessionAttribute UserSessionInfo userSessionInfo,
                                        @RequestParam Integer orderId){
        if(userSessionInfo.getAdmin() != 1) {
            return CommonResult.accessDenied();
        }

        orderService.cancelOrderByAdmin(orderId);
        return CommonResult.success();
    }
//OK
    @PostMapping("user/cancel")
    public Result<?> cancelOrderByUser(@SessionAttribute UserSessionInfo userSessionInfo,
                                       @RequestParam Integer orderId){
        if(userSessionInfo.getAdmin() != 0) {
            return CommonResult.failed();
        }

        orderService.cancelOrderByUser(userSessionInfo.getId(),orderId);
        return CommonResult.success();
    }
//OK
    @PostMapping("user/update")
    public Result<?> updateOrderBookingTime(@SessionAttribute UserSessionInfo userSessionInfo,
                                            @RequestParam Integer orderId,
                                            @RequestParam Integer siteId,
                                            @RequestParam  String oldStartTime,
                                            @RequestParam  String startTime,
                                            @RequestParam  String endTime){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime oldStart = LocalDateTime.parse(oldStartTime,df);
        LocalDateTime start = LocalDateTime.parse(startTime,df);
        LocalDateTime end = LocalDateTime.parse(endTime,df);
        SiteBookingOrderUpdateParam updateParam = new SiteBookingOrderUpdateParam(orderId,siteId,oldStart,start,end);

        try{
            orderService.updateOrderBookTime(updateParam);
        }catch (BookingTimeConflictException e){
            return OrderResult.reserveTimeConflict();
        }
        return CommonResult.success();
    }
    @PostMapping("list")
    public Result<?> listOrder(@SessionAttribute UserSessionInfo userSessionInfo,
                               @RequestParam Integer status,
                               @RequestParam Integer pageNum,
                               @RequestParam Integer pageSize){
        PageParam pageParam = new PageParam(pageNum,pageSize);

        if(userSessionInfo.getAdmin() != 1) {
            return CommonResult.accessDenied();
        }

        return CommonResult.success().data(orderService.listOrders(status,pageParam)).total(orderService.getOrderCount(status));
    }
//OK
    @PostMapping("user/list")
    public Result<?> listUserOrder(@SessionAttribute UserSessionInfo userSessionInfo,
                                   @RequestParam Integer status,
                                   @RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize){

        PageParam pageParam = new PageParam(pageNum,pageSize);
        return CommonResult.success().data(orderService.getOrderByUser(userSessionInfo.getId(),status,pageParam)).total(orderService.getOrderCount(status));
    }

    @PostMapping("get")
    public Result<?> getOrderById(@SessionAttribute UserSessionInfo userSessionInfo,
                                  @RequestParam Integer orderId){

        return CommonResult.success().data(orderService.getOrderById(orderId));
    }

}
