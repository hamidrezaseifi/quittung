package de.seifi.quittung.db.base;

import java.util.List;
import java.util.Optional;

public interface ISqliteRepository<M, T> {

    Optional<M> getById(T id);

    List<M> getAll();

    Optional<M> create(M model);

    Optional<M> update(M model);

    boolean delete(M model);

}
