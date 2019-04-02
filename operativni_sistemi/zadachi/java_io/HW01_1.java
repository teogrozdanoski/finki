//package Lab_2019;

import java.io.File;
import java.util.Scanner;

public class HW01_1 {

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		String source = scanner.nextLine();
		
		try {
			int totalSize = 0;
			float averageSize = 0;
			int txtFilesCounter = 0;
			File folder = new File(source);
			File[] files = folder.listFiles();
			
			for(File f: files) {
				if(f.isFile() && f.getName().endsWith(".txt")) {
					totalSize += f.length();
					txtFilesCounter++;
				}
			}
			
			averageSize = totalSize/txtFilesCounter;
			
			System.out.println("The average file size is: " + averageSize + " bytes");
		} catch (Exception e) {
			throw e;
		} finally {
			scanner.close();
		}

	}

}

/*
Да се напише Java програма која ќе прикаже колкава е просечната големина на датотеките со екстензија .txt во именик зададен како аргумент на командна линија.

Напомена: Користете ја File класата за пристап до содржината на именикот.
*/
