import java.io.*;
import java.net.*;

public class server{
	private static String ip = "140.112.30.39";
	private static int port = 12345;
	private static int[] weather, budget, drink;
	private static String[] rest;
	private static double[] location;
	private static int restNum;

	public static void main(String args[]) throws IOException{
		ServerSocket ser = createServer();
		String service;
		readData();
		
		while (true){
			Socket client = ser.accept();
			OutputStream os = client.getOutputStream();
			PrintWriter pw = new PrintWriter(os, true);
			InputStreamReader isr = new InputStreamReader(client.getInputStream(), "UTF-8");
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
		
		int ans = (weather[weatherP]&budget[budgetP]&drink[drinkP]);
		int count = 0;
	
//		System.out.println(ans);
		for (int i = 0; ans > 0 && count < 3 && i < restNum; i++, ans /= 2)
			if (ans % 2 == 1 && count < 3){
				pw.println(rest[i]);
				pw.println(location[i * 2]);
				pw.println(location[i * 2 + 1]);
				count++;
			}
		if (count == 0){
			pw.println("Sorry, we can't find any restaurant for you.");
			pw.println(-1);
			pw.println(-1);
			count++;		
		}
		while (count < 3){
			pw.println("");
			pw.println(-1);
			pw.println(-1);
			count++;
		}


	}

	private static void addRest(BufferedReader br) throws IOException{
		int weatherP, budgetP, drinkP;
		weatherP = Integer.parseInt(br.readLine());
		budgetP = Integer.parseInt(br.readLine());
		drinkP = Integer.parseInt(br.readLine());
		String newRest = br.readLine();
		for (int i = 0; i < restNum; i++)
			if (newRest.equalsIgnoreCase(rest[i])){
				System.out.println("restaurant already exist");
				return;
			}

		System.out.println("add restaurant: " + weatherP + budgetP + drinkP + " " + newRest);
		FileWriter fout = new FileWriter("./data", true);
		PrintWriter pw = new PrintWriter(new BufferedWriter(fout));
		pw.println("" + weatherP + budgetP + drinkP + " " + newRest);
		pw.flush();
		fout.close();

		if (weatherP == 0)
			for (int i = 1; i <= 3; i++)
				weather[i] |= (1<<restNum);
		else
			weather[weatherP] |= (1<<restNum);
		if (budgetP == 0)
			for (int i = 1; i <= 3; i++)
				budget[i] |= (1<<restNum);
		else
			budget[budgetP] |= (1<<restNum);
		if (drinkP == 0)
			drink[1] &= (~(1<<restNum));
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
			if (weatherP == 0)
				for (int i = 1; i <= 3; i++)
					weather[i] |= (1<<count);
			else
				weather[weatherP] |= (1<<count);
			if (budgetP == 0)
				for (int i = 0; i <= 3; i++)
					budget[i] |= (1<<count);
			else
				budget[budgetP] |= (1<<count);
			if (drinkP == 0)
				drink[1] &= (~(1<<count));
			for (int i = 4; i < buf.length; i++)
				restName[i - 4] = buf[i];
			rest[count] = new String(restName).substring(0, 
					buf.length - 4);
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
		location = new double[70];
		for (int i = 0; i < 65; i++)
			location[i] = -1;
		location[0] = 25.022109;
		location[1] = 121.541279;
		location[2] = 25.022588;
		location[3] = 121.542912;
		location[4] = 25.0232094;
		location[5] = 121.5426625;
		location[6] = 25.0239313;
		location[7] = 121.5415714;

		for (int i = 0; i < 4; i++){
			weather[i] = 0;
			budget[i] = 0;
		}
		int v = (~(1<<31));
		weather[0] = v; budget[0] = v;
		drink[0] = v; drink[1] = v;
		System.setProperty("file.encoding", "UTF-8");
	}
}
