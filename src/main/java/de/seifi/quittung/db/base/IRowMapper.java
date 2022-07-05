package de.seifi.quittung.db.base;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IRowMapper<M> {

    M mapRow(ResultSet rs) throws SQLException;
}
