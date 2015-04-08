package com.hadoop.log.hive;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * hdfs로그 파일을 하이브 테이블로 임포트한다.
 * 
 * @author cuckoo03
 *
 */
public class LogImportHiveClient {
	private static String driverName = "org.apache.hadoop.hive.jdbc.HiveDriverjdbc.HiveDriver";
	private static final String url = "jdbc:hive://master:10000/default";
	private static final String user = "";
	private static final String password = "";
	private static final String tableName = "apache-logs";

	public void run(String basePath) {
		try {
			Class.forName(driverName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}

		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			Statement stmt = conn.createStatement();

			String inputDir = basePath + getCurrentDate();
			String filePath = getCurrentDate() + ".txt";
			String str = "load data inpath '" + inputDir + "/" + filePath
					+ "' overwrite into table" + tableName;
			ResultSet rs = stmt.executeQuery(str);
			
			// 시간별 접속자수 조회해서 인서트
			str = "INSERT OVERWRITE TABLE user_conn_count_hour select substr(time, 0, 14), count(*) from "
					+ tableName + " group by substr(time, 0, 14)";
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String getCurrentDate() {
		return null;
	}

	public static void main(String[] args) {
		LogImportHiveClient client = new LogImportHiveClient();
		if (args.length != 1) {
			System.out.println("Usage LogImportHIveClient <input dir>");
			System.exit(1);
		}
		client.run(args[0]);
	}
}
