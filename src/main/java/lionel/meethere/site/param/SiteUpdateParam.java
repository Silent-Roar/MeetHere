package lionel.meethere.site.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteUpdateParam {

    @Positive
    private Integer id;

    private String name;

    private Integer stadiumId;

    private String location;

    private String descrption;

    @DecimalMin("0.01")
    @DecimalMax("100000.00")
    private BigDecimal rent;

    private String image;

}
