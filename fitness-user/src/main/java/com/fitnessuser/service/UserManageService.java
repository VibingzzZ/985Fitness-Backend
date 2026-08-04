package com.fitnessuser.service;

import com.fitnessuser.dto.UpdateUserStatusReq;
import com.fitnessuser.dto.UserPageQueryReq;
import com.fitnessuser.vo.AdminUserListResp;
import com.fitnessuser.vo.PageResp;

public interface UserManageService {
    PageResp<AdminUserListResp> findUsers(UserPageQueryReq request);

    void updateStatus(Long userId, UpdateUserStatusReq request);
}
