package com.ahjswy.cn.cldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ahjswy.cn.app.AccountPreference;
import com.ahjswy.cn.app.SystemState;
import com.ahjswy.cn.utils.MLog;

import android.widget.Button;

public class CloudDBBase {
	static {
		try {
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			MLog.d("Driver  已经加载");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// jswy cloud database name
	public String DBNAME = SystemState.getAccountSet().getDatabase();
	// cloud url
	String URL = "jdbc:jtds:sqlserver://" + new AccountPreference().getServerIp() + ":1433/" + DBNAME;
	// cloud databas user name
	final public String USERNAME = "sa";
	// cloud databas password
	final public String PASSWROD = "055164312312";
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
