package com.fitnessuser.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.fitnessuser.dto.CoachScheduleQueryReq;
import com.fitnessuser.entity.Coach;
import com.fitnessuser.entity.Schedule;
import com.fitnessuser.exception.UserBusinessException;
import com.fitnessuser.mapper.CoachMapper;
import com.fitnessuser.mapper.ScheduleMapper;
import com.fitnessuser.vo.CoachResp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CoachServiceImplTest {
    @Mock
    private CoachMapper coachMapper;
    @Mock
    private ScheduleMapper scheduleMapper;

    private CoachServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CoachServiceImpl(coachMapper, scheduleMapper);
    }

    @Test
    void shouldMapAvailableCoachesWithoutExposingPhone() {
        Coach coach = new Coach();
        coach.setId(1001L);
        coach.setStoreId(2001L);
        coach.setName("李教练");
        coach.setPhone("13800000000");
        coach.setSpecialties(List.of("力量训练"));
        coach.setStatus(1);
        when(coachMapper.selectList(any(Wrapper.class))).thenReturn(List.of(coach));

        List<CoachResp> response = service.findAvailableCoaches(2001L);

        assertThat(response).hasSize(1);
        assertThat(response.getFirst().getCoachId()).isEqualTo(1001L);
        assertThat(response.getFirst().getName()).isEqualTo("李教练");
    }

    @Test
    void shouldReturnScheduleAvailabilityForRequestedDate() {
        Coach coach = new Coach();
        coach.setId(1001L);
        coach.setStatus(1);
        when(coachMapper.selectOne(any(Wrapper.class))).thenReturn(coach);

        Schedule schedule = new Schedule();
        schedule.setId(3001L);
        schedule.setStartTime(LocalDateTime.of(2026, 8, 10, 10, 0));
        schedule.setEndTime(LocalDateTime.of(2026, 8, 10, 11, 0));
        schedule.setCapacity(5);
        schedule.setBookedCount(2);
        schedule.setStatus(1);
        when(scheduleMapper.selectList(any(Wrapper.class))).thenReturn(List.of(schedule));

        CoachScheduleQueryReq request = new CoachScheduleQueryReq();
        request.setCoachId(1001L);
        request.setDate(LocalDate.of(2026, 8, 10));

        var response = service.findSchedules(request);

        assertThat(response).singleElement().satisfies(item -> {
            assertThat(item.getScheduleId()).isEqualTo(3001L);
            assertThat(item.getAvailableCount()).isEqualTo(3);
        });
    }

    @Test
    void shouldRejectSchedulesForUnavailableCoach() {
        when(coachMapper.selectOne(any(Wrapper.class))).thenReturn(null);
        CoachScheduleQueryReq request = new CoachScheduleQueryReq();
        request.setCoachId(1001L);
        request.setDate(LocalDate.of(2026, 8, 10));

        assertThatThrownBy(() -> service.findSchedules(request))
                .isInstanceOf(UserBusinessException.class)
                .hasMessage("教练不存在或暂不可预约");
        verify(scheduleMapper, never()).selectList(any(Wrapper.class));
    }

}
