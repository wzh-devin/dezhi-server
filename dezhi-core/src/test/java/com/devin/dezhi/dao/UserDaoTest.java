package com.devin.dezhi.dao;

import com.devin.dezhi.domain.entity.User;
import com.devin.dezhi.utils.ConvertUtils;
import com.devin.dezhi.utils.EncryptUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 2025/12/7 04:08.
 *
 * <p></p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@SpringBootTest
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void initSaveAdmin() {
        User user = new User();
        user.setId(ConvertUtils.toBigInteger(0));
        user.setUsername("administrator");
        user.setPassword(EncryptUtils.bcrypt("administrator"));
        user.setEmail("example@example.com");
        user.init();
        userDao.save(user);
    }
}
