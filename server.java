import java.io.*;
import java.net.*;

public class server{
	private static String ip = "140.112.30.34";
	private static int port = 12345;
	private static int[] weather, budget, drink;
	private static String[] rest;
	private static int restNum;

	public static void main(String args[]) throws IOException{
		ServerSocket ser = createServer();
		String service;
		readData();
		
		while (true){
			Socket client = ser.accept();
			OutputStream os = client.getOutputStream();
			PrintWriter pw = new PrintWriter(os, true);
			InputStreamReader isr = new InputStreamReader(client.getInputStream());
			BufferedReader br = new BufferedReader(isr);

			service = br.readLine();
			if (service.equals("search"))
				searchRest(br, pw);
			else if (service.equals("addRest"))
				addRest(br);
		}
	}

	private static ServerSocket createServer() throws IOException{
		InetAddress addr = InetAddress.getByName(ip);
		return new ServerSocket(port, 50, addr);
	}

	private static void searchRest(BufferedReader br, PrintWriter pw) throws IOException{
		int weatherP, budgetP, drinkP;
		weatherP = Integer.parseInt(br.readLine());
		budgetP = Integer.parseInt(br.readLine());
		drinkP = Integer.parseInt(br.readLine());
		System.out.println("search: " + weatherP + budgetP + drinkP);			
	}

	private static void addRest(BufferedReader br) throws IOException{
		int weatherP, budgetP, drinkP;
		weatherP = Integer.parseInt(br.readLine());
		budgetP = Integer.parseInt(br.readLine());
		drinkP = Integer.parseInt(br.readLine());
		String newRest = br.readLine();

		System.out.println("add restaurant: " + weatherP + budgetP + drinkP + " " + newRest);
		FileWriter fout = new FileWriter("./data", true);
		PrintWriter pw = new PrintWriter(new BufferedWriter(fout));
		pw.println("" + weatherP + budgetP + drinkP + " " + newRest);
		pw.flush();
		fout.close();

		weather[weatherP] |= (1<<restNum);
		budget[budgetP] |= (1<<restNum);
		drink[drinkP] |= (1<<restNum);
		rest[restNum] = newRest;
		restNum++;
	}
	private static void readData() throws IOException{
		FileReader fin = new FileReader("./data");
		BufferedReader br = new BufferedReader(fin);
		init();
		char[] buf = new char[65536];
		char[] restName = new char[65536];
		int count = 0;
		int weatherP, budgetP, drinkP;
		while (br.ready()){
			buf = (br.readLine()).toCharArray();
			weatherP = buf[0] - '0';
			budgetP = buf[1] - '0';
			drinkP = buf[2] - '0';
			weather[weatherP] |= (1<<count);
			budget[budgetP] |= (1<<count);
			drink[drinkP] |= (1<<count);
			for (int i = 4; i < buf.length; i++)
				restName[i] = buf[i];
			rest[count] = new String(restName);
			count++;
		}
		fin.close();
		restNum = count;
	}
	private static void init(){
		weather = new int[4];
		budget = new int[4];
		drink = new int[4];
		rest = new String[32];
		for (int i = 0; i < 4; i++){
			weather[i] = 0;
			budget[i] = 0;
			drink[i] = 0;
		}
		weather[0] = 1; budget[0] = 1; drink[0] = 1;
	}
}
