package com.fitnessuser.controller.Admin;

import com.fitness985.fitnesscommon.result.Result;
import com.fitnessuser.dto.CoachPageQueryReq;
import com.fitnessuser.dto.CoachUpdateReq;
import com.fitnessuser.dto.ScheduleUpsertReq;
import com.fitnessuser.service.CoachService;
import com.fitnessuser.vo.AdminCoachResp;
import com.fitnessuser.vo.AdminScheduleResp;
import com.fitnessuser.vo.PageResp;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/tutors")
public class CoachManageController {
    private final CoachService coachService;

    public CoachManageController(CoachService coachService) {
        this.coachService = coachService;
    }

    @GetMapping
    public Result<PageResp<AdminCoachResp>> findCoaches(@Valid CoachPageQueryReq request) {
        return Result.success(coachService.findAdminCoaches(request));
    }

    @PutMapping("/{coachId}")
    public Result<AdminCoachResp> updateCoach(
            @PathVariable Long coachId, @Valid @RequestBody CoachUpdateReq request) {
        return Result.success(coachService.updateCoach(coachId, request));
    }

    @DeleteMapping("/{coachId}")
    public Result<String> removeCoach(@PathVariable Long coachId) {
        coachService.removeCoach(coachId);
        return Result.success("教练已下线");
    }

    @PostMapping("/{coachId}/schedules")
    public Result<List<AdminScheduleResp>> upsertSchedules(
            @PathVariable Long coachId, @Valid @RequestBody List<@Valid ScheduleUpsertReq> requests) {
        return Result.success(coachService.upsertSchedules(coachId, requests));
    }
}
