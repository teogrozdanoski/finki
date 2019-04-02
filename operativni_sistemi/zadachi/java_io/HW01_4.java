//package Lab_2019;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class HW01_4 {

	public static void main(String[] args) throws IOException {
		
		String absPath = "C:/Users/Teo Grozdanoski/Documents/myFolder/";
		String source = absPath + "rezultati.csv";
		String destination = absPath + "rezultati.tsv";
		
		File fsrc = null;
		File fdst = null;
		
		BufferedReader reader = null;
		BufferedWriter writer = null;
		
		String line;
		String outputString;
		String[] spl;
		StringBuilder sb = new StringBuilder();
		
		float krs = 0;
		float hrs = 0;
		float aok = 0;
		int count = 0;
		
		try {
			
			fsrc = new File(source);
			fdst = new File(destination);
			
			if(!fsrc.exists()) {
				System.out.println("Izvornata datoteka ne postoi");
				return;
			}
			if(!fdst.exists()) {
				System.out.println("Destinaciskata datoteka ne postoi");
				return;
			}
			
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(fsrc)));
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fdst)));
			
			line = reader.readLine();
			spl = line.split("\\W");
			sb.append(spl[0] + "\t" + spl[1] + "\t" + spl[2] + "\t" + spl[3] + "\n");
			outputString = sb.toString();
			sb = new StringBuilder();
			writer.write(outputString);
			
			while((line = reader.readLine()) != null) {
				spl = line.split("\\W");
				sb.append(spl[0] + "\t" + spl[1] + "\t" + spl[2] + "\t" + spl[3] + "\t" + (Integer.parseInt(spl[1])+Integer.parseInt(spl[2])+Integer.parseInt(spl[3]))/3.0 + "\n");
				outputString = sb.toString();
				sb = new StringBuilder();
				writer.write(outputString);
				krs += Integer.parseInt(spl[1]);
				hrs += Integer.parseInt(spl[2]);
				aok += Integer.parseInt(spl[3]);
				count++;
			}
			
			sb.append("KRS: " + krs/count + "\tHRS:" + hrs/count + "\tAOK: " + aok/count);
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
Некој систем за е-учење генерира извештаи за оценки на студенти по предмети во CSV (Comma Separated Values) формат. Да се напише Java програма, која на екран ќе го отпечати просекот на секој студент од датотеката rezultati.csv (види слика), како и просечната оценка што ја имаат студентите по секој од предметите наведени во првата редица. Програмата треба да работи за произволен број на редици.

Студент,КРС,НРС,АОК
11234,8,9,8
13456,6,7,9
11111,7,8,8
10123,10,10,10

Бонус: За подобра читливост на извештајот, прочитаната rezultati.csv датотека трансформирајте ја во TSV (Tab Separated Values) формат и снимете ја како rezultati.tsv.
*/
