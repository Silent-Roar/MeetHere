package lionel.meethere.order.dao;

import lionel.meethere.order.entity.SiteBookingOrder;
import lionel.meethere.order.param.SiteBookingOrderUpdateParam;
import lionel.meethere.order.vo.SiteBookingOrderUserVO;
import lionel.meethere.paging.PageParam;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SiteBookingOrderMapper {

    @Insert("insert into site_order(id,user_id,site_id,site_name,rent,status,start_time,end_time) values(#{id},#{userId},#{siteId},#{siteName},#{rent},#{status},#{startTime},#{endTime});")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertOrder(SiteBookingOrder siteBookingOrder);

    @Update("update site_order set status=#{newstatus} where id=#{id};")
    int updateOrderStatus(@Param("id") Integer id,
                          @Param("newstatus") Integer status);

    @Update("update site_order set start_time=#{startTime},end_time=#{endTime} where id=#{orderId};")
    int updateOrderBookTime(SiteBookingOrderUpdateParam updateParam);

    @Results(
            id = "siteBookingOrder", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "siteId", column = "site_id"),
            @Result(property = "siteName", column = "site_name"),
            @Result(property = "rent", column = "rent"),
            @Result(property = "status", column = "status"),
            @Result(property = "startTime", column = "start_time"),
            @Result(property = "endTime", column = "end_time")
    }
    )
    @Select("select * from site_order where id=#{id};")
    SiteBookingOrder getOrderById(Integer id);

    @Results(
            id = "siteBookingOrderUserVO", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "siteId", column = "site_id"),
            @Result(property = "siteName", column = "site_name"),
            @Result(property = "rent", column = "rent"),
            @Result(property = "status", column = "status"),
            @Result(property = "startTime", column = "start_time"),
            @Result(property = "endTime", column = "end_time")
    }
    )
    @Select("select * from site_order where user_id=#{userId} and status=#{status} order by start_time desc limit ${pageParam.pageSize * (pageParam.pageNum - 1)},#{pageParam.pageSize};")
    List<SiteBookingOrderUserVO> getOrderByUser(@Param("userId") Integer userId,
                                                @Param("status") Integer status,
                                                @Param("pageParam")PageParam pageParam);

    @ResultMap("siteBookingOrder")
    @Select("select * from site_order where status=#{status} order by start_time desc limit ${pageParam.pageSize * (pageParam.pageNum - 1)},#{pageParam.pageSize};")
    List<SiteBookingOrder> listOrders(@Param("status") Integer status,
                                             @Param("pageParam") PageParam pageParam);

    @Select("select count(*) from site_order where status=#{status}")
    int getOrderCount(Integer status);

}
