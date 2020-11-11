package model.interpeter;

import java.io.PrintWriter;
import java.util.LinkedList;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
//import model.interpeter.assets.DataWriterClient;
import shuntingYard.*;

public class AssignCommand implements Command {

	//This command  assign, variable to value or variable to path 
	public double doCommand(String[] args) {
		String var = args[0];
		StringBuilder sb = new StringBuilder();
		String path;
		Double value;

		if (!MyInterpreter.SymbolTbl.containsKey(var)) {
			System.out.println(var + "is unvalid input.");
			return -1;
		}


		// bind path
		if (args[1].equals("bind")) {
			if (MyInterpreter.SymbolTbl.containsKey(args[0]) && (MyInterpreter.SymbolTbl.get(args[0]).doubleValue()!=-1))
				return 0;
			
			for (int i = 2; i < args.length - 1; i++) {
				if (args[i].equals("\""))
					continue;
				sb.append(args[i]);
			}
			sb.deleteCharAt(sb.length()-1);
			path = sb.toString();

			
			
			//bind var and path 
			MyInterpreter.pathToValue.put(path, new SimpleDoubleProperty(0.0));
			MyInterpreter.SymbolTbl.put(var, new SimpleDoubleProperty(0.0));
			
			MyInterpreter.SymbolTbl.get(var).bindBidirectional(MyInterpreter.pathToValue.get(path));
			MyInterpreter.varToPath.put(var, path);
			

		}

		else {
			for (int i = 1; i < args.length - 1; i++) // create an equation
				sb.append(args[i]);
			value = ShuntingYard.calc(sb.toString());
			MyInterpreter.SymbolTbl.get(var).set(value);
			path = MyInterpreter.varToPath.get(var);
			if(path!=null) {
				System.out.println("set " + path + " " + value);
				ConnectCommand.out.println("set " + path + " " + value);
			}

		}

		return 0;
	}

}
