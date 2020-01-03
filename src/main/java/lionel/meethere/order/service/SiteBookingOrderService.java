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
import lionel.meethere.order.vo.SiteBookingOrderAdminVO;
import lionel.meethere.order.vo.SiteBookingOrderUserVO;
import lionel.meethere.paging.PageParam;

import lionel.meethere.site.entity.Site;
import lionel.meethere.site.service.SiteService;
import lionel.meethere.stadium.service.StadiumService;
import lionel.meethere.stadium.vo.StadiumVO;
import lionel.meethere.user.service.UserService;
import lionel.meethere.user.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class SiteBookingOrderService {

    @Autowired
    private SiteBookingOrderMapper siteBookingOrderMapper;

    @Autowired
    private SiteBookTimeService siteBookTimeService;

    @Autowired
    private StadiumService stadiumService;

    @Autowired
    private SiteService siteService;

    @Autowired
    private UserService userService;


    public void createSiteBookingOrder(Integer userId, SiteBookingOrderCreateParam createParam){
        if(!siteBookTimeService.tryBooking(createParam.getSiteId(),createParam.getStartTime(),createParam.getEndTime())){
            throw new BookingTimeConflictException();
        }
        siteBookTimeService.insertBokingTime(createParam.getSiteId(),createParam.getStartTime(),createParam.getEndTime());
        siteBookingOrderMapper.insertOrder(convertToSiteBookingOrder(userId,createParam));
    }

    private SiteBookingOrder convertToSiteBookingOrder(Integer userId,SiteBookingOrderCreateParam createParam){
        SiteBookingOrder siteBookingOrder = new SiteBookingOrder();
        BeanUtils.copyProperties(createParam,siteBookingOrder);
        siteBookingOrder.setUserId(userId);
        siteBookingOrder.setStatus(OrderStatus.UNAUDITED);

        return siteBookingOrder;
    }

    public void auditOrder(Integer orderId, Integer auditStatus){
        SiteBookingOrder order = siteBookingOrderMapper.getOrderById(orderId);

        if(!order.getStatus().equals(OrderStatus.UNAUDITED)){
            throw new WrongOrderStatusException();
        }

        if(auditStatus.equals(AuditStatus.SUCCESS)){
            siteBookingOrderMapper.updateOrderStatus(orderId,OrderStatus.AUDITED);
        }
        else {
            siteBookingOrderMapper.updateOrderStatus(orderId,OrderStatus.AUDITED_FAILED);
        }

    }

    public void cancelOrderByUser(Integer userId, Integer orderId){
        SiteBookingOrder order = siteBookingOrderMapper.getOrderById(orderId);
        System.out.println("userID"+userId+"orderID"+orderId+"  "+order.getUserId());
        if(!userId.equals(order.getUserId())) {
            throw new UserIdNotMatchOrderException();
        }

        if(!order.getStatus().equals(OrderStatus.CANCEL)){
            siteBookingOrderMapper.updateOrderStatus(orderId,OrderStatus.CANCEL);
            siteBookTimeService.cancelSiteBookTime(order.getSiteId(),order.getStartTime());
        }

    }

    public void cancelOrderByAdmin(Integer orderId){
        SiteBookingOrder order = siteBookingOrderMapper.getOrderById(orderId);

        if(!order.getStatus().equals(OrderStatus.CANCEL)){
            siteBookingOrderMapper.updateOrderStatus(orderId,OrderStatus.CANCEL);
            siteBookTimeService.cancelSiteBookTime(order.getSiteId(),order.getStartTime());
        }
    }

    public void updateOrderBookTime(SiteBookingOrderUpdateParam updateParam){
        if(!siteBookTimeService.tryUpdateBookingTime(updateParam.getSiteId(),updateParam.getOldStartTime(),updateParam.getStartTime(),updateParam.getEndTime())){
            throw new BookingTimeConflictException();
        }
        siteBookTimeService.updateSiteBookTime(updateParam);
        siteBookingOrderMapper.updateOrderBookTime(updateParam);
        siteBookingOrderMapper.updateOrderStatus(updateParam.getOrderId(),OrderStatus.UNAUDITED);

    }

    public SiteBookingOrderAdminVO getOrderById(Integer id){
        return convertToSiteBookingOrderAdminVO(siteBookingOrderMapper.getOrderById(id));
    }

    public List<SiteBookingOrderUserVO> getOrderByUser(Integer userId, Integer status, PageParam pageParam){
        List<SiteBookingOrderUserVO> orderUserVOS = siteBookingOrderMapper.getOrderByUser(userId,status,pageParam);
        for(SiteBookingOrderUserVO vo : orderUserVOS){
            vo.setStadiumName(stadiumService.getStadiumById(siteService.getSiteById(vo.getSiteId()).getStadiumId()).getName());
        }
        return orderUserVOS;
    }


    public List<SiteBookingOrderAdminVO> listOrders(Integer status, PageParam pageParam) {
        return convertToSiteBookingOrderAdminVOList(siteBookingOrderMapper.listOrders(status, pageParam));
    }


    private SiteBookingOrderAdminVO convertToSiteBookingOrderAdminVO(SiteBookingOrder siteBookingOrder){
        SiteBookingOrderAdminVO orderAdminVO = new SiteBookingOrderAdminVO();
        BeanUtils.copyProperties(siteBookingOrder,orderAdminVO);
        UserVO userVO = userService.getUserById(siteBookingOrder.getUserId());
        Site site = siteService.getSiteById(siteBookingOrder.getSiteId());
        StadiumVO stadiumVO = stadiumService.getStadiumById(site.getStadiumId());

        orderAdminVO.setStadiumName(stadiumVO.getName());
        orderAdminVO.setUserId(userVO.getId());
        orderAdminVO.setUserName(userVO.getUsername());
        return orderAdminVO;
    }

    private List<SiteBookingOrderAdminVO>  convertToSiteBookingOrderAdminVOList(List<SiteBookingOrder> siteBookingOrders){
        List<SiteBookingOrderAdminVO> orderAdminVOS = new ArrayList<>();
        for(SiteBookingOrder order : siteBookingOrders){
            orderAdminVOS.add(convertToSiteBookingOrderAdminVO(order));
        }
        return orderAdminVOS;
    }

    public int getOrderCount(Integer status){
        return siteBookingOrderMapper.getOrderCount(status);
    }

}
