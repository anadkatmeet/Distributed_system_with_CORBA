package server;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.omg.CORBA.StringHolder;

import client.PlayerClient;
import GameServerIDL.GameServerInterfacePOA;

public class GameServerImpl extends GameServerInterfacePOA {
	Hashtable<String, ArrayList<PlayerClient>> hashtable=new Hashtable<String,ArrayList<PlayerClient>>();
	UDPServer udp;
	final int portNA=1411;
	final int portEU=1412;
	final int portAS=1413;
	
	public GameServerImpl(){}//for start class
	
	public GameServerImpl(int port){
		udp=new UDPServer(port, this);
		udp.start();
	}
	//finds region when ipadd is passed
	private String findRegion(String ip) {
		String pre = ip.substring(0, ip.indexOf("."));
	  if (pre.equals("132")){
	    return "NA";
	  }else if (pre.equals("93")){
		return "EU";
	  }else if (pre.equals("182")){
	    return "AS";
	  }
	return "";
	}
	
	//used by getplayerstatus to find users locally
	public String calculateStatus() {
		int online = 0, offline = 0;
		Enumeration<ArrayList<PlayerClient>> e = hashtable.elements();
		
		while (e.hasMoreElements()) {
			ArrayList<PlayerClient> ar = (ArrayList<PlayerClient>) e.nextElement();
			for (int i = 0; i < ar.size(); i++) {
				if (ar.get(i).status) {
					online++;
				} else {
					offline++;
				}
			}
		}
		
		return online + " online, " + offline + " offline";
	}
	
	
	@Override
	public String createPlayerAccount(String firstName, String lastName, int age,
			String userName, String password, String ipAdd) {
		ArrayList<PlayerClient> list = new ArrayList<PlayerClient>();
		PlayerClient pc=new PlayerClient();
		int len=0;
		boolean isCorrect = true;
		
		if (hashtable.containsKey(userName.substring(0, 1))) {	//if that key exists
			len = hashtable.get(userName.substring(0, 1)).size();
			if (len > 0) {										//if the size of list under that key is >0
				for (int i = 0; i < len; i++) {
					if (userName.equalsIgnoreCase(hashtable.get(userName.subSequence(0, 1)).get(i).username)) {
						isCorrect = false;
						// it means already registered						
						break;
					}
				}
			}
		}
		String filename;
		//if the isCorrect flag is set,then insert into hashtable
		if (isCorrect) {
			pc.username=userName;
			pc.password=password;
			pc.age=age;
			pc.firstName=firstName;
			pc.lastName=lastName;
			pc.status=false;
			pc.ipAdd=ipAdd;
			
			synchronized (hashtable) {
			
				//server's log
				filename = "log/server/"+findRegion(ipAdd) + "-server.log";
				doLog(filename,"Player Creation Request with username: "+userName);
				
			if (hashtable.containsKey(userName.substring(0, 1))) {
				len = hashtable.get(userName.substring(0, 1)).size();
				for (int j = 0; j < len; j++) {
					list.add(hashtable.get(userName.substring(0, 1)).get(j));
				}
			}
			
			list.add(pc);
			hashtable.put(userName.substring(0, 1), list);
			//ans.value="successful";
			
			//player's log
			filename = "log/client/"+userName + findRegion(ipAdd) + ".log";
			doLog(filename,"Account Created ");

			//server's log
			filename = "log/server/"+findRegion(ipAdd) + "-server.log";
			doLog(filename,"Status: Account Created with username: "+userName);
			
			return "successful";
			}//synchronized block ends
		}else{
			//ans.value="creation failed";
			//server's log
			filename = "log/server/"+findRegion(ipAdd) + "-server.log";
			doLog(filename,"Status: player creation failed");
			return "creation failed";
		}
		
		
	}

	@Override
	public String processSignIn(String userName, String password, String ipAdd) {
		
		//server's log
		String filename = "log/server/"+findRegion(ipAdd) + "-server.log";
		doLog(filename,"SignIn request from username:"+userName);
		
		int result=0;
		PlayerClient pc=new PlayerClient();
		String location="NA";
		if (hashtable.containsKey(userName.substring(0, 1))) {	//if that key exists

			for (int i = 0; i < hashtable.get(userName.substring(0, 1)).size(); i++) {
				PlayerClient tempClient = hashtable.get(userName.substring(0, 1)).get(i);//it will fetch each entry into a temporary client object

				if (userName.equalsIgnoreCase(tempClient.username)) {	//verify username
					if (password.equalsIgnoreCase(tempClient.password)) {	//verify password
						pc = tempClient;
						synchronized (hashtable) {
							if (!tempClient.status) {	//finally verify sign in status
								hashtable.get(userName.substring(0, 1)).get(i).status = true;
								// ans.value "u got signed in!!";
								result = 1;
								break;
							} else {
								// ans.value "already signed in!!";
								result = 2;
								break;
							}
						}//synchronization ends
						
					} else {
						// ans.value "invalid password";
						result = 3;
					}
				} else {
					// ans.value "invalid username";
					result = 4;
				}
			}

		}
		//setting logs
		if (result == 0) {
			//server's log
			filename = "log/server/"+findRegion(ipAdd) + "-server.log";
			doLog(filename,"Staus:User is invalid");
			return "User is invalid";
			//ans.value= "User is invalid";
		
		} else if (result == 1) {
			
			//server's log
			filename = "log/server/"+findRegion(ipAdd) + "-server.log";
			doLog(filename,"Staus:Successfully signed in");
			//player's log
			filename = "log/client/"+userName + findRegion(ipAdd) + ".log";
			doLog(filename,"Successfully signed in");
			return "Signed In successfully";
		
		} else if (result == 2) {
			
			//server's log
			filename = "log/server/"+findRegion(ipAdd) + "-server.log";
			doLog(filename,"Staus:already signed in");
			//player's log
			filename = "log/client/"+userName + findRegion(ipAdd) + ".log";
			doLog(filename,"already signed in");
			return "Already Signed In";
		
		} else if (result == 3) {
			//server's log
			filename = "log/server/"+findRegion(ipAdd) + "-server.log";
			doLog(filename,"Staus:invalid password");
			return "Invalid Password";
			
		} else if (result == 4) {
			//server's log
			filename = "log/server/"+findRegion(ipAdd) + "-server.log";
			doLog(filename,"Staus:invalid username");
			return "Invalid Username";
		}
		return "";
		
	}

	@Override
	public String processSignOut(String userName, String ipAdd) {
		int result=0;
		PlayerClient pc = new PlayerClient();
		
		//server's log
		String filename = "log/server/"+findRegion(ipAdd) + "-server.log";
		doLog(filename,"Signout request from username:"+userName);
		
		if (hashtable.containsKey(userName.substring(0, 1))) {	//if that key exists in hashtable

			for (int i = 0; i < hashtable.get(userName.substring(0, 1)).size(); i++) {	
				PlayerClient tempClient = hashtable.get(userName.substring(0, 1)).get(i);

				if (userName.equalsIgnoreCase(tempClient.username)) {	//verify usename
					pc = tempClient;
					synchronized (hashtable) {	//syn at the time of checking whether the status is already true or not
						
						if (tempClient.status) {
							hashtable.get(userName.substring(0, 1)).get(i).status = false;
							// ans.value "u got signed out!!";
							result = 1;
							break;
							
						} else {
							// ans.value "u need to sign in first!!";
							result = 2;
							break;
						
						}
					}//synchronization ends
				} else {
					// ans.value "invalid username";
					result = 3;
				}
			}

		}
		//setting logs
		if (result == 0) {	
			//server's log
			filename = "log/server/"+findRegion(ipAdd) + "-server.log";
			doLog(filename,"Staus:user is invalid");
			return "User is invalid";
		
		} else if (result == 1) {
			
			//server's log
			filename = "log/server/"+findRegion(ipAdd) + "-server.log";
			doLog(filename,"Staus:Signed out successfully");
			//player's log
			filename = "log/client/"+userName + findRegion(ipAdd) + ".log";
			doLog(filename,"Successfully signed out");
			return "Signed Out successfully";
			
		} else if (result==2) {
			
			//server's log
			filename = "log/server/"+findRegion(ipAdd) + "-server.log";
			doLog(filename,"Staus:already signed out");
			//player's log
			filename = "log/client/"+userName + findRegion(ipAdd) + ".log";
			doLog(filename,"already signed out");
			return "Already Signed Out!";
		
		} else if (result == 3) {
			
			//server's log
			filename = "log/server/"+findRegion(ipAdd) + "-server.log";
			doLog(filename,"Staus:invalid username");
			return "Invalid Username";
		
		}
		return"";
	}

	@Override
	public String getPlayerStatus(String ipAdd) {
		String filename;
		
		//server's log
		filename = "log/server/"+findRegion(ipAdd) + "-server.log";
		doLog(filename,"Player status request from:"+findRegion(ipAdd)+"region");
		
		String region=findRegion(ipAdd);
		int port1=0,port2=0;
		String localSatus=calculateStatus();
		String replyfromUDP1="",replyfromUDP2="";
		int serverPort=0;
		
		//it will check the region & it will set the other two region's port nos into port1 & port2 variables
		if (region.equals("NA")) {
			//call eu,as
			port1=portEU;port2=portAS;
			localSatus="NA: "+localSatus;
			replyfromUDP1="EU: ";
			replyfromUDP2="AS: ";
		}
		else if (region.equals("EU")) {
			//call na,as
			port1=portNA;port2=portAS;
			localSatus="EU: "+localSatus;
			replyfromUDP1="NA: ";
			replyfromUDP2="AS: ";
		}
		else if (region.equals("AS")) {
			//call eu,na
			port1=portNA;port2=portEU;
			localSatus="AS: "+localSatus;
			replyfromUDP1="NA: ";
			replyfromUDP2="EU: ";
		}
		
		DatagramSocket aSocket = null;
		//running loop twice for other two servers, i.e. calling other two servers via UDP
		for (int i = 0; i < 2; i++) {
			
			if (i==0) {
				serverPort = port1;
			}
			else if(i==1)
				serverPort = port2;
			
			String replytemp=callUDP(aSocket,serverPort,"call");
			replytemp.trim();
			if (i==0) {
				replyfromUDP1=replyfromUDP1 + replytemp;
			}else if (i==1) {
				replyfromUDP2=replyfromUDP2 + replytemp;
			}
		}
		String temp=localSatus+" "+replyfromUDP1+" "+replyfromUDP2;
		//server's log
				filename = "log/server/"+findRegion(ipAdd) + "-server.log";
				doLog(filename,"From : " + findRegion(ipAdd) + " region, result:"
						+ temp);
				
		//admin's log
				filename = "log/admin.log";
				doLog(filename,"From : " + findRegion(ipAdd) + " region, result:"
						+ temp);
		
		
		return temp;
		
		
	}
	
