package com.fitnessuser.service;

import com.fitness985.fitnesssecurity.LoginPrincipal;
import com.fitnessuser.dto.BindPhoneReq;
import com.fitnessuser.vo.BindPhoneResp;
import com.fitnessuser.vo.UserInfoResp;
import org.springframework.stereotype.Service;

@Service
public interface ClientService {

    /**
     * 获取用户信息
     *
     * @param principal 用户基础信息
     * @return UserInfoResp 用户信息
     */
    UserInfoResp getUserInfo(LoginPrincipal principal);

    /**
     * 获取用户余额
     * @param principal 用户基础信息
     * @return String 用户余额
     */
    String getUserBalance(LoginPrincipal principal);

    /**
     * 绑定手机号
     * @param req 绑定手机号请求
     * @return Object 绑定结果
     */
    BindPhoneResp bindPhone(BindPhoneReq req);
}
