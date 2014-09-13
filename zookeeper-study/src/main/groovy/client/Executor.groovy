package client


import org.apache.zookeeper.WatchedEvent
import org.apache.zookeeper.Watcher
import org.apache.zookeeper.ZooKeeper

class Executor implements Watcher, Runnable, DataMonitorListener {
	private String filename
	private String[] exec
	private ZooKeeper zk
	private DataMonitor dm
	private Process child

	public Executor(String hostPort, String znode, String filename, String[] exec) {
		this.filename = filename
		this.exec = exec
		zk = new ZooKeeper(hostPort, 3000, this)
		dm = new DataMonitor(zk, znode, null, this)
	}

	static class StreamWriter extends Thread {
		private OutputStream os
		private InputStream is

		StreamWriter(InputStream is, OutputStream os) {
			this.is = is
			this.os = os
			start()
		}

		@Override
		public void run() {
			println "StreamWriter run()"
			byte[] b = new byte[80]
			int rc

			try {
				while ((rc = is.read(b)) > 0) {
					os.write(b, 0, rc)
				}
			} catch (IOException e) {
				e.printStackTrace()
			}
		}
	}

	@Override
	public void run() {
		println "Executor run()"
		try {
			synchronized (this) {
				while (!dm.dead) {
					wait()
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace()
		}
	}

	@Override
	public void process(WatchedEvent event) {
		println "Executor process()"
		dm.process(event)
	}

	@Override
	public void exsists(byte[] data) {
		println "Executor exsists()"
		if (data == null) {
			if (child != null) {
				println "Killing process"
				child.destroy()

				try {
					child.waitFor()
				} catch (InterruptedException e) {
					e.printStackTrace()
				}
			}
			child = null
		} else {
			if (child != null) {
				println "Stopping child"
				child.destroy()
				try {
					child.waitFor()
				} catch (InterruptedException e) {
					e.printStackTrace()
				}
			}
			try {
				FileOutputStream fos = new FileOutputStream(filename)
				fos.write(data)
				fos.close()
			} catch (IOException e) {
				e.printStackTrace()
			}
			
			try {
				println "Starting child"
				child = Runtime.getRuntime().exec(exec)
				new StreamWriter(child.getInputStream(), System.out)
				new StreamWriter(child.getErrorStream(), System.err)
			} catch (IOException e) {
				e.printStackTrace()
			}
		}
	}

	@Override
	public void closing(int rc) {
		println "Executor closing()"
		synchronized (this) {
			notifyAll()
		}
	}

	static main(args) {
		/*
		if (args.length < 4) {
			System.err.println("USAGE:Executor hostport znode filenames[args]")
			System.exit(2)
		}
		*/
		String hostPort = "192.168.1.101:2181"
		String znode = "/servers/server2"
		String filename = "c:/home/zookeeper/zk_test.txt"
//		String exec = new String[args.length - 3]
		String[] exec = new String[0]
//		System.arraycopy(args, 3, exec, 0, exec.length())

		try {
			new Executor(hostPort, znode, filename, exec).run()
		} catch (Exception e) {
			e.printStackTrace()
		}
	}
}
