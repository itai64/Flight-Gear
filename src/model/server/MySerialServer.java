package model.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MySerialServer implements Server {
	volatile boolean _stop = false;
	
	public MySerialServer() {
		this._stop = false;
	}
	public void open(int port, ClientHandler ch) {
		try {
			ServerSocket server = new ServerSocket(port);
//			server.setSoTimeout(1000);
			Thread t = new Thread(() -> {
				try {
					while (!this._stop) {
						Socket aClient = server.accept(); // blocking call
						ch.handelClient(aClient.getInputStream(), aClient.getOutputStream());
//						aClient.getInputStream().close();
						aClient.close();
						Thread.sleep(50);
					}
					if (server != null)
						server.close();

				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("I0 exception.");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
			t.start();
			System.out.println("serial server is running");
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			System.out.println("Socket time out.");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("I0 exception.");
		}
	}

	public void stop() {
		this._stop = true;
	}

}
