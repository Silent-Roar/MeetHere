package lionel.meethere.order.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteBookingOrderUpdateParam {

    private Integer orderId;

    private Integer siteId;

    private LocalDateTime oldStartTime;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

}
