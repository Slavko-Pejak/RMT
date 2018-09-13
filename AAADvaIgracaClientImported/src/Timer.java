
public class Timer implements Runnable {
	boolean end = false;
	
	@Override
	public void run() {
		double startTime = System.currentTimeMillis();
		while(!end) {
			double currentTime = System.currentTimeMillis();
			if(currentTime - startTime >= 90000) {
				end = true;
			}
		}
	}
}
