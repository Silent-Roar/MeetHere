package lionel.meethere.comment.dto;

import lionel.meethere.user.vo.UserVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {

    private Integer id;

    private Integer reviewerId;

    private Integer siteId;

    private String content;

    private Integer status;

    private LocalDateTime createTime;

}
