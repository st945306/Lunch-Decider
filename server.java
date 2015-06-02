import java.io.*;
import java.net.*;

public class server{
	public static void main(String args[]) throws IOException{
		int port = 12345;
		InetAddress addr = InetAddress.getByName("140.112.30.34");
		ServerSocket ser = new ServerSocket(port, 50, addr);

		Socket client = ser.accept();
		OutputStream os = client.getOutputStream();
		PrintWriter pw = new PrintWriter(os, true);
//		pw.println("Your are connected!");
		
		InputStreamReader isr = new InputStreamReader(client.getInputStream());
		BufferedReader br = new BufferedReader(isr);
		String str = br.readLine();
		System.out.println(str);	
	}
}
