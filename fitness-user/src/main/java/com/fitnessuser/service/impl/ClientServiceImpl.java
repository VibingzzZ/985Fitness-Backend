package com.fitnessuser.service.impl;

import com.fitness985.fitnesssecurity.LoginPrincipal;
import com.fitness985.fitnesssecurity.jwt.JwtTokenService;
import com.fitnessuser.dto.BindPhoneReq;
import com.fitnessuser.dto.PasswordLoginReq;
import com.fitnessuser.dto.PasswordRegisterReq;
import com.fitnessuser.dto.UpdateUserProfileReq;
import com.fitnessuser.dto.WechatLoginReq;
import com.fitnessuser.entity.User;
import com.fitnessuser.exception.UserBusinessException;
import com.fitnessuser.mapper.ClientMapper;
import com.fitnessuser.service.ClientService;
import com.fitnessuser.vo.BindPhoneResp;
import com.fitnessuser.vo.LoginResp;
import com.fitnessuser.vo.StoredValueBalanceResp;
import com.fitnessuser.vo.UserInfoResp;
import com.fitnessuser.wechat.WechatGateway;
import com.fitnessuser.wechat.WechatSession;
import java.time.LocalDateTime;
import java.util.Set;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientMapper clientMapper;
    private final WechatGateway wechatGateway;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;

    public ClientServiceImpl(
            ClientMapper clientMapper,
            WechatGateway wechatGateway,
            JwtTokenService jwtTokenService,
            PasswordEncoder passwordEncoder) {
        this.clientMapper = clientMapper;
        this.wechatGateway = wechatGateway;
        this.jwtTokenService = jwtTokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public LoginResp wechatLogin(WechatLoginReq request) {
        WechatSession session = wechatGateway.exchangeLoginCode(request.getCode());
        User user = clientMapper.findByOpenid(session.openid());
        LocalDateTime now = LocalDateTime.now();
        if (user == null) {
            user = new User();
            user.setOpenid(session.openid());
            user.setUnionid(session.unionid());
            user.setNickname(request.getNickname());
            user.setAvatar(request.getAvatar());
            user.setStatus(1);
            user.setRegisterTime(now);
            user.setLastLoginTime(now);
            user.setCreateTime(now);
            user.setUpdateTime(now);
            user.setDeleted(0);
            clientMapper.insert(user);
        } else {
            ensureUsable(user);
            if (StringUtils.hasText(request.getNickname())) {
                user.setNickname(request.getNickname());
            }
            if (StringUtils.hasText(request.getAvatar())) {
                user.setAvatar(request.getAvatar());
            }
            user.setLastLoginTime(now);
            clientMapper.updateById(user);
        }
        if (StringUtils.hasText(request.getPhoneCode())) {
            bindPhone(user.getId(), phoneRequest(request.getPhoneCode()));
            user = requireUser(user.getId());
        }
        return buildLoginResp(user);
    }

    @Override
    @Transactional
    public LoginResp passwordLogin(PasswordLoginReq request) {
        User user = clientMapper.findByPhone(request.getPhone());
        if (user == null) {
            throw new UserBusinessException("手机号未注册");
        }
        ensureUsable(user);
        if (!StringUtils.hasText(user.getPassword())) {
            throw new UserBusinessException("该账号未设置密码，请使用微信登录");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserBusinessException("密码错误");
        }
        user.setLastLoginTime(LocalDateTime.now());
        clientMapper.updateById(user);
        return buildLoginResp(user);
    }

    @Override
    @Transactional
    public LoginResp passwordRegister(PasswordRegisterReq request) {
        if (clientMapper.countByPhone(request.getPhone()) > 0) {
            throw new UserBusinessException("该手机号已注册");
        }
        LocalDateTime now = LocalDateTime.now();
        User user = new User();
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(
                StringUtils.hasText(request.getNickname())
                        ? request.getNickname()
                        : "用户" + request.getPhone().substring(request.getPhone().length() - 4));
        user.setStatus(1);
        user.setRegisterTime(now);
        user.setLastLoginTime(now);
        user.setCreateTime(now);
        user.setUpdateTime(now);
        user.setDeleted(0);
        clientMapper.insert(user);
        return buildLoginResp(user);
    }

    @Override
    public UserInfoResp getUserInfo(Long userId) {
        return toUserInfo(requireUser(userId));
    }

    @Override
    @Transactional
    public UserInfoResp updateUserInfo(Long userId, UpdateUserProfileReq request) {
        User user = requireUser(userId);
        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }
        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }
        if (request.getBirthday() != null) {
            user.setBirthday(request.getBirthday());
        }
        clientMapper.updateById(user);
        return toUserInfo(user);
    }

    @Override
    public StoredValueBalanceResp getUserBalance(Long userId) {
        requireUser(userId);
        StoredValueBalanceResp balance = clientMapper.findBalance(userId);
        return balance == null
                ? StoredValueBalanceResp.builder()
                        .balance(0L)
                        .giftBalance(0L)
                        .totalBalance(0L)
                        .updatedAt(null)
                        .build()
                : balance;
    }

    @Override
    @Transactional
    public BindPhoneResp bindPhone(Long userId, BindPhoneReq request) {
        User user = requireUser(userId);
        String phone = wechatGateway.exchangePhoneCode(request.getCode());
        if (clientMapper.countByPhone(phone) > 0 && !phone.equals(user.getPhone())) {
            throw new UserBusinessException("该手机号已绑定其他账号");
        }
        user.setPhone(phone);
        clientMapper.updateById(user);
        BindPhoneResp response = new BindPhoneResp();
        response.setPhoneMasked(maskPhone(phone));
        response.setBoundAt(LocalDateTime.now().toString());
        return response;
    }

    private User requireUser(Long userId) {
        User user = clientMapper.selectById(userId);
        if (user == null) {
            throw new UserBusinessException("用户不存在");
        }
        ensureUsable(user);
        return user;
    }

    private void ensureUsable(User user) {
        if (!Integer.valueOf(1).equals(user.getStatus())) {
            throw new UserBusinessException("用户账号不可用");
        }
    }

    private LoginResp buildLoginResp(User user) {
        LoginPrincipal principal = new LoginPrincipal(user.getId(), user.getNickname(), Set.of("USER"));
        return LoginResp.builder()
                .accessToken(jwtTokenService.createAccessToken(principal))
                .refreshToken(jwtTokenService.createRefreshToken(principal))
                .tokenType("Bearer")
                .expiresIn(jwtTokenService.getAccessTokenExpirationSeconds())
                .user(toUserInfo(user))
                .build();
    }

    private UserInfoResp toUserInfo(User user) {
        return UserInfoResp.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .phoneMasked(maskPhone(user.getPhone()))
                .gender(user.getGender())
                .birthday(user.getBirthday())
                .status(user.getStatus())
                .registerTime(user.getRegisterTime())
                .faceStatus(clientMapper.countActiveFaces(user.getId()) > 0 ? "ENROLLED" : "NOT_ENROLLED")
                .activeCards(clientMapper.findActiveCards(user.getId()))
                .build();
    }

    private BindPhoneReq phoneRequest(String code) {
        BindPhoneReq request = new BindPhoneReq();
        request.setCode(code);
        return request;
    }

    public static String maskPhone(String phone) {
        if (!StringUtils.hasText(phone) || phone.length() < 7) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
    }
}
