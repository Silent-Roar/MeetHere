package lionel.meethere.comment.service;

import lionel.meethere.comment.dao.CommentMapper;
import lionel.meethere.comment.dto.CommentDTO;
import lionel.meethere.comment.entity.Comment;
import lionel.meethere.comment.status.CommentStatus;
import lionel.meethere.comment.vo.CommentVO;
import lionel.meethere.paging.PageParam;
import lionel.meethere.user.dao.UserMapper;
import lionel.meethere.user.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    public void publishComment(CommentDTO commentDTO){
        commentMapper.publishCommnet(convertToComment(commentDTO));
    }

    public Comment convertToComment(CommentDTO commentDTO){
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO,comment);
        comment.setStatus(CommentStatus.UNAUDITED);
        comment.setCreateTime(LocalDateTime.now());
        return comment;
    }

    public void deleteComment(Integer commentId){
        commentMapper.deleteCommnet(commentId);
    }

    public void auditComment(Integer id, Integer status){
        commentMapper.updateCommentStatus(id,status);
    }

    public CommentVO getCommentById(Integer id){
        return convertToCommentVO(commentMapper.getCommentById(id));
    }

    private CommentVO convertToCommentVO(CommentDTO commentDTO){
        CommentVO commentVO = new CommentVO();
        BeanUtils.copyProperties( commentDTO,commentVO);
        UserVO reviewerVO = userMapper.getUserById(commentDTO.getReviewerId());
        commentVO.setReviewer(reviewerVO);
        return commentVO;
    }

    public List<CommentVO> getCommentsBySite( Integer siteId){
        return convertToCommentVOList(commentMapper.getAuditedCommentsBySite(siteId));

    }

    private List<CommentVO> convertToCommentVOList(List<CommentDTO> commentDTOList){
        List<CommentVO> commentVOList = new ArrayList<>();
        for(CommentDTO commentDTO : commentDTOList){
            commentVOList.add(convertToCommentVO(commentDTO));
        }
        return commentVOList;
    }

    public int getCommentCount(Integer status){
        return commentMapper.getCommentCount(status);
    }

    public List<CommentVO> getCommentsByStatus(PageParam pageParam, Integer status){
        List<CommentDTO> commentDTOS = commentMapper.getCommentsByStatus(pageParam,status);
        return convertToCommentVOList(commentDTOS);

    }
}
