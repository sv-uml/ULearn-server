package com.ulearn.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class AppDao {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	/**
	 * Gets id, name and password for given user email
	 * @param email - user's email
	 * @return id, name and password
	 */
	public Map<String, Object> getUserByEmail(String email) {
		Map<String, Object> ret = new HashMap<>();
		Map<String, Object> params = new HashMap<>();
		params.put("email", email);
		List<Map<String, Object>> lst = namedParameterJdbcTemplate.queryForList("SELECT id, name, password FROM users where email = :email", params);
		if (lst.isEmpty()) {
			return ret;
		} else {
			return lst.get(0);
		}
	}
	
	/**
	 * Checks if given user email is registered
	 * @param email - user email
	 * @return true if registered, false otherwise
	 */
	public boolean userExists(String email) {
		Map<String, Object> params = new HashMap<>();
		params.put("email", email);
		List<Map<String, Object>> lst = namedParameterJdbcTemplate.queryForList("SELECT id FROM users where email = :email", params);
		return (!lst.isEmpty());
	}
	
	/**
	 * Registers given name, email and password
	 * @param name - name of user
	 * @param email - email of user
	 * @param password - encrypted password of user
	 * @return id of user
	 */
	public int register(String name, String email, String password) {
		KeyHolder holder = new GeneratedKeyHolder();
		Map<String, Object> params = new HashMap<>();
		params.put("name", name);
		params.put("email", email);
		params.put("password", password);
		
		namedParameterJdbcTemplate.getJdbcOperations().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement pst = con.prepareStatement("INSERT INTO users (name, email, password) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
				pst.setString(1, name);
				pst.setString(2, email);
				pst.setString(3, password);
				return pst;
			}
		}, holder);
		return Integer.parseInt(String.valueOf(holder.getKeys().get("id")));
	}
	
}
