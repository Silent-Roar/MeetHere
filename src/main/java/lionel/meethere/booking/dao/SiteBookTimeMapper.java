package lionel.meethere.booking.dao;

import lionel.meethere.booking.entity.SiteBookingTime;
import lionel.meethere.order.param.SiteBookingOrderUpdateParam;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Mapper
@Repository
public interface SiteBookTimeMapper {

    @Insert("insert into site_booked_time(id,site_id,start_time,end_time) values (#{id},#{siteId},#{startTime},#{endTime});")
    int insertBookTime(SiteBookingTime bookingTime);

    @Results(
            id = "siteBookingTime",value = {
            @Result(property="id", column="id"),
            @Result(property="siteId", column="site_id"),
            @Result(property="startTime", column="start_time"),
            @Result(property="endTime", column="end_time"),
    }
    )
    @Select("select * from site_booked_time where site_id=#{siteId} and start_time between #{startTime} and #{endTime} or site_id=#{siteId} and end_time between #{startTime} and #{endTime};")
    SiteBookingTime getSiteBookingTimeBetweenStimeAndEtime(@Param("siteId") Integer siteId,
                                                  @Param("startTime")LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime);

    @Delete("delete from site_booked_time where site_id=#{siteId} and start_time=#{startTime};")
    int deleteBookTimeByStartTime(@Param("siteId")Integer siteId,
                                  @Param("startTime")LocalDateTime startTime);

    @Update("update site_booked_time set start_time=#{startTime}, end_time=#{endTime} where site_id=#{siteId} and start_time=#{oldStartTime}")
    int updateBookTime(SiteBookingOrderUpdateParam updateParam);
}
