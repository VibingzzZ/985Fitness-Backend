package com.fitnessuser.controller.Client;

import com.fitness985.fitnesscommon.result.Result;
import com.fitnessuser.dto.CoachScheduleQueryReq;
import com.fitnessuser.service.CoachService;
import com.fitnessuser.vo.CoachResp;
import com.fitnessuser.vo.ScheduleResp;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/client")
public class CoachController {
    private final CoachService coachService;

    public CoachController(CoachService coachService) {
        this.coachService = coachService;
    }

    @GetMapping("/tutors")
    public Result<List<CoachResp>> findCoaches(@RequestParam(required = false) Long storeId) {
        return Result.success(coachService.findAvailableCoaches(storeId));
    }

    @GetMapping("/tutors/{coachId}")
    public Result<CoachResp> getCoach(@PathVariable Long coachId) {
        return Result.success(coachService.getAvailableCoach(coachId));
    }

    @GetMapping("/schedules")
    public Result<List<ScheduleResp>> findSchedules(
            @Valid CoachScheduleQueryReq request) {
        return Result.success(coachService.findSchedules(request));
    }
}
