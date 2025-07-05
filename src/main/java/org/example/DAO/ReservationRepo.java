package org.example.DAO;

import jakarta.persistence.TypedQuery;
import org.example.entity.Reservation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public final class ReservationRepo extends Repo<Reservation> {

    public ReservationRepo() {
        super(Reservation.class);
    }

    public List<Reservation> getAllByWorkspace(int workspaceId) {
        String jpql = "SELECT r FROM Reservation r WHERE r.workspace.id = :workspaceId";
        TypedQuery<Reservation> query = entityManager.createQuery(jpql, Reservation.class);
        query.setParameter("workspaceId", workspaceId);

        return query.getResultList();
    }
}
