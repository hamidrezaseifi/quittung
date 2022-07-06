package de.seifi.quittung.db.rowmapper;

import de.seifi.quittung.db.base.IRowMapper;
import de.seifi.quittung.models.QuittungModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QuittungRowMapper implements IRowMapper<QuittungModel> {
    @Override
    public QuittungModel mapRow(ResultSet rs) throws SQLException {
    	
    	int id = rs.getInt("id");
    	int quittungNummer = rs.getInt("quittung_nummer");
		String createDate = rs.getString("create_date");
		String createTime = rs.getString("create_time");
		
		QuittungModel model = new QuittungModel(id, quittungNummer, createDate, createTime);
		
        return model;
    }
}
