//package lab123_practice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class HW01_3 {

	public static void main(String[] args) throws IOException {

		String absolutePath = "C:\\Users\\Teo Grozdanoski\\Documents\\FINKI\\Leten_Semestar_2018-2019\\Operativni_Sistemi\\Kod\\io_test\\";
		String stringSource = absolutePath + "izvor.txt";
		String stringDestination = absolutePath + "destinacija.txt";

		File src = null;
		File dst = null;

		BufferedReader reader = null;
		BufferedWriter writer = null;

		StringBuilder sb = null;

		try {

			src = new File(stringSource);
			dst = new File(stringDestination);

			reader = new BufferedReader(new InputStreamReader(new FileInputStream(src)));
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dst)));

			sb = new StringBuilder();
			String line = "";

			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			writer.write(sb.reverse().toString());

		} catch (FileNotFoundException e) {

			e.printStackTrace();

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
