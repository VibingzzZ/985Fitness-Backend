package com.fitnessuser.service;

import com.fitnessuser.dto.BindPhoneReq;
import com.fitnessuser.dto.UpdateUserProfileReq;
import com.fitnessuser.dto.WechatLoginReq;
import com.fitnessuser.vo.BindPhoneResp;
import com.fitnessuser.vo.LoginResp;
import com.fitnessuser.vo.StoredValueBalanceResp;
import com.fitnessuser.vo.UserInfoResp;

public interface ClientService {
    LoginResp wechatLogin(WechatLoginReq request);

    UserInfoResp getUserInfo(Long userId);

    UserInfoResp updateUserInfo(Long userId, UpdateUserProfileReq request);

    StoredValueBalanceResp getUserBalance(Long userId);

    BindPhoneResp bindPhone(Long userId, BindPhoneReq request);
}
