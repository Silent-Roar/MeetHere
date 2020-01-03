package lionel.meethere.news.service;

import lionel.meethere.news.dao.NewsMapper;
import lionel.meethere.news.dto.NewsCatalogDTO;
import lionel.meethere.news.dto.NewsDTO;
import lionel.meethere.news.entity.News;
import lionel.meethere.news.param.NewsPublishParam;
import lionel.meethere.news.param.NewsUpdateParam;
import lionel.meethere.news.vo.NewsCatalogVO;
import lionel.meethere.news.vo.NewsVO;
import lionel.meethere.paging.PageParam;
import lionel.meethere.user.dao.UserMapper;
import lionel.meethere.user.vo.UserVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class NewsServiceTest {

    @InjectMocks
    private NewsService newsService;

    @Mock
    private NewsMapper newsMapper;

    @Mock
    private UserMapper userMapper;


    @Test
    void when_service_do_publishNews_with_publishParam_then_dispatch_to_mapper_insert_news() {

        NewsPublishParam publishParam = new NewsPublishParam("title","content","image");
        News news = new News();
        news.setWriterId(1);
        news.setTitle("title");
        news.setContent("content");
        news.setImage("image");

        newsService.publishNews(1,publishParam);
        verify(newsMapper,times(1)).insertNews(news);

    }

    @Test
    void when_service_do_deleteNews_by_id_1_then_dispatch_to_mapper_delete_news_by_id_1() {
        newsService.deleteNews(1);
        verify(newsMapper,times(1)).deleteNews(1);
    }

    @Test
    void when_service_do_updateNews_with_updateParam_then_diapatch_to_mapper_update_news() {
        LocalDateTime time = LocalDateTime.now();
        NewsUpdateParam updateParam = new NewsUpdateParam(1,"title","content","image",time);
        newsService.updateNews(updateParam);
        verify(newsMapper, times(1)).updateNews(updateParam);
    }

    @Test
    void when_service_do_getNewsCatalogList_then_return_list_of_newsCatalogVO(){
        PageParam pageParam = new PageParam(1,2);
        String createTime =LocalDateTime.now().toString();
        List<NewsCatalogDTO> newsCatalogDTOS = new ArrayList<>();
        newsCatalogDTOS.add(new NewsCatalogDTO(1,1,"title",createTime));
        newsCatalogDTOS.add(new NewsCatalogDTO(2,2,"title2",createTime));


        when(newsMapper.getNewsCatalogList(pageParam)).thenReturn(newsCatalogDTOS);
        when(userMapper.getUserById(1)).thenReturn(new UserVO(1,"admin1","18982170688"));
        when(userMapper.getUserById(2)).thenReturn(new UserVO(2,"admin2","18982170687"));

        List<NewsCatalogVO> newsCatalogVOS = newsService.getNewsCatalogList(pageParam);
        verify(newsMapper,times(1)).getNewsCatalogList(pageParam);
        verify(userMapper,times(1)).getUserById(1);
        verify(userMapper,times(1)).getUserById(2);

        NewsCatalogVO newsCatalogVO1 = newsCatalogVOS.get(0);
        Assertions.assertAll(
                ()-> assertEquals(1,newsCatalogVO1.getId()),
                ()-> assertEquals(1,newsCatalogVO1.getWriter().getId()),
                ()-> assertEquals("admin1",newsCatalogVO1.getWriter().getUsername()),
                ()-> assertEquals("title",newsCatalogVO1.getTitle()),
                ()-> assertEquals(createTime,newsCatalogVO1.getCreateTime())
                );
    }

    @Test
    void when_service_do_getNews_then_return_newsVO() {
        String createTime = LocalDateTime.now().toString();
        NewsDTO newsDTO = new NewsDTO(1,6,"title","content","image",createTime);

        when(newsMapper.getNewsById(1)).thenReturn(newsDTO);
        when(userMapper.getUserById(6)).thenReturn(new UserVO(6,"admin1","18982170688"));

        NewsVO newsVO = newsService.getNews(1);
        verify(newsMapper,times(1)).getNewsById(1);
        verify(userMapper,times(1)).getUserById(6);
        Assertions.assertAll(
                ()-> assertEquals(1,newsVO.getId()),
                ()-> assertEquals(6,newsVO.getWriterId()),
                ()-> assertEquals("admin1",newsVO.getWriter()),
                ()-> assertEquals("title",newsVO.getTitle()),
                ()-> assertEquals("content",newsVO.getContent()),
                ()-> assertEquals("image",newsVO.getImage()),
                ()-> assertEquals(createTime,newsVO.getCreateTime())
        );

    }

    @Test
    void when_do_getNewsCount_then_return_the_count_of_news(){
        when(newsMapper.getNewsCount()).thenReturn(10);

        int num = newsService.getNewsCount();

        assertEquals(10,num);
        verify(newsMapper,times(1)).getNewsCount();

    }

    @Test
    void when_do_getNewsList_then_return_the_list_of_news(){
        PageParam pageParam = new PageParam(1,2);
        String createTime =LocalDateTime.now().toString();
        List<NewsVO> newsListVOS = new ArrayList<>();
        newsListVOS.add(new NewsVO(1,6,"lyb","title","content","image",createTime));
        newsListVOS.add(new NewsVO(2,6,"xyl","title2","content","image",createTime));
        List<NewsDTO> newsDTOList = newsMapper.listNews(pageParam);
        when(newsService.getNewsList(pageParam)).thenReturn(newsListVOS);
        verify(newsMapper,times(1)).listNews(pageParam);
        NewsVO nReturn = newsListVOS.get(0);
        Assertions.assertAll(
                ()->assertEquals(1,nReturn.getId()),
                ()->assertEquals(6,nReturn.getWriterId()),
                ()->assertEquals("title",nReturn.getTitle()),
                ()->assertEquals("content",nReturn.getContent()),
                ()->assertEquals("image",nReturn.getImage()),
                ()->assertEquals(createTime,nReturn.getCreateTime())
        );
    }
}