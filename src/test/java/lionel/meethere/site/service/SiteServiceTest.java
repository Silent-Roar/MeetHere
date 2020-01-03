package lionel.meethere.site.service;

import lionel.meethere.paging.PageParam;
import lionel.meethere.site.dao.SiteMapper;
import lionel.meethere.site.entity.Site;
import lionel.meethere.site.param.SiteUpdateParam;
import lionel.meethere.stadium.dao.StadiumMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@SpringBootTest
class SiteServiceTest {

    @InjectMocks
    private SiteService siteService;

    @Mock
    private SiteMapper siteMapper;

    @Mock
    private StadiumMapper stadiumMapper;

    public void setup() throws Exception{
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void when_service_do_create_site_then_dispatch_to_mapper_insert_news() {
        Site site = new Site(1, "场地1", 1, "中山北路", "场地太小", new BigDecimal(20), "111");
        siteService.createSite(site);
        verify(siteMapper,times(1)).insertSite(site);
    }

    @Test
    void when_service_do_delete_site_by_Id_then_dispatch_mapper_to_delete_site() {
        Site site = new Site(1, "场地1", 1, "中山北路", "场地太小", new BigDecimal(20), "111");
        siteService.deleteSite(1);
        verify(siteMapper,times(1)).deleteSite(1);
    }

    @Test
    void when_service_do_update_site_with_updateparam_then_dispatch_mapper_to_update() {
        SiteUpdateParam siteUpdateParam = new SiteUpdateParam(1, "场地1", 1, "中山北路", "场地太小", new BigDecimal(20), "111");
        Site site = new Site(2, "场地1", 2, "中山北路", "光线不好", new BigDecimal(20), null);
        when(siteMapper.getSite(1)).thenReturn(site);
        siteService.updateSite(siteUpdateParam);
        verify(siteMapper,times(1)).updateSite(site);
    }

    @Test
    void when_service_do_get_site_by_id_then_dispatch_mapper_to_return_site(){
        Site site = new Site(1, "场地1", 1, "中山北路", "场地太小", new BigDecimal(20), "111");
        when(siteMapper.getSite(1)).thenReturn(site);
        Site returnS = siteService.getSiteById(1);
        verify(siteMapper,times(1)).getSite(1);
    }

    @Test
    void when_service_do_get_sites_then_dispatch_mapper_to_return_sitelist() {
        PageParam pageParam = new PageParam(1,3);
        List<Site> siteList = new ArrayList<>();
        siteList.add(new Site(2, "场地1", 2, "中山北路", "光线不好", new BigDecimal(20), null));
        siteList.add(new Site(3, "场地2", 2, "中山北路", "场地太小", new BigDecimal(20), null));
        siteList.add(new Site(4, "场地1", 3, "中山北路", "场地太小", new BigDecimal(20), null));

        when(siteMapper.listSites(pageParam)).thenReturn(siteList);

        List<Site> returnS = siteService.getSites(pageParam);
        verify(siteMapper,times(1)).listSites(pageParam);
        assertEquals(siteList,returnS);
    }

    @Test
    void when_service_do_get_sites_divided_by_stadium_then_dispatch_mapper_to_return_sitelist() {
        PageParam pageParam = new PageParam(1,3);
        List<Site> siteList = new ArrayList<>();
        siteList.add(new Site(2, "场地1", 2, "中山北路", "光线不好", new BigDecimal(20), null));
        siteList.add(new Site(3, "场地2", 2, "中山北路", "场地太小", new BigDecimal(20), null));

        when(siteMapper.listSitesByStadium(2,pageParam)).thenReturn(siteList);
        List<Site> returnS = siteService.getSitesByStadium(2,pageParam);
        verify(siteMapper,times(1)).listSitesByStadium(2,pageParam);
        assertEquals(siteList,returnS);
    }

    @Test
    void when_service_do_get_sitesCount_then_dispatch_mapper_to_return_count() {
        when(siteMapper.getSiteCount()).thenReturn(2);
        int num = siteService.getSiteCount();
        verify(siteMapper,times(1)).getSiteCount();
        assertEquals(2,num);
    }

    @Test
    void when_service_do_get_sitesCount_divided_by_stadium_then_dispatch_mapper_to_return_count() {
        when(siteMapper.getSiteCountByStadium(1)).thenReturn(2);
        int num = siteService.getSiteCountByStadium(1);
        verify(siteMapper,times(1)).getSiteCountByStadium(1);
        assertEquals(2,num);
    }
}