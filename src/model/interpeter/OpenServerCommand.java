package model.interpeter;

import model.interpeter.assets.DataReaderServer;

public class OpenServerCommand implements Command {

	//This command open a DataReaderServer
	public double doCommand(String[] param_input) {
		int delay = 1000;
		int port = Integer.parseInt(param_input[0]);
		int frequency = Integer.parseInt(param_input[1]);

		new Thread(() -> {
			(new DataReaderServer(port, frequency)).runServer();
		}).start();
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return 0;
	}

}
