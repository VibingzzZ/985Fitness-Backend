package com.fitnessuser.controller.Client;

import com.fitness985.fitnesscommon.result.Result;
import com.fitness985.fitnesssecurity.LoginPrincipal;
import com.fitness985.fitnesssecurity.jwt.JwtTokenService;
import com.fitnessuser.dto.BindPhoneReq;
import com.fitnessuser.service.ClientService;
import com.fitnessuser.vo.BindPhoneResp;
import com.fitnessuser.vo.UserInfoResp;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/client")

/**
 * 用户
 */
public class ClientController {

    @Autowired
    private ClientService clientService;
    /**
     * 获取用户信息
     * @return  UserInfoResp 用户信息
     */
    @GetMapping("profile")
    public Result<UserInfoResp> GetUserInfo(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        String token = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
        }
        LoginPrincipal  principal= JwtTokenService.parseAccessToken(token);
        UserInfoResp userInfo = clientService.getUserInfo(principal);

        return Result.success(userInfo);
    }

    /**
     * 查询用户余额
     * @param request    请求
     * @return  String 用户余额
     */
    @GetMapping("balance")
    public Result<String> GetUserBalance(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        String token = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
        }

        LoginPrincipal  principal= JwtTokenService.parseAccessToken(token);
        return Result.success(clientService.getUserBalance(principal));
    }


    /**
     * 绑定用户手机号
     *
     */
    @PostMapping("phone")
    public Result<BindPhoneResp> bindPhone(BindPhoneReq  req){
        return Result.success(clientService.bindPhone(req));
    }

    /**
     *  上传用户人脸
     * @param request    请求
     * @return  String 用户余额
     */


    /**
     * //TODO:临时密码登录，后续删除
     * 临时密码登录
     * @param password 密码
     * @return 登录结果
     */
    @PostMapping("login")
    public Result<String> login( String password){
        if (password.equals("123456")){
            return Result.success("登录成功");
        }
        return Result.error("密码错误");
    }

//    /**
//     *  合同管理
//     *  获取合同列表
//     */
//    @GetMapping("contract")

}
