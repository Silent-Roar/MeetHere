package lionel.meethere.order.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteBookingOrderCreateParam {

    private Integer siteId;

    private String siteName;

    private BigDecimal rent;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
