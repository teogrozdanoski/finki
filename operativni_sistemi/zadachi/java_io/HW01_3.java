//package Lab_2019;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class HW01_3 {

	public static void main(String[] args) throws Exception {

		String absPath = "C:/Users/Teo Grozdanoski/Documents/myFolder/";
		String source = absPath + "izvorbuf.txt";
		String destination = absPath + "destinacijabuf.txt";
		
		File fsrc = null;
		File fdst = null;
		
		BufferedReader reader = null;
		BufferedWriter writer = null;
		
		String line = "";
		String outputString = "";
		
		StringBuilder sb = new StringBuilder();
		
		try {		
			
			fsrc = new File(source);
			fdst = new File(destination);
			
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(fsrc)));
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fdst)));
			
			if(!fsrc.exists()) {
				System.out.println("Izvornata datoteka ne postoi");
				return;
			}
			if(!fdst.exists()) {
				System.out.println("Destinaciskata datoteka ne postoi");
				return;
			}
			
			while((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			
			sb.reverse();
			outputString = sb.toString();
			
			writer.write(outputString);
			
		} catch (Exception e) {
			throw e;
		} finally {
			reader.close();
			writer.flush();
			writer.close();
		}
		
	}

}

/*
Да се напише Java програма која со користење на I/O стримови ќе ја прочита содржината на датотеката izvor.txt, а потоа нејзината содржина ќе ја испише превртена во празната датотека destinacija.txt. Читањето и запишувањето реализирајте го со баферирано читање и запишување Пример:

Пример:
izvor.txt                   destinacija.txt
Оперативни системи          иметсис инвитарепО

Напомена: Сами креирајте ги овие две датотеки и пополнете ја izvor.txt со произволна содржина.
*/
