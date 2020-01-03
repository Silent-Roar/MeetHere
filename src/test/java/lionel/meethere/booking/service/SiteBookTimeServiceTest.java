package lionel.meethere.booking.service;

import lionel.meethere.booking.dao.SiteBookTimeMapper;
import lionel.meethere.booking.entity.SiteBookingTime;
import lionel.meethere.order.entity.SiteBookingOrder;
import lionel.meethere.order.param.SiteBookingOrderUpdateParam;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SiteBookTimeServiceTest {

    @Mock
    private SiteBookTimeMapper siteBookTimeMapper;

    @InjectMocks
    private SiteBookTimeService siteBookTimeService;

    @Before
    public void setup() throws Exception{
        MockitoAnnotations.initMocks(this);
    }

    private LocalDateTime localDateTime = LocalDateTime.now();
    private LocalDateTime localDateTime2 = LocalDateTime.now().plus(Duration.ofHours(2));

    @Test
    void when_service_do_try_booking_in_available_period_then_return_true() {
        SiteBookingTime siteBookingTime = new SiteBookingTime(1,1,localDateTime,localDateTime2);
        when(siteBookTimeMapper.getSiteBookingTimeBetweenStimeAndEtime(1,localDateTime,localDateTime2)).thenReturn(siteBookingTime);
        Boolean result = siteBookTimeService.tryBooking(1,localDateTime,localDateTime2);
        verify(siteBookTimeMapper,times(1)).getSiteBookingTimeBetweenStimeAndEtime(1,localDateTime,localDateTime2);
    }

    @Test
    void when_service_do_try_booking_in_unavailable_period_then_return_true() {
        SiteBookingTime siteBookingTime = new SiteBookingTime(1,1,localDateTime,localDateTime2);
        when(siteBookTimeMapper.getSiteBookingTimeBetweenStimeAndEtime(1,localDateTime,localDateTime2)).thenReturn(null);
        Boolean result = siteBookTimeService.tryBooking(1,localDateTime,localDateTime2);
        verify(siteBookTimeMapper,times(1)).getSiteBookingTimeBetweenStimeAndEtime(1,localDateTime,localDateTime2);
    }

    @Test
    void when_service_do_insert_siteBookTime_then_insert() {
        SiteBookingTime siteBookingTime =siteBookTimeService.convertToSiteBookingTime(1,localDateTime,localDateTime2);
        siteBookTimeService.insertBokingTime(1,localDateTime,localDateTime2);
        verify(siteBookTimeMapper,times(1)).insertBookTime(siteBookingTime);
    }

    @Test
    void when_service_do_cancel_then_delete_sitebooktime() {
        SiteBookingTime siteBookingTime =siteBookTimeService.convertToSiteBookingTime(1,localDateTime,localDateTime2);
        siteBookTimeService.cancelSiteBookTime(1,localDateTime);
        verify(siteBookTimeMapper,times(1)).deleteBookTimeByStartTime(1,localDateTime);
    }

    @Test
    void when_service_do_update_then_update_sitebooktime() {
        SiteBookingOrderUpdateParam s = new SiteBookingOrderUpdateParam(1,1,localDateTime,localDateTime2,localDateTime2.plus(Duration.ofHours(2)));
        siteBookTimeService.updateSiteBookTime(s);
        verify(siteBookTimeMapper,times(1)).updateBookTime(s);
    }

    @Test
    void when_service_do_updateBookingTime_then_return_result(){
        SiteBookingTime siteBookingTime =siteBookTimeService.convertToSiteBookingTime(1,localDateTime,localDateTime2);
        siteBookTimeService.tryUpdateBookingTime(1,localDateTime,localDateTime2,localDateTime2.plus(Duration.ofHours(2)));
        when(siteBookTimeService.tryBooking(1,localDateTime2,localDateTime2.plus(Duration.ofHours(2)))).thenReturn(null);
        when(siteBookTimeService.tryBooking(2,localDateTime2,localDateTime2.plus(Duration.ofHours(2)))).thenReturn(null);
        verify(siteBookTimeMapper,times(1)).deleteBookTimeByStartTime(1,localDateTime);

    }
}