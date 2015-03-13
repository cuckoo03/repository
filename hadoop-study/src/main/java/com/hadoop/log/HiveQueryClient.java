package com.hadoop.log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * https://cwiki.apache.org/confluence/display/Hive/HiveClient
 * @author cuckoo03
 *
 */
public class HiveQueryClient {
	private static String driverName = "org.apache.hadoop.hive.jdbc.HiveDriver";
	Connection conn = null;

	public static void main(String[] args) throws SQLException {
		HiveQueryClient client = new HiveQueryClient();
		client.run();
	}

	public void run() throws SQLException {
		try {
			Class.forName(driverName);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			System.exit(1);
		}

		Connection conn = null;
		try {
			conn = DriverManager.getConnection(
					"jdbc:hive://192.168.1.101:10000/default", "", "");
			Statement stmt = conn.createStatement();
			String query1 = "drop table test_table";
			stmt.executeQuery(query1);

			ResultSet rs = stmt
					.executeQuery("create table test_table (key int, value String)");

			query1 = "show tables 'test_table'";
			System.out.println("running query:" + query1);
			rs = stmt.executeQuery(query1);
			if (rs.next()) {
				System.out.println(rs.getString(1));
			}
			query1 = "describe test_table";
			System.out.println("running query:" + query1);
			rs = stmt.executeQuery(query1);
			while (rs.next()) {
				System.out.println(rs.getString(1) + "\t" + rs.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}
}