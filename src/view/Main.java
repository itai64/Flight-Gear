package view;

import javafx.application.Application;
import javafx.stage.Stage;
import model.MyModel;
import viewModel.ViewModel;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {

			MyModel m = new MyModel();
			ViewModel vm = new ViewModel(m);
			m.addObserver(vm);

			FXMLLoader loader = new FXMLLoader(getClass().getResource("GuiMainWindow.fxml"));
			Parent root = loader.load();

			GuiController ctrl = loader.getController();
			ctrl.setViewModel(vm);
			vm.addObserver(ctrl);
			primaryStage.setTitle("FlightGear Desktop App");
			primaryStage.setScene(new Scene(root, 828, 290));
			primaryStage.show();



		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
