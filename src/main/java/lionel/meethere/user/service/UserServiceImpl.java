package lionel.meethere.user.service;

import lionel.meethere.paging.PageParam;
import lionel.meethere.user.dao.UserMapper;
import lionel.meethere.user.entity.User;
import lionel.meethere.user.exception.IncorrectUsernameOrPasswordException;
import lionel.meethere.user.exception.UsernameAlreadyExistException;
import lionel.meethere.user.exception.UsernameNotExistsException;
import lionel.meethere.user.param.LoginParam;
import lionel.meethere.user.param.RegisterParam;
import lionel.meethere.user.session.UserSessionInfo;
import lionel.meethere.user.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("ALL")
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public User login(LoginParam loginParam) {
        String username = loginParam.getUsername();
        String password = loginParam.getPassword();
        User user = userMapper.getUserByUsername(username);
        if(user == null) {
            throw new UsernameNotExistsException();
        }
        if(!password.equals(user.getPassword())) {
            throw new IncorrectUsernameOrPasswordException();
        }
        return user;
    }

    @Override
    public UserVO getUserByUsername(String username) {
        User user = userMapper.getUserByUsername(username);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO);
        return userVO;
    }

    @Override
    public UserVO getUserById(Integer id) {
        return userMapper.getUserById(id);
    }

    @Override
    public int register(RegisterParam registerParam) {
        String username = registerParam.getUsername();
        String password = registerParam.getPassword();
        String telephone = registerParam.getTelephone();
        System.out.println(telephone);

        if(userMapper.getUserByUsername(username) != null) {
            throw new UsernameAlreadyExistException();
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setTelephone(telephone);
        user.setAdmin(0);

        return userMapper.insertUser(user);
    }


    @Override
    public int updatePassword(UserSessionInfo userSessionInfo, String oldPassword, String newPassword) {
        User user = userMapper.getUserByUsername(userSessionInfo.getUsername());
        if(oldPassword.equals(user.getPassword())) {
            throw new IncorrectUsernameOrPasswordException();
        }
        return userMapper.updatePasswordById(userSessionInfo.getId(),newPassword);
    }

    @Override
    public int updateUsername(Integer id, String username) {
        if(userMapper.getUserByUsername(username)!=null) {
            throw  new UsernameAlreadyExistException();
        }
        return userMapper.updateUsernameById(id,username);
    }

    @Override
    public int updateTelephone(Integer id, String telephone) {
        return userMapper.updateTelephoneById(id,telephone);
    }

    @Override
    public int updatePermission(Integer id, Integer admin) {
        return userMapper.updatePermission(id,admin);
    }

    @Override
    public List<User> getUserList(PageParam pageParam) {
        return userMapper.getUserList(pageParam);
    }

    @Override
    public int deleteUserById(Integer id) {
        return userMapper.deleteUserById(id);
    }

    @Override
    public int getCountOfUser(){return userMapper.getCountOfUser();}

}
