package de.seifi.quittung.db;

import de.seifi.quittung.models.QuittungItemModel;
import de.seifi.quittung.models.QuittungModel;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbConnection {

	private static final String quittungInsertSql = "Insert into quittung " +
													"(quittung_nummer, create_date, create_time) values(?, ?, ?)";

	private static final String quittungItemInsertSql = "Insert into quittung_item " +
														"(quittung_id, bezeichnung , menge , preis) values(?, ?, ?, ?)";

	private static final String quittungCreateSql = "CREATE TABLE quittung " +
						  "(id INTEGER PRIMARY KEY ASC, quittung_nummer INT, create_date VARCHAR(10), create_time VARCHAR(8))";

	private static final String quittungItemCreateSql = "CREATE TABLE quittung_item " +
							   "(id INTEGER PRIMARY KEY ASC, quittung_id INT,  bezeichnung VARCHAR(255),  menge INT, preis FLOAT)";

	private static final String quittungGetMaxNummerSql = "select max(quittung_nummer) as max_num from quittung where create_date = ?";

	private static final String quittungGetMaxIdSql = "select max(id) as max_id from quittung";

	private static final String quittungGetAllSql = "select id, quittung_nummer, create_date, create_time from quittung";

	private static final String quittungGetByIdSql = "select id, quittung_nummer, create_date, create_time from quittung where id = ?";

	private static final String quittungItemGetByIdSql = "select id, quittung_id, bezeichnung , menge , preis from quittung_item where id = ?";

	private static final String quittungItemGetByQuittungIdSql = "select id, quittung_id, bezeichnung , menge , preis from quittung_item where quittung_id = ?";

	public static Connection getConnection() {

		String dbPathStr = getCurrentDbPath();
		
		String jdbcUrl = String.format("jdbc:sqlite:%s", dbPathStr);
		
        Connection conn = null;
        try {
            // db parameters
            String url = jdbcUrl;
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            return conn;
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
        
        return null;
        
        /*finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }*/
    }
	
	public static String getWorkingRootPath() {
		Path currentRelativePath = Paths.get("");
		String path = currentRelativePath.toAbsolutePath().toString();
		
		return path;
	}
	
	public static String getCurrentDbPath() {
		String rootPath = getWorkingRootPath();
		Path dbPath = Path.of(rootPath, "quittung.db");
		String dbPathStr = dbPath.toAbsolutePath().toString();
		dbPathStr = dbPathStr.replace("\\", "/");
		
		return dbPathStr;
	}
	
	public static void initialDbIfNotExists() {
		String dbPathStr = getCurrentDbPath();
		if(new File(dbPathStr).exists()) {
			return;
		}
		
		Connection conn = getConnection();
		
		try {
			Statement stmt = conn.createStatement();

			stmt.executeUpdate(quittungCreateSql);

			stmt.executeUpdate(quittungItemCreateSql);
			
		} catch (SQLException ex) {
            System.out.println(ex.getMessage());
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
      
	}

	public static int getLastQuittungNummer(String date){

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
			System.out.println(ex.getMessage());
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

	public static int getLastQuittungId(Connection conn){

		int lastQuittungNummer = 0;

		try{
			PreparedStatement pStatement = conn.prepareStatement(quittungGetMaxIdSql);
			ResultSet rs = pStatement.executeQuery();
			if(rs.next()){
				lastQuittungNummer = rs.getInt("max_id");
			}

		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}

		return lastQuittungNummer;
	}

	public static QuittungModel createQuittung(QuittungModel savingModel) {

		Connection conn = getConnection();
		int insertedId = 0;
		
		try{

			PreparedStatement pStatement = conn.prepareStatement(quittungInsertSql);
			pStatement.setInt(1, savingModel.getNummer());
			pStatement.setString(2, savingModel.getDate());
			pStatement.setString(3, savingModel.getTime());
			int rs = pStatement.executeUpdate();

			insertedId = getLastQuittungId(conn);

			//quittung_id, bezeichnung , menge , preis
			for(QuittungItemModel item: savingModel.getItems()){
				pStatement = conn.prepareStatement(quittungItemInsertSql);
				pStatement.setInt(1, insertedId);
				pStatement.setString(2, item.getBezeichnung());
				pStatement.setInt(3, item.getMenge());
				pStatement.setFloat(4, item.getPreis());
				rs = pStatement.executeUpdate();
			}

		} catch (SQLException ex) {
			System.out.println(ex.getMessage());

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

		return getQuittungById(insertedId);

	}

	public static QuittungModel getQuittungById(int id){

		QuittungModel model = null;
		
		Connection conn = getConnection();

		try{

			PreparedStatement pStatement = conn.prepareStatement(quittungGetByIdSql);
			pStatement.setInt(1, id);
			ResultSet rs = pStatement.executeQuery();
			
			if(rs.next()){
				//id, quittung_nummer, create_date, create_time
				int quittungNummer = rs.getInt("quittung_nummer");
				String createDate = rs.getString("create_date");
				String createTime = rs.getString("create_time");
				
				model = new QuittungModel(id, quittungNummer, createDate, createTime);
				model.setItems(getQuittungItemsByQuittungId(id));
				
			}

		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
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
		
		return model;

	}


	public static List<QuittungItemModel> getQuittungItemsByQuittungId(int quittungId){

		List<QuittungItemModel> list = new ArrayList<QuittungItemModel>();
		
		Connection conn = getConnection();

		try{

			PreparedStatement pStatement = conn.prepareStatement(quittungItemGetByQuittungIdSql);
			pStatement.setInt(1, quittungId);
			ResultSet rs = pStatement.executeQuery();
			
			while(rs.next()){
				//id, quittung_id, bezeichnung , menge , preis
				int id = rs.getInt("id");
				String bezeichnung = rs.getString("bezeichnung");
				int menge = rs.getInt("menge");
				float preis = rs.getFloat("preis");
				
				QuittungItemModel item = new QuittungItemModel(id, bezeichnung, menge, preis);
				list.add(item);
			}

		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
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


	public static void test(){

		int id = -1;
		int quittungNummer = -1;
		String createDate = "--";
		String createTime = "--";

		Connection conn = getConnection();

		try{
			Statement stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery(quittungGetAllSql);
			while(rs.next()){
				//id, quittung_nummer, create_date, create_time
				id = rs.getInt("id");
				quittungNummer = rs.getInt("quittung_nummer");
				createDate = rs.getString("create_date");
				createTime = rs.getString("create_time");

			}

		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
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

	}

}
