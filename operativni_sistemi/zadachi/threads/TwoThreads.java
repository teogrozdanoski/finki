//package Lab_2019;

public class TwoThreads {
	
	public static class ThreadAB implements Runnable {

		private String string1;
		private String string2;
		
		public ThreadAB(String param1, String param2) {
			string1 = param1;
			string2 = param2;
		}

		@Override
		public void run() {
			System.out.println(string1);
			System.out.println(string2);
		}
		
	}
	
    public static void main(String[] args) {
    	

    	Thread threadAB = new Thread(new ThreadAB("A", "B"));
    	Thread thread12 = new Thread(new ThreadAB("1", "2"));

    	threadAB.start();
    	thread12.start();
    	
    }

}
