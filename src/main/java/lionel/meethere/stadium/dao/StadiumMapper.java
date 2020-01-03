package lionel.meethere.stadium.dao;

import lionel.meethere.paging.PageParam;
import lionel.meethere.stadium.entity.Stadium;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StadiumMapper {

    @Select("select * from stadium where id=#{id}")
    Stadium getStadium(Integer id);

    @Select("select * from stadium order by id limit ${pageSize * (pageNum - 1)},#{pageSize}")
    List<Stadium> getStadiumList(PageParam pageParam);

    @Select("select count(*) from stadium")
    int getStadiumCount();

    @Insert("insert into stadium(id,name,location,image) values(#{id},#{name},#{location},#{image})")
    int createStadium(Stadium stadium);

    @Delete("delete from stadium where id=#{id}")
    int deleteStadium(Integer id);

    @Update("update stadium set location=#{location},image=#{image} where id=#{id}")
    int updateStadium (Stadium stadium);

}
