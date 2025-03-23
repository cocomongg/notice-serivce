package com.rsupport.notice.domain.user.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.rsupport.notice.common.error.CoreException;
import com.rsupport.notice.domain.user.entity.User;
import com.rsupport.notice.domain.user.repository.UserRepository;
import com.rsupport.notice.support.DatabaseCleanUp;
import com.rsupport.notice.support.TestContainerSupport;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest extends TestContainerSupport {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.execute();
    }

    @DisplayName("userId에 해당하는 사용자를 조회한다.")
    @Test
    void getUser() {
        // given
        User user = new User(null, "사용자1", LocalDateTime.now(), null);
        User savedUser = userRepository.save(user);

        // when
        User result = userService.getUser(savedUser.getUserId());

        // then
        assertEquals(savedUser.getUserId(), result.getUserId());
    }

    @DisplayName("존재하지 않는 userId로 사용자를 조회하면 CoreException이 발생한다.")
    @Test
    void getUser_NotFound() {
        // given
        Long notExistsUserId = 1L;

        // when, then
        assertThatThrownBy(() -> userService.getUser(notExistsUserId))
            .isInstanceOf(CoreException.class);
    }
}