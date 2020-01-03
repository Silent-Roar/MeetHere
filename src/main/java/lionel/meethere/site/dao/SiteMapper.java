package lionel.meethere.site.dao;

import lionel.meethere.paging.PageParam;
import lionel.meethere.site.entity.Site;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SiteMapper {


    @Insert("insert into site(id,name,stadium_id,location,description,rent,image) values(#{id},#{name},#{stadiumId},#{location},#{description},#{rent},#{image});")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertSite(Site site);

    @Delete("delete from site where id=#{id};")
    int deleteSite(Integer id);

    @Update("update site set name=#{name},stadium_id=#{stadiumId},location=#{location},description=#{description},rent=#{rent},image=#{image} where id=#{id};")
    int updateSite(Site site);

    @Results(
            id = "site", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "stadiumId", column = "stadium_id"),
            @Result(property = "location", column = "location"),
            @Result(property = "description", column = "description"),
            @Result(property = "rent", column = "rent"),
            @Result(property = "image", column = "image")
    }
    )
    @Select("select * from site where id=#{id};")
    Site getSite(Integer id);

    @ResultMap("site")
    @Select("select * from site order by id limit ${pageSize * (pageNum - 1)},#{pageSize}; ")
    List<Site> listSites(PageParam pageParam);

    @ResultMap("site")
    @Select("select * from site where stadium_id=#{stadiumId} order by id limit ${pageParam.pageSize * (pageParam.pageNum - 1)},#{pageParam.pageSize}; ")
    List<Site> listSitesByStadium(Integer stadiumId, PageParam pageParam);

    @Select("select count(*) from site")
    int getSiteCount();

    @Select("select count(*) from site where stadium_id=#{stadiumId}")
    int getSiteCountByStadium(Integer stadiumId);
}
