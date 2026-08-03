package com.fitnessuser.service.impl;

import com.fitness985.fitnesssecurity.LoginPrincipal;
import com.fitnessuser.dto.BindPhoneReq;
import com.fitnessuser.mapper.ClientMapper;
import com.fitnessuser.service.ClientService;
import com.fitnessuser.vo.BindPhoneResp;
import com.fitnessuser.vo.UserInfoResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ClientServiceImpl  implements ClientService {

    @Autowired
    private ClientMapper clientMapper;
    /**
     * 获取用户信息
     *
     * @param principal
     * @return
     */
    @Override
    public UserInfoResp getUserInfo(LoginPrincipal principal) {
        Long userId = principal.userId();

        return clientMapper.getUserInfo(userId);
    }

    /**
     * 获取用户余额
     *
     * @param principal 用户基础信息
     * @return Balance 用户余额
     */
    @Override
    public String getUserBalance(LoginPrincipal principal) {
        Long userId = principal.userId();
        return clientMapper.getUserBalance(userId);
    }

    /**
     * 绑定手机号
     *
     * @param req
     * @return
     */
    @Override
    public BindPhoneResp bindPhone(BindPhoneReq req) {

        //1.获取用户输入的验证码
        //TODO:获取验证码,从redis中获取进行校验
        //2.两者进行比较，用HasEquals

        //2.1如果不相同，那么返回错误信息

        //2.2相同，则将信息封装到BindPhoneResp中
        BindPhoneResp bindPhoneResp = new BindPhoneResp();
        //TODO:用户手机号模糊化
        bindPhoneResp.setBoundedAt(LocalDateTime.now().toString());
        // 3.封装完后将当前在redis中的验证码拉入黑名单或者删除
        return bindPhoneResp;
    }
}
