package com.fitnessuser.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.fitnessuser.entity.User;
import com.fitnessuser.entity.UserFace;
import com.fitnessuser.mapper.ClientMapper;
import com.fitnessuser.mapper.UserFaceMapper;
import java.util.List;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CancellationCleanupServiceTest {
    @Mock
    private ClientMapper clientMapper;
    @Mock
    private UserFaceMapper userFaceMapper;

    private CancellationCleanupService service;

    @BeforeEach
    void setUp() {
        MybatisConfiguration configuration = new MybatisConfiguration();
        MapperBuilderAssistant assistant = new MapperBuilderAssistant(configuration, "test");
        TableInfoHelper.initTableInfo(assistant, User.class);
        TableInfoHelper.initTableInfo(assistant, UserFace.class);
        service = new CancellationCleanupService(clientMapper, userFaceMapper);
    }

    @Test
    void shouldReturnZeroWithoutUpdatesWhenNoUserIsDue() {
        when(clientMapper.selectList(any())).thenReturn(List.of());

        int count = service.cleanupBatch();

        assertThat(count).isZero();
        verify(userFaceMapper, never()).update(isNull(), any());
        verify(clientMapper, never()).update(isNull(), any());
    }

    @Test
    void shouldClearFacesBeforeAnonymizingUsers() {
        User first = user(922337203685477000L);
        User second = user(922337203685477001L);
        when(clientMapper.selectList(any())).thenReturn(List.of(first, second));

        int count = service.cleanupBatch();

        assertThat(count).isEqualTo(2);
        InOrder updates = inOrder(userFaceMapper, clientMapper);
        updates.verify(userFaceMapper).update(isNull(), any());
        updates.verify(clientMapper).update(isNull(), any());
    }

    @Test
    void shouldPropagateCleanupFailure() {
        when(clientMapper.selectList(any())).thenReturn(List.of(user(1001L)));
        when(userFaceMapper.update(isNull(), any()))
                .thenThrow(new IllegalStateException("face cleanup failed"));

        assertThatThrownBy(service::cleanupBatch)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("face cleanup failed");
        verify(clientMapper, never()).update(isNull(), any());
    }

    private User user(Long id) {
        User user = new User();
        user.setId(id);
        return user;
    }
}
