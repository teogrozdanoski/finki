//package io.exam;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ExamIO {

	private static void moveWritableTxtFiles(String from, String to) {
		File src = new File(from);
		File dst = new File(to);

		if (!src.exists()) {
			System.out.println("Does not exist");
		}

		if (!dst.exists()) {
			dst.mkdirs();
		}

		File[] files = src.listFiles();
		for (File f : files) {
			if (f.isFile()&&f.getName().endsWith(".txt") && f.canWrite()) {
				f.renameTo(new File(to + "\\" + f.getName()));
			}
		}
	}

	private static void deserializeData(String source, List<byte[]> data, long elementLength) throws IOException {
		File src = null;
		DataInputStream dis = null;
		
		try {
			
			src = new File(source);
			dis = new DataInputStream(new FileInputStream(src));
			
			while(dis.available() > 0) {
				data.add(dis.readNBytes((int) elementLength));
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(dis != null) {
				dis.close();
			}
		}
	}

	private static void invertLargeFile(String source, String destination) throws Exception {
		File src = null;
		File dst = null;

		RandomAccessFile reader = null;
		RandomAccessFile writer = null;
		
		try {
			src = new File(source);
			dst = new File(destination);

			reader = new RandomAccessFile(src, "r");
			writer = new RandomAccessFile(dst, "rw");
			
			int character;
			
			for(long i=src.length()-1; i>=0; i--) {
				reader.seek(i);
				character = reader.read();
				writer.write(character);
			}

		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (writer != null) {
				writer.close();
			}
		}

	}

	public static void main(String[] args) throws Exception {

		Scanner scanner = new Scanner(System.in);

		String folderPath = "C:\\Users\\Teo Grozdanoski\\Documents\\FINKI\\Leten_Semestar_2018-2019\\Operativni_Sistemi\\Kod\\io_exam\\";

		String fileFrom = folderPath + "from";
		String fileTo = folderPath + "to";
		moveWritableTxtFiles(fileFrom, fileTo);

		String deserializationSource = folderPath + "sourceD.txt";
		long elementLength = scanner.nextLong(); // primer: vnesi 2
		List<byte[]> data = new ArrayList<byte[]>();
		deserializeData(deserializationSource, data, elementLength);

		String invertSource = folderPath + "source.txt";
		String invertDestination = folderPath + "destination.txt";
		invertLargeFile(invertSource, invertDestination);

		scanner.close();

	}

}

/*
Using Java I/O, implement the following methods of the ExamIO class:

(10 points) moveWritableTxtFiles(String from, String to)

Moves all files with the .txt extension which have writing permissions, from the from directory, to the to directory. If the from directory does not exist you should write "Does not exist", and if the to directory does not exist you need to create it.
(10 points) void deserializeData(String source, List<byte[]> data, long elementLength)

Reads the content of the source file, which contains a large amount of data, all in the same length in bytes, without a delimiter. Each of the data elements has a length of elementLength. The data read should be written in the data list, using data.add(readElement).
(Bonus 5 points) void invertLargeFile(String source, String destination)

The content of the source file is written in a reverse order (char by char) into the destination file. The source file content is too large and cannot be fitted into memory.
*/
