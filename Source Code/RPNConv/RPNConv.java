/*
    Algebraic to RPN converter
    (C) Valer BOCAN  (vbocan@dataman.ro)
    
    Date of creation:       Thursday,  June     3, 1999
    Date of last update:    Tuesday,   January 25, 2000
*/

public class RPNConv {
	
	public static void main(String argv[]) {
        System.out.println();
        System.out.println("Algebraic to RPN / Forth Converter   v1.0");
        System.out.println("  (C)1999 Valer BOCAN <vbocan@dataman.ro> <http://www.dataman.ro>");
        System.out.println();
        
        if(argv.length < 1) {
            System.out.println("Usage:");
            System.out.println("    java RPNConv \"Expression in Algebraic Form\" [\"Initial Stack Layout\"]");
            System.out.println("Example:");	
            System.out.println("    java RPNConv \"(A+B)*(A-B)\" \"AB\"");
            System.exit(1);
        }

	    RPNParser parser = new RPNParser();
        String rpn = null;
        try {
            rpn = parser.Parse(argv[0]);
        } catch(Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
            System.exit(2);
        }
        System.out.println("--------------------------------------------------");
        System.out.println("Algebraic form: " + argv[0]);
        System.out.println("RPN form:       " + rpn);
        System.out.println("--------------------------------------------------");
        System.out.println("Equivalent Forth words:");
        
        if(argv.length < 2) {
            System.out.println("No initial stack specified.");
            System.exit(3);
        }
        
        String Forth = new String();
        RPNProc rpnp = new RPNProc();
        try {
            Forth = rpnp.Process(rpn, argv[1]);
        } catch(Exception ex) {
            System.out.println("Could not resolve expression. Try changing parameters.");
            System.exit(4);
        }
        if(Forth.indexOf("^") != -1) {
            System.out.println("Forth does not support the power operator.");
            System.exit(5);
        }
        System.out.println(Forth);
	}
	
}