package lionel.meethere.news.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsDTO {

    private Integer id;

    private Integer writerId;

    private String title;

    private String content;

    private String image;

    private String createTime;
}
