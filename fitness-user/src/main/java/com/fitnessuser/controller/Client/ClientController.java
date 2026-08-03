package com.fitnessuser.controller.Client;

import com.fitness985.fitnesscommon.result.Result;
import com.fitness985.fitnesssecurity.SecurityUtils;
import com.fitnessuser.dto.BindPhoneReq;
import com.fitnessuser.dto.UpdateUserProfileReq;
import com.fitnessuser.dto.WechatLoginReq;
import com.fitnessuser.service.ClientService;
import com.fitnessuser.vo.BindPhoneResp;
import com.fitnessuser.vo.LoginResp;
import com.fitnessuser.vo.StoredValueBalanceResp;
import com.fitnessuser.vo.UserInfoResp;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/client")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/auth/wechat-login")
    public Result<LoginResp> wechatLogin(@Valid @RequestBody WechatLoginReq request) {
        return Result.success(clientService.wechatLogin(request));
    }

    @GetMapping("/profile")
    public Result<UserInfoResp> getUserInfo() {
        return Result.success(clientService.getUserInfo(SecurityUtils.currentUserId()));
    }

    @PatchMapping("/profile")
    public Result<UserInfoResp> updateUserInfo(
            @Valid @RequestBody UpdateUserProfileReq request) {
        return Result.success(
                clientService.updateUserInfo(SecurityUtils.currentUserId(), request));
    }

    @GetMapping({"/stored-value/balance", "/balance"})
    public Result<StoredValueBalanceResp> getUserBalance() {
        return Result.success(clientService.getUserBalance(SecurityUtils.currentUserId()));
    }

    @PostMapping("/phone")
    public Result<BindPhoneResp> bindPhone(@Valid @RequestBody BindPhoneReq request) {
        return Result.success(clientService.bindPhone(SecurityUtils.currentUserId(), request));
    }

    /**
     * 临时密码登录TODO:后续删除
     * @param password
     * @return
     */
    @PostMapping("/auth/login")
    public Result<Void> login(@Valid String password) {
        if (password.equals("123456")) {
            return Result.success();
        }
        return Result.error("密码错误");
    }
}
