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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class NewsService {

    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    private UserMapper userMapper;

    public void publishNews(Integer adminId, NewsPublishParam newsPublishParam){
        newsMapper.insertNews(convertToNews(adminId,newsPublishParam));
    }

    private News convertToNews(Integer adminId, NewsPublishParam newsPublishParam){
        News news = new News();
        BeanUtils.copyProperties(newsPublishParam,news);
        news.setWriterId(adminId);
        news.setCreateTime(LocalDateTime.now());
        news.setModifiedTime(LocalDateTime.now());
        return news;
    }
    public void deleteNews(Integer id){
        newsMapper.deleteNews(id);
    }

    public void updateNews(NewsUpdateParam updateParam){
        newsMapper.updateNews(updateParam);
    }

    public List<NewsCatalogVO> getNewsCatalogList(PageParam pageParam){

        List<NewsCatalogDTO> newsCatalogDTOS = newsMapper.getNewsCatalogList(pageParam);
        List<NewsCatalogVO> newsCatalogVOS = new ArrayList<>();
        for(NewsCatalogDTO newsCatalogDTO : newsCatalogDTOS){
            newsCatalogVOS.add(convertToNewsCatalogVO(newsCatalogDTO));
        }
        return newsCatalogVOS;
    }

    private NewsCatalogVO convertToNewsCatalogVO(NewsCatalogDTO newsCatalogDTO){
        NewsCatalogVO newsCatalogVO = new NewsCatalogVO();
        BeanUtils.copyProperties(newsCatalogDTO,newsCatalogVO);
        UserVO admin = userMapper.getUserById(newsCatalogDTO.getWriterId());
        newsCatalogVO.setWriter(admin);
        return newsCatalogVO;
    }
    public NewsVO getNews(Integer id){
        return convertToNewsVO(newsMapper.getNewsById(id));
    }

    private NewsVO convertToNewsVO(NewsDTO newsDTO){
        NewsVO newsVO = new NewsVO();
        BeanUtils.copyProperties(newsDTO,newsVO);
        UserVO admin = userMapper.getUserById(newsDTO.getWriterId());
        newsVO.setWriterId(admin.getId());
        newsVO.setWriter(admin.getUsername());
        return newsVO;
    }
    public List<NewsVO> getNewsList(PageParam pageParam){

        List<NewsDTO>  newsDTOS = newsMapper.listNews(pageParam);
        List<NewsVO> newsVOS = new ArrayList<>();
        for(NewsDTO newsDTO : newsDTOS){
            newsVOS.add(convertToNewsVO(newsDTO));
        }
        return newsVOS;
    }

    public int getNewsCount(){
       return newsMapper.getNewsCount();
    }



}
