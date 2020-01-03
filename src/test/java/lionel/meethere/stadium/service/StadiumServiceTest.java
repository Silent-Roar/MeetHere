package lionel.meethere.stadium.service;

import lionel.meethere.paging.PageParam;
import lionel.meethere.site.dao.SiteMapper;
import lionel.meethere.stadium.dao.StadiumMapper;
import lionel.meethere.stadium.entity.Stadium;
import lionel.meethere.stadium.vo.StadiumVO;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class StadiumServiceTest {

    @Mock
    private StadiumMapper stadiumMapper;

    @Mock
    private SiteMapper siteMapper;

    @InjectMocks
    private StadiumService stadiumService;

    @Before
    public void setup() throws Exception{
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void when_service_do_get_list_of_stadiums_then_dispatch_mapper_to_return_list_of_stadiums() {
        PageParam pageParam = new PageParam(1,3);
        List<Stadium> stadiumList = new ArrayList<>();
        stadiumList.add(new Stadium(1,"Software Testing健身房","中山北路",null));
        stadiumList.add(new Stadium(2,"OOAD体育馆","中山北路",null));
        stadiumList.add(new Stadium(3,"OS运动馆","东川路",null));

        when(stadiumMapper.getStadiumList(pageParam)).thenReturn(stadiumList);
        List<StadiumVO> returnstadiumList = stadiumService.getStadiums(pageParam);
        verify(stadiumMapper,times(1)).getStadiumList(pageParam);

        StadiumVO stadiumVO = returnstadiumList.get(0);
        assertAll(
                ()->assertEquals(1,stadiumVO.getId()),
                ()->assertEquals("中山北路",stadiumVO.getLocation()),
                ()->assertEquals(null,stadiumVO.getImage()),
                ()->assertEquals(0,stadiumVO.getSiteCount())
        );
    }

    @Test
    void when_service_do_get_stadiums_by_id_then_dispatch_mapper_to_return_stadium(){
        when(stadiumMapper.getStadium(3)).thenReturn(new Stadium(3,"OS运动馆","东川路",null));
        StadiumVO stadiumVO = stadiumService.getStadiumById(3);
        verify(stadiumMapper,times(1)).getStadium(3);

        assertAll(
                ()->assertEquals(3,stadiumVO.getId()),
                ()->assertEquals("OS运动馆",stadiumVO.getName()),
                ()->assertEquals("东川路",stadiumVO.getLocation()),
                ()->assertEquals(null,stadiumVO.getImage())
        );
    }

    @Test
    void when_service_get_count_of_stadium_then_dispatch_mapper_to_return_count() {
        when(stadiumMapper.getStadiumCount()).thenReturn(2);
        int num = stadiumService.getStadiumCount();
        verify(stadiumMapper,times(1)).getStadiumCount();
        assertEquals(2,num);
    }

    @Test
    void when_service_create_stadium_then_dispatch_mapper_to_create() {
        Stadium stadium = new Stadium(1,"Software Testing健身房","中山北路",null);
        stadiumService.createStadium(stadium);
        verify(stadiumMapper,times(1)).createStadium(stadium);
    }

    @Test
    void when_service_delete_stadium_then_dispatch_mapper_to_delete() {
        Stadium stadium = new Stadium(1,"Software Testing健身房","中山北路",null);
        stadiumService.delteStadium(1);
        verify(stadiumMapper,times(1)).deleteStadium(1);
    }

    @Test
    void when_service_update_stadium_then_disparch_mapper_to_update() {
        Stadium stadium = new Stadium(2,"OOAD体育馆","中山北路",null);
        when(stadiumMapper.updateStadium(stadium)).thenReturn(1);
        stadiumService.updateStadium(stadium);
        verify(stadiumMapper,times(1)).updateStadium(stadium);

    }
}