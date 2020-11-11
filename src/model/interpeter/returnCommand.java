package model.interpeter;

import shuntingYard.ShuntingYard;

public class returnCommand implements Command {

	public double doCommand(String[] args) {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < args.length - 1; i++)
			sb.append(args[i]);

		double value = ShuntingYard.calc(sb.toString());

		if (!MyInterpreter.pathToVar.isEmpty())
			MyInterpreter.pathToVar.clear();

		if (!MyInterpreter.varToPath.isEmpty())
			MyInterpreter.varToPath.clear();

		if (!MyInterpreter.SymbolTbl.isEmpty())
			MyInterpreter.SymbolTbl.clear();

		if (!MyInterpreter.pathToValue.isEmpty())
			MyInterpreter.pathToValue.clear();
		return value;
	}

}
