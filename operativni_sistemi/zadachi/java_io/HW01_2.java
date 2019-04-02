//

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class HW01_2 {

	public static void main(String[] args) throws IOException {

		String absolutePath = "C:\\Users\\Teo Grozdanoski\\Documents\\FINKI\\Leten_Semestar_2018-2019\\Operativni_Sistemi\\Kod\\io_test\\";

		String sourceString = absolutePath + "izvor.txt";
		String destinationString = absolutePath + "destinacija.txt";

		File src = null;
		File dst = null;

		RandomAccessFile in = null;
		RandomAccessFile out = null;

		try {

			src = new File(sourceString);
			dst = new File(destinationString);

			in = new RandomAccessFile(src, "r");
			out = new RandomAccessFile(dst, "rw");
			
			for(Long i=src.length()-1; i>=0; i--) {
				out.seek(i);
				out.write(in.read());
			}
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		
		} finally {
			
			in.close();
			out.close();
			
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
