package lionel.meethere.comment.dao;

import lionel.meethere.comment.dto.CommentDTO;
import lionel.meethere.comment.entity.Comment;
import lionel.meethere.comment.status.CommentStatus;
import lionel.meethere.paging.PageParam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentMapperTest {

    @Autowired
    private CommentMapper commentMapper;

    @BeforeEach
    void setUp(){
        LocalDateTime localDateTime = LocalDateTime.now();
        this.commentMapper.publishCommnet(new Comment(2,1,2,"good", CommentStatus.UNAUDITED,localDateTime));
        this.commentMapper.publishCommnet(new Comment(3,1,2,"not good", CommentStatus.AUDITED,localDateTime));
        this.commentMapper.publishCommnet(new Comment(4,2,2,"very good", CommentStatus.AUDITED,localDateTime));
        this.commentMapper.publishCommnet(new Comment(5,3,2,"bad", CommentStatus.AUDITED,localDateTime));
    }

    @Test
    @Transactional
    void when_publish_a_comment_then_insert_success(){
        LocalDateTime localDateTime = LocalDateTime.now();
        Comment comment = new Comment(1,2,3,"nice",CommentStatus.UNAUDITED,localDateTime);
        this.commentMapper.publishCommnet(comment);

        CommentDTO commentDTO = commentMapper.getCommentById(1);
        Assertions.assertAll                                                                                                                                                                                                                                                                                                                                          (
                () -> assertEquals(1,commentDTO.getId()),
                () -> assertEquals(2,commentDTO.getReviewerId()),
                () -> assertEquals(3,commentDTO.getSiteId()),
                () -> assertEquals("nice",commentDTO.getContent()),
                () -> assertEquals(localDateTime,commentDTO.getCreateTime())
        );
    }

    @Test
    void when_delete_a_comment_by_Id_then_delete_success(){
        assertNotNull(this.commentMapper.getCommentById(2));
        assertEquals(1,this.commentMapper.deleteCommnet(2));
        assertNull(this.commentMapper.getCommentById(2));
    }


    @Test
    void when_update_a_comment_status_then_change_the_status(){

        assertEquals(1,this.commentMapper.updateCommentStatus(2,CommentStatus.AUDITED));
    }

    @Test
    void when_get_comment_by_valid_Id_should_return_a_commentDTO(){
        CommentDTO commentDTO = commentMapper.getCommentById(2);
        assertNotNull(commentDTO);
        Assertions.assertAll(
                ()->assertEquals(2,commentDTO.getId()),
                ()->assertEquals(1,commentDTO.getReviewerId()),
                ()->assertEquals(2,commentDTO.getSiteId()),
                ()->assertEquals("good",commentDTO.getContent())
        );

    }

    @Test
    void when_get_comment_by_invalid_Id_should_return_null(){
        assertNull(this.commentMapper.getCommentById(10));
    }

    @ParameterizedTest
    @MethodSource("commentPageProvider")
    void when_enter_a_pageParam_should_return_the_comment_in_that_page(PageParam pageParam, int wsize){

        List<CommentDTO> commentDTOS = this.commentMapper.getAuditedCommentsBySite(2);
        assertEquals(wsize,commentDTOS.size());

    }

    static Stream<Arguments> commentPageProvider(){
        return Stream.of(
                arguments(new PageParam(1,1),1),
                arguments(new PageParam(1,3),3),
                arguments(new PageParam(1,4),3),
                arguments(new PageParam(2,2),1),
                arguments(new PageParam(2,3),0),
                arguments(new PageParam(3,1),1)
        );
    }

}