package lionel.meethere.comment.dao;

import lionel.meethere.comment.dto.CommentDTO;
import lionel.meethere.comment.entity.Comment;
import lionel.meethere.paging.PageParam;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("insert into comment(id,user_id,site_id,content,status,create_time) values(#{id},#{reviewerId},#{siteId},#{content},#{status},#{createTime});")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int publishCommnet(Comment comment);

    @Delete("delete from comment where id=#{id};")
    int deleteCommnet(Integer id);

    @Update("update comment set status=#{status} where id=#{id}")
    int updateCommentStatus(@Param("id") Integer id,
                            @Param("status") Integer status);

    @Results(
            id = "commentDTO",value = {
            @Result(property="id", column="id"),
            @Result(property="reviewerId", column="user_id"),
            @Result(property="siteId", column="site_id"),
            @Result(property="content", column="content"),
            @Result(property="status", column="status"),
            @Result(property ="createTime", column = "create_time")
    }
    )
    @Select("select * from comment where id=#{id};")
    CommentDTO getCommentById(Integer id);

    @ResultMap("commentDTO")
    @Select("select * from comment where status=1 and site_id=#{siteId};")
    List<CommentDTO> getAuditedCommentsBySite(@Param("siteId")   Integer siteId);


    @Select("select count(*) from comment where status=#{status}")
    int getCommentCount(Integer status);

    @ResultMap("commentDTO")
    @Select("select * from comment where status=#{status} limit ${pageParam.pageSize*(pageParam.pageNum-1)},#{pageParam.pageSize};")
    List<CommentDTO> getCommentsByStatus(@Param("pageParam")PageParam pageParam,
                                              @Param("status")   Integer status);

    @Select("select count(*) from comment where status=#{status}")
    int getNewCommentCount(Integer status);
}
