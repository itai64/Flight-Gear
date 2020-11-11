package model.interpeter;

import model.interpeter.assets.DataReaderServer;

public class DisconnectCommand implements Command {

	public double doCommand(String[] args) {
		DataReaderServer.close();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		ConnectCommand.out.println("bye");
		ConnectCommand.out.close();

		return 0;
	}

}
