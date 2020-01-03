package lionel.meethere.comment.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentPublishParam {

    Integer siteId;

    String content;
}
