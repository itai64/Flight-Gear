package model.interpeter;

public class SleepCommand implements Command {

	public double doCommand(String[] args) {

		try {
			Thread.sleep(Long.parseLong(args[0]));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {Thread.currentThread().interrupt();}
		
		return 0;
	}
}
