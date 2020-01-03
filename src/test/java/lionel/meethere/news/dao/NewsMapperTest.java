package lionel.meethere.news.dao;

import lionel.meethere.news.dto.NewsCatalogDTO;
import lionel.meethere.news.dto.NewsDTO;
import lionel.meethere.news.entity.News;
import lionel.meethere.news.param.NewsUpdateParam;
import lionel.meethere.paging.PageParam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NewsMapperTest {

    @Autowired
    private NewsMapper newsMapper;

    @BeforeEach
    void setUp(){
        LocalDateTime localDateTime = LocalDateTime.now();
        this.newsMapper.insertNews(new News(2,1,"news title2","content2",null, localDateTime,localDateTime));
        this.newsMapper.insertNews(new News(3,1,"news title3","content3",null, localDateTime,localDateTime));
        this.newsMapper.insertNews(new News(4,2,"news title4","content4",null, localDateTime,localDateTime));
        this.newsMapper.insertNews(new News(5,3,"news title5","content5",null, localDateTime,localDateTime));
    }

    @Test
    @Transactional
    void when_insert_a_news_should_insert_success(){
        LocalDateTime localDateTime = LocalDateTime.now();
        News news = new News(1,1,"news title","content",null, localDateTime,localDateTime);
        this.newsMapper.insertNews(news);

        NewsDTO newsDTO = newsMapper.getNewsById(1);
        Assertions.assertAll                                                                                                                                                                                                                                                                                                                                          (
                () -> assertEquals(1,newsDTO.getId()),
                () -> assertEquals(1,newsDTO.getWriterId()),
                () -> assertEquals("news title",newsDTO.getTitle()),
                () -> assertEquals("content",newsDTO.getContent()),
                () -> assertEquals(null,newsDTO.getImage())
        );
    }

    @Test
    void when_delete_a_news_by_Id_should_delete_success(){
        assertNotNull(this.newsMapper.getNewsById(2));
        assertEquals(1,this.newsMapper.deleteNews(2));
        assertNull(this.newsMapper.getNewsById(2));
    }

    @Test
    void when_update_a_news_with_updateParam_should_update_the_property(){
        LocalDateTime time = LocalDateTime.now();
        assertEquals(1,this.newsMapper.updateNews(
                new NewsUpdateParam(2,"title2.1","content2.1","image",time)));

        NewsDTO newsDTO = newsMapper.getNewsById(2);

        Assertions.assertAll(
                () -> assertEquals("title2.1",newsDTO.getTitle()),
                () -> assertEquals("content2.1",newsDTO.getContent()),
                () -> assertEquals("image",newsDTO.getImage())
        );
    }

    @Test
    void when_get_news_by_valid_Id_should_return_a_newsDTO(){
        assertNotNull(this.newsMapper.getNewsById(2));
    }

    @Test
    void when_get_news_by_invalid_Id_should_return_null(){
        assertNull(this.newsMapper.getNewsById(10));
    }

    @ParameterizedTest
    @MethodSource("pageParamProvider")
    void when_enter_a_pageParam_should_return_the_newscatelog_in_that_page(PageParam pageParam,int wsize){
        List<NewsCatalogDTO> newsCatalogDTOS = this.newsMapper.getNewsCatalogList(pageParam);
        assertEquals(wsize,newsCatalogDTOS.size());

    }

    static Stream<Arguments>  pageParamProvider(){
        return Stream.of(
                arguments(new PageParam(1,1),1),
                arguments(new PageParam(1,4),4),
                arguments(new PageParam(1,5),4),
                arguments(new PageParam(2,2),2),
                arguments(new PageParam(2,3),1),
                arguments(new PageParam(3,2),0)
        );
    }
}