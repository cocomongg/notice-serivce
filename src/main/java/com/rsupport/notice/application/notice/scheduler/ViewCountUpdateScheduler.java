package com.rsupport.notice.application.notice.scheduler;

import com.rsupport.notice.domain.notice.service.NoticeService;
import com.rsupport.notice.infra.redis.CustomRedisRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Slf4j
@RequiredArgsConstructor
@Component
public class ViewCountUpdateScheduler {

    private static final String VIEW_COUNT_KEY_PREFIX = "notice:viewCount:";
    private static final String VIEW_COUNT_UPDATE_KEY = "notice:viewCount:update";

    private final CustomRedisRepository redisRepository;
    private final NoticeService noticeService;

    @Scheduled(fixedRate = 60000 * 3) // 3분 주기
    public void updateNoticeViewCounts() {
        try {
            Set<Object> noticeIdSet = redisRepository.members(VIEW_COUNT_UPDATE_KEY);
            if (CollectionUtils.isEmpty(noticeIdSet)) {
                return;
            }

            for (Object noticeIdStr : noticeIdSet) {
                Long noticeId = Long.valueOf((String)noticeIdStr);
                String viewCountKey = VIEW_COUNT_KEY_PREFIX + noticeIdStr;
                Integer viewCount = redisRepository.getValue(viewCountKey, Integer.class);
                if (viewCount != null) {
                    noticeService.updateViewCount(noticeId, viewCount);
                    log.info("Update ViewCount, noticeId[{}] viewCount[{}]", noticeId, viewCount);
                }

                redisRepository.removeFromSet(VIEW_COUNT_UPDATE_KEY, noticeIdStr);
            }
        } catch (Exception e) {
            log.error("Failed to scheduling update notice view counts", e);
        }
    }
}