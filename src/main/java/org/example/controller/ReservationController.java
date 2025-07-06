package org.example.controller;

import org.example.entity.Reservation;
import org.example.service.ReservationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public String getAllReservations(Model model) {
        List<Reservation> reservations = reservationService.getAllReservations();
        model.addAttribute("reservations", reservations);
        return "reservations-list";
    }

    @PostMapping("/cancel")
    public String cancelReservation(@RequestParam("id") int id, Model model) {
        boolean cancelled = reservationService.cancelReservation(id);

        if (cancelled) {
            model.addAttribute("message", "Reservation cancelled successfully.");
        } else {
            model.addAttribute("error", "Reservation not found.");
        }

        List<Reservation> reservations = reservationService.getAllReservations();
        model.addAttribute("reservations", reservations);
        return "reservations-list";
    }
}
