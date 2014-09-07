package client

import java.nio.file.WatchEvent

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher

class Executor implements Watcher, Runnable {
	public Executor(String hostPort, String znode, String filename, String[] exec) {

	}

	@Override
	public void run() {
	}

	@Override
	public void process(WatchedEvent event) {
	}
	static main(args) {
		if (args.length < 4) {
			System.err.println("USAGE:Executor hostport znode filenames[args]")
			System.exit(2)
		}
		String hostPort = "127.0.0.1"
		String znode = "server1"
		String 
	}
}
