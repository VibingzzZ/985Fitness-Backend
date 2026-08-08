package com.fitnessuser.service.impl;

import com.fitnessuser.crypto.PhoneCryptoService;
import com.fitnessuser.dto.UpdateUserStatusReq;
import com.fitnessuser.dto.UserPageQueryReq;
import com.fitnessuser.entity.User;
import com.fitnessuser.exception.UserBusinessException;
import com.fitnessuser.mapper.ClientMapper;
import com.fitnessuser.mapper.UserManageMapper;
import com.fitnessuser.service.UserManageService;
import com.fitnessuser.vo.AdminUserListResp;
import com.fitnessuser.vo.PageResp;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserManageServiceImpl implements UserManageService {
    private final UserManageMapper userManageMapper;
    private final ClientMapper clientMapper;
    private final PhoneCryptoService phoneCryptoService;

    public UserManageServiceImpl(
            UserManageMapper userManageMapper,
            ClientMapper clientMapper,
            PhoneCryptoService phoneCryptoService) {
        this.userManageMapper = userManageMapper;
        this.clientMapper = clientMapper;
        this.phoneCryptoService = phoneCryptoService;
    }

    @Override
    public PageResp<AdminUserListResp> findUsers(UserPageQueryReq request) {
        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            request.setPhoneHash(phoneCryptoService.hash(request.getPhone()));
        }
        List<AdminUserListResp> list = userManageMapper.findPage(request).stream()
                .map(this::toResponse)
                .toList();
        return PageResp.<AdminUserListResp>builder()
                .list(list)
                .pageNo(request.getPageNo())
                .pageSize(request.getPageSize())
                .total(userManageMapper.count(request))
                .build();
    }

    @Override
    @Transactional
    public void updateStatus(Long userId, UpdateUserStatusReq request) {
        User user = clientMapper.selectById(userId);
        if (user == null) {
            throw new UserBusinessException("用户不存在");
        }
        user.setStatus(request.getStatus());
        if (request.getRemark() != null) {
            user.setRemark(request.getRemark());
        }
        clientMapper.updateById(user);
    }

    private AdminUserListResp toResponse(User user) {
        AdminUserListResp response = new AdminUserListResp();
        response.setUserId(user.getId());
        response.setNickname(user.getNickname());
        response.setAvatar(user.getAvatar());
        response.setPhoneMasked(
                ClientServiceImpl.maskPhone(phoneCryptoService.decrypt(user.getPhone())));
        response.setGender(user.getGender());
        response.setStatus(user.getStatus());
        response.setRegisterTime(user.getRegisterTime());
        response.setLastLoginTime(user.getLastLoginTime());
        return response;
    }
}
