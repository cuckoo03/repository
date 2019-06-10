package com.jcraft.jsch

import groovy.transform.TypeChecked;

@TypeChecked
class SFTPClientMain {
	private Session session
	private ChannelSftp channel 

	void connect(String userId, String password, String ip, int port)
	throws JSchException {
		println "connecting"
		def jsch = new JSch()
		session = jsch.getSession(userId, ip, port)
		session.setConfig("StrictHostKeyChecking", "no")
		session.setPassword(password)
		session.connect()
		
		channel = (com.jcraft.jsch.ChannelSftp) session.openChannel("sftp")
		channel.connect()
		println "connect"
	}
	void upload(String uploadPath, String fileName) 
	throws SftpException, FileNotFoundException {
		channel.cd(uploadPath)
		def file = new File(fileName)
		def fis = new FileInputStream(file)
		channel.put(fis, file.getName())
		fis.close()
		println "file upload success. ${file.getAbsolutePath()}" 
	}
	void download(String downloadPath, String fileName) 
	throws SftpException {
		def buffer = new byte[1024]
		channel.cd(downloadPath)
		
		def file = new File(fileName)
		def is = channel.get(file.getName())
		def bis = new BufferedInputStream(is)
		def newFile = new File(fileName)
		if (newFile.exists()) {
			println "local file exist. skip."
			return
		}
		
		def fos = new FileOutputStream(newFile)
		def bos = new BufferedOutputStream(fos)
		def readCount
		while ((readCount = bis.read(buffer)) > 0) {
			bos.write(buffer, 0, readCount)
		}
		bis.close()
		bos.close()
		println "file download success. ${file.getAbsolutePath()}"
	}
	void disconnect() {
		if (session.isConnected()) {
			println "disconnecting"
			channel.disconnect()	
			session.disconnect()
			println "disconnect"
		}
	}
	static main(args) {
		def main = new SFTPClientMain()
		try {
			main.connect("hadoop", "tapaman", "210.122.10.72", 22)
			main.upload("/home/hadoop", "pom.xml")
			main.download("/home/hadoop", "pom.xml")
		} catch(Exception e) {
			println e.printStackTrace()
		} finally {
			main.disconnect()
		}
	}
}