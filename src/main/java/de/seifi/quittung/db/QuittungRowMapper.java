package de.seifi.quittung.db;

import de.seifi.quittung.db.base.IRowMapper;
import de.seifi.quittung.models.QuittungModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QuittungRowMapper implements IRowMapper<QuittungModel> {
    @Override
    public QuittungModel mapRow(ResultSet rs) throws SQLException {
        return null;
    }
}
