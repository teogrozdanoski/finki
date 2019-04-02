//package Lab_2019;

import java.io.File;
import java.io.RandomAccessFile;

public class HW01_2 {

	public static void main(String[] args) throws Exception {

		long position = 0;
		
		String absPath = "C:/Users/Teo Grozdanoski/Documents/myFolder/";
		String source = absPath + "izvorb.txt";
		String destination = absPath + "destinacijab.txt";
		
		File fsrc = null;
		File fdst = null;
		
		RandomAccessFile input = null;
		RandomAccessFile output = null;
				
		try {
			
			fsrc = new File(source);
			fdst = new File(destination);
			
			input = new RandomAccessFile(fsrc, "r");
			output = new RandomAccessFile(fdst, "rw");
			
			position = fsrc.length();
			
			while (position > 0) {
			      position -= 1;
			      input.seek(position);
			      byte b = input.readByte();
			      output.write((char) b);
			    }
			
			if(!fsrc.exists()) {
				System.out.println("Izvornata datoteka ne postoi");
				return;
			}
			if(!fdst.exists()) {
				System.out.println("Destinaciskata datoteka ne postoi");
				return;
			}
			
		} catch (Exception e) {
			throw e;
		} finally {
			input.close();
			output.close();
		}
	}

}

/*
Да се напише Java програма која со користење на I/O стримови ќе ја прочита содржината на датотеката izvor.txt, а потоа нејзината содржина ќе ја испише превртена во празната датотека destinacija.txt. Читањето и запишувањето реализирајте го со стримови кои работат бајт по бајт.

Пример:
izvor.txt                   destinacija.txt
Оперативни системи          иметсис инвитарепО

Напомена: Сами креирајте ги овие две датотеки и пополнете ја izvor.txt со произволна содржина.
*/
