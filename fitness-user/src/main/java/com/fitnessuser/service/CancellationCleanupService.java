package com.fitnessuser.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fitnessuser.entity.User;
import com.fitnessuser.entity.UserFace;
import com.fitnessuser.mapper.ClientMapper;
import com.fitnessuser.mapper.UserFaceMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CancellationCleanupService {
    private static final int BATCH_SIZE = 100;

    private final ClientMapper clientMapper;
    private final UserFaceMapper userFaceMapper;

    public CancellationCleanupService(ClientMapper clientMapper, UserFaceMapper userFaceMapper) {
        this.clientMapper = clientMapper;
        this.userFaceMapper = userFaceMapper;
    }

    @Transactional
    public int cleanupBatch() {
        LocalDateTime now = LocalDateTime.now();
        List<Long> userIds = clientMapper
                .selectList(new LambdaQueryWrapper<User>()
                        .select(User::getId)
                        .eq(User::getStatus, 2)
                        .eq(User::getDeleted, 0)
                        .le(User::getScheduledDeletionAt, now)
                        .orderByAsc(User::getScheduledDeletionAt)
                        .last("LIMIT " + BATCH_SIZE + " FOR UPDATE SKIP LOCKED"))
                .stream()
                .map(User::getId)
                .toList();

        if (userIds.isEmpty()) {
            return 0;
        }

        userFaceMapper.update(
                null,
                new LambdaUpdateWrapper<UserFace>()
                        .in(UserFace::getUserId, userIds)
                        .set(UserFace::getStatus, 0)
                        .set(UserFace::getFaceId, null)
                        .set(UserFace::getFeatureEnc, null)
                        .set(UserFace::getImageUrl, null));

        clientMapper.update(
                null,
                new LambdaUpdateWrapper<User>()
                        .in(User::getId, userIds)
                        .eq(User::getStatus, 2)
                        .eq(User::getDeleted, 0)
                        .set(User::getOpenid, null)
                        .set(User::getUnionid, null)
                        .set(User::getNickname, "已注销用户")
                        .set(User::getAvatar, null)
                        .set(User::getPhone, null)
                        .set(User::getPhoneHash, null)
                        .set(User::getPassword, null)
                        .set(User::getGender, null)
                        .set(User::getBirthday, null)
                        .set(User::getRemark, null)
                        .set(User::getCancellationReason, null)
                        .set(User::getDeleted, 1)
                        .set(User::getUpdateTime, now));

        return userIds.size();
    }
}
