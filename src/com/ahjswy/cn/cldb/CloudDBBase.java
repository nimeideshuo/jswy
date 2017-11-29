package com.ahjswy.cn.cldb;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
	final public String USERNAME = SystemState.getDBUser() == null ? "" : SystemState.getDBUser().userid;
	// cloud databas password
	final public String PASSWROD = SystemState.getDBUser() == null ? "" : SystemState.getDBUser().password;
	public Connection conn;

	public CloudDBBase() {
		try {
			MLog.d("准备连接...");
			conn = getConnection(URL, USERNAME, PASSWROD);
			if (conn == null) {
				MLog.d("连接失败!");
			} else {
				MLog.d("连接成功");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ResultSet executeQuery(String sql) {
		if (conn == null) {
			try {
				conn = getConnection(URL, USERNAME, PASSWROD);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (conn == null) {
				return null;
			}
		}
		try {
			ResultSet result = conn.createStatement().executeQuery(sql); // 事务中读
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Connection getConnection(String url, String userName, String passwrod) throws SQLException {
		MLog.d("连接对象");
		return DriverManager.getConnection(url, userName, passwrod);
	}
}
