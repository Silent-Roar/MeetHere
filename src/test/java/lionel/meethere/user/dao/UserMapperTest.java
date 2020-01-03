package lionel.meethere.user.dao;

import lionel.meethere.user.entity.User;
import lionel.meethere.user.vo.UserVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @BeforeEach
    void setup(){
        this.userMapper.insertUser(new User(2,"lyb","12345678910","123",1));
        this.userMapper.insertUser(new User(3,"xyl","12345678911","123",1));
        this.userMapper.insertUser(new User(4,"zhangl","12345678912","123",1));
        this.userMapper.insertUser(new User(5,"zhoul","12345678913","123",1));
    }

    @Test
    @Transactional
    void when_insert_a_user_should_insert_success() {
        User user = new User(1, "test", "18982170688", "123", 1);
        this.userMapper.insertUser(user);
        UserVO userVO = userMapper.getUserById(1);
        Assertions.assertAll(
                () -> assertEquals(1, userVO.getId()),
                () -> assertEquals("test", userVO.getUsername())
        );
    }

    @Test
    void when_delete_a_user_by_Id_should_delete_success(){
        assertNotNull(this.userMapper.getUserById(2));
        assertEquals(1,this.userMapper.deleteUserById(2));
        assertNull(this.userMapper.getUserById(2));
    }

    @Test
    void when_update_a_user_with_updateParam_should_update_the_property(){

        assertEquals(1,this.userMapper.updatePasswordById(2,"321"));
        assertEquals(1,this.userMapper.updateTelephoneById(2,"18982170688"));
        assertEquals(1,this.userMapper.updateUsernameById(2,"luyubo"));
        UserVO userVO = userMapper.getUserById(2);
        Assertions.assertAll(
                () -> assertEquals("luyubo",userVO.getUsername())
        );
    }

    @Test
    void when_get_user_by_valid_Id_should_return_a_userVO(){
        UserVO userVO = userMapper.getUserById(2);
        assertNotNull(userVO);
        Assertions.assertAll(
                ()->assertEquals(2,userVO.getId()),
                ()->assertEquals("lyb",userVO.getUsername()),
                ()->assertEquals("12345678910",userVO.getTelephone())
        );

    }

    @Test
    void when_get_user_by_invalid_Id_should_return_null(){
        assertNull(this.userMapper.getUserById(10));
    }


}