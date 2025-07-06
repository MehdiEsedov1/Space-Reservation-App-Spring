package org.example.service;

import org.example.DAO.ReservationRepo;
import org.example.DAO.WorkspaceRepo;
import org.example.entity.Interval;
import org.example.entity.Reservation;
import org.example.entity.Workspace;
import org.example.exception.NotFoundException;
import org.example.exception.WorkspaceSaveFailed;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class WorkspaceService {

    private final WorkspaceRepo workspaceRepo;
    private final ReservationRepo reservationRepo;

    public WorkspaceService(WorkspaceRepo workspaceRepo, ReservationRepo reservationRepo) {
        this.workspaceRepo = workspaceRepo;
        this.reservationRepo = reservationRepo;
    }

    public Workspace getWorkspaceById(int id) throws NotFoundException {
        return workspaceRepo.getById(id)
                .orElseThrow(() -> new NotFoundException("Workspace not found!"));
    }

    public List<Workspace> getAllWorkspaces() {
        return workspaceRepo.getAll();
    }

    public List<Workspace> getAvailableWorkspaces(Interval interval) {
        return getAllWorkspaces().stream()
                .filter(workspace -> isAvailable(workspace.getId(), interval))
                .toList();
    }

    public int createWorkspace(Workspace workspace) {
        return workspaceRepo.save(workspace);
    }

    public int editWorkspace(int id, Workspace updatedWorkspace) throws WorkspaceSaveFailed {
        if (!workspaceExists(id)) {
            throw new WorkspaceSaveFailed("Couldn't save workspace: not found.");
        }

        updatedWorkspace.setId(id);
        return workspaceRepo.save(updatedWorkspace);
    }

    public boolean deleteWorkspace(int id) {
        return workspaceRepo.delete(id);
    }

    public boolean workspaceExists(int id) {
        return workspaceRepo.getById(id).isPresent();
    }

    public boolean isAvailable(int workspaceId, Interval interval) {
        List<Reservation> reservations = reservationRepo.getAllByWorkspace(workspaceId);
        return reservations.stream()
                .noneMatch(existing -> Interval.isOverlap(interval, existing.getInterval()));
    }
}
