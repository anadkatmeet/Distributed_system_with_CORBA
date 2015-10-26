package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.omg.CORBA.StringHolder;

public class UDPServer extends Thread{
	int port;
	GameServerImpl server;
	
	public UDPServer(int port,GameServerImpl server){
		this.server=server;
		this.port=port;
	}
	
	public void run(){
		DatagramSocket aSocket = null;
		try {
			aSocket=new DatagramSocket(port);
			
			while(true){
					byte[] buffer = new byte[64000];	//kept inside the while loop so its initiated on every request
					DatagramPacket request = new DatagramPacket(buffer,buffer.length);
					aSocket.receive(request);
					
					String temp=new String(request.getData());
					temp=temp.trim();
					//when server passes "call", it means call calculate status method
					if (temp.contains("call")) {
						buffer=server.calculateStatus().getBytes();
					}
					else if(temp.contains("_")){	//this means request came from transferAccount method
						String[] userInfo=temp.split("_");
						
						//0-unm,1-pwd,2-newip,3-age,4-fnm,5-lnm
						
						//StringHolder replyFromServer=new StringHolder();
						String reply1=server.createPlayerAccount(userInfo[4], userInfo[5], Integer.parseInt(userInfo[3]), userInfo[0], userInfo[1], userInfo[2]);
						buffer=reply1.getBytes();
					}else
					{	
						buffer="error somewhere".getBytes();
					}
					DatagramPacket reply = new DatagramPacket(buffer, buffer.length, request.getAddress(), request.getPort());
					aSocket.send(reply);
					
			}
		} catch (SocketException e) {
			System.out.println("SocketException : " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IOException : " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
		}
	}
}
