import java.io.*;
import java.net.*;

public class client{
	public static void main(String args[]) throws IOException{
		String ip = "140.112.30.34";
		int port = 12345;
		Socket socket = new Socket(ip, port);
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);

//		System.out.println(br.readLine());
		BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
		pw.println(userInput.readLine());
	
	}
}
