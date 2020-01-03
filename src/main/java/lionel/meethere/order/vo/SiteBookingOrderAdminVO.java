package lionel.meethere.order.vo;

import lionel.meethere.user.vo.UserVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteBookingOrderAdminVO {

    private Integer id;

    private Integer userId;

    private String userName;

    private Integer siteId;

    private String stadiumName;

    private String siteName;

    private BigDecimal rent;

    private Integer status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
