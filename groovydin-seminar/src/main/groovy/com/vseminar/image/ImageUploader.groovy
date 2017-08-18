package com.vseminar.image

import groovy.transform.TypeChecked

import java.util.regex.Pattern

import com.vaadin.server.Page
import com.vaadin.server.VaadinService
import com.vaadin.server.VaadinServlet
import com.vaadin.ui.Notification
import com.vaadin.ui.UI
import com.vaadin.ui.Upload.Receiver

@TypeChecked
class ImageUploader implements Receiver {
	final Pattern pattern = Pattern.compile("([^\\s]+(\\.(?i)(jpg|jpeg|gif|png)))")
	private File file
	private String imgPath
	
	@Override
	OutputStream receiveUpload(String filename, String mimeType) {
		FileOutputStream fos = null
		try {
			imgPath = "img/upload/$filename"
			
			def isAllowExt = pattern.matcher(filename).matches()
			if (!isAllowExt) {
				throw new IOException("allow extension *.jpg|jpeg|gif|png")
			}
			file = getFile(imgPath)
			if (file.exists()) {
				file.delete()
			}
			
			file.getParentFile().mkdir()
			file.createNewFile()
			
			fos = new FileOutputStream(file)
		} catch (IOException e) {
			new Notification("could not open file", e.getMessage(), 
				Notification.TYPE_ERROR_MESSAGE).show(Page.getCurrent())
				
			return null
		}

		return fos
	}
	
	static File getFile(String imgPath) {
		def baseDirectory = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath()

		return new File("$baseDirectory/VAADIN/themes/${UI.getCurrent().getTheme()}/$imgPath")
	}
	
	static String getUrl(String imgPath) {
		def contextPath = VaadinServlet.getCurrent().getServletContext().getContextPath()

		return "$contextPath/VAADIN/themes/${UI.getCurrent().getTheme()}/$imgPath"
	}

	File getSuccessUploadFile() {
		return this.file
	}
	
	String getImgPath() {
		return this.imgPath
	}
}
