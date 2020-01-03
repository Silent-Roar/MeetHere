package lionel.meethere.stadium.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StadiumVO {

    private Integer id;

    private String name;

    private String location;

    private String image;

    private Integer siteCount;

}
