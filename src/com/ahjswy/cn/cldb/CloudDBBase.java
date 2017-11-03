package com.ahjswy.cn.cldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ahjswy.cn.app.AccountPreference;
import com.ahjswy.cn.app.SystemState;
import com.ahjswy.cn.utils.MLog;

public class CloudDBBase {
	static {
		try {
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			MLog.d("Driver  已经加载");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static AccountPreference ap = new AccountPreference();
	// jswy cloud database name
	public String DBNAME = SystemState.getAccountSet().getDatabase();
	// cloud url
	String URL = "jdbc:jtds:sqlserver://" + ap.getServerIp() + ":1433/" + DBNAME;
	// cloud databas user name
	// final public String USERNAME = "sa";
	final public String USERNAME = "";// SystemState.getDBUser() == null ? "" :
										// SystemState.getDBUser().userid;
	// cloud databas password
	final public String PASSWROD ="";// SystemState.getDBUser() == null ? "" :
									// SystemState.getDBUser().password;
	Connection conn;

	public ResultSet executeQuery(String url, String userName, String passwrod, String sql) {

		try {
			MLog.d("连接对象");
			conn = DriverManager.getConnection(url, userName, passwrod);
			ResultSet result = conn.createStatement().executeQuery(sql); // 事务中读
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * executeQuery jswy cloud database
	 * 
	 * @param sql
	 * @return
	 */
	public ResultSet executeQuery(String sql) {
		return executeQuery(URL, USERNAME, PASSWROD, sql);
	}

}
