package de.seifi.quittung.db;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import de.seifi.quittung.db.base.ISqliteRepository;
import de.seifi.quittung.db.base.SqliteRepositoryBase;
import de.seifi.quittung.exception.DataSqlException;
import de.seifi.quittung.models.ProduktModel;

public class ProduktRepository extends SqliteRepositoryBase implements ISqliteRepository<ProduktModel, Integer> {

	@Override
	public void createTable(Connection connection) throws DataSqlException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Optional<ProduktModel> getById(Integer id) throws DataSqlException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProduktModel> getAll() throws DataSqlException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<ProduktModel> create(ProduktModel model) throws DataSqlException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<ProduktModel> update(ProduktModel model) throws DataSqlException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(ProduktModel model) throws DataSqlException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getTableName() {
		
		return "produkt";
	}

}
