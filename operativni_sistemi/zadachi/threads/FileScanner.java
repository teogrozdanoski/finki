//package os_threads;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileScanner extends Thread {

	private String fileToScan;
	// TODO: Initialize the start value of the counter
	private static Long counter = (long) 0;
	private static Long size = (long) 0;

	public FileScanner(String fileToScan) {
		this.fileToScan = fileToScan;
		// TODO: Increment the counter on every creation of FileScanner object
		counter = counter + 1;
	}

	public static void printInfo(File file) {
		/*
		 * TODO: Print the info for the @argument File file, according to the
		 * requirement of the task
		 */
		if (!file.isDirectory()) {
			System.out.println("file: " + file.getAbsolutePath() + " - " + file.length());
		}

	}
	
	public static Long calculateDirectorySize(File file) {
		if(!file.isDirectory()) {
			size = size + file.length();
		}
		else {
			File[] files = file.listFiles();
			for(File f: files) {
				calculateDirectorySize(f);
			}
		}
		
		return size;
		
	}

	public static Long getCounter() {
		return counter;
	}

	public void run() {

        //TODO Create object File with the absolute path fileToScan.
        File file;
        file = new File(fileToScan);

        //TODO Create a list of all the files that are in the directory file.
        File [] files = null;
        files = file.listFiles();
        
        // Create a list to put all FileScanners inside so they can be join()-ed
        List<FileScanner> fileScanners = new ArrayList<>();


        for (File f : files) {

            /*
            * TODO If the File f is not a directory, print its info using the function printInfo(f)
            * */
        	if(!f.isDirectory() && f.isFile()) {
        		printInfo(f);
        	}

            /*
            * TODO If the File f is a directory, create a thread from type FileScanner and start it.
            * */
        	if(f.isDirectory()) {
        		
        		FileScanner fs = new FileScanner(f.getAbsolutePath());
        		System.out.println("dir: " + file.getAbsolutePath() + " - " + calculateDirectorySize(f));
        		fileScanners.add(fs);
        		fs.start();
        	}

            //TODO: wait for all the FileScanner-s to finish
        	for(FileScanner fiSc: fileScanners) {
        		try {
					fiSc.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
        	}
        }

    }

	public static void main(String[] args) throws InterruptedException {
		String FILE_TO_SCAN = "C:\\Users\\Teo Grozdanoski\\Documents\\FINKI\\Leten_Semestar_2018-2019\\Operativni_Sistemi\\Kod\\threads_test";

		// TODO Construct a FileScanner object with the fileToScan = FILE_TO_SCAN
		FileScanner fileScanner;
		fileScanner = new FileScanner(FILE_TO_SCAN);

		// TODO Start the thread from type FileScanner
		fileScanner.start();

		// TODO wait for the fileScanner to finish
		fileScanner.join();

		// TODO print a message that displays the number of thread that were created
		System.out.println("Global counter: " + FileScanner.getCounter());

	}
}

/*
Да се имплементира класа FileScanner која што ќе се однесува како thread. Во класата FileScanner се чуваат податоци за : - патеката на директориумот што треба да се скенира - статичка променлива counter што ќе брои колку нишки од класата FileScanner ќе се креираат Во класата FileScanner да се имплементираа статички методот што ќе печати информации за некоја датотека од следниот формат:

dir: C:\Users\185026\Desktop\lab1 - reshenija 4096 (dir за директориуми, апсолутна патека и големина)

file: C:\Users\Stefan\Desktop\spisok.pdf 29198 (file за обични фајлови, апсолутна патека и големина)

Дополнително да се преоптовари методот run() од класата Thread, така што ќе печати информации за директориумот за којшто е повикан. Доколку во директориумот има други под директориуми, да се креира нова нишка од тип FileScanner што ќе ги прави истите работи како и претходно за фајловите/директориумите што се наоѓаат во тие директориуми (рекурзивно).

На крај да се испечати вредноста на counter-от, односно колку вкупно нишки биле креирани.
*/
