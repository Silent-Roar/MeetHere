package lionel.meethere.user.controller;

import lionel.meethere.paging.PageParam;
import lionel.meethere.result.CommonResult;
import lionel.meethere.result.Result;
import lionel.meethere.result.UserResult;
import lionel.meethere.user.exception.IncorrectUsernameOrPasswordException;
import lionel.meethere.user.exception.UsernameAlreadyExistException;
import lionel.meethere.user.service.UserService;
import lionel.meethere.user.session.UserSessionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/user/")
public class UserController {
    @Autowired
    UserService service;
    public static final int MAX_USER_NAME_LENGTH = 20;
    public static final int MIN_USER_NAME_LENGTH = 2;
    public static final int MAX_PASS_WORD_LENGTH = 20;
    public static final int MIN_PASS_WORD_LENGTH = 2;
    public static final int MAX_TELEPHONE_LENGTH = 11;
    public static final int ADMIN_STATUS = 1;
    public static final int USER_STATUS = 0;

//ok
    @PostMapping("update/username")
    public Result<?> updateUsername(@SessionAttribute UserSessionInfo userSessionInfo,
                                 @RequestParam String newName) {

        if(newName.length() < MIN_USER_NAME_LENGTH || newName.length() > MAX_USER_NAME_LENGTH){
            return UserResult.invalidUsernameOrPassword();
        }
        try{
            service.updateUsername(userSessionInfo.getId(), newName);
        }catch (UsernameAlreadyExistException e){
            return CommonResult.failed();
        }

            return CommonResult.success();
    }
//ok
    @PostMapping("update/password")
    public Result<?> updatePassword(@SessionAttribute UserSessionInfo userSessionInfo,
                                    @RequestParam String oldPassword,
                                    @RequestParam String newPassword){

        if(newPassword.length() < MIN_PASS_WORD_LENGTH || newPassword.length() > MAX_PASS_WORD_LENGTH){
            return UserResult.invalidUsernameOrPassword();
        }
        try {
            service.updatePassword(userSessionInfo, oldPassword, newPassword);
        }catch (IncorrectUsernameOrPasswordException e){
            return UserResult.incorrectUsernameOrPassword();
        }
            return CommonResult.success();
    }
//ok
    @PostMapping("update/telephone")
    public Result<?> updateTelephone(@SessionAttribute UserSessionInfo userSessionInfo,
                                     @RequestParam String telephone){
        if(telephone.length() != MAX_TELEPHONE_LENGTH){
            return UserResult.invalidTelephone();
        }
            service.updateTelephone(userSessionInfo.getId(),telephone);
            return CommonResult.success();
    }

    @PostMapping("update/permission")
    public Result<?> updatePermission(@SessionAttribute UserSessionInfo userSessionInfo,
                                      @RequestParam Integer userId,
                                      @RequestParam Integer permission){
        if(userSessionInfo.getAdmin() != ADMIN_STATUS){
            return CommonResult.accessDenied();
        }
        service.updatePermission(userId,permission);
        return CommonResult.success();
    }

    @PostMapping("delete")
    public Result<?> deleteUser(@SessionAttribute UserSessionInfo userSessionInfo,
                                @RequestParam Integer userId){
        if(userSessionInfo.getAdmin() != ADMIN_STATUS){
            return CommonResult.accessDenied();
        }
        service.deleteUserById(userId);
        return CommonResult.success();
    }

    @PostMapping("list")
    public Result<?> getUerList(@SessionAttribute UserSessionInfo userSessionInfo,
                                @RequestParam Integer pageNum,
                                @RequestParam Integer pageSize){

        PageParam pageParam = new PageParam(pageNum,pageSize);
        if(userSessionInfo.getAdmin() != ADMIN_STATUS){
            return CommonResult.accessDenied();
        }

        return CommonResult.success().data(service.getUserList(pageParam)).total(service.getCountOfUser());
    }
   //ok
    @PostMapping("get")
    public Result<?> getUserById(@SessionAttribute UserSessionInfo userSessionInfo,
                                 @RequestParam Integer id){
        return CommonResult.success().data(service.getUserById(id));
    }

}
