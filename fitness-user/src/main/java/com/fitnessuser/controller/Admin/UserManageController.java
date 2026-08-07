package com.fitnessuser.controller.Admin;

import com.fitness985.fitnesscommon.result.Result;
import com.fitnessuser.dto.UpdateUserStatusReq;
import com.fitnessuser.dto.UserPageQueryReq;
import com.fitnessuser.service.UserManageService;
import com.fitnessuser.vo.AdminUserListResp;
import com.fitnessuser.vo.PageResp;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/users")
public class UserManageController {
    private final UserManageService userManageService;

    // 构造函数
    public UserManageController(UserManageService userManageService) {
        this.userManageService = userManageService;
    }

    @GetMapping
    public Result<PageResp<AdminUserListResp>> findUsers(@Valid UserPageQueryReq request) {
        return Result.success(userManageService.findUsers(request));
    }

    @PatchMapping("/{userId}/status")
    public Result<Void> updateStatus(
            @PathVariable Long userId, @Valid @RequestBody UpdateUserStatusReq request) {
        userManageService.updateStatus(userId, request);
        return Result.success();
    }
}
