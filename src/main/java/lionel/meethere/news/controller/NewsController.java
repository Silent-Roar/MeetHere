package lionel.meethere.news.controller;

import lionel.meethere.news.param.NewsPublishParam;
import lionel.meethere.news.param.NewsUpdateParam;
import lionel.meethere.news.service.NewsService;
import lionel.meethere.paging.PageParam;
import lionel.meethere.result.CommonResult;
import lionel.meethere.result.Result;
import lionel.meethere.user.session.UserSessionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@RestController
@CrossOrigin
@RequestMapping("/news/")
public class NewsController {

    @Autowired
    private NewsService newsService;


    @PostMapping("publish")
    public Result<?> publishNews(@SessionAttribute UserSessionInfo userSessionInfo,
                                 @RequestParam String title,
                                 @RequestParam String content,
                                 @RequestParam String image) {

        NewsPublishParam newsPublishParam = new NewsPublishParam(title, content, image);

        if (userSessionInfo.getAdmin() != 1) {
            return CommonResult.accessDenied();
        }
        newsService.publishNews(userSessionInfo.getId(), newsPublishParam);
        return CommonResult.success();
    }

    @PostMapping("delete")
    public Result<?> deleteNews(@SessionAttribute UserSessionInfo userSessionInfo,
                                @RequestParam Integer newsId) {
        if (userSessionInfo.getAdmin() != 1) {
            return CommonResult.accessDenied();
        }
        newsService.deleteNews(newsId);
        return CommonResult.success();
    }

    @PostMapping("update")
    public Result<?> updateNews(@SessionAttribute UserSessionInfo userSessionInfo,
                                @RequestParam Integer id,
                                @RequestParam String title,
                                @RequestParam String content,
                                @RequestParam String image) {

        NewsUpdateParam updateParam = new NewsUpdateParam(id, title, content, image,LocalDateTime.now());
        if (userSessionInfo.getAdmin() != 1) {
            return CommonResult.accessDenied();
        }
        newsService.updateNews(updateParam);
        return CommonResult.success();
    }


    @PostMapping("get")
    public Result<?> getNews(@RequestParam Integer id) {
        return CommonResult.success().data(newsService.getNews(id));
    }

    @PostMapping("getcatalog")
    public Result<?> getCatalog(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize) {
        PageParam pageParam = new PageParam(pageNum, pageSize);
        return CommonResult.success().data(newsService.getNewsCatalogList(pageParam)).total(newsService.getNewsCount());
    }

    @PostMapping("list")
    public Result<?> listNews(@SessionAttribute UserSessionInfo userSessionInfo,
                              @RequestParam Integer pageNum,
                              @RequestParam Integer pageSize) {
        if(!userSessionInfo.getAdmin().equals(1)) {
            return CommonResult.accessDenied();
        }
        PageParam pageParam = new PageParam(pageNum, pageSize);
        return CommonResult.success().data(newsService.getNewsList(pageParam)).total(newsService.getNewsCount());
    }
}
