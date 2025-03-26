package com.rsupport.notice.infra.notice;

import com.rsupport.notice.common.utils.DateUtils;
import com.rsupport.notice.domain.notice.repository.NoticeViewCountRepository;
import com.rsupport.notice.infra.redis.CustomRedisRepository;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class NoticeViewCountRepositoryImpl implements NoticeViewCountRepository {

    private static final String VIEW_COUNT_KEY_PREFIX = "notice:viewCount:";
    private static final String USER_VIEW_KEY_PREFIX = "notice:view:user:";
    private static final String VIEW_COUNT_UPDATE_KEY = "notice:viewCount:update";

    private final CustomRedisRepository redisRepository;

    @Override
    public long incrementViewCount(Long noticeId, Long userId) {
        String viewCountKey = VIEW_COUNT_KEY_PREFIX + noticeId;
        String userViewKey = USER_VIEW_KEY_PREFIX + userId;

        if(redisRepository.isMemberOfSet(userViewKey, String.valueOf(noticeId))) {
            return this.getViewCount(noticeId);
        }

        try {
            redisRepository.addToSet(userViewKey, String.valueOf(noticeId));

            long secondsUntilMidnight = DateUtils.getSecondsUntilMidnight();
            redisRepository.setExpire(userViewKey, Duration.of(secondsUntilMidnight, ChronoUnit.SECONDS));

            redisRepository.addToSet(VIEW_COUNT_UPDATE_KEY, String.valueOf(noticeId));

            return redisRepository.incrementValue(viewCountKey);
        } catch (Exception e) {
            log.error("Failed to increment view count, noticeId[{}], userId[{}]", noticeId, userId, e);
        }

        return 0L;
    }

    @Override
    public long getViewCount(Long noticeId) {
        String key = VIEW_COUNT_KEY_PREFIX + noticeId;
        return redisRepository.getValue(key, Long.class);
    }
}
