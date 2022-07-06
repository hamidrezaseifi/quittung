package de.seifi.quittung.db.rowmapper;

import de.seifi.quittung.db.base.IRowMapper;
import de.seifi.quittung.models.ProduktModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProduktRowMapper implements IRowMapper<ProduktModel> {
    @Override
    public ProduktModel mapRow(ResultSet rs) throws SQLException {
    	
    	int id = rs.getInt("id");
    	String produkt = rs.getString("produkt_name");
		float lastPreis = rs.getFloat("last_preis");
		
		ProduktModel model = new ProduktModel(id, produkt, lastPreis);
		
        return model;
    }
}
