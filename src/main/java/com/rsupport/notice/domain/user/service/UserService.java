package com.rsupport.notice.domain.user.service;

import com.rsupport.notice.common.error.CoreException;
import com.rsupport.notice.domain.user.entity.User;
import com.rsupport.notice.domain.user.error.UserErrorCode;
import com.rsupport.notice.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new CoreException(UserErrorCode.USER_NOT_FOUND, "userId: " + userId));
    }
}
