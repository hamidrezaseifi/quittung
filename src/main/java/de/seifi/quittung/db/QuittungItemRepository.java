package de.seifi.quittung.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.seifi.quittung.exception.DataSqlException;
import de.seifi.quittung.db.base.ISqliteRepository;
import de.seifi.quittung.db.base.SqliteRepositoryBase;
import de.seifi.quittung.db.rowmapper.QuittungItemRowMapper;
import de.seifi.quittung.models.QuittungItemModel;

public class QuittungItemRepository extends SqliteRepositoryBase implements ISqliteRepository<QuittungItemModel, Integer> {

	private final QuittungItemRowMapper rowMapper = new QuittungItemRowMapper();
	
	private static final String quittungItemDeleteByQuittungIdSql = "Delete from quittung_item  where quittung_id = ?";


	private static final String quittungItemGetByQuittungIdSql = "select id, quittung_id, produkt, artikel_nummer, menge , preis from quittung_item where quittung_id = ?";



	private static final String quittungItemDeleteByIdSql = "Delete from quittung_item  where id = ?";


	private static final String createSql = "CREATE TABLE quittung_item " +
							   "(id INTEGER PRIMARY KEY ASC, quittung_id INT, produkt VARCHAR(255), artikel_nummer VARCHAR(255),  menge INT, preis FLOAT)";


	private static final String quittungItemGetByIdSql = "select id, quittung_id, produkt, artikel_nummer, menge , preis from quittung_item where id = ?";
	
	private static final String quittungItemInsertSql = "Insert into quittung_item " +
			"(quittung_id, produkt, artikel_nummer, menge , preis) values(?, ?, ?, ?, ?)";


	@Override
	public Optional<QuittungItemModel> getById(Integer id) throws DataSqlException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<QuittungItemModel> getAll() throws DataSqlException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<QuittungItemModel> create(QuittungItemModel model) throws DataSqlException {
		Connection conn = getConnection();
		
		try {
			PreparedStatement pStatement = conn.prepareStatement(quittungItemInsertSql);
			pStatement.setInt(1, model.getQuittungId());
			pStatement.setString(2, model.getProdukt());
			pStatement.setString(3, model.getArtikelNummer());
			pStatement.setInt(4, model.getMenge());
			pStatement.setFloat(5, model.getPreis());
			pStatement.executeUpdate();
		} catch (SQLException ex) {
			throw new DataSqlException(String.format("Fehler beim Speichern von Quittung Item: %s", ex.getMessage()));
		}
		
		return Optional.empty();
	}

	@Override
	public Optional<QuittungItemModel> update(QuittungItemModel model) throws DataSqlException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(QuittungItemModel model) throws DataSqlException {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean deleteByQuittungId(Integer quittungId) throws DataSqlException {
		Connection conn = getConnection();
		
		try {
			PreparedStatement pStatement = conn.prepareStatement(quittungItemDeleteByQuittungIdSql);
			pStatement.setInt(1, quittungId);
			pStatement.executeUpdate();
		} catch (SQLException ex) {
			throw new DataSqlException(String.format("Fehler beim löschen von Quittung Items: %s", ex.getMessage()));
		}
		
		
		return true;
	}

	@Override
	public String getTableName() {
		
		return "quittung_item";
	}



	public List<QuittungItemModel> getByQuittungId(int quittungId) throws DataSqlException {

		List<QuittungItemModel> list = new ArrayList<QuittungItemModel>();
		
		Connection conn = getConnection();

		try{

			PreparedStatement pStatement = conn.prepareStatement(quittungItemGetByQuittungIdSql);
			pStatement.setInt(1, quittungId);
			ResultSet rs = pStatement.executeQuery();
			
			while(rs.next()){
				
				QuittungItemModel item = rowMapper.mapRow(rs);
				list.add(item);
			}

		} catch (SQLException ex) {
			throw new DataSqlException(String.format("Fehler beim Lesen von Einzeln von Quittung: %s", ex.getMessage()));
		}
		finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		
		return list;

	}

	@Override
	protected String getCreateSql() {
		
		return createSql;
	}

}
