package de.seifi.quittung.db.rowmapper;

import de.seifi.quittung.db.base.IRowMapper;
import de.seifi.quittung.models.QuittungItemModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QuittungItemRowMapper implements IRowMapper<QuittungItemModel> {
    @Override
    public QuittungItemModel mapRow(ResultSet rs) throws SQLException {
    	
    	int id = rs.getInt("id");
		String produkt = rs.getString("produkt");
		String artikelNummer = rs.getString("artikel_nummer");
		int menge = rs.getInt("menge");
		float preis = rs.getFloat("preis");
		
		QuittungItemModel model = new QuittungItemModel(id, produkt, artikelNummer, menge, preis);
		
        return model;
    }
}
