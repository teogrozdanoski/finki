//package os_threads;

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

/*
Извршете го примерот од TwoThreads.java. Потоа, модифицирајте ја програмата така што ќе користите само една класа за нитки, ThreadAB. Во конструкторот на класата ќе се предадат двата стринга кои соодветната инстанца треба да ги отпечати. Нитката не треба да ја наследува класата Thread. Однесувањето на новата програма треба да биде исто како на оригиналната, односно повторно треба да имате две нитки кои ќе го извршуваат посебно методот run(): едната нитка ќе печати A и B, додека другата ќе печати 1 и 2.

Што би се случило доколку едната нитка треба да ја испечати целата азбука, а другата нитка броевите од 1 до 26? Дали можете да го предвидите изгледот на излезот од програмата?
*/
