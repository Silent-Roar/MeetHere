package lionel.meethere.news.vo;

import lionel.meethere.user.vo.UserVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsCatalogVO {

    private Integer id;

    private UserVO writer;

    private String title;

    private String createTime;
}
