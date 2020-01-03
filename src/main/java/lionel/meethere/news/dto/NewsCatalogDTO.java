package lionel.meethere.news.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsCatalogDTO {

    private Integer id;

    private Integer writerId;

    private String title;

    private String createTime;
}
