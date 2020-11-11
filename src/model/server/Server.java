package model.server;

public interface Server {
	public void open(int port, ClientHandler ch);
	public void stop();
}
