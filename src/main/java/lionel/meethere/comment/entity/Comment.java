package lionel.meethere.comment.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_id")
    private Integer reviewerId;

    @Column(name = "site_id")
    private Integer siteId;

    @Column(name = "content")
    private String content;

    @Column(name = "status")
    private Integer status;


    @Column(name = "create_time")
    @CreatedDate
    private LocalDateTime createTime;



}
