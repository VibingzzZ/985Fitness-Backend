package com.fitnessuser.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fitness985.fitnesssecurity.jwt.JwtTokenService;
import com.fitnessuser.crypto.PhoneCryptoService;
import com.fitnessuser.dto.BindPhoneReq;
import com.fitnessuser.dto.CancellationReq;
import com.fitnessuser.dto.UpdateUserProfileReq;
import com.fitnessuser.dto.WechatLoginReq;
import com.fitnessuser.entity.User;
import com.fitnessuser.exception.UserBusinessException;
import com.fitnessuser.mapper.ClientMapper;
import com.fitnessuser.vo.LoginResp;
import com.fitnessuser.vo.StoredValueBalanceResp;
import com.fitnessuser.wechat.WechatGateway;
import com.fitnessuser.wechat.WechatSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {
    @Mock
    private ClientMapper clientMapper;
    @Mock
    private WechatGateway wechatGateway;
    @Mock
    private JwtTokenService jwtTokenService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private PhoneCryptoService phoneCryptoService;

    private ClientServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ClientServiceImpl(
                clientMapper,
                wechatGateway,
                jwtTokenService,
                passwordEncoder,
                phoneCryptoService);
    }

    @Test
    void shouldCreateUserAndReturnTokensOnFirstWechatLogin() {
        WechatLoginReq request = new WechatLoginReq();
        request.setCode("login-code");
        request.setNickname("新用户");
        when(wechatGateway.exchangeLoginCode("login-code"))
                .thenReturn(new WechatSession("openid-1", "unionid-1"));
        when(clientMapper.findByOpenid("openid-1")).thenReturn(null);
        when(clientMapper.insert(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1001L);
            return 1;
        });
        when(clientMapper.findActiveCards(1001L)).thenReturn(List.of());
        when(jwtTokenService.createAccessToken(any())).thenReturn("access-token");
        when(jwtTokenService.createRefreshToken(any())).thenReturn("refresh-token");
        when(jwtTokenService.getAccessTokenExpirationSeconds()).thenReturn(7200L);

        LoginResp response = service.wechatLogin(request);

        assertThat(response.getAccessToken()).isEqualTo("access-token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh-token");
        assertThat(response.getUser().getUserId()).isEqualTo(1001L);
        assertThat(response.getUser().getNickname()).isEqualTo("新用户");
        verify(clientMapper).insert(any(User.class));
    }

    @Test
    void shouldOnlyUpdateFieldsProvidedByProfilePatch() {
        User user = activeUser(1001L);
        user.setNickname("原昵称");
        user.setAvatar("old-avatar");
        user.setGender(1);
        user.setBirthday(LocalDate.of(2000, 1, 1));
        when(clientMapper.selectById(1001L)).thenReturn(user);
        when(clientMapper.findActiveCards(1001L)).thenReturn(List.of());
        UpdateUserProfileReq request = new UpdateUserProfileReq();
        request.setNickname("新昵称");

        service.updateUserInfo(1001L, request);

        assertThat(user.getNickname()).isEqualTo("新昵称");
        assertThat(user.getAvatar()).isEqualTo("old-avatar");
        assertThat(user.getGender()).isEqualTo(1);
        assertThat(user.getBirthday()).isEqualTo(LocalDate.of(2000, 1, 1));
    }

    @Test
    void shouldRejectPhoneAlreadyBoundToAnotherUser() {
        User user = activeUser(1001L);
        user.setPhone("13900000000");
        when(clientMapper.selectById(1001L)).thenReturn(user);
        when(wechatGateway.exchangePhoneCode("phone-code")).thenReturn("13800000000");
        when(phoneCryptoService.hash("13800000000")).thenReturn("phone-hash");
        when(phoneCryptoService.decrypt("13900000000")).thenReturn("13900000000");
        when(clientMapper.countByPhone("phone-hash", "13800000000")).thenReturn(1L);
        BindPhoneReq request = new BindPhoneReq();
        request.setCode("phone-code");

        assertThatThrownBy(() -> service.bindPhone(1001L, request))
                .isInstanceOf(UserBusinessException.class)
                .hasMessage("该手机号已绑定其他账号");
        verify(clientMapper, never()).updateById(user);
    }

    @Test
    void shouldRejectCancellationWhenRefundIsPending() {
        when(clientMapper.selectById(1001L)).thenReturn(activeUser(1001L));
        when(clientMapper.countPendingRefunds(1001L)).thenReturn(1L);
        CancellationReq request = new CancellationReq();

        assertThatThrownBy(() -> service.requestCancellation(1001L, request))
                .isInstanceOf(UserBusinessException.class)
                .hasMessage("存在处理中的退款，暂时无法注销");
    }

    @Test
    void shouldMarkUserAndRevokeFaceOnCancellation() {
        User user = activeUser(1001L);
        when(clientMapper.selectById(1001L)).thenReturn(user);
        CancellationReq request = new CancellationReq();
        request.setReason("不再使用");

        var response = service.requestCancellation(1001L, request);

        assertThat(user.getStatus()).isEqualTo(2);
        assertThat(user.getCancellationReason()).isEqualTo("不再使用");
        assertThat(response.getScheduledDeletionAt()).isEqualTo(response.getRequestedAt().plusDays(30));
        verify(clientMapper).revokeFaces(1001L);
    }

    @Test
    void shouldReturnZeroBalanceWhenAccountDoesNotExist() {
        when(clientMapper.selectById(1001L)).thenReturn(activeUser(1001L));
        when(clientMapper.findBalance(1001L)).thenReturn(null);

        StoredValueBalanceResp response = service.getUserBalance(1001L);

        assertThat(response.getBalance()).isZero();
        assertThat(response.getGiftBalance()).isZero();
        assertThat(response.getTotalBalance()).isZero();
    }

    private User activeUser(Long id) {
        User user = new User();
        user.setId(id);
        user.setOpenid("openid-" + id);
        user.setStatus(1);
        user.setRegisterTime(LocalDateTime.now());
        return user;
    }
}
