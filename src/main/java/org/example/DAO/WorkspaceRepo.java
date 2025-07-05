package org.example.DAO;

import org.example.entity.Reservation;
import org.example.entity.Workspace;
import org.example.exception.WorkspaceIsReservedException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public final class WorkspaceRepo extends Repo<Workspace> {

    private final ReservationRepo reservationRepo;

    public WorkspaceRepo(ReservationRepo reservationRepo) {
        super(Workspace.class);
        this.reservationRepo = reservationRepo;
    }

    @Override
    public boolean delete(int id) {
        List<Reservation> reservations = reservationRepo.getAllByWorkspace(id);

        if (!reservations.isEmpty()) {
            throw new WorkspaceIsReservedException("Workspace has dependent reservations; ID: " + id);
        }

        return super.delete(id);
    }
}
