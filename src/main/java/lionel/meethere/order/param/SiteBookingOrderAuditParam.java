package lionel.meethere.order.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteBookingOrderAuditParam {

    private Integer orderId;

    private Integer auditStatus;
}
