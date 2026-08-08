package com.fitnessuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fitnessuser.dto.CoachScheduleQueryReq;
import com.fitnessuser.dto.CoachPageQueryReq;
import com.fitnessuser.dto.CoachUpdateReq;
import com.fitnessuser.dto.ScheduleUpsertReq;
import com.fitnessuser.entity.Coach;
import com.fitnessuser.entity.Schedule;
import com.fitnessuser.exception.UserBusinessException;
import com.fitnessuser.mapper.CoachMapper;
import com.fitnessuser.mapper.ScheduleMapper;
import com.fitnessuser.service.CoachService;
import com.fitnessuser.vo.CoachResp;
import com.fitnessuser.vo.ScheduleResp;
import com.fitnessuser.vo.AdminCoachResp;
import com.fitnessuser.vo.AdminScheduleResp;
import com.fitnessuser.vo.PageResp;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CoachServiceImpl implements CoachService {
    private final CoachMapper coachMapper;
    private final ScheduleMapper scheduleMapper;

    public CoachServiceImpl(CoachMapper coachMapper, ScheduleMapper scheduleMapper) {
        this.coachMapper = coachMapper;
        this.scheduleMapper = scheduleMapper;
    }

    @Override
    public List<CoachResp> findAvailableCoaches(Long storeId) {
        LambdaQueryWrapper<Coach> wrapper = new LambdaQueryWrapper<Coach>()
                .eq(Coach::getStatus, 1)
                .eq(Coach::getDeleted, 0)
                .orderByAsc(Coach::getId);
        if (storeId != null) {
            wrapper.eq(Coach::getStoreId, storeId);
        }
        return coachMapper.selectList(wrapper).stream().map(this::toCoachResp).toList();
    }

    @Override
    public CoachResp getAvailableCoach(Long coachId) {
        Coach coach = coachMapper.selectOne(new LambdaQueryWrapper<Coach>()
                .eq(Coach::getId, coachId)
                .eq(Coach::getStatus, 1)
                .eq(Coach::getDeleted, 0));
        if (coach == null) {
            throw new UserBusinessException("教练不存在或暂不可预约");
        }
        return toCoachResp(coach);
    }

    @Override
    public List<ScheduleResp> findSchedules(CoachScheduleQueryReq request) {
        getAvailableCoach(request.getCoachId());
        LocalDateTime start = request.getDate().atStartOfDay();
        LocalDateTime end = request.getDate().plusDays(1).atStartOfDay();
        LambdaQueryWrapper<Schedule> wrapper = new LambdaQueryWrapper<Schedule>()
                .eq(Schedule::getCoachId, request.getCoachId())
                .eq(Schedule::getStatus, 1)
                .eq(Schedule::getDeleted, 0)
                .ge(Schedule::getStartTime, start)
                .lt(Schedule::getStartTime, end)
                .orderByAsc(Schedule::getStartTime);
        if (request.getStoreId() != null) {
            wrapper.eq(Schedule::getStoreId, request.getStoreId());
        }
        return scheduleMapper.selectList(wrapper).stream().map(this::toScheduleResp).toList();
    }

    @Override
    public PageResp<AdminCoachResp> findAdminCoaches(CoachPageQueryReq request) {
        LambdaQueryWrapper<Coach> wrapper = new LambdaQueryWrapper<Coach>()
                .eq(Coach::getDeleted, 0).orderByDesc(Coach::getId);
        if (request.getStoreId() != null)
            wrapper.eq(Coach::getStoreId, request.getStoreId());
        if (request.getStatus() != null)
            wrapper.eq(Coach::getStatus, request.getStatus());
        if (request.getKeyword() != null && !request.getKeyword().isBlank()) {
            wrapper.like(Coach::getName, request.getKeyword().trim());
        }
        Long total = coachMapper.selectCount(wrapper);
        List<Coach> coaches = coachMapper.selectList(wrapper.last("LIMIT " + request.getPageSize()
                + " OFFSET " + request.offset()));
        return PageResp.<AdminCoachResp>builder().list(coaches.stream().map(this::toAdminCoachResp).toList())
                .pageNo(request.getPageNo()).pageSize(request.getPageSize()).total(total).build();
    }

    @Override
    public AdminCoachResp updateCoach(Long coachId, CoachUpdateReq request) {
        Coach coach = coachMapper.selectById(coachId);
        if (coach == null || coach.getDeleted() != 0)
            throw new UserBusinessException("教练不存在");
        if (request.getStoreId() != null)
            coach.setStoreId(request.getStoreId());
        coach.setName(request.getName());
        coach.setAvatar(request.getAvatar());
        coach.setGender(request.getGender());
        coach.setSpecialties(request.getSpecialties());
        coach.setIntro(request.getIntro());
        coach.setPrice(request.getPrice());
        if (request.getStatus() != null)
            coach.setStatus(request.getStatus());
        coachMapper.updateById(coach);
        return toAdminCoachResp(coachMapper.selectById(coachId));
    }

    @Override
    public void removeCoach(Long coachId) {
        Coach coach = coachMapper.selectById(coachId);
        if (coach == null || coach.getDeleted() != 0)
            throw new UserBusinessException("教练不存在");
        coach.setStatus(2);
        coachMapper.updateById(coach);
    }

    @Override
    public List<AdminScheduleResp> upsertSchedules(Long coachId, List<ScheduleUpsertReq> requests) {
        Coach coach = coachMapper.selectById(coachId);
        if (coach == null || coach.getDeleted() != 0 || !Integer.valueOf(1).equals(coach.getStatus())) {
            throw new UserBusinessException("教练不存在或不可排班");
        }
        List<AdminScheduleResp> result = new ArrayList<>();
        for (ScheduleUpsertReq request : requests) {
            if (!request.getStartTime().isBefore(request.getEndTime()))
                throw new UserBusinessException("排班开始时间必须早于结束时间");
            Schedule schedule = request.getScheduleId() == null
                    ? new Schedule()
                    : scheduleMapper.selectById(request.getScheduleId());
            if (schedule == null || (schedule.getDeleted() != null && schedule.getDeleted() != 0)
                    || (request.getScheduleId() != null && !coachId.equals(schedule.getCoachId())))
                throw new UserBusinessException("排班不存在");
            int booked = schedule.getBookedCount() == null ? 0 : schedule.getBookedCount();
            if (request.getCapacity() < booked)
                throw new UserBusinessException("容量不能小于已预约人数");
            LambdaQueryWrapper<Schedule> overlap = new LambdaQueryWrapper<Schedule>().eq(Schedule::getCoachId, coachId)
                    .eq(Schedule::getDeleted, 0).lt(Schedule::getStartTime, request.getEndTime())
                    .gt(Schedule::getEndTime, request.getStartTime());
            if (schedule.getId() != null)
                overlap.ne(Schedule::getId, schedule.getId());
            if (scheduleMapper.selectCount(overlap) > 0)
                throw new UserBusinessException("排班时间与已有排班重叠");
            schedule.setCoachId(coachId);
            schedule.setStoreId(coach.getStoreId());
            schedule.setStartTime(request.getStartTime());
            schedule.setEndTime(request.getEndTime());
            schedule.setCapacity(request.getCapacity());
            if (schedule.getBookedCount() == null)
                schedule.setBookedCount(0);
            if (schedule.getStatus() == null)
                schedule.setStatus(1);
            if (schedule.getId() == null)
                scheduleMapper.insert(schedule);
            else
                scheduleMapper.updateById(schedule);
            result.add(toAdminScheduleResp(schedule));
        }
        return result;
    }

    private CoachResp toCoachResp(Coach coach) {
        return CoachResp.builder()
                .coachId(coach.getId())
                .storeId(coach.getStoreId())
                .name(coach.getName())
                .avatar(coach.getAvatar())
                .gender(coach.getGender())
                .specialties(coach.getSpecialties())
                .intro(coach.getIntro())
                .price(coach.getPrice())
                .status(coach.getStatus())
                .build();
    }

    private ScheduleResp toScheduleResp(Schedule schedule) {
        int capacity = schedule.getCapacity() == null ? 0 : schedule.getCapacity();
        int booked = schedule.getBookedCount() == null ? 0 : schedule.getBookedCount();
        return ScheduleResp.builder()
                .scheduleId(schedule.getId())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .capacity(capacity)
                .bookedCount(booked)
                .availableCount(Math.max(capacity - booked, 0))
                .status(schedule.getStatus())
                .build();
    }

    private AdminCoachResp toAdminCoachResp(Coach c) {
        return AdminCoachResp.builder().coachId(c.getId()).userId(c.getUserId()).storeId(c.getStoreId())
                .name(c.getName()).avatar(c.getAvatar()).gender(c.getGender()).specialties(c.getSpecialties())
                .intro(c.getIntro()).price(c.getPrice()).status(c.getStatus()).build();
    }

    private AdminScheduleResp toAdminScheduleResp(Schedule s) {
        return AdminScheduleResp.builder().scheduleId(s.getId()).storeId(s.getStoreId()).coachId(s.getCoachId())
                .startTime(s.getStartTime()).endTime(s.getEndTime()).capacity(s.getCapacity())
                .bookedCount(s.getBookedCount()).status(s.getStatus()).build();
    }
}
