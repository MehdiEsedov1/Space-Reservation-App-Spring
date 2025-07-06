package org.example.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.example.entity.IEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class Repo<T extends IEntity> {

    private final Class<T> clazz;

    @PersistenceContext
    protected EntityManager entityManager;

    protected Repo(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Optional<T> getById(int id) {
        return Optional.ofNullable(entityManager.find(clazz, id));
    }

    public List<T> getAll() {
        String jpql = "SELECT e FROM " + clazz.getSimpleName() + " e ORDER BY e.id";
        TypedQuery<T> query = entityManager.createQuery(jpql, clazz);
        List<T> resultList = query.getResultList();
        return resultList != null ? resultList : Collections.emptyList();
    }

    public int save(T entity) {
        entityManager.persist(entity);
        entityManager.flush();
        return entity.getId();
    }

    public boolean delete(int id) {
        T entity = entityManager.find(clazz, id);
        if (entity == null) return false;

        entityManager.remove(entity);
        return true;
    }

    protected int getLastId() {
        String jpql = "SELECT MAX(e.id) FROM " + clazz.getSimpleName() + " e";
        Integer maxId = entityManager.createQuery(jpql, Integer.class).getSingleResult();
        return maxId != null ? maxId : 0;
    }
}
