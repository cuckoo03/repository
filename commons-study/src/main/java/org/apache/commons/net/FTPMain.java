package org.apache.commons.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;

public class FTPMain {
	public void connect() {
		FTPClient client = new FTPClient();
		client.setControlEncoding("UTF-8");

		FTPClientConfig config = new FTPClientConfig();
		config.setDefaultDateFormatStr("yyyy mm dd");
		config.setRecentDateFormatStr("m d hh:mm");
//		client.configure(config);

		File f = null;
		FileOutputStream fos = null;
		try {
			client.connect("127.0.0.1");
			client.login("admin", "12341234");

			FTPFile[] files = client.listDirectories();

			for (int i = 0; i < files.length; i++) {
				FTPFile file = files[i];
				System.out.println("filename:" + file.getName() + " | size:"
						+ file.getSize());
			}

			client.changeWorkingDirectory("/logs");
			f = new File("c:\\logs", "down.java");
			fos = new FileOutputStream(f);
			boolean isSuccess = client.retrieveFile("java1.java", fos);
			if (isSuccess) {
				System.out.println("download success");
			} else {
				System.out.println("download fail");
			}
			
			fos.close();
			
			client.logout();

			if (client.isConnected()) {
				client.disconnect();
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		FTPMain main = new FTPMain();
		main.connect();
	}
}
