package lionel.meethere.user.controller;

import lionel.meethere.result.CommonResult;
import lionel.meethere.result.Result;
import lionel.meethere.user.session.UserSessionInfo;
import lionel.meethere.user.entity.User;
import lionel.meethere.user.exception.IncorrectUsernameOrPasswordException;
import lionel.meethere.user.exception.UsernameAlreadyExistException;
import lionel.meethere.user.exception.UsernameNotExistsException;
import lionel.meethere.user.param.LoginParam;
import lionel.meethere.user.param.RegisterParam;
import lionel.meethere.result.UserResult;
import lionel.meethere.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@CrossOrigin
public class LoginController {

    @Autowired
    private UserService userService;

    public static final int MAX_UMAX_USER_NAME_LENGTHNameLength = 20;
    public static final int MIN_USER_NAME_LENGTH = 2;
    public static final int MAX_PASS_WORD_LENGTH = 20;
    public static final int MIN_PASS_WORD_LENGTH = 2;

    @PostMapping("/login")
    public Result<?> login(@RequestParam String username,@RequestParam String  password,
            HttpSession httpSession){
        //参数校验
        LoginParam loginParam = new LoginParam(username,password);
        if(loginParam.getUsername().length() < MIN_USER_NAME_LENGTH || loginParam.getUsername().length() > MAX_UMAX_USER_NAME_LENGTHNameLength
                ||loginParam.getPassword().length() < MIN_PASS_WORD_LENGTH ||loginParam.getPassword().length() > MAX_PASS_WORD_LENGTH){
            return UserResult.invalidUsernameOrPassword();
        }

        UserSessionInfo userSessionInfo = new UserSessionInfo();

        //请求转发，会话管理
        try{
            User user = userService.login(loginParam);
            BeanUtils.copyProperties(user,userSessionInfo);
            httpSession.setAttribute("userSessionInfo",userSessionInfo);
        }catch (UsernameNotExistsException e){
            return UserResult.usernameNotExists();
        }
        catch (IncorrectUsernameOrPasswordException e){
            return UserResult.incorrectUsernameOrPassword();
        }

        return CommonResult.success().data(userSessionInfo);
    }

    @PostMapping("/register")
    public Result<?> register(@RequestParam String username,@RequestParam String  password){

        RegisterParam registerParam = new RegisterParam(username,password,"");
        //参数校验
        if(registerParam.getUsername().length() < MIN_USER_NAME_LENGTH || registerParam.getUsername().length() > MAX_UMAX_USER_NAME_LENGTHNameLength
                || registerParam.getPassword().length() < MIN_USER_NAME_LENGTH || registerParam.getPassword().length() > MAX_PASS_WORD_LENGTH){
            return UserResult.invalidUsernameOrPassword();
        }

         try{
             userService.register(registerParam);
         }catch (UsernameAlreadyExistException e){
             return UserResult.usernameAlreadyExists();
         }
         return CommonResult.success();
    }

//OK

    @PostMapping("/logout")
    public Result<?> logout(HttpSession session){
        session.invalidate();
        return CommonResult.success();
    }

}
