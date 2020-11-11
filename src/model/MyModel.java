package model;

import model.interpeter.*;
import model.server.Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;

public class MyModel extends Observable {

	Server calcServer;
	int calcServerPort = 5566;
	PrintWriter outTocalcServer;
	BufferedReader inFromCalcServer;
	String[][] matrix;
	String path;
	String ipForCalcServer;
	int portForCalcServer;
	Thread t;
	boolean scriptRunMode = false;

	public MyModel() {
		Command openServer = MyInterpreter.commandMap.get("openDataServer");
		String[] args = { "5400", "10" };
		openServer.doCommand(args);
	}

	public void runScript(String[] lines) {
		scriptRunMode = true;
		t = new Thread(() -> {
	
		while (!Thread.currentThread().isInterrupted()) {MyInterpreter.interpret(lines);}
		});
		t.start();
	}
	

	public void stopScript() {
		if (t != null && scriptRunMode )
			t.interrupt();
		scriptRunMode = false;
	}

	public void controlElevatorAileron(double elevator, double aileron) {
		System.out.println("set /controls/flight/elevator " + elevator);
		ConnectCommand.out.println("set /controls/flight/elevator " + elevator);
		System.out.println("set /controls/flight/aileron " + aileron);
		ConnectCommand.out.println("set /controls/flight/aileron " + aileron);
		ConnectCommand.out.flush();

	}

	public void controlRudder(double rudder) {
		System.out.println("set /controls/flight/rudder " + rudder);
		ConnectCommand.out.println("set /controls/flight/rudder " + rudder);
		ConnectCommand.out.flush();
	}

	public void controlThrottle(double throttle) {
		System.out.println("set /controls/engines/current-engine/throttle " + throttle);
		ConnectCommand.out.println("set /controls/engines/current-engine/throttle " + throttle);
		ConnectCommand.out.flush();
	}

	public void connectToSim(String ip, String port) {
		String[] args = { ip, port };
		Command connect = MyInterpreter.commandMap.get("connect");
		connect.doCommand(args);
	}

	public String connectToCalcServer(String ip, String port, String[][] matrix, String init, String goal) {
		ipForCalcServer = ip;
		portForCalcServer = Integer.parseInt(port);
		this.matrix = matrix;
		return getPathFromCalcServer(init, goal);
	}

	public String getPathFromCalcServer(String init, String goal) {
		try {

			Socket theServer = new Socket(ipForCalcServer, portForCalcServer);
			System.out.println("connected to calc server");
			outTocalcServer = new PrintWriter(theServer.getOutputStream());
			inFromCalcServer = new BufferedReader(new InputStreamReader(theServer.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		int i, j;
		System.out.println("sending problem...");
		for (i = 0; i < matrix.length - 1; i++) {
			for (j = 0; j < matrix[i].length - 1; j++) {
				outTocalcServer.print(matrix[i][j] + ",");
			}
			outTocalcServer.println(matrix[i][j]);
		}

		outTocalcServer.println("end");// end of matrix sign
		outTocalcServer.println(init);
		outTocalcServer.println(goal);
		outTocalcServer.flush();
		System.out.println("\tend\n\t" + init + "\n\t" + goal);
		System.out.println("\tproblem sent, waiting for solution...");
		try {
			this.path = inFromCalcServer.readLine();
//			System.out.println(this.path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("\tsolution received");
		return path;
	}

	public void getAircraftPosition() {
		new Thread(() -> {
			while (true) {
				String[] pos = new String[3];
				pos[0] = String.valueOf(MyInterpreter.SymbolTbl.get("positionX").getValue());
				pos[1] = String.valueOf(MyInterpreter.SymbolTbl.get("positionY").getValue());
				pos[2] = String.valueOf(MyInterpreter.SymbolTbl.get("heading").getValue());

				this.setChanged();
				this.notifyObservers(pos);
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
