/*
	DELTA Forth - The Forth Interpreter
	(C) Valer BOCAN  (vbocan@dataman.ro)
	
	Date of creation:       Wednesday,  November 12, 1997
	Date of last update:    Tuesday,    January  25, 2000
	
	Description: The DELTA Forth Interpreter
*/

public class Interpret
{
	static public void main(String argv[]) throws TForthException {
		int Space = 1024;
		
		TForthEngine inter;
		
		System.out.println();
		System.out.println("DELTA Forth Interpreter     (C)1997-2000 Valer BOCAN <vbocan@dataman.ro>");
		System.out.println("  Version 1.0               All Rights Reserved");
		System.out.println("  http://www.dataman.ro/dforth");
		System.out.println();
		if(argv.length < 1) {
			System.out.println("Usage:");
			System.out.println("   java Interpret <code.out> [/M:<space>]");
			System.out.println();
			System.out.println("   where:");
			System.out.println("     - code.out   : P-code to be executed.");
			System.out.println("     - /M:<space> : Addressable space in 1K cells increments. (Default 1024)");
			System.exit(2);
		}
		
		// Process parameters
		if(argv.length > 1) {	
			if(argv[1].startsWith("/M:")) {
					try {
						Space = (new Integer(argv[1].substring(3))).intValue();
					} catch(NumberFormatException ex) {
							System.out.println("ERROR: Invalid number in option /M.\n");
							System.exit(3);
					}
			} else {
				System.out.println("ERROR: Malformed /M option.\n");
				System.exit(3);
			}
		}
		
		if(Space < 256) {
			System.out.println("ERROR: DELTA Forth requires at least 256 K cells to run.\n");
			System.exit(4);
		}
		
		try {
			inter = new TForthEngine(argv[0], Space);
			inter.LaunchExecution();
		} catch(TForthException ex) {
			System.out.println("\nRUNTIME ERROR: " + ex.getMessage());
			System.exit(2);
		}
	}
}