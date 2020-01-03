package lionel.meethere.comment.service;

import lionel.meethere.comment.dao.CommentMapper;
import lionel.meethere.comment.dto.CommentDTO;
import lionel.meethere.comment.entity.Comment;
import lionel.meethere.comment.status.CommentStatus;
import lionel.meethere.comment.vo.CommentVO;
import lionel.meethere.paging.PageParam;
import lionel.meethere.user.dao.UserMapper;
import lionel.meethere.user.vo.UserVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

@SpringBootTest
class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private UserMapper userMapper;

    @Test
    void when_service_do_publishComment_then_mapper_insert_comment() {
        LocalDateTime time = LocalDateTime.now();
        CommentDTO commentDTO = new CommentDTO(1,2,3,"good",0,time);
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO,comment);
        comment.setStatus(CommentStatus.UNAUDITED);
        commentService.publishComment(commentDTO);
        verify(commentMapper).publishCommnet(any());

    }

    @Test
    void when_service_do_deleteComment_by_id_1_then_mapper_delete_comment_by_id_1() {
        commentService.deleteComment(1);
        verify(commentMapper).deleteCommnet(1);

    }

    @ParameterizedTest
    @MethodSource("auditCommentSourceProvider")
    void when_service_do_auditComment_then_mapper_update_comment_status(Integer commnetId,Integer status) {
        commentService.auditComment(commnetId,status);
        verify(commentMapper).updateCommentStatus(commnetId,status);
    }

    static Stream<Arguments> auditCommentSourceProvider(){
        return Stream.of(
                arguments(1,CommentStatus.AUDITED),
                arguments(1,CommentStatus.AUDITED_FAILED),
                arguments(2,CommentStatus.AUDITED_FAILED)
        );
    }

    @Test
    void when_service_do_getCommentById_1_then_mapper_find_comment_which_id_equal_1() {
        LocalDateTime time = LocalDateTime.now();
        CommentDTO commentDTO = new CommentDTO(1,2,3,"good",0,time);
        UserVO userVO = new UserVO(2,"lionel","18912344321");

        when(commentMapper.getCommentById(1)).thenReturn(commentDTO);
        when(userMapper.getUserById(2)).thenReturn(userVO);

        CommentVO commentVO = commentService.getCommentById(1);
        Assertions.assertAll(
                ()->assertEquals(1,commentVO.getId()),
                ()->assertEquals(2,commentVO.getReviewer().getId()),
                ()->assertEquals("lionel",commentVO.getReviewer().getUsername()),
                ()->assertEquals("18912344321",commentVO.getReviewer().getTelephone()),
                ()->assertEquals("good",commentVO.getContent()),
                ()->assertEquals(time,commentVO.getCreateTime())
        );

    }

    @Test
    void when_service_do_getCommentsBySite_2_mapper_return_Comments_related_to_site_2() {
        LocalDateTime time = LocalDateTime.now();
        PageParam pageParam = new PageParam(1,2);
        List<CommentDTO> commentDTOList = new ArrayList<>();
        commentDTOList.add(new CommentDTO(1,2,3,"hh",0,time));
        commentDTOList.add(new CommentDTO(2,1,3,"aa",0,time));;

        when(commentMapper.getAuditedCommentsBySite(3)).thenReturn(commentDTOList);
        when(userMapper.getUserById(2)).thenReturn(new UserVO(2,"gg","18912345678"));
        when(userMapper.getUserById(1)).thenReturn(new UserVO(1,"mm","18987654321"));

        List<CommentVO> commentVOList = commentService.getCommentsBySite(3);
        assertEquals(2,commentVOList.size());


    }

    @Test
    void when_service_do_getCommentsByStatus_1_mapper_return_Comments_related_to_status_1() {
        LocalDateTime time = LocalDateTime.now();
        PageParam pageParam = new PageParam(1,2);
        List<CommentDTO> commentDTOList = new ArrayList<>();
        commentDTOList.add(new CommentDTO(1,2,3,"hh",0,time));
        commentDTOList.add(new CommentDTO(2,1,3,"aa",0,time));;

        when(commentMapper.getCommentsByStatus(pageParam,1)).thenReturn(commentDTOList);
        when(userMapper.getUserById(2)).thenReturn(new UserVO(2,"gg","18912345678"));
        when(userMapper.getUserById(1)).thenReturn(new UserVO(1,"mm","18987654321"));

        List<CommentVO> commentVOList = commentService.getCommentsByStatus(pageParam,1);
        assertEquals(2,commentVOList.size());
        CommentVO commentVO = commentVOList.get(0);
        Assertions.assertAll(
                ()->assertEquals(1,commentVO.getId()),
                ()->assertEquals(2,commentVO.getReviewer().getId()),
                ()->assertEquals("gg",commentVO.getReviewer().getUsername()),
                ()->assertEquals("18912345678",commentVO.getReviewer().getTelephone()),
                ()->assertEquals("hh",commentVO.getContent()),
                ()->assertEquals(time,commentVO.getCreateTime())
        );


    }
    @Test
    void getCommentCount() {
        when(commentMapper.getCommentCount(1)).thenReturn(10);

        int num = commentService.getCommentCount(1);

        assertEquals(10,num);
        verify(commentMapper,times(1)).getCommentCount(1);
    }
}