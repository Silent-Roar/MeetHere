package lionel.meethere.order.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteBookingOrderUserVO {

    private Integer id;

    private Integer siteId;

    private String stadiumName;

    private String siteName;

    private BigDecimal rent;

    private Integer status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
