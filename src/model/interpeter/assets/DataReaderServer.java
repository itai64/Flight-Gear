package model.interpeter.assets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import javafx.beans.property.SimpleDoubleProperty;
import model.interpeter.MyInterpreter;

public class DataReaderServer {

	private static volatile boolean stop;
	private int port;
	private int frequency;
	public ArrayList<String> varNames;
	BufferedReader input;

	public DataReaderServer(int port, int frequency) {
		stop = false;
		this.input = null;
		if (port < 1 || port > 65000)
			System.out.println("Unvalid port!");
		this.port = port;
		if (frequency < 1)
			System.out.println("Unvalid frequency");
		this.frequency = frequency;

	}

	public void runServer() {
		try {
			ServerSocket server = new ServerSocket(port);
			System.out.println("Data Reader server is up and running.");
			try {
				while (!stop) {
					Socket client = server.accept(); // blocking call
					System.out.println("simulator has been conncated to the Data Reader Server");
					input = new BufferedReader(new InputStreamReader(client.getInputStream()));
					String line, name;
					String[] lines;
					double value;
					while ((line = input.readLine()) != null) {
						lines = line.split(",");

						if (lines.length != MyInterpreter.varList.size()) {
							System.out.println("Connections Problems");
							System.out.println("lines.length = " + lines.length + "and varList.size is "
									+ MyInterpreter.varList.size());
							continue;
						}

						for (int i = 0; i < lines.length; i++) {
//							System.out.println("aileron --> "+lines[17]+", elevator ---> "+lines[18] +", rudder --> "+lines[19]);
							name = MyInterpreter.varList.get(i);
							value = Double.parseDouble(lines[i]);
							MyInterpreter.SymbolTbl.get(name).set(value);
							MyInterpreter.pathToValue.put(MyInterpreter.varToPath.get(name),
									new SimpleDoubleProperty(value));
						}

						try {
							Thread.sleep(1000 / frequency);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					}
					input.close();
					client.close();
				}

			} catch (SocketTimeoutException e) {
				e.printStackTrace();
			}

			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void close() {
		stop = true;
	}

}
