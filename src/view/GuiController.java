package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import model.server.Position;
import view.Joystick;
import viewModel.ViewModel;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.Scanner;

public class GuiController implements Initializable, Observer {
	@FXML
	private TextArea commands;
	ViewModel vm;
	@FXML
	MapView map;
	@FXML
	MapView aircraft;
	@FXML
	MapView path;
	@FXML
	Circle joyStick;
	@FXML
	Circle outerCircle;
	@FXML
	Slider throttle;
	@FXML
	Slider rudder;
	@FXML
	Button connect;
	@FXML
	Button loadData;
	@FXML
	Button loadScript;
	@FXML
	Button runScript;
	@FXML
	TextArea scriptAsText;
	@FXML
	Button calcPath;
	@FXML
	RadioButton autoPilot;
	@FXML
	RadioButton manual;
	Joystick myJoyStick;
	boolean isScriptLoaded = false;
	boolean isConnected = false;
	boolean isConnectedToCalcServer = false;
	boolean isScriptRunning = false;


	/*
	 * This function allow to open a txt file from the disk that will use us as a
	 * scripts of commands for the interpreter
	 * 
	 * @return File
	 */
	public File openFile() {
		System.out.println("open file");
		FileChooser fc = new FileChooser();
		fc.setTitle("open file");
		fc.setInitialDirectory(new File("./resources"));
		fc.setSelectedExtensionFilter(new ExtensionFilter("txt files (*.txt)", "*.txt"));
		File chosen = fc.showOpenDialog(null);
		if (chosen != null)
			System.out.println(chosen.getName());
		return chosen;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ToggleGroup tg = new ToggleGroup();
		manual.setToggleGroup(tg);
		autoPilot.setToggleGroup(tg);
		manual.fire();
		this.myJoyStick = new Joystick(this);
		this.map.setVc(this);
		this.aircraft.setVc(this);
		this.path.setVc(this);

	}

	public void setViewModel(ViewModel vm) {
		this.vm = vm;
		vm.joyStickX.bind(joyStick.centerXProperty());
		vm.joyStickY.bind(joyStick.centerYProperty());
		vm.throttle.bind(throttle.valueProperty());
		vm.rudder.bind(rudder.valueProperty());

		this.aircraft.latitude.bind(vm.latitude);
		this.aircraft.longitude.bind(vm.longitude);
		this.aircraft.heading.bind(vm.heading);

	}

	/*
	 * This function manage and define the calculation of the plane to the
	 * destination point Establish the connection and the GUI
	 * 
	 */
	public void calcPath() {
		if (!isConnectedToCalcServer) {
			Stage popup = new Stage();
			VBox box = new VBox(20);
			Label ipLabel = new Label("IP:");
			Label portLabel = new Label("PORT:");
			TextField ipUserInput = new TextField();
			TextField portUserInput = new TextField();
			Button submit = new Button("Submit");
			box.getChildren().addAll(ipLabel, ipUserInput, portLabel, portUserInput, submit);
			popup.setScene(new Scene(box, 350, 250));
			popup.setTitle("Connect to path calc server");
			popup.show();
			submit.setOnAction(e -> {
				String ip = ipUserInput.getText();
				String port = portUserInput.getText();
				Position currentPos = map.currentPosition, destPoint = path.destPoint;
				popup.close();
				System.out.println("connected to calc server...");

				this.path.setPath(vm.connectToCalcServerVm(ip, port, map.matrix, currentPos, destPoint), currentPos,
						destPoint);
				isConnectedToCalcServer = true;

			});
		} else {

			Position currentPos = map.currentPosition, destPoint = path.destPoint;//
			this.path.setPath(vm.getPathFromCalcServerVm(currentPos, destPoint), currentPos, destPoint);
		}
	}

	/*
	 * This function manage and define the connection to the Flight gear simulator
	 * 
	 * 
	 */
	public void connectToFlightgear() {
		Stage popup = new Stage();
		VBox box = new VBox(20);
		Label ipLabel = new Label("IP:");
		Label portLabel = new Label("PORT:");
		TextField ipUserInput = new TextField();
		TextField portUserInput = new TextField();
		Button submit = new Button("Submit");
		box.getChildren().addAll(ipLabel, ipUserInput, portLabel, portUserInput, submit);
		popup.setScene(new Scene(box, 350, 250));
		popup.setTitle("Connect to FlightGear");
		popup.show();
		submit.setOnAction(e -> {
			String ip = ipUserInput.getText(); 
			String port = portUserInput.getText();
			vm.connectToSimVM(ip, port);
			popup.close();
			System.out.println("connected to FlightGear...");
		});
		isConnected = true;
	}

