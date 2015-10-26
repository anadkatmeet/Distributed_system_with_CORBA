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
import server.GameServer;

public class PlayerClient {
	
	public String username,password,firstName,lastName,ipAdd;
	public int age=23;
	public boolean status=false;
	
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
		System.out.println("1. Create a new account");
		System.out.println("2. Sign in");
		System.out.println("3. Sign out");
		System.out.println("4. Transfer to other server");
		System.out.println("5. Exit");
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
		
		int i=keyboard.nextInt();
		if (i==1) {
			new PlayerClient().createPlayerAccount(server,region);
			
		}else if (i==2) {
			new PlayerClient().signIn(server,region);
			
		}else if (i==3) {
			new PlayerClient().signOut(server,region);
		
		}else if (i==4) {
			new PlayerClient().transerAccount(server, region);
		
		}
		else break;
		
		System.out.println("Do you wish to continue ? y/n");
		get = keyboard.next();
		
		}
	}
	
	private void transerAccount(GameServerInterface server, int region) {
		String replyFromServer;
		System.out.println("Enter ur Username");
		Scanner keyboard=new Scanner(System.in);
		String unm=keyboard.next();
		System.out.println("Enter ur Password");
		String pwd=keyboard.next();
		System.out.println("To which region u wanna move??\n1-NorthAmerica\n2-Europe\n3-Asia");
		int newRegion=keyboard.nextInt();
		replyFromServer=server.transferAccount(unm, pwd, generateIp(region), generateIp(newRegion));
		System.out.println(replyFromServer);
	}
	
	private void signOut(GameServerInterface server,int region) {
		Scanner keyboard=new Scanner(System.in);
		System.out.println("enter ur username");
		String userName = keyboard.next();
		
		String replyFromServer;
		replyFromServer=server.processSignOut(userName, generateIp(region));
		System.out.println(replyFromServer);
		
	}
	private void signIn(GameServerInterface server,int region) {
		Scanner keyboard=new Scanner(System.in);
		System.out.println("enter ur username");
		String userName = keyboard.next();
		System.out.println("enter ur password");
		String password = keyboard.next();
		String ip=generateIp(region);
		String replyFromServer;
		replyFromServer=server.processSignIn(userName, password, generateIp(region));
		System.out.println(replyFromServer);
		
	}
	private void createPlayerAccount(GameServerInterface server,int region){
		Scanner keyboard=new Scanner(System.in);
		System.out.println("enter ur username between 6 to 15 characters");
		String unm = keyboard.next();
		while (!(unm.length() >= 6 && unm.length() <= 15)) {
			System.out
					.println("Username should be between 6 to 15 characters");
			unm = keyboard.next();
		}
		this.username = unm;
		
		System.out.println("Enter password at least 6 characters long");
		String pwd = keyboard.next();
		while (!(pwd.length() >= 6)) {
			System.out.println("Password should be at least 6 characters long");
			pwd = keyboard.next();
		}
		this.password = pwd;
		System.out.println("Enter ur age");
		this.age=keyboard.nextInt();
		
		System.out.println("Enter firstname");
		this.firstName=keyboard.next();
		System.out.println("Enter lastname");
		this.lastName=keyboard.next();
		
		String replyFromServer;
		
		replyFromServer=server.createPlayerAccount(firstName, lastName, age, username, password, generateIp(region));
		System.out.println(replyFromServer);
	}

}
