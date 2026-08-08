package com.fitnessuser.service;

import com.fitnessuser.dto.CoachScheduleQueryReq;
import com.fitnessuser.vo.CoachResp;
import com.fitnessuser.vo.ScheduleResp;
import com.fitnessuser.dto.CoachPageQueryReq;
import com.fitnessuser.dto.CoachUpdateReq;
import com.fitnessuser.dto.ScheduleUpsertReq;
import com.fitnessuser.vo.AdminCoachResp;
import com.fitnessuser.vo.AdminScheduleResp;
import com.fitnessuser.vo.PageResp;
import java.util.List;
import java.util.List;

public interface CoachService {
    List<CoachResp> findAvailableCoaches(Long storeId);

    CoachResp getAvailableCoach(Long coachId);

    List<ScheduleResp> findSchedules(CoachScheduleQueryReq request);

    PageResp<AdminCoachResp> findAdminCoaches(CoachPageQueryReq request);

    AdminCoachResp updateCoach(Long coachId, CoachUpdateReq request);

    void removeCoach(Long coachId);

    List<AdminScheduleResp> upsertSchedules(Long coachId, List<ScheduleUpsertReq> requests);
}
