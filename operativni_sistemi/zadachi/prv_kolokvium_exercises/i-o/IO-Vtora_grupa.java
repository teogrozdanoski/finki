//package io.exam;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class ExamIO_2 {
	
	private static void copyLargeTxtFiles(String from, String to, long size) {
		File source = new File(from);
		File destination = new File(to);
		
		if(!source.exists()) {
			System.out.println("Does not exist");
//			return; // Does not specify if the method continues or exits.
		}
		if(!destination.exists()) {
			destination.mkdirs();
		}
		
		File[] files = source.listFiles();
		for(File f: files) {
			if(f.isFile()&&f.getName().endsWith(".txt")&&f.length()>size) {
				f.renameTo(new File(destination + "//" + f.getName()));
			}
		}
	}
	
	private static void serializeData(String destination, List<byte[]> data) throws Exception {
		DataOutputStream dos = null;
		File dst = null;
		
		try {
			
			dst = new File(destination);
			dos = new DataOutputStream(new FileOutputStream(dst));

			for(int i=0; i<data.size(); i++) {
				dos.write(data.get(i));
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		} finally {
			if(dos != null) {
				dos.close();
			}
		}
	}
	
	private static byte[] deserializeDataAtPosition(String source, long position, long elementLength) throws Exception {
		File src = null;
		RandomAccessFile raf = null;
		byte[] result = new byte[(int) elementLength];
		
		try {
			
			src = new File(source);
			raf = new RandomAccessFile(src, "r");
			
			if(position < src.length()) {
				raf.seek(position);
			}
			else {
				System.out.println("Position value out of bounds");
				return null;
			}
			
			raf.read(result, (int) position, (int) elementLength);
			
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		} finally {
			if(raf != null) {
				raf.close();
			}
		}
		
		return result;
	}

	public static void main(String[] args) throws Exception {
		
		String directoryPath = "C:\\Users\\Teo Grozdanoski\\Documents\\FINKI\\Leten_Semestar_2018-2019\\Operativni_Sistemi\\Kod\\io_exam\\";
		// define elementLength for serialization and deserialization
		long elementLength = 3;
		
		String copySource = directoryPath + "from";
		String copyDestination = directoryPath + "to";
		copyLargeTxtFiles(copySource, copyDestination, 0);
		
		String serializationDestination = directoryPath + "serDestination.txt";
		List<byte[]> data = new ArrayList<byte[]>();
		
		// Fill data with byte[]
		DataInputStream dis = new DataInputStream(new FileInputStream(new File(directoryPath + "serSource.txt")));
		while(dis.available() > 0) {
			data.add(dis.readNBytes((int) elementLength));
		}
		// End of fill
		
		serializeData(serializationDestination, data);
		
		String deserializationSource = directoryPath + "serialized.dat";
		long position = 0;
		byte[] byteResult;
		byteResult = deserializeDataAtPosition(deserializationSource, position, elementLength);
		
		System.out.println("deserialized data: " + byteResult);
		
		if(dis != null) {
			dis.close();
		}
	}

}

/*
Using Java I/O, implement the following methods in the ExamIO class:

(10 points)void copyLargeTxtFiles(String from, String to, long size)

Copies all .txt files which are larger than size (in bytes) from the from directory into the to directory. If the from directory does not exist, you should write "Does not exist" and if to does not exist you need to create it.
(10 points)void serializeData(String destination, List<byte[]> data)

The list of data in data is written into the destination file, without delimiters (as a continuous stream of bytes). All elements from data have the same length (same number of bytes).
(Bonus 5 points)byte[] deserializeDataAtPosition(String source, long position, long elementLength)

Reads and returns the data at the position position from the source file, which contains a large number of data, all with the same length in bytes, without delimiters. All data elements have the same elementLength length. You should not read the entire file in this method.
*/
