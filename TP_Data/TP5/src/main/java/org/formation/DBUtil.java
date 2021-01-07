package org.formation;

import java.sql.Connection;
import java.sql.SQLException;

import org.h2.jdbcx.JdbcDataSource;

public class DBUtil {

	private static JdbcDataSource dataSource;
	
	static {
		dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:default;DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");
        dataSource.setPassword("sa");
	}
	public static Connection geConnection() throws SQLException { 
		return dataSource.getConnection();
	}
}
