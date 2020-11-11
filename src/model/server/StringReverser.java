package model.server;

public class StringReverser implements Solver<String,String> {

	public String solve(String instring) {
		StringBuilder output_string = new StringBuilder();
		output_string.append(instring);
		output_string = output_string.reverse();

		return output_string.toString();
	}	

}
