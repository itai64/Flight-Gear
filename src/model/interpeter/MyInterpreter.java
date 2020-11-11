/*
 * @Auther Guy Cohen          ID - 204536221
 * @Auther Itai Blumenkants   ID - 313260119
 * 
 */

package model.interpeter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class MyInterpreter {

	public static HashMap<String, Command> commandMap = new HashMap<>();
	public static HashMap<String, String> varToPath = new HashMap<>();
	public static HashMap<String, DoubleProperty> SymbolTbl = new HashMap<>();
	public static HashMap<String, DoubleProperty> pathToValue = new HashMap<>();
	public static HashMap<String, LinkedList<String>> pathToVar = new HashMap<>();
	public static ArrayList<String> varList = new ArrayList<>();

	static {
		commandMap.put("disconnect", new DisconnectCommand());
		commandMap.put("return", new returnCommand());
		commandMap.put("=", new AssignCommand());
		commandMap.put("openDataServer", new OpenServerCommand());
		commandMap.put("while", new LoopCommand());
		commandMap.put("sleep", new SleepCommand());
		commandMap.put("var", new VarCommand());
		commandMap.put("connect", new ConnectCommand());
		commandMap.put("print", new PrintCommand());

		
		try {
			BufferedReader br = new BufferedReader(new FileReader("./resources/var_Names.txt"));
			String line,name,path;
			String[] nameNpath;
			while((line = br.readLine()) != null){
				nameNpath = line.split(",");
				// NAME , PATH
				name = nameNpath[0];
				path = nameNpath[1];
				SymbolTbl.put(name, new SimpleDoubleProperty(0.0));
				pathToValue.put(path, new SimpleDoubleProperty(0.0));
				SymbolTbl.get(name).bindBidirectional(pathToValue.get(path));
				varToPath.put(name, path);
				varList.add(name);

			}
			br.close();
		} catch (IOException e) {e.printStackTrace();}
	}

	public static double interpret(String[] lines) {
		return parser(lexer(lines));

	}

	
	public static ArrayList<String> lexer(String[] code) {
		ArrayList<String> arrayCode = new ArrayList<String>();
		String[] tmp;
		String[] tmp1;

		for (String cell : code) {
			tmp = cell.split("[ ]+");
			
			for(String s :tmp) {
				tmp1=s.split("(?<=[-+*/()=])|(?=[-+*/()=])");
				for (int i = 0; i < tmp1.length; i++) {
					tmp1[i]= tmp1[i].trim();
					arrayCode.add(tmp1[i]);
					
				}
			}
			arrayCode.add(";");
		}

		return arrayCode;
	}

	public static double parser(ArrayList<String> cli) {
		double statusCode = 0;
		String word;
		String[] args;
		int i = 0;

		while (i < cli.size() - 1) {
			if (Thread.currentThread().isInterrupted()) 
				return 0;
			word = cli.get(i);				
			if (commandMap.containsKey(word)) {
				args = extractCommands(i, cli);
				i = Integer.parseInt(args[args.length - 1]);
				args[args.length - 1] = ";";
				statusCode = commandMap.get(word).doCommand(args);
			}
			i++;
		}
		return statusCode;
	}

	public static String[] extractCommands(int index, ArrayList<String> rawCode) {
		int i = index + 1;
		ArrayList<String> command = new ArrayList<String>();

		switch (rawCode.get(index)) {
		case "print":
			while (!rawCode.get(i).equals(";")) {
				command.add(rawCode.get(i++));
			}
			break;

		case "=":
			command.add(rawCode.get(index - 1));
			while (!rawCode.get(i).equals(";"))
				command.add(rawCode.get(i++));
			break;

		case "disconnect":
			break;

		case "var":
			command.add(rawCode.get(i));
			break;

		case "while":
			while (!rawCode.get(i).equals("}"))
				command.add(rawCode.get(i++));
			command.add(rawCode.get(i));
			break;

		default:
			while (!rawCode.get(i).equals(";")) {
				command.add(rawCode.get(i));
				i++;
			}

		}

		
		command.add(i + "");
		String[] cmd = new String[command.size()];
		command.toArray(cmd);

		return cmd;
	}

}
