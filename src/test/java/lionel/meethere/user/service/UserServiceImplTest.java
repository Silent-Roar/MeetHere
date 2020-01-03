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
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
@SpringBootTest
class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Before
    public void setup() throws Exception{
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void when_service_do_login_with_matched_name_and_passwd_then_dispatch_mapper_to_return_user(){
        User user = new User(1,"lyb","18982170688","123456789",1);
        LoginParam loginParam = new LoginParam("lyb","123456789");

        when((userMapper.getUserByUsername("lyb"))).thenReturn(user);
        User returnU = userService.login(loginParam);
        verify(userMapper,times(1)).getUserByUsername("lyb");
    }

    @Test
    void when_service_do_login_with_unexisted_user_then_dispatch_mapper_to_throw_notexsistedException(){
        User user = new User(1,"lyb","18982170688","123456789",1);
        LoginParam loginParam = new LoginParam("lyb","123456789");

        when((userMapper.getUserByUsername("lyb"))).thenReturn(null);
        assertThrows(UsernameNotExistsException.class,()->userService.login(loginParam));
    }


    @Test
    void when_service_do_login_with_incorrect_passwd_then_dispatch_mapper_to_throw_notexsistedException(){
        User user = new User(1,"lyb","18982170688","123456789",1);
        LoginParam loginParam = new LoginParam("lyb","123456788");

        when((userMapper.getUserByUsername("lyb"))).thenReturn(user);
        assertThrows(IncorrectUsernameOrPasswordException.class,()->userService.login(loginParam));
    }
    @Test
    void when_service_do_get_user_by_username_then_dispatch_mapper_to_return_user(){
        User user =new User(1,"lyb","18982170688","123456789",1);
        when(userMapper.getUserByUsername("lyb")).thenReturn(user);
        UserVO returnU = userService.getUserByUsername("lyb");
        verify(userMapper,times(1)).getUserByUsername("lyb");
    }

    @Test
    void when_service_do_get_user_by_id_then_dispatch_mapper_to_return_uservo() {
        UserVO user =new UserVO(1,"lyb","18982170688");
        when(userMapper.getUserById(1)).thenReturn(user);
        UserVO returnU = userService.getUserById(1);
        verify(userMapper,times(1)).getUserById(1);
    }

    @Test
    void when_service_do_register_then_dispatch_mapper_to_insert_user() {
        RegisterParam registerParam = new RegisterParam("lyb","123456789","18982170688");
        User user =new User(null,"lyb","18982170688","123456789",0);
        userService.register(registerParam);
        verify(userMapper,times(1)).insertUser(user);
    }

    @Test
    void when_service_do_register_with_exsist_username_then_dispatch_mapper_to_deny(){
        RegisterParam registerParam = new RegisterParam("lyb","123456789","18982170688");
        User user =new User(null,"lyb","18982170688","123456788",0);
        when(userMapper.getUserByUsername("lyb")).thenReturn(user);
        assertThrows(UsernameAlreadyExistException.class,()->userService.register(registerParam));
    }

    @Test
    void when_service_do_update_password_with_matched_oldpasswd_then_dispatch_mapper_to_update_user() {
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",0);
        String oldPassword = "123456789";
        String newPassword = "987654321";
        User user =new User(1,"lyb","18982170688","123456789",0);
        when(userMapper.getUserByUsername("lyb")).thenReturn(user);
        userService.updatePassword(userSessionInfo,oldPassword,newPassword);
        verify(userMapper,times(1)).updatePasswordById(1,"987654321");
    }

    @Test
    void when_service_do_update_password_with_unmatched_oldpasswd_then_dispatch_mapper_to_update_user() {
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",0);
        String oldPassword = "123456789";
        String newPassword = "987654321";
        User user =new User(1,"lyb","18982170688","123456789",0);
        when(userMapper.getUserByUsername("lyb")).thenReturn(user);
        assertEquals(0,userService.updatePassword(userSessionInfo,oldPassword,newPassword));
    }

    @Test
    void when_service_do_update_password_with_same_password_then_throw_incorrectException(){
        UserSessionInfo userSessionInfo = new UserSessionInfo(1,"lyb",0);
        String oldPassword = "123456789";
        String newPassword = "123456789";
        User user =new User(1,"lyb","18982170688","123456789",0);
        when(userMapper.getUserByUsername("lyb")).thenReturn(user);
        assertThrows(IncorrectUsernameOrPasswordException.class,()->userService.updatePassword(userSessionInfo,oldPassword,newPassword));
    }

    @Test
    void when_service_do_update_username_with_valid_username_then_dispatch_mapper_to_update_user() {
        User user =new User(1,"lyb","18982170688","123456789",0);
        String newUserName = "luyubo";
        userService.updateUsername(1,newUserName);
        verify(userMapper,times(1)).updateUsernameById(1,newUserName);
    }

    @Test
    void when_service_do_update_username_with_same_usernamethen_dispatch_mapper_to_update_user() {
        User user =new User(1,"lyb","18982170688","123456789",0);
        String newUserName = "lyb";
        userService.updateUsername(1,newUserName);
        when(userMapper.getUserByUsername("lyb")).thenReturn(user);
        assertThrows(UsernameAlreadyExistException.class,()->userService.updateUsername(1,newUserName));
    }

    @Test
    void when_service_do_update_telephone_then_dispatch_mapper_to_update_user() {
        User user =new User(1,"lyb","18982170688","123456789",0);
        String newTelephone = "18982170687";
        userService.updateTelephone(1,newTelephone);
        verify(userMapper,times(1)).updateTelephoneById(1,newTelephone);
    }

    @Test
    void when_service_do_update_permission_then_dispatch_mapper_to_update_user() {
        User user =new User(1,"lyb","18982170688","123456789",0);
        Integer newPermission = 1;
        userService.updatePermission(1,newPermission);
        verify(userMapper,times(1)).updatePermission(1,newPermission);
    }


    @Test
    void when_service_do_getUserList_then_return_list_of_user() {
        PageParam pageParam = new PageParam(1,3);
        List<User> userList = new ArrayList<>();
        userList.add(new User(3,"xyl","12345678911","123",1));
        userList.add(new User(4,"zhangl","12345678912","123",1));
        userList.add(new User(5,"zhoul","12345678913","123",1));

        when(userMapper.getUserList(pageParam)).thenReturn(userList);
        when(userMapper.getUserById(3)).thenReturn(new UserVO(3,"xyl","18982170688"));

        List<User> returnUserList = userService.getUserList(pageParam);
        verify(userMapper,times(1)).getUserList(pageParam);

        User user = returnUserList.get(0);
        Assertions.assertAll(
                ()->assertEquals(3,user.getId()),
                ()->assertEquals("xyl",user.getUsername()),
                ()->assertEquals("12345678911",user.getTelephone()),
                ()->assertEquals("123",user.getPassword()),
                ()->assertEquals(1,user.getAdmin())
        );
    }

    @Test
    void when_service_do_delete_user_by_Id_then_dispatch_mapper_to_delete_user() {
        User user =new User(1,"lyb","18982170688","123456789",0);
        userService.deleteUserById(1);
        verify(userMapper,times(1)).deleteUserById(1);
    }

    @Test
    void when_service_do_get_count_of_user_then_return_count(){
        userService.getCountOfUser();
        verify(userMapper,times(1)).getCountOfUser();
    }
}