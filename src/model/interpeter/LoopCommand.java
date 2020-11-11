package model.interpeter;

import java.util.ArrayList;

import javafx.beans.property.DoubleProperty;

public class LoopCommand implements Command {
	Command[] commands;

	public double doCommand(String[] args) {
		double returnValue = 0;
		String var = args[0];
		String condition = args[1];
		int index = 5;
		ArrayList<String> lines = new ArrayList<>();
		Double value = MyInterpreter.SymbolTbl.get(var).doubleValue();
		double upperBound = Double.parseDouble(args[2]);

		while (!args[index].equals("}")) {
			lines.add(args[index++]);
		}

		//condition recognizer
		switch (condition) {
		case "==":
			while (value == upperBound) {
				returnValue = MyInterpreter.parser(lines);
				value = MyInterpreter.SymbolTbl.get(var).doubleValue();
			}
			break;
		case "<=":
			while (value <= upperBound) {
				returnValue = MyInterpreter.parser(lines);
				value = MyInterpreter.SymbolTbl.get(var).doubleValue();
			}
			break;
		case ">=":
			while (value >= upperBound) {
				returnValue = MyInterpreter.parser(lines);
				value = MyInterpreter.SymbolTbl.get(var).doubleValue();
			}
			break;
		case "<":
			while (value < upperBound) {
				returnValue = MyInterpreter.parser(lines);
				value = MyInterpreter.SymbolTbl.get(var).doubleValue();
			}
			break;
		case ">":
			while (value > upperBound) {
				returnValue = MyInterpreter.parser(lines);
				value = MyInterpreter.SymbolTbl.get(var).doubleValue();
			}
			break;
		case "!=":
			while (value != upperBound) {
				returnValue = MyInterpreter.parser(lines);
				value = MyInterpreter.SymbolTbl.get(var).doubleValue();
			}
			break;
		}
		return returnValue;
	}
}
