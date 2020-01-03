package lionel.meethere.comment.vo;

import lionel.meethere.user.vo.UserVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentVO {

    private Integer id;

    private UserVO reviewer;

    private String content;

    private Integer status;

    private LocalDateTime createTime;
}
