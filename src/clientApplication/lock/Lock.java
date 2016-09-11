package clientApplication.lock;

public class Lock {
	private boolean isLocked = false;

	public synchronized void lock() throws InterruptedException {
		while (isLocked) {
			wait();
		}
		isLocked = true;
	}

	public synchronized void unlock() {
		isLocked = false;
		notifyAll();
		try {
			wait(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}