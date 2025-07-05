package org.example.controller;

import org.example.entity.Interval;
import org.example.entity.Workspace;
import org.example.exception.InvalidTimeIntervalException;
import org.example.service.WorkspaceService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @GetMapping
    public String viewAllWorkspaces(Model model) {
        List<Workspace> all = workspaceService.getAllWorkspaces();
        model.addAttribute("workspaces", all);
        return "workspaces";
    }

    @GetMapping("/available")
    public String showAvailabilityForm() {
        return "check-availability";
    }

    @PostMapping("/available")
    public String findAvailableWorkspaces(
            @RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date start,
            @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date end,
            Model model) {

        try {
            Interval interval = new Interval.IntervalBuilder()
                    .startTime(start)
                    .endTime(end)
                    .build();

            List<Workspace> available = workspaceService.getAvailableWorkspaces(interval);

            model.addAttribute("availableWorkspaces", available);
            model.addAttribute("start", start);
            model.addAttribute("end", end);

        } catch (InvalidTimeIntervalException e) {
            model.addAttribute("error", "Invalid time interval: " + e.getMessage());
        }

        return "check-availability";
    }
}
