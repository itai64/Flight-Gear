package viewModel;

import javafx.beans.property.*;
import model.MyModel;
import model.server.Position;
import java.util.Observable;
import java.util.Observer;

public class ViewModel extends Observable implements Observer {

	MyModel m;
	public DoubleProperty joyStickX, joyStickY, throttle, rudder, longitude, latitude, heading;

	public ViewModel(MyModel m) {
		this.m = m;
		joyStickY = new SimpleDoubleProperty();
		joyStickX = new SimpleDoubleProperty();
		throttle = new SimpleDoubleProperty();
		rudder = new SimpleDoubleProperty();
		latitude = new SimpleDoubleProperty();
		longitude = new SimpleDoubleProperty();
		heading = new SimpleDoubleProperty();
	}

	public void runScriptVm(String script) {
		m.runScript(script.split("\n"));
	}
	
	public void stopScriptVm() {
		m.stopScript();
	}

	public void controlElevatorAileronVm() {
		double elevatorVal;
		double aileronVal;
		elevatorVal = -joyStickY.doubleValue() / 61;
		aileronVal = joyStickX.doubleValue() / 61;
		m.controlElevatorAileron(elevatorVal, aileronVal);
	}

	public void controlRudderVm() {
		m.controlRudder(rudder.doubleValue());
	}
	

	public void controlThrottleVm() {
		m.controlThrottle(throttle.doubleValue());
	}

	public String getPathFromCalcServerVm(Position currentPos, Position destPoint) {
		String initPointAsString = currentPos.getX() + "," + currentPos.getY();
		String goalPointAsString = destPoint.getX() + "," + destPoint.getY();
		return m.getPathFromCalcServer(initPointAsString, goalPointAsString);
	}

	public void connectToSimVM(String ip, String port) {
		m.connectToSim(ip, port);
	}

	public String connectToCalcServerVm(String ip, String port, double[][] matrix, Position currentPos,
			Position destPoint) {
		String[][] matrixAsString = new String[matrix.length][matrix[0].length];
		String initPointAsString = currentPos.getX() + "," + currentPos.getY(); 
		String goalPointAsString = destPoint.getX() + "," + destPoint.getY();
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[i].length; j++)
				matrixAsString[i][j] = String.valueOf(matrix[i][j]);
		return m.connectToCalcServer(ip, port, matrixAsString, initPointAsString, goalPointAsString);
	}

	public void getAircraftPosition() {
		m.getAircraftPosition();
	}

	@Override
	public void update(Observable o, Object arg) {
		latitude.setValue(Double.parseDouble(((String[]) arg)[0]));
		longitude.setValue(Double.parseDouble(((String[]) arg)[1]));
		heading.setValue(Double.parseDouble(((String[]) arg)[2]));
		this.setChanged();
		this.notifyObservers();
	}

}
