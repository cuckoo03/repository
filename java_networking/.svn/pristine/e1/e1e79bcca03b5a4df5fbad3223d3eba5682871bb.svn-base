package com;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Exam10_4DaumSearch {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String keyword = URLEncoder.encode("사과", "UTF-8");
			String query = "w=tot&t__nil_searchbox=btn&sug=&q=" + keyword;
			String u = "http://search.daum.net/search?";
			System.out.println(u + query);

			URL url = new URL(u);
			URLConnection conn = url.openConnection();
			HttpURLConnection httpUrlConn = (HttpURLConnection) conn;
			httpUrlConn.setRequestMethod("GET");
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			httpUrlConn.setDefaultUseCaches(false);

			PrintWriter out = new PrintWriter(httpUrlConn.getOutputStream());
			out.println(query);
			out.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					httpUrlConn.getInputStream()));
			PrintWriter pw = new PrintWriter(new FileWriter("Exam10_4.html"));
			String line = null;

			while ((line = in.readLine()) != null) {
				pw.println(line);
			}
			in.close();
			pw.close();
			System.out.println("검색된 결과가 파일에 저장되었습니다");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
