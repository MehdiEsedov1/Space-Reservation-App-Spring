package org.example.controller;

import org.example.entity.Workspace;
import org.example.enums.WorkspaceType;
import org.example.exception.NotFoundException;
import org.example.exception.WorkspaceIsReservedException;
import org.example.exception.WorkspaceSaveFailed;
import org.example.service.ReservationService;
import org.example.service.WorkspaceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final WorkspaceService workspaceService;
    private final ReservationService reservationService;

    public AdminController(WorkspaceService workspaceService, ReservationService reservationService) {
        this.workspaceService = workspaceService;
        this.reservationService = reservationService;
    }

    @GetMapping
    public String adminHome(Model model) {
        List<Workspace> workspaces = workspaceService.getAllWorkspaces();
        model.addAttribute("workspaces", workspaces);
        model.addAttribute("reservations", reservationService.getAllReservations());
        return "admin";
    }

    @PostMapping("/create")
    public String createWorkspace(@RequestParam String name,
                                  @RequestParam WorkspaceType type,
                                  @RequestParam BigDecimal price,
                                  @RequestParam String address,
                                  Model model) {
        Workspace workspace = new Workspace(name, type, price);
        try {
            workspaceService.createWorkspace(workspace);
            model.addAttribute("successMessage", "Workspace created successfully.");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to create workspace.");
        }
        model.addAttribute("workspaces", workspaceService.getAllWorkspaces());
        model.addAttribute("reservations", reservationService.getAllReservations());
        return "admin";
    }

    @PostMapping("/update")
    public String updateWorkspace(@RequestParam int id,
                                  @RequestParam(required = false) String name,
                                  @RequestParam(required = false) WorkspaceType type,
                                  @RequestParam(required = false) BigDecimal price,
                                  @RequestParam(required = false) String address,
                                  Model model) {
        try {
            Workspace existing = workspaceService.getWorkspaceById(id);
            if (name != null) existing.setName(name);
            if (type != null) existing.setType(type);
            if (price != null) existing.setPrice(price);
            workspaceService.editWorkspace(id, existing);
            model.addAttribute("successMessage", "Workspace updated successfully.");
        } catch (NotFoundException e) {
            model.addAttribute("errorMessage", "Workspace not found.");
        } catch (WorkspaceSaveFailed e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        model.addAttribute("workspaces", workspaceService.getAllWorkspaces());
        model.addAttribute("reservations", reservationService.getAllReservations());
        return "admin";
    }

    @PostMapping("/delete")
    public String deleteWorkspace(@RequestParam int id, Model model) {
        try {
            boolean deleted = workspaceService.deleteWorkspace(id);
            if (deleted) {
                model.addAttribute("successMessage", "Workspace deleted successfully.");
            } else {
                model.addAttribute("errorMessage", "Workspace not found.");
            }
        } catch (WorkspaceIsReservedException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        model.addAttribute("workspaces", workspaceService.getAllWorkspaces());
        model.addAttribute("reservations", reservationService.getAllReservations());
        return "admin";
    }
}
