package lionel.meethere.stadium.dao;

import lionel.meethere.paging.PageParam;
import lionel.meethere.stadium.entity.Stadium;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StadiumMapperTest {
    @Autowired
    private StadiumMapper stadiumMapper;

    @BeforeEach
    void setup() {
        this.stadiumMapper.createStadium(new Stadium(2,"OOAD体育馆","中山北路",null));
        this.stadiumMapper.createStadium(new Stadium(3,"OS运动馆","东川路",null));
    }

    @Test
    void when_insert_a_stadium_should_insert_success() {
        Stadium stadium = new Stadium(1,"Software Testing健身房","中山北路",null);
        this.stadiumMapper.createStadium(stadium);
        Stadium sReturn = stadiumMapper.getStadium(1);
        Assertions.assertAll(
                () -> assertEquals(1, sReturn.getId()),
                () -> assertEquals("Software Testing健身房", sReturn.getName()),
                () -> assertEquals("中山北路", sReturn.getLocation()),
                () -> assertEquals(null, sReturn.getImage())
        );
    }

    @Test
    void when_delete_a_stadium_by_Id_should_delete_success() {
        assertNotNull(this.stadiumMapper.getStadium(2));
        assertEquals(1, this.stadiumMapper.deleteStadium(2));
        assertNull(this.stadiumMapper.getStadium(2));
    }

    @Test
    void when_get_stadium_by_valid_Id_should_return_a_stadium() {
        Stadium stadium = stadiumMapper.getStadium(2);
        assertNotNull(stadium);
        Assertions.assertAll(
                ()->assertEquals("OOAD体育馆",stadium.getName()),
                ()->assertEquals("中山北路",stadium.getLocation()),
                ()->assertEquals(null,stadium.getImage())
        );
    }

    @Test
    void when_get_stadium_by_invalid_Id_should_return_a_stadium() {
        assertNull(this.stadiumMapper.getStadium(10));
    }

    @Test
    void when_get_stadium_count_should_return_count(){
        assertEquals(2,stadiumMapper.getStadiumCount());
    }

    @Test
    void when_get_stadium_list_should_return_stadium_list(){
        PageParam pageParam = new PageParam(1,3);
        List<Stadium> stadiumList = this.stadiumMapper.getStadiumList(pageParam);
        Stadium stadium = stadiumList.get(0);
        Assertions.assertAll(
                ()->assertEquals(2,stadium.getId()),
                ()->assertEquals("OOAD体育馆",stadium.getName()),
                ()->assertEquals("中山北路",stadium.getLocation()),
                ()->assertEquals(null,stadium.getImage())
        );
    }

    @Test
    void when_update_a_stadium_with_param_should_update_stadium(){
        Stadium stadium = new Stadium(2,"Software Testing健身房","中山北路",null);
        assertEquals(1,stadiumMapper.updateStadium(stadium));
        Stadium sReturn = stadiumMapper.getStadium(2);
        Assertions.assertAll(
                ()->assertEquals(2,stadium.getId()),
                ()->assertEquals("Software Testing健身房",stadium.getName()),
                ()->assertEquals("中山北路",stadium.getLocation()),
                ()->assertEquals(null,stadium.getImage())
        );
    }
}