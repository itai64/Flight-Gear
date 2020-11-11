package model.interpeter;

import javafx.beans.property.SimpleDoubleProperty;

public class VarCommand implements Command {

	public double doCommand(String[] args) {
		if (MyInterpreter.SymbolTbl.containsKey(args[0])) 
			return 0;

		if (MyInterpreter.SymbolTbl.containsKey(args[0])|| args.length!=2) {
			System.out.println("problem with var command");
			return -1;
		}
		
		MyInterpreter.SymbolTbl.put(args[0], new SimpleDoubleProperty(-1.0)); // (value = -1) flag for AssignCommand
		return 0;
	}
}
