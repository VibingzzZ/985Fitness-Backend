package com.fitnessuser.controller.Client;

import com.fitness985.fitnesscommon.result.Result;
import com.fitnessuser.vo.UserInfoResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/client")
public class ClientController {

    /**
     * 获取用户信息
     * @return  UserInfoResp 用户信息
     */
    @GetMapping("profile")
    public Result<UserInfoResp> GetUserInfo() {
        return null;
    }

}
