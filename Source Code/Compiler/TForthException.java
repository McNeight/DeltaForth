/*
	DELTA Forth - The Forth Compiler
	(C) Valer BOCAN  (vbocan@dataman.ro)
	
	Date of creation:       Tuesday, September 23, 1997
	Date of last update:    Friday,  February   6, 1998
	
	Description: Definition of the TForthException class
*/

public class TForthException extends Throwable {
	String ExceptionMessage;    // Exception message
	
	public TForthException() {
		ExceptionMessage = new String("Undefined exception.");
	}
	// Constructors
	public TForthException(String Message) {
		ExceptionMessage = new String(Message);
	}
	// Gets the exception message
	public String getMessage() {
		return ExceptionMessage;
	}
}