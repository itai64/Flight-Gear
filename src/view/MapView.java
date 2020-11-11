package view;

import javafx.scene.canvas.Canvas;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import model.interpeter.MyInterpreter;
import model.server.Position;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MapView extends Canvas {

	String path;
	GuiController vc;
	double heightDelta;
	double maxHeight, minHeight;
	double[][] matrix;
	double pixelSize;
	Point2D initCoordinate;
	int columns, rows;

	Image aircraft;
	DoubleProperty longitude, latitude, heading;
	Position currentPosition;
	double x, y;
	int[] mapSize;

	Position initPoint;
	Position destPoint;
	String[] Ppath;
	int Prows, Pcolumns;
	Image Px;

	public MapView() {
		currentPosition = new Position();
		try {
			aircraft = new Image(new FileInputStream("./resources/airplan.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		longitude = new SimpleDoubleProperty();
		latitude = new SimpleDoubleProperty();
		heading = new SimpleDoubleProperty();

		initPoint = new Position();
		destPoint = new Position();
		try {
			Px = new Image(new FileInputStream("./resources/sign.png"));
		} catch (FileNotFoundException e) {
		}
	}

	public void setPath(String path) {
		this.path = path;
	}

	@SuppressWarnings("resource")
	public void loadCSV() {
		FileChooser fc = new FileChooser();
		fc.setTitle("Choose CSV file");
		fc.setInitialDirectory(new File("./resources"));
		fc.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("csv", "*.csv"));
		File chosen = fc.showOpenDialog(null);
		if (chosen != null) {
			System.out.println(chosen.getName());
		}
		try {
			Scanner s = new Scanner(new FileReader(chosen)).useDelimiter("\n");
			this.setMapDisplay(s);
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void setVc(GuiController vc) {
		this.vc = vc;
	}

	public void setMapDisplay(Scanner s) {
		ArrayList<String[]> arr = new ArrayList<>();
		while (s.hasNext()) {
			String[] read = s.next().split(",");
			arr.add(read);
		}

		initCoordinate = new Point2D(Double.parseDouble(arr.get(0)[0]), Double.parseDouble(arr.get(0)[1]));
		pixelSize = Double.parseDouble(arr.get(1)[0]);
		columns = arr.get(2).length;
		rows = arr.size() - 2;
		matrix = new double[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				matrix[i][j] = Double.parseDouble(arr.get(i + 2)[j]);
				if (matrix[i][j] < minHeight)
					minHeight = matrix[i][j];
				else if (matrix[i][j] > maxHeight)
					maxHeight = matrix[i][j];
			}
		}
		heightDelta = maxHeight - minHeight;
	}

	public Color getColor(double cellHeight) {
		Color c = Color.hsb(100 * cellHeight / heightDelta, 1.0, 0.5);
		double red = c.getRed(), green = c.getGreen(), blue = c.getBlue();
		return Color.color(red, green, blue);
	}

	public void mapDrawer() {
		GraphicsContext gc = getGraphicsContext2D();
		double H = this.getHeight();
		double W = this.getWidth();
		double h = H / rows;
		double w = W / columns;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				gc.setFill(getColor(this.matrix[i][j]));
				gc.fillRect(j * w, i * h, w, h);

			}
		}
	}

	public void setAircraft() {

		x = vc.map.initCoordinate.getX();
		y = vc.map.initCoordinate.getY();

		System.out.println("The Aircraft initialize coordinare are :  " + x + " , " + y);
		mapSize = new int[] { vc.map.rows, vc.map.columns };
		pixelSize = vc.map.pixelSize;
	}

	public void position() {
		double longNew = ((MyInterpreter.SymbolTbl.get("positionX").getValue() - x) + pixelSize) / pixelSize;
		double latNew = (-(MyInterpreter.SymbolTbl.get("positionY").getValue() - y) + pixelSize) / pixelSize;

		if (mapSize != null) {
			int row = Math.round((float) (mapSize[0] * longNew / getHeight()));
			int column = Math.round((float) (mapSize[1] * latNew / getWidth()));
			currentPosition.setX(row);
			currentPosition.setY(column);

		}
		GraphicsContext gc = getGraphicsContext2D();
		gc.clearRect(0, 0, getWidth(), getHeight());
		gc.drawImage(aircraft, longNew, latNew, 25, 25);
	}

	public void setPath(String path, Position initPoint, Position destPoint) {
		this.Ppath = path.split(",");
		this.initPoint = initPoint;
		this.destPoint = destPoint;
		convertPathToLine();
	}

	public void setDestination() {
		setOnMouseClicked(e -> {
			GraphicsContext gc = getGraphicsContext2D();
			gc.clearRect(0, 0, getWidth(), getHeight());
			gc.drawImage(Px, e.getX() - 10, e.getY() - 10, 20, 20);
			int row = Math.round((float) (vc.aircraft.mapSize[0] * e.getY() / getHeight()));
			int column = Math.round((float) (vc.aircraft.mapSize[1] * e.getX() / getWidth()));
			if (row >= vc.aircraft.mapSize[0])
				row = vc.aircraft.mapSize[0] - 1;
			if (column >= vc.aircraft.mapSize[1])
				column = vc.aircraft.mapSize[1] - 1;
			destPoint.setX(row);
			destPoint.setY(column);
		});
	}

	public void convertPathToLine() {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter("./resources/mat.txt"));
			Prows = vc.aircraft.mapSize[0];
			Pcolumns = vc.aircraft.mapSize[1];
			int[][] pathMatrix = new int[Prows][Pcolumns];
			GraphicsContext gc = getGraphicsContext2D();
			double H = this.getHeight();
			double W = this.getWidth();
			double h = H / Prows;
			double w = W / Pcolumns;
			int i = initPoint.getY(), j = initPoint.getX();
			pathMatrix[i][j] = 1;
			gc.setFill(Color.BLACK);
			gc.fillOval(j * w + 5, i * h + 7, w / 3, h / 3);
			for (String s : Ppath) {
//				System.out.print(s + ",");
				if (s.equals("Up"))
					i--;
				if (s.equals("Down"))
					i++;
				if (s.equals("Left"))
					j--;
				if (s.equals("Right"))
					j++;
				pathMatrix[i][j] = 1;
				gc.setFill(Color.BLACK);
				gc.fillOval(j * w + 7, i * h + 5, w / 3, h / 3);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
