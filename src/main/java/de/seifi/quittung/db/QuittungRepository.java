package de.seifi.quittung.db;

import de.seifi.quittung.db.base.ISqliteRepository;
import de.seifi.quittung.db.base.SqliteRepositoryBase;
import de.seifi.quittung.db.rowmapper.QuittungRowMapper;
import de.seifi.quittung.exception.DataSqlException;
import de.seifi.quittung.models.QuittungItemModel;
import de.seifi.quittung.models.QuittungModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuittungRepository extends SqliteRepositoryBase implements ISqliteRepository<QuittungModel, Integer> {
	
	private final QuittungRowMapper rowMapper = new QuittungRowMapper();
	private final QuittungItemRepository ItemRepository = new QuittungItemRepository();
	
	
	private static final String quittungInsertSql = "Insert into quittung " +
			"(quittung_nummer, create_date, create_time) values(?, ?, ?)";

	private static final String quittungDeleteSql = "Delete from quittung where id = ?";

	private static final String quittungCreateSql = "CREATE TABLE quittung " +
			  "(id INTEGER PRIMARY KEY ASC, quittung_nummer INT, create_date VARCHAR(10), create_time VARCHAR(8))";

	private static final String quittungGetMaxNummerSql = "select max(quittung_nummer) as max_num from quittung where create_date = ?";

	private static final String quittungGetMaxIdSql = "select max(id) as max_id from quittung";

	private static final String quittungGetAllSql = "select id, quittung_nummer, create_date, create_time from quittung order by create_date desc, create_time desc";

	private static final String quittungGetByIdSql = "select id, quittung_nummer, create_date, create_time from quittung where id = ?";


	@Override
	public void createTable(Connection connection) throws DataSqlException {
		Statement stmt;
		try {
			
			stmt = connection.createStatement();
			stmt.executeUpdate(quittungCreateSql);
			
		} catch (Exception ex) {
			throw new DataSqlException(String.format("Fehler beim Erstellen von der Tabelle '%s': %s", getTableName(), ex.getMessage()));
		}
		
		
	}
	
    @Override
    public Optional<QuittungModel> getById(Integer id) throws DataSqlException {
    	QuittungModel model = null;
		
		Connection conn = getConnection();

		try{

			PreparedStatement pStatement = conn.prepareStatement(quittungGetByIdSql);
			pStatement.setInt(1, id);
			ResultSet rs = pStatement.executeQuery();
			
			if(rs.next()){
				model = rowMapper.mapRow(rs);

				model.setItems(ItemRepository.getByQuittungId(id));
				
				return Optional.of(model);
			}

		} catch (SQLException ex) {
			throw new DataSqlException(String.format("Fehler beim Lesen von Quittung: %s", ex.getMessage()));
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
		
        return Optional.empty();
    }

    @Override
    public List<QuittungModel> getAll() throws DataSqlException {
		Connection conn = getConnection();

		List<QuittungModel> list = new ArrayList<QuittungModel>();
		
		try{
			Statement stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery(quittungGetAllSql);
			while(rs.next()){
				QuittungModel model = rowMapper.mapRow(rs);

				model.setItems(ItemRepository.getByQuittungId(model.getId()));

				list.add(model);
			}

		} catch (SQLException ex) {
			throw new DataSqlException(String.format("Fehler beim Lesen von Quittung: %s", ex.getMessage()));
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
    public Optional<QuittungModel> create(QuittungModel model) throws DataSqlException {
    	Connection conn = getConnection();
    	
		int insertedId = -1;
		
		try{

			PreparedStatement pStatement = conn.prepareStatement(quittungInsertSql);
			pStatement.setInt(1, model.getNummer());
			pStatement.setString(2, model.getDate());
			pStatement.setString(3, model.getTime());
			pStatement.executeUpdate();

			insertedId = getLastQuittungId(conn);

			for(QuittungItemModel item: model.getItems()){
				item.setQuittungId(insertedId);
				ItemRepository.create(item);
				
			}

		} catch (SQLException ex) {

			if(insertedId > 0){
				deleteById(insertedId);

			}
			throw new DataSqlException(String.format("Fehler beim Speichern von Quittung: %s", ex.getMessage()));

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

		return getById(insertedId);
    }

    @Override
    public Optional<QuittungModel> update(QuittungModel model) throws DataSqlException {
        return Optional.empty();
    }

    @Override
    public boolean delete(QuittungModel model) throws DataSqlException {
		
		return deleteById(model.getId());
		
    }

    public boolean deleteById(Integer id) throws DataSqlException {
    	Connection conn = getConnection();
    	
		try {
			PreparedStatement pStatement = conn.prepareStatement(quittungDeleteSql);
			pStatement.setInt(1, id);
			pStatement.executeUpdate();
		} catch (SQLException ex) {
			throw new DataSqlException(String.format("Fehler beim löschen von Quittung : %s", ex.getMessage()));
		}

		ItemRepository.deleteByQuittungId(id);
		
		return true;
		
    }

	@Override
	public String getTableName() {
		
		return "quittung";
	}
	
	public int getLastQuittungNummer(String date) throws DataSqlException {

		int lastQuittungNummer = 0;

		Connection conn = getConnection();

		try{
			PreparedStatement pStatement = conn.prepareStatement(quittungGetMaxNummerSql);
			pStatement.setString(1, date);
			ResultSet rs = pStatement.executeQuery();
			if(rs.next()){
				lastQuittungNummer = rs.getInt("max_num");
			}

		} catch (SQLException ex) {
			throw new DataSqlException(String.format("Fehler beim Lesen von den letzten Quittung Nummer: %s", ex.getMessage()));
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

		return lastQuittungNummer;
	}

	public int getLastQuittungId(Connection conn) throws DataSqlException {

		int lastQuittungNummer = 0;

		try{
			PreparedStatement pStatement = conn.prepareStatement(quittungGetMaxIdSql);
			ResultSet rs = pStatement.executeQuery();
			if(rs.next()){
				lastQuittungNummer = rs.getInt("max_id");
			}

		} catch (SQLException ex) {
			throw new DataSqlException(String.format("Fehler beim Lesen von den letzten Quittung ID: %s", ex.getMessage()));
		}

		return lastQuittungNummer;
	}
}
