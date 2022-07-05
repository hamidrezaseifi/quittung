package de.seifi.quittung.db;

import de.seifi.quittung.db.base.ISqliteRepository;
import de.seifi.quittung.db.base.SqliteRepositoryBase;
import de.seifi.quittung.models.QuittungModel;

import java.util.List;
import java.util.Optional;

public class QuittungRepository extends SqliteRepositoryBase implements ISqliteRepository<QuittungModel, Integer> {
    @Override
    public Optional<QuittungModel> getById(Integer id) {
        return Optional.empty();
    }

    @Override
    public List<QuittungModel> getAll() {
        return null;
    }

    @Override
    public Optional<QuittungModel> create(QuittungModel model) {
        return Optional.empty();
    }

    @Override
    public Optional<QuittungModel> update(QuittungModel model) {
        return Optional.empty();
    }

    @Override
    public boolean delete(QuittungModel model) {
        return false;
    }
}
