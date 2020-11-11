package model.server;

public class MainServer {

	public static void main(String[] args) {
		MySerialServer calcServer = new MySerialServer();
		MyClientHandler clientHandler = new MyClientHandler();
		int port = 5566;
		calcServer.open(port, clientHandler);

	}

}
