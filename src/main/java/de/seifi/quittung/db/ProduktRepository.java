package de.seifi.quittung.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.seifi.quittung.db.base.ISqliteRepository;
import de.seifi.quittung.db.base.SqliteRepositoryBase;
import de.seifi.quittung.db.rowmapper.ProduktRowMapper;
import de.seifi.quittung.exception.DataSqlException;
import de.seifi.quittung.models.ProduktModel;
import de.seifi.quittung.models.QuittungItemModel;
import de.seifi.quittung.models.QuittungModel;

public class ProduktRepository extends SqliteRepositoryBase implements ISqliteRepository<ProduktModel, Integer> {

	private final ProduktRowMapper rowMapper = new ProduktRowMapper();
	
	private static final String createSql = "CREATE TABLE produkt " +
			  "(id INTEGER PRIMARY KEY ASC, produkt_name VARCHAR(10), last_preis FLOAT)";

	private static final String insertSql = "insert into produkt (produkt_name , last_preis) values (?, ?)";

	private static final String updateSql = "update produkt set last_preis=? where id=?";

	private static final String getByIdSql = "select id, produkt_name , last_preis from produkt where id=?";

	private static final String getByProduktSql = "select id, produkt_name , last_preis from produkt where produkt_name=?";

	private static final String getAllSql = "select id, produkt_name , last_preis from produkt order by produkt_name";


	@Override
	public Optional<ProduktModel> getById(Integer id) throws DataSqlException {
		ProduktModel model = null;
		
		Connection conn = getConnection();

		try{

			PreparedStatement pStatement = conn.prepareStatement(getByIdSql);
			pStatement.setInt(1, id);
			ResultSet rs = pStatement.executeQuery();
			
			if(rs.next()){
				model = rowMapper.mapRow(rs);
				
				return Optional.of(model);
			}

		} catch (SQLException ex) {
			throw new DataSqlException(String.format("Fehler beim Lesen von Produkt: %s", ex.getMessage()));
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

	public Optional<ProduktModel> getByProduktName(String produktName) throws DataSqlException {
		ProduktModel model = null;
		
		Connection conn = getConnection();

		try{

			PreparedStatement pStatement = conn.prepareStatement(getByProduktSql);
			pStatement.setString(1, produktName);
			ResultSet rs = pStatement.executeQuery();
			
			if(rs.next()){
				model = rowMapper.mapRow(rs);
				
				return Optional.of(model);
			}

		} catch (SQLException ex) {
			throw new DataSqlException(String.format("Fehler beim Lesen von Produkt: %s", ex.getMessage()));
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
	public List<ProduktModel> getAll() throws DataSqlException {
		Connection conn = getConnection();

		List<ProduktModel> list = new ArrayList<ProduktModel>();
		
		try{
			Statement stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery(getAllSql);
			while(rs.next()){
				ProduktModel model = rowMapper.mapRow(rs);

				list.add(model);
			}

		} catch (SQLException ex) {
			throw new DataSqlException(String.format("Fehler beim Lesen von Produkt: %s", ex.getMessage()));
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
	public Optional<ProduktModel> create(ProduktModel model) throws DataSqlException {
		Connection conn = getConnection();
    	
		int insertedId = -1;
		
		try{

			PreparedStatement pStatement = conn.prepareStatement(insertSql);
			pStatement.setString(1, model.getProdukt());
			pStatement.setFloat(2, model.getLastPreis());
			pStatement.executeUpdate();

		} catch (SQLException ex) {

			
			throw new DataSqlException(String.format("Fehler beim Speichern von Produkt: %s", ex.getMessage()));

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
	public Optional<ProduktModel> update(ProduktModel model) throws DataSqlException {
		Connection conn = getConnection();
    	
		int insertedId = -1;
		
		try{

			PreparedStatement pStatement = conn.prepareStatement(updateSql);
			pStatement.setFloat(1, model.getLastPreis());
			pStatement.setInt(2, model.getId());
			pStatement.executeUpdate();

		} catch (SQLException ex) {

			
			throw new DataSqlException(String.format("Fehler beim Aktuallisieren von Produkt: %s", ex.getMessage()));

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
	
	public Optional<ProduktModel> updateOrCreate(ProduktModel model) throws DataSqlException {
		Optional<ProduktModel> foundOptional = getByProduktName(model.getProdukt());
		if(foundOptional.isPresent()) {
			ProduktModel updateModel = foundOptional.get();
			updateModel.setLastPreis(model.getLastPreis());
			return update(updateModel);
		}
		else {
			return create(model);
		}
    	
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

	@Override
	protected String getCreateSql() {
		
		return createSql;
	}

}
