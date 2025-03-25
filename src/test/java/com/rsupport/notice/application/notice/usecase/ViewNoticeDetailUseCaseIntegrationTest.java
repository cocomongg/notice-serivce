package com.rsupport.notice.application.notice.usecase;

import static org.assertj.core.api.Assertions.assertThat;

import com.rsupport.notice.application.notice.dto.NoticeDetailInfo;
import com.rsupport.notice.domain.notice.dto.command.CreateNoticeCommand;
import com.rsupport.notice.domain.notice.entity.Notice;
import com.rsupport.notice.domain.notice.repository.NoticeRepository;
import com.rsupport.notice.domain.user.entity.User;
import com.rsupport.notice.domain.user.repository.UserRepository;
import com.rsupport.notice.infra.redis.CustomRedisRepository;
import com.rsupport.notice.support.DatabaseCleanUp;
import com.rsupport.notice.support.RedisCleanUp;
import com.rsupport.notice.support.TestContainerSupport;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ViewNoticeDetailUseCaseIntegrationTest extends TestContainerSupport {

    @Autowired
    private ViewNoticeDetailUseCase viewNoticeDetailUseCase;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private CustomRedisRepository customRedisRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @Autowired
    private RedisCleanUp redisCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.execute();
        redisCleanUp.execute();
    }

    @DisplayName("noticeId에 해당하는 NoticeDetailInfo를 반환하며 조회수가 1증가한다.")
    @Test
    void should_ReturnNoticeDetailInfo_When_ByNoticeId() {
        // given
        int viewCount = 10;
        LocalDateTime now = LocalDateTime.now();
        CreateNoticeCommand createNoticeCommand = new CreateNoticeCommand("title", "content", now,
            now, 1L);
        Notice notice = new Notice(createNoticeCommand);
        Notice savedNotice = noticeRepository.save(notice);
        Long noticeId = savedNotice.getNoticeId();

        User user = new User(null, "사용자", now, now);
        User savedUser = userRepository.save(user);

        String viewCountKey = "notice:viewCount:" + noticeId;
        customRedisRepository.setValue(viewCountKey, viewCount);

        // when
        NoticeDetailInfo result = viewNoticeDetailUseCase.execute(noticeId);

        // then
        assertThat(result.getNotice().getNoticeId()).isEqualTo(savedNotice.getNoticeId());
        assertThat(result.getUser().getUserId()).isEqualTo(savedUser.getUserId());
        assertThat(result.getNoticeFileList()).isEmpty();
        assertThat(result.getViewCount()).isEqualTo(viewCount + 1);

        Long value = customRedisRepository.getValue(viewCountKey, Long.class);
        assertThat(value).isEqualTo(viewCount + 1);
    }
}