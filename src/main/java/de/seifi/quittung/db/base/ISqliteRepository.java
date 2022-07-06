package de.seifi.quittung.db.base;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import de.seifi.quittung.exception.DataSqlException;

public interface ISqliteRepository<M, T> {
	
	String getTableName();
	
	void createTable(Connection connection) throws DataSqlException;
	
    Optional<M> getById(T id) throws DataSqlException;

    List<M> getAll() throws DataSqlException;

    Optional<M> create(M model) throws DataSqlException;

    Optional<M> update(M model) throws DataSqlException;

    boolean delete(M model) throws DataSqlException;

}