	//calling UDP server by specifying port no & string msg
	private String callUDP(DatagramSocket aSocket,int serverPort,String msg){
		try {
			aSocket = new DatagramSocket();
			byte[] m = msg.getBytes();
			InetAddress aHost = InetAddress.getByName("localhost");
			
			DatagramPacket request = new DatagramPacket(m,
					m.length, aHost, serverPort);
			aSocket.send(request);

			byte[] buffer = new byte[100000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			aSocket.receive(reply);
			
			return new String(reply.getData());
			
		} catch (SocketException e) {
			
			System.out.println("Error in SocketException : " + e.getMessage());
		} catch (IOException e) {
			
			System.out.println("Error in IOException : " + e.getMessage());
		} catch (Exception e) {
			
			System.out.println("Error in Exception : " + e.getMessage());
		} finally {

			if (aSocket != null)
				aSocket.close();

			
		}
		return "";
	}
	
	//used for logging
	private void doLog(String filename, String string) {
		Logger logger = Logger.getLogger(GameServerImpl.class.getName());
		logger.setUseParentHandlers(false); //wont print to console
		FileHandler fileHandler = null;
		try {
			File file = new File(filename);
			if (file.exists()) {
				//if file already exists,it will append the data
				fileHandler = new FileHandler(filename, true);
			} else {
				//will create new file 
				fileHandler = new FileHandler(filename);
			}

			logger.addHandler(fileHandler);
			SimpleFormatter formatter = new SimpleFormatter();
			fileHandler.setFormatter(formatter);
			logger.info(string);
		} catch (SecurityException e) {
			logger.info("SecurityException : " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.info("IOException : " + e.getMessage());
			e.printStackTrace();
		} finally {
			fileHandler.close();
		}
	}
	
	@Override
	public String suspendAccount(String adminUnm, String adminPwd, String ipAdd,
			String username) {
		String filename;
		//server's log
		filename = "log/server/"+findRegion(ipAdd) + "-server.log";
		doLog(filename,"Suspend account request from:"+findRegion(ipAdd)+" region to remove "+username);
		
		int len;
		int result=0;
		if (hashtable.containsKey(username.substring(0, 1))) { //if hashtable contains the key
			synchronized (hashtable) {
				
				len = hashtable.get(username.substring(0, 1)).size();
				if (len > 0) {
					for (int i = 0; i < len; i++) {	//if length under the key is >0, then traverse through the length & find the username
						if (username.equalsIgnoreCase(hashtable.get(username.subSequence(0, 1)).get(i).username)) {
							hashtable.get(username.subSequence(0, 1)).remove(i);
							// it means unm exists,so suspend it!
							result=1;
							
							//server's log
							filename = "log/server/"+findRegion(ipAdd) + "-server.log";
							doLog(filename,"Status: Removed successully");
							
							//player's log
							filename = "log/client/"+username + findRegion(ipAdd) + ".log";
							doLog(filename,"account suspended");
							
							//admin's log
							filename = "log/admin.log";
							doLog(filename,"account suspended for "+username+ " from "+findRegion(ipAdd)+" region");
							break;
						}
					}
				}//synchronization ends
			}
		}
		//setting log if no match found for username
		if (result==0) {
			
			//server's log
			filename = "log/server/"+findRegion(ipAdd) + "-server.log";
			doLog(filename,"Status: wrong username");
			return "unm not found";
		}
		else if (result==1) {
			return "successful!!";
		}
		return "";
	}

	
	//it uses two fuctions, viz createAccount & suspendAccount
	@Override
	public String transferAccount(String userName, String password,
			String oldipApp, String newipAdd) {
		int len,result=0;
		int serverPort=0;
		String filename;
		//server's log
		filename = "log/server/"+findRegion(oldipApp) + "-server.log";
		doLog(filename,"Transfer request from "+userName);
		
		if (hashtable.containsKey(userName.substring(0, 1))) {
			
			len = hashtable.get(userName.substring(0, 1)).size();
			//if it contains the key,then traverse through its length
			if (len > 0) {
				
				for (int i = 0; i < len; i++) {
					String key=userName.substring(0, 1);
					if (userName.equalsIgnoreCase(hashtable.get(userName.subSequence(0, 1)).get(i).username)) {
						
						// it means unm exists,so transfer it!
						String oldRegion=findRegion(oldipApp);
						String newRegion=findRegion(newipAdd);
						
						if (newRegion.equalsIgnoreCase("NA")) {
							serverPort = portNA;
						}
						else if(newRegion.equalsIgnoreCase("EU"))
						{	
							serverPort = portEU;
						}
						else if(newRegion.equalsIgnoreCase("AS"))
						{	
							serverPort = portAS;
						}
						DatagramSocket aSocket = null;
						String newUserInfo=userName+"_"+password+"_"+newipAdd+"_"+hashtable.get(key).get(i).age+"_"+hashtable.get(key).get(i).firstName+"_"+hashtable.get(key).get(i).lastName;
						
						//it will call udp(of the region to which transfer is needed) by passing all the user info
						String replytemp=callUDP(aSocket, serverPort,newUserInfo);
						String temp;
						temp=replytemp;
							
						//if added successful,then suspend the local one
						if (temp.contains("successful")) {
							StringHolder replyFromServer=new StringHolder();
							String r=suspendAccount("admin", "admin", oldipApp, userName);
							//ans.value=replyFromServer.value;
							
							//server's log
							filename = "log/server/"+findRegion(oldipApp) + "-server.log";
							doLog(filename,"Status: transfer successful to "+findRegion(newipAdd)+" region");
							
							//client's log
							filename = "log/client/"+userName + findRegion(oldipApp) + ".log";
							doLog(filename,"Account transfered to "+findRegion(newipAdd)+" region");
							
						}							
						result=1;
						break;
					}
				}
			}
			
		}
		
		if (result==0) {
			//unm didnt found in local server so no transfer possible
			return "wrong unm";
		}
		else if (result==1) {
			return "successful!!";
		}
		return "";
	}//transfer method ends


}//class ends

