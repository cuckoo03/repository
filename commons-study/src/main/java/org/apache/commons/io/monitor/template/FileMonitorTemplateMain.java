package org.apache.commons.io.monitor.template;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationListener;

public class FileMonitorTemplateMain {

	public static void main(String[] args) {
		FileMonitorContext context = new FileMonitorContext("c:\\logs", 1000L); 
		IOFileFilter filter = FileFilterUtils.directoryFileFilter();
		FileAlterationListener listener = new FileMonitorListener();
		FileMonitorTemplate monitor = new FileMonitorTemplate(context, filter, listener);
		
		try {
			System.out.println("monitoring start");
			monitor.start();
			
			Thread.sleep(10000);
			
			monitor.stop();
			System.out.println("monitoring end");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}