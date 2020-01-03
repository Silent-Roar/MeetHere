package lionel.meethere.site.controller;

import com.mysql.cj.xdevapi.UpdateParams;
import lionel.meethere.paging.PageParam;
import lionel.meethere.result.CommonResult;
import lionel.meethere.result.Result;
import lionel.meethere.site.entity.Site;
import lionel.meethere.site.param.SiteUpdateParam;
import lionel.meethere.site.service.SiteService;
import lionel.meethere.user.session.UserSessionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.stream.Stream;

@RestController
@CrossOrigin
public class SiteController {

    @Autowired
    private SiteService siteService;

    //ok
    @PostMapping("/site/get")
    public Result<?> getSite(@RequestParam Integer id){
        return CommonResult.success().data(siteService.getSiteById(id));
    }


    @PostMapping("/site/list")
    public Result<?> getSiteList(@RequestParam Integer pageNum,@RequestParam Integer pageSize){
        PageParam pageParam = new PageParam(pageNum,pageSize);
        return CommonResult.success().data(siteService.getSites(pageParam)).total(siteService.getSiteCount());
    }
    //ok
    @PostMapping("/site/listByStadium")
    public Result<?> getSiteListByStadium(@RequestParam Integer pageNum,@RequestParam Integer pageSize,@RequestParam Integer id){
        PageParam pageParam = new PageParam(pageNum,pageSize);
        return CommonResult.success().data(siteService.getSitesByStadium(id,pageParam)).total(siteService.getSiteCountByStadium(id));
    }

    @PostMapping("/site/create")
    public Result<?> createSite(@SessionAttribute UserSessionInfo userSessionInfo,
                                @RequestParam String name,
                                @RequestParam Integer stadiumId,
                                @RequestParam String location,
                                @RequestParam String description,
                                @RequestParam BigDecimal rent,
                                @RequestParam String image){

        if(userSessionInfo.getAdmin() == 0){
            return CommonResult.accessDenied();
        }

        Site site = new Site(0,name,stadiumId,location,description,rent,image);
        siteService.createSite(site);
        return CommonResult.success();
    }

    @PostMapping("/site/delete")
    public Result<?> deleteSite(@SessionAttribute UserSessionInfo userSessionInfo,
                                @RequestParam Integer id){
        if(userSessionInfo.getAdmin() == 0){
            return CommonResult.accessDenied();
        }
        siteService.deleteSite(id);
        return CommonResult.success();
    }

    @PostMapping("/site/update")
    public Result<?> updateSite(@SessionAttribute UserSessionInfo userSessionInfo,
                                @RequestParam Integer id,
                                @RequestParam String name,
                                @RequestParam Integer stadiumId,
                                @RequestParam String location,
                                @RequestParam String description,
                                @RequestParam BigDecimal rent,
                                @RequestParam String image){

        SiteUpdateParam siteUpdateParam = new SiteUpdateParam(id,name,stadiumId,location,description,rent,image);
        if(userSessionInfo.getAdmin() == 0){
            return CommonResult.accessDenied();
        }
        siteService.updateSite(siteUpdateParam);
        return CommonResult.success();
    }

}
