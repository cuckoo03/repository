package client;

public interface DataMonitorListener {
	void exsists(byte[] data);

	void closing(int rc);
}
