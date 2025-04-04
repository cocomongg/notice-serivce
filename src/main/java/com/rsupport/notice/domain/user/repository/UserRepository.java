package com.rsupport.notice.domain.user.repository;

import com.rsupport.notice.domain.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByUserIdIn(List<Long> userIdList);
}