	/*
	 * This function load a CSV file that contains matrix of heights 
	 * Draw the map by parsing the matrix heights
	 * Draw the aircraft on the map 
	 */
	public void loadData() {
		this.map.loadCSV();
		this.map.mapDrawer();
		this.aircraft.setAircraft();
		vm.getAircraftPosition();
	} 

	/*
	 * This function initialize the GUI to Auto Pilot Mode  
	 */
	public void autoPilotMode() {
		manual.disarm();
		rudder.setDisable(true);
		throttle.setDisable(true);
		joyStick.setDisable(true);
		loadScript.setDisable(false);
		runScript.setDisable(false);
		System.out.println("autopilot mode is on");
	}

	/*
	 * This function run the commands script and validate that the script has been loaded  
	 */
	public void runScript() {
		if (isConnected && isScriptLoaded) {
			isScriptRunning = true;
			vm.runScriptVm(scriptAsText.getText());
		} else {
			if (!isConnected) {
				popuper("Please connect first");
			}
			if (!isScriptLoaded) {
				popuper("Please load script first");
			}
		}
	}

	/*
	 * This function sets up a popup box with message
	 */
	public void popuper(String name) {
		Stage popup = new Stage();
		VBox box = new VBox(10);
		Label msg = new Label(name);
		Button ok = new Button("ok");
		box.getChildren().addAll(msg, ok);
		popup.setScene(new Scene(box, 250, 100));
		popup.setTitle("Massage");
		popup.show();
		ok.setOnAction(e -> {
			if (name.contains("script"))
				loadScript();
			if (name.contains("connect"))
				connectToFlightgear();
			popup.close();
		});
	}  
		

	/*
	 * This function load a txt file that contains commands to the simulator
	 * The commands added to the text area in the GUI   
	 */
	public void loadScript() {
		FileChooser fc = new FileChooser();
		fc.setTitle("Choose script");
		fc.setInitialDirectory(new File("./resources"));
		fc.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("txt", "*.txt"));
		File chosen = fc.showOpenDialog(null);
		try {
			Scanner s = new Scanner(new FileReader(chosen)).useDelimiter("\n");
			while (s.hasNext()) {
				scriptAsText.appendText(s.next() + "\n");
			}
		} catch (FileNotFoundException e) {
		}
		isScriptLoaded = true;

	}

	/*
	 * This function initialize the GUI to Auto Manual Mode  
	 */

	public void manualMode() {
		autoPilot.disarm();
		rudder.setDisable(false);
		throttle.setDisable(false);
		loadScript.setDisable(true);
		joyStick.setDisable(false);
		if(vm != null && isScriptRunning) {
			vm.stopScriptVm();
			isScriptRunning = false;
		}
		System.out.println("manual mode is on");
	}

	/*
	 * This function limit the joystick movement to the frame circle  
	 */
	public void moveElevatorAileron() {
		myJoyStick.moveJoyStick();
	} 

	
	/*
	 * This function extracts the values from the joystick
	 */
	public void valFromJoystick() {
		vm.controlElevatorAileronVm();
	}

	/*
	 * This function extracts the values from the Throttle
	 */
	public void moveThrottle() {
		vm.controlThrottleVm();
	}

	/*
	 * This function extracts the values from the Rudder
	 */
	public void moveRudder() {
		vm.controlRudderVm();
	}
	
	/*
	 * This function initialize the rudder to zero and move the slider to the middle 
	 */
	public void centerRudder() {
		rudder.setValue(0);
		moveRudder();
	}

	/*
	 * This function sets and destination sign and extracts the destination coordinates 
	 */
	public void setDest() {
		path.setDestination();

	}

	/*
	 * This function update the aircraft position as par of the observer 
	 */
	@Override
	public void update(Observable o, Object arg) {
		this.aircraft.position();

	}

}
