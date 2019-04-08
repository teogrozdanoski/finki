package exam.io;

import java.io.File;
import java.io.RandomAccessFile;

public class java_io_june {

	private static void manage(String in, String out) throws Exception {
		
		File src = null;
		File dst = null;
		File writeTo = null;
		RandomAccessFile reader = null;
		RandomAccessFile writer = null;
		
		try {
			src = new File(in);
			dst = new File(out);
			if(!src.exists()) {
				System.out.println("ne postoi");
			}
			if(dst.exists()) {
//				System.out.println("brisam");
				dst.delete();
//				System.out.println("kreiram");
				dst.mkdirs();
			}
			writeTo = new File(src.getParent() + "\\resources\\writable-content.txt");
//			System.out.println(writeTo.getAbsolutePath());
			writer = new RandomAccessFile(writeTo, "rw");
			File[] files = src.listFiles();
			for(File f: files) {
				if(f.isFile() && f.getName().endsWith(".dat")) {
					if(f.isHidden()) {
						System.out.println("zbunet sum " + f.getAbsolutePath());
						f.delete();
					}
					else {
						if(f.canWrite()) {
							System.out.println("pomesutvam  " + f.getAbsolutePath());
							f.renameTo(new File(out + "\\" + f.getName()));
						}
						else if(!f.canWrite()){
							reader = new RandomAccessFile(f, "r");
							System.out.println("dopisuvam " + f.getAbsolutePath());
							int c;
							while((c = reader.read()) != -1) {
								writer.seek(writeTo.length());
								writer.write(c);
							}
							writer.writeChars("\n");
						}
					}					
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		} finally {
			if(reader != null) {
				reader.close();
			}
			if(writer != null) {
				writer.close();
			}
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		
		String absPath = "C:\\Users\\Teo Grozdanoski\\Documents\\FINKI\\Leten_Semestar_2018-2019\\Operativni_Sistemi\\Kod\\io_test\\";
		String source = absPath + "in";
		String destintaion = absPath + "out";
		
		manage(source, destintaion);
		
	}
	
}

/*
Потребно е да ја имплементирате функцијата manage(String in, String out) која врши организација на текстуалните датотеки (*.dat) од именикот in според нивните привилегии на следниот начин:

доколку датотеката има привилегии за запишување тогаш таа треба да се премести во out именикот. При преместувањето, во конзола испечатете pomestuvam и апсолутното име на датотеката која се копира.

доколку датотеката нема привилегии за запишување тогаш нејзината содржина додадете ја на крај од датотеката writable-content.txt во resources именикот. При додавањето, во конзола испечатете dopisuvam и апсолутното име на датотеката. Сметајте дека овие датотеки може да бидат многу поголеми од физичката меморија на компјутерот.

доколку датотеката е скриена (hidden), тогаш избришете ја од in именикот, и во конзола испечатете zbunet sum и апсолутното име на датотеката.

Доколку in именикот не постои, испечатете на екран ne postoi.

Доколку out именикот веќе постои, избришете ја неговата содржина. Претпоставете дека во out именикот има само датотеки.
*/
