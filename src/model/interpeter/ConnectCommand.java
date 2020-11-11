package model.interpeter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ConnectCommand implements Command {
	public static PrintWriter out;
	public static BufferedReader in;
	@SuppressWarnings("resource")
	@Override
   //This command connects as a client to remote server 
	public double doCommand(String[] code) { 
		try {
			Socket client = new Socket( InetAddress.getByName(code[0]), Integer.parseInt(code[1]));
			out = new PrintWriter(client.getOutputStream(),true);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return 0;
	}

}
