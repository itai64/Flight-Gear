package model.server;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;;


public interface ClientHandler {

	public void handelClient(InputStream inFromClient ,OutputStream outToClient)throws IOException;
}
