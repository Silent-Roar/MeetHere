package lionel.meethere.news.dao;

import lionel.meethere.news.dto.NewsCatalogDTO;
import lionel.meethere.news.dto.NewsDTO;
import lionel.meethere.news.entity.News;
import lionel.meethere.news.param.NewsUpdateParam;
import lionel.meethere.paging.PageParam;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NewsMapper {

    @Insert("insert into news(id,writer_id,title,content,image,create_time,modified_time) values(#{id},#{writerId},#{title},#{content},#{image},#{createTime},#{modifiedTime});")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertNews(News news);

    @Delete("delete from news where id=#{id}")
    int deleteNews(Integer id);

    @Update("update news set title=#{title},content=#{content},image=#{image},modified_time=#{modifiedTime} where id=#{id};")
    int updateNews(NewsUpdateParam newsUpdateParam);

    @Results(
            id = "newsDTO",value = {
            @Result(property="id", column="id"),
            @Result(property="writerId", column="writer_id"),
            @Result(property="title", column="title"),
            @Result(property="content", column="content"),
            @Result(property="image", column="image"),
            @Result(property="createTime", column="create_time"),
    }
    )
    @Select("select * from news where id=#{id};")
    NewsDTO getNewsById(Integer id);

    @ResultMap("newsDTO")
    @Select("select * from news order by create_time desc limit ${pageSize * (pageNum - 1)},#{pageSize}")
    List<NewsDTO> listNews(PageParam pageParam);

    @Results(
            id = "newsCatalogDTO",value = {
            @Result(property="id", column="id"),
            @Result(property="writerId", column="writer_id"),
            @Result(property="title", column="title"),
            @Result(property="createTime", column="create_time"),
    }
    )
    @Select("select * from news order by create_time desc limit ${pageSize * (pageNum - 1)},#{pageSize}")
    List<NewsCatalogDTO> getNewsCatalogList(PageParam pageParam);

    @Select("select count(*) from news")
    int getNewsCount();
}
