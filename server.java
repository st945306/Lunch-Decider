import java.io.*;
import java.net.*;

public class server{
	private static String ip = "140.112.30.34";
	private static int port = 12345;
	public static void main(String args[]) throws IOException{
		ServerSocket ser = createServer();
		int[] pointer = new int[3];
		String newRest, service;
		
		while (true){
			Socket client = ser.accept();
			OutputStream os = client.getOutputStream();
			PrintWriter pw = new PrintWriter(os, true);
			InputStreamReader isr = new InputStreamReader(client.getInputStream());
			BufferedReader br = new BufferedReader(isr);

			service = br.readLine();
			if (service.equals("search")){
				for (int i = 0; i < 3; i++)
					pointer[i] = Integer.parseInt(br.readLine());
				searchRest(pointer, pw);
			}
			else if (service.equals("addRest")){
				for (int i = 0; i < 3; i++)
					pointer[i] = Integer.parseInt(br.readLine());
				newRest = br.readLine();
				addRest(pointer, newRest);
			}
		}
	}

	private static ServerSocket createServer() throws IOException{
		InetAddress addr = InetAddress.getByName(ip);
		return new ServerSocket(port, 50, addr);
	}

	private static void searchRest(int[] pointer, PrintWriter pw){
		System.out.println("search restaurant for " + pointer[0] + pointer[1] + pointer[2]);			
	}

	private static void addRest(int[] pointer, String newRest){
		System.out.println("add restaurant " + newRest + " for " + pointer[0] + pointer[1] + pointer[2]);		
	}
}
