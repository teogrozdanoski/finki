/*

package Lab_2019;

import java.net.*;
import java.io.*;

public class TCPServer {

	private ServerSocket server;
	
	public TCPServer() throws IOException {
		this.server = new ServerSocket(9876);
	}
	
	private void listen() throws IOException {
		double outputD;
		long outputL;
		boolean outputB;
		String outputS;
		Socket client = this.server.accept();
		String clientAddress = client.getInetAddress().getHostAddress();
		System.out.println("\r\nNew connection from " + clientAddress);
		
		DataInputStream dis = new DataInputStream(client.getInputStream());

		long count = 0;
		
		while(true) {
			if(count == 0) {
				outputD = dis.readDouble();
				System.out.println("\r\nNew message from " + clientAddress + ": " + outputD);
				count++;
			}
			if(count == 1) {
				outputL = dis.readLong();
				System.out.println("\r\nNew message from " + clientAddress + ": " + outputL);
				count++;
			}
			if(count == 2) {
				outputB = dis.readBoolean();
				System.out.println("\r\nNew message from " + clientAddress + ": " + outputB);
				count++;
			}
			if(count == 3) {
				outputS = dis.readUTF();
				System.out.println("\r\nNew message from " + clientAddress + ": " + outputS);
				count=0;
			}
		}
	}
	
	public static void main(String[] args) throws IOException {

		TCPServer app = new TCPServer();
		System.out.println("\r\nRunning server: " + " Host= " + app.server.getInetAddress() + " Port= " + app.server.getLocalPort());
		
		app.listen();
	}

}

package Lab_2019;

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class TCPClient {
	
	private Socket socket;
	private Scanner scanner;
	
	private TCPClient(InetAddress serverAddress, int serverPort) throws IOException {
		this.socket = new Socket(serverAddress, serverPort);
		this.scanner = new Scanner(System.in);
	}
	
	private void start() throws IOException {
		double inputD;
		long inputL;
		boolean inputB;
		String inputS;
		
		DataOutputStream dosOut = new DataOutputStream(this.socket.getOutputStream());
		
		long count = 0;
		
		while(true) {
			if(count == 0) {
				inputD = Double.parseDouble(scanner.nextLine());
				dosOut.writeDouble(inputD);
				dosOut.flush();
				count++;
			}
			if(count == 1) {
				inputL = Long.parseLong(scanner.nextLine());
				dosOut.writeLong(inputL);
				dosOut.flush();
				count++;
			}
			if(count == 2) {
				inputB = Boolean.parseBoolean(scanner.nextLine());
				dosOut.writeBoolean(inputB);
				dosOut.flush();
				count++;
			}
			if(count == 3) {
				inputS = scanner.nextLine();
				dosOut.writeUTF(inputS);
				dosOut.flush();
				count=0;
			}
		}
	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		
		TCPClient client = new TCPClient(InetAddress.getByName("localhost"), 9876);
		System.out.println("\r\nConnected to Server: " + client.socket.getInetAddress());
		client.start();

	}

}
*/

/*
Потребно е да се симулира TCP/IP конекција и комуникација меѓу клиент-сервер.

За таа цел, потребно е да креирате две апликации, TCPServer, која ке се извршува на порта 9876 и ќе игра улога на сервер, и TCP Client која ќе се поврзе со серверот и ке му праќа пораки.

Пораките што треба да ги прати TCPClient се следни, и се праќаат во ист редослед како што е дадено:

1.25 (Double) 123584124 (Long) true (boolean) "UTF String" (String)

TCPServer ја очекува истата секвенца на пораки, во ист редослед, и како што ги добива пораките, ги печати на екран.

_Напомена_: Користете соодветни стримови за пишување/читање на примитивни типови*
*/
