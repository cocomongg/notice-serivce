package com.rsupport.notice.infra.notice;

import static org.assertj.core.api.Assertions.assertThat;

import com.rsupport.notice.support.RedisCleanUp;
import com.rsupport.notice.support.TestContainerSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NoticeViewCountRepositoryTest extends TestContainerSupport {
    @Autowired
    private NoticeViewCountRepositoryImpl noticeViewCountRepository;

    @Autowired
    private RedisCleanUp redisCleanUp;

    @AfterEach
    public void tearDown() {
        redisCleanUp.execute();
    }

    @DisplayName("처음 조회 시 조회수가 1로 증가한다.")
    @Test
    public void should_return1_when_firstTimeView() {
        // given
        Long noticeId = 1L;
        Long userId = 1L;

        // when
        long viewCount = noticeViewCountRepository.incrementViewCount(noticeId, userId);

        // then
        assertThat(viewCount).isEqualTo(1L);
    }

    @DisplayName("같은 사용자가 이미 조회한 경우 조회수가 증가하지 않는다.")
    @Test
    public void should_returnSameCount_when_userAlreadyViewed() {
        // given
        Long noticeId = 1L;
        Long userId = 1L;

        // when
        long firstViewCount = noticeViewCountRepository.incrementViewCount(noticeId, userId);
        long secondViewCount = noticeViewCountRepository.incrementViewCount(noticeId, userId);

        // then
        assertThat(firstViewCount).isEqualTo(1L);
        assertThat(secondViewCount).isEqualTo(1L);
    }

    @DisplayName("다른 사용자가 조회한 경우 조회수가 증가한다.")
    @Test
    public void should_IncreseCount_When_otherUserViewed() {
        // given
        Long noticeId = 1L;
        Long userId = 1L;
        Long otherUserId = 2L;

        // when
        long firstViewCount = noticeViewCountRepository.incrementViewCount(noticeId, userId);
        long secondViewCount = noticeViewCountRepository.incrementViewCount(noticeId, otherUserId);

        // then
        assertThat(firstViewCount).isEqualTo(1L);
        assertThat(secondViewCount).isEqualTo(2L);
    }
}