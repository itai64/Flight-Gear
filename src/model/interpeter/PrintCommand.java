package model.interpeter;

public class PrintCommand implements Command {

	//This command print the value of variable to the terminal.
	public double doCommand(String[] args) {
		
		if(MyInterpreter.SymbolTbl.containsKey(args[0]))
			System.out.println("Print: " + MyInterpreter.SymbolTbl.get(args[0]).get());
		
		else {
			if(args[0].startsWith("\""))
				System.out.println("Print: " + args[0].substring(1,args[0].length()-1));
			else
				System.out.println("Print: "+args[0]);
		}
		return 0;
	}
}
