package org.example.controller;

import org.example.entity.Interval;
import org.example.entity.Reservation;
import org.example.exception.InvalidTimeIntervalException;
import org.example.service.ReservationService;
import org.example.service.WorkspaceService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private final WorkspaceService workspaceService;
    private final ReservationService reservationService;

    public UserController(WorkspaceService workspaceService, ReservationService reservationService) {
        this.workspaceService = workspaceService;
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public String viewReservations(Model model) {
        List<Reservation> reservations = reservationService.getAllReservations();
        model.addAttribute("reservations", reservations);
        return "user-reservations";
    }

    @GetMapping("/reservations/new")
    public String showReservationForm(Model model) {
        model.addAttribute("workspaces", workspaceService.getAllWorkspaces());
        return "make-reservation";
    }

    @PostMapping("/reservations")
    public String makeReservation(@RequestParam("name") String name,
                                  @RequestParam("workspaceId") int workspaceId,
                                  @RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date start,
                                  @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date end,
                                  Model model) {

        Interval interval;
        try {
            interval = new Interval.IntervalBuilder()
                    .startTime(start)
                    .endTime(end)
                    .build();
        } catch (InvalidTimeIntervalException e) {
            model.addAttribute("error", "Invalid time interval: " + e.getMessage());
            model.addAttribute("workspaces", workspaceService.getAllWorkspaces());
            return "make-reservation";
        }

        if (interval.getStartTime().before(new Date())) {
            model.addAttribute("error", "Reservation time cannot be in the past.");
            model.addAttribute("workspaces", workspaceService.getAllWorkspaces());
            return "make-reservation";
        }

        boolean available = workspaceService.isAvailable(workspaceId, interval);
        if (!available) {
            model.addAttribute("error", "Workspace is not available for the selected interval.");
            model.addAttribute("workspaces", workspaceService.getAllWorkspaces());
            return "make-reservation";
        }

        reservationService.makeReservation(name, workspaceId, interval);
        model.addAttribute("message", "Reservation created successfully.");
        return "redirect:/user/reservations";
    }

    @PostMapping("/reservations/cancel")
    public String cancelReservation(@RequestParam("id") int id, Model model) {
        boolean cancelled = reservationService.cancelReservation(id);
        if (cancelled) {
            model.addAttribute("message", "Reservation cancelled successfully.");
        } else {
            model.addAttribute("error", "Reservation not found.");
        }

        List<Reservation> reservations = reservationService.getAllReservations();
        model.addAttribute("reservations", reservations);
        return "user-reservations";
    }
}
