/*
	DELTA Forth - The Forth Compiler
	(C) Valer BOCAN  (vbocan@dataman.ro)
	
	Date of creation:       Wednesday,  September 24, 1997
	Date of last update:    Tuesday,    January   25, 2000
	
	Description: Main compiler class
*/

public class DeltaForth {
	
	static TCompiler comp;
	
	public static void main(String argv[]) {
		String OutFile = null;
		
		System.out.println();
		System.out.println("DELTA Forth Compiler     (C)1997-2000 Valer BOCAN <vbocan@dataman.ro>");
		System.out.println("  Version 1.0            All Rights Reserved");
		System.out.println("  http://www.dataman.ro/dforth");
		System.out.println();
		if(argv.length < 1) {
			System.out.println("Usage:");
			System.out.println("   java DeltaForth <input_file.4th> [<output_file.out>]");
			System.out.println();
			System.out.println("   where:");
			System.out.println("     - input_file.4th  : DELTA Forth source file.");
			System.out.println("     - output_file.out : P-code file.");
			System.exit(2);
		}
		
		// Parse parameters
		if(argv.length <= 1)	OutFile = new String(argv[0] + ".out");
									   else OutFile = argv[1];
			
		try {
			comp = new TCompiler(argv[0], OutFile);
			comp.Parse();
		} catch(TForthException ex) {
			System.out.println("ERROR: " + ex.getMessage());
			System.exit(1);
		}
	}
}