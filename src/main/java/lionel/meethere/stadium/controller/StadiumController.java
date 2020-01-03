package lionel.meethere.stadium.controller;

import lionel.meethere.paging.PageParam;
import lionel.meethere.result.CommonResult;
import lionel.meethere.result.Result;
import lionel.meethere.stadium.entity.Stadium;
import lionel.meethere.stadium.service.StadiumService;
import lionel.meethere.user.session.UserSessionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Column;

@RestController()
@CrossOrigin
public class StadiumController {

    @Autowired
    private StadiumService stadiumService;

    //OK
    @PostMapping("/stadium/list")
    public Result<?> getStadium(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize) {
        PageParam pageParam = new PageParam(pageNum, pageSize);
        return CommonResult.success().data(stadiumService.getStadiums(pageParam)).total(stadiumService.getStadiumCount());
    }

    //OK
    @PostMapping("/stadium/get")
    public Result<?> getStadiumById(@RequestParam Integer id) {
        return CommonResult.success().data(stadiumService.getStadiumById(id));
    }

    @PostMapping("/stadium/create")
    public Result<?> createStadium(@SessionAttribute UserSessionInfo userSessionInfo,
                                   @RequestParam String name,
                                   @RequestParam String location,
                                   @RequestParam String image) {

        Stadium stadium = new Stadium();
        stadium.setName(name);
        stadium.setLocation(location);
        stadium.setImage(image);
        if (userSessionInfo.getAdmin() == 0) {
            return CommonResult.accessDenied();
        }
        stadiumService.createStadium(stadium);
        return CommonResult.success();
    }

    @PostMapping("/stadium/delete")
    public Result<?> deleteStadium(@SessionAttribute UserSessionInfo userSessionInfo,
                                   @RequestParam Integer id) {
        System.out.println(id);
        if (userSessionInfo.getAdmin() == 0) {
            return CommonResult.accessDenied();
        }

        stadiumService.delteStadium(id);
        return CommonResult.success();
    }

    @PostMapping("/stadium/update")
    public Result<?> updateStadium(@SessionAttribute UserSessionInfo userSessionInfo,
                                   @RequestParam Integer id,
                                   @RequestParam String name,
                                   @RequestParam String location,
                                   @RequestParam String image) {

        Stadium stadium = new Stadium(id,name,location,image);
        if (userSessionInfo.getAdmin() == 0) {
            return CommonResult.accessDenied();
        }
        stadiumService.updateStadium(stadium);
        return CommonResult.success();
    }
}
