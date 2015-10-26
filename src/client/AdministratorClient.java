package client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import org.omg.CORBA.ORB;
import org.omg.CORBA.StringHolder;

import GameServerIDL.GameServerInterface;
import GameServerIDL.GameServerInterfaceHelper;

public class AdministratorClient {
	
	//generates the ip when user selects the region
	private String generateIp(int region){
		String ip = "";
		if (region==1){
			ip = "132";
		}else if (region==2){
			ip = "93";
		}else if (region==3){
			ip = "182";
		}
		for (int i = 0; i < 3; i++) {
			ip = ip + "." + new Random().nextInt(255);
		}
		return ip.substring(0, ip.length()-1);
		
	}
	
	private static void showMenu() {
		System.out.println("1. getPlayerStatus");
		System.out.println("2. Suspend");
		System.out.println("3. Exit");
	}
	
	public static void main(String[] args) throws IOException {
		ORB orbNA=ORB.init(args,null);
		ORB orbEU=ORB.init(args,null);
		ORB orbAS=ORB.init(args,null);
		
		BufferedReader br=new BufferedReader(new FileReader("ior.txt"));
		String iorNA=br.readLine();
		String iorEU=br.readLine();
		String iorAS=br.readLine();
		br.close();
		
		org.omg.CORBA.Object objNA= orbNA.string_to_object(iorNA);
		org.omg.CORBA.Object objEU= orbEU.string_to_object(iorEU);
		org.omg.CORBA.Object objAS= orbAS.string_to_object(iorAS);
		
		GameServerInterface serverNA=GameServerInterfaceHelper.narrow(objNA);
		GameServerInterface serverEU=GameServerInterfaceHelper.narrow(objEU);
		GameServerInterface serverAS=GameServerInterfaceHelper.narrow(objAS);
		
		GameServerInterface server=null;
		Scanner keyboard=new Scanner(System.in);
		
		String get="y";
		while(get.equalsIgnoreCase("y")){
			System.out.println("Enter your region\n1-NorthAmerica\n2-Europe\n3-Asia");
			int region=keyboard.nextInt();
			
			if (region==1) {
				server=serverNA;
			}
			else if (region==2) {
				server=serverEU;
			}
			else if (region==3) {
				server=serverAS;
			}
		showMenu();
		//Scanner keyboard=new Scanner(System.in);
		int i=keyboard.nextInt();
		if (i==1) {
			new AdministratorClient().processStatus(server,region);
		}
		else if (i==2) {
			new AdministratorClient().suspendAccount(server,region);
		}
		
		else break;
			
		System.out.println("Do you wish to continue ? y/n");
		get = keyboard.next();
		
		}
	}
	
	

	private void suspendAccount(GameServerInterface server, int region) {
		String replyFromServer;
		Scanner keyboard=new Scanner(System.in);
		/*System.out.println("Username please");
		String unm=keyboard.next();
		while (!(unm.equals("admin"))) {
			System.out.println("wrong username, please reenter");
			unm = keyboard.next();
		}
		System.out.println("Password please");
		String pwd=keyboard.next();
		while (!(pwd.equals("admin"))) {
			System.out.println("wrong password, please reenter");
			pwd = keyboard.next();
		}*/
		String unm="admin";String pwd="admin";
		
		System.out.println("Enter the username to suspend");
		String usernameToSuspend=keyboard.next();
		replyFromServer=server.suspendAccount(unm, pwd, generateIp(region), usernameToSuspend);
		System.out.println(replyFromServer);
	}

	public void processStatus(GameServerInterface server,int region){
		Scanner keyboard=new Scanner(System.in);
		System.out.println("Username please");
		String unm=keyboard.next();
		while (!(unm.equals("admin"))) {
			System.out.println("wrong username, please reenter");
			unm = keyboard.next();
		}
		System.out.println("Password please");
		String pwd=keyboard.next();
		while (!(pwd.equals("admin"))) {
			System.out.println("wrong password, please reenter");
			pwd = keyboard.next();
		}
		
		String replyFromServer;
		replyFromServer=server.getPlayerStatus(generateIp(region));
		System.out.println(replyFromServer);
		
	}
	
}
