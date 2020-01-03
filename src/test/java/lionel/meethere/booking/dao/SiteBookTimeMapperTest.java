package lionel.meethere.booking.dao;

import lionel.meethere.booking.entity.SiteBookingTime;
import lionel.meethere.order.param.SiteBookingOrderUpdateParam;
import lionel.meethere.site.dao.SiteMapper;
import lionel.meethere.site.entity.Site;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SiteBookTimeMapperTest {
    @Autowired
    private SiteBookTimeMapper siteBookTimeMapper;

    private LocalDateTime localDateTime = LocalDateTime.now();
    private LocalDateTime localDateTime2 = LocalDateTime.now().plus(Duration.ofHours(2));
    @BeforeEach
    void setup(){
        this.siteBookTimeMapper.insertBookTime(new SiteBookingTime(2,2, localDateTime,localDateTime2));
        this.siteBookTimeMapper.insertBookTime(new SiteBookingTime(3,2, localDateTime,localDateTime2));
        this.siteBookTimeMapper.insertBookTime(new SiteBookingTime(4,3, localDateTime,localDateTime2));
        this.siteBookTimeMapper.insertBookTime(new SiteBookingTime(5,4, localDateTime,localDateTime2));
    }

    @Test
    @Transactional
    void when_insert_a_siteBookingOrder_should_insert() {
        SiteBookingTime siteBookingTime = new SiteBookingTime(1, 1, localDateTime, localDateTime2);
        this.siteBookTimeMapper.insertBookTime(siteBookingTime);
        SiteBookingTime sReturn = siteBookTimeMapper.getSiteBookingTimeBetweenStimeAndEtime(1, localDateTime, localDateTime2);
        Assertions.assertAll(
                () -> assertEquals(1, sReturn.getId()),
                () -> assertEquals(1, sReturn.getSiteId()),
                () -> assertEquals(localDateTime, sReturn.getStartTime()),
                () -> assertEquals(localDateTime2, sReturn.getEndTime())
        );
    }

    @Test
    void when_select_siteBookingTimes_between_specific_start_and_end_tiem_should_select(){
        SiteBookingTime siteBookingTime = siteBookTimeMapper.getSiteBookingTimeBetweenStimeAndEtime(3,localDateTime,localDateTime2);
        assertNotNull(siteBookingTime);
        Assertions.assertAll(
                ()->assertEquals(4,siteBookingTime.getId()),
                ()->assertEquals(3,siteBookingTime.getSiteId()),
                ()->assertEquals(localDateTime,siteBookingTime.getStartTime()),
                ()->assertEquals(localDateTime2,siteBookingTime.getEndTime())
        );
    }

    @Test
    void when_delete_siteBookingTime_with_startTime_should_delete(){
        assertNotNull(siteBookTimeMapper.getSiteBookingTimeBetweenStimeAndEtime(3,localDateTime,localDateTime2));
        siteBookTimeMapper.deleteBookTimeByStartTime(3,localDateTime);
        assertNull(siteBookTimeMapper.getSiteBookingTimeBetweenStimeAndEtime(3,localDateTime,localDateTime2));
    }

    @Test
    void when_update_siteBookingTime_with_updateParam_should_update(){
        SiteBookingOrderUpdateParam s = new SiteBookingOrderUpdateParam(4,3,localDateTime,localDateTime2,localDateTime2.plus(Duration.ofHours(2)));
        siteBookTimeMapper.updateBookTime(s);
        SiteBookingTime sReturn = siteBookTimeMapper.getSiteBookingTimeBetweenStimeAndEtime(3,localDateTime2,localDateTime2.plus(Duration.ofHours(2)));
        Assertions.assertAll(
                ()->assertEquals(3,sReturn.getSiteId()),
                ()->assertEquals(localDateTime2,sReturn.getStartTime()),
                ()->assertEquals(localDateTime2.plus(Duration.ofHours(2)),sReturn.getEndTime())
        );
    }
}