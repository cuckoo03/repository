package exam.lock

class GThreadTest implements Runnable{
	private boolean dead
	@Override
	public void run() {
		println "run()"
		try {
			synchronized (this) {
				while (!dead) {
					this.wait()
				}
				println "dead"
			}
		} catch (InterruptedException e) {
			e.printStackTrace()
		}
	}

	static main(args) {
		GThreadTest t = new GThreadTest()
		new Thread(t).start()
		println "sleeped"
		sleep(3000)
		t.dead = true
		synchronized (t) {
			println "notifyAll"
			t.notifyAll()
		}
	}
}
