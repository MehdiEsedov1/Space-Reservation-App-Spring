package org.example.service;

import org.example.DAO.ReservationRepo;
import org.example.entity.Interval;
import org.example.entity.Reservation;
import org.example.entity.Workspace;
import org.example.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public final class ReservationService {

    private final ReservationRepo reservationRepo;
    private final WorkspaceService workspaceService;

    public ReservationService(ReservationRepo reservationRepo, WorkspaceService workspaceService) {
        this.reservationRepo = reservationRepo;
        this.workspaceService = workspaceService;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepo.getAll();
    }

    public Optional<Integer> makeReservation(String name, int spaceId, Interval interval) {
        Optional<Workspace> workspaceOpt = tryGetWorkspace(spaceId);
        if (workspaceOpt.isEmpty()) return Optional.empty();

        Workspace workspace = workspaceOpt.get();

        if (!workspaceService.isAvailable(spaceId, interval)) {
            return Optional.empty();
        }

        Reservation reservation = new Reservation(name, workspace, interval);
        int id = reservationRepo.save(reservation);
        return Optional.of(id);
    }

    public boolean cancelReservation(int id) {
        return reservationRepo.delete(id);
    }

    private Optional<Workspace> tryGetWorkspace(int spaceId) {
        try {
            return Optional.of(workspaceService.getWorkspaceById(spaceId));
        } catch (NotFoundException e) {
            return Optional.empty();
        }
    }
}
