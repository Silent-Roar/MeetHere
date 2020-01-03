package lionel.meethere.site.dao;

import lionel.meethere.order.status.OrderStatus;
import lionel.meethere.paging.PageParam;
import lionel.meethere.site.entity.Site;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SiteMapperTest {

    @Autowired
    private SiteMapper siteMapper;

    @BeforeEach
    void setup() {
        siteMapper.insertSite(new Site(2, "篮球场", 1, "中山北路", "设施齐全", new BigDecimal(20), null));
        siteMapper.insertSite(new Site(3, "羽毛球场", 2, "金沙江路", null, new BigDecimal(10), null));
        siteMapper.insertSite(new Site(4, "篮球场", 2, "金沙江路", null, new BigDecimal(15), null));
        siteMapper.insertSite(new Site(5, "乒乓球场", 2, "金沙江路", null, new BigDecimal(5), null));
    }

    @Test
    void when_insert_a_site_should_insert_success() {
        Site site = new Site(1, "足球场", 1, "中山北路", "新建场地", new BigDecimal(40), null);
        this.siteMapper.insertSite(site);
        Site sReturn = siteMapper.getSite(1);
        Assertions.assertAll(
                () -> assertEquals(1, sReturn.getId()),
                () -> assertEquals("足球场", sReturn.getName()),
                () -> assertEquals(1, sReturn.getStadiumId()),
                () -> assertEquals("中山北路", sReturn.getLocation()),
                () -> assertEquals("新建场地", sReturn.getDescription()),
                () -> assertEquals(new BigDecimal(40).setScale(2), sReturn.getRent()),
                () -> assertEquals(null, sReturn.getImage())
                );
    }

    @Test
    void when_delete_a_site_by_Id_should_delete_success() {
        assertNotNull(this.siteMapper.getSite(2));
        assertEquals(1, this.siteMapper.deleteSite(2));
        assertNull(this.siteMapper.getSite(2));
    }

    @Test
    void when_update_a_site_should_update_success() {
        Site updateSite = new Site(2, "足球场", 1, "中山北路", "新建场地", new BigDecimal(40), null);
        assertEquals(1, siteMapper.updateSite(updateSite));
        Site sReturn = siteMapper.getSite(2);
        Assertions.assertAll(
                () -> assertEquals(2, sReturn.getId()),
                () -> assertEquals("足球场", sReturn.getName()),
                () -> assertEquals(1, sReturn.getStadiumId()),
                () -> assertEquals("中山北路", sReturn.getLocation()),
                () -> assertEquals("新建场地", sReturn.getDescription()),
                () -> assertEquals(new BigDecimal(40).setScale(2), sReturn.getRent()),
                () -> assertEquals(null, sReturn.getImage())
        );
    }

    @Test
    void when_get_site_by_valid_Id_should_return_a_site() {
        Site site = siteMapper.getSite(2);
        assertNotNull(site);
        Assertions.assertAll(
                () -> assertEquals(2, site.getId()),
                () -> assertEquals("篮球场", site.getName()),
                () -> assertEquals(1, site.getStadiumId()),
                () -> assertEquals("中山北路", site.getLocation()),
                () -> assertEquals("设施齐全", site.getDescription()),
                () -> assertEquals(new BigDecimal(20).setScale(2), site.getRent()),
                () -> assertEquals(null, site.getImage())
        );
    }

    @Test
    void when_get_site_by_invalid_Id_should_return_a_site() {
        assertNull(this.siteMapper.getSite(100));
    }



    @ParameterizedTest
    @MethodSource("listSiteSourceProvider")
    void when_get_site_list_should_return_site_list(PageParam pageParam, int wsize) {
        List<Site> siteList = this.siteMapper.listSites(pageParam);

        assertEquals(wsize,siteList.size());
        if(!siteList.isEmpty()) {
            Site site = siteList.get(0);
            Assertions.assertAll(
                    () -> assertEquals(2, site.getId()),
                    () -> assertEquals("篮球场", site.getName()),
                    () -> assertEquals(1, site.getStadiumId()),
                    () -> assertEquals("中山北路", site.getLocation()),
                    () -> assertEquals("设施齐全", site.getDescription()),
                    () -> assertEquals(new BigDecimal(20).setScale(2), site.getRent()),
                    () -> assertEquals(null, site.getImage())
            );
        }
    }
    static Stream<Arguments> listSiteSourceProvider() {
        return Stream.of(
                arguments(new PageParam(1,1),1),
                arguments(new PageParam(1,3),3),
                arguments(new PageParam(1,6),4),
                arguments(new PageParam(3,2),0)
        );
    }

    @Test
    void when_get_site_list_should_return_site_list() {
        PageParam pageParam = new PageParam(1,3);
        int stadiumId = 2;
        List<Site> siteList = this.siteMapper.listSitesByStadium(stadiumId,pageParam);

        assertEquals(3,siteList.size());
        if(!siteList.isEmpty()) {
            Site site = siteList.get(0);
            Assertions.assertAll(
                    () -> assertEquals(3, site.getId()),
                    () -> assertEquals("羽毛球场", site.getName()),
                    () -> assertEquals(2, site.getStadiumId()),
                    () -> assertEquals("金沙江路", site.getLocation()),
                    () -> assertEquals(null, site.getDescription()),
                    () -> assertEquals(new BigDecimal(10).setScale(2), site.getRent()),
                    () -> assertEquals(null, site.getImage())
            );
        }
    }

    @Test
    void when_get_site_count_should_return_count() {
        assertEquals(4, siteMapper.getSiteCount());
    }

    @Test
    void when_get_site_count_by_stadium_should_return_count() {
        assertEquals(3, siteMapper.getSiteCountByStadium(2));
    }
}