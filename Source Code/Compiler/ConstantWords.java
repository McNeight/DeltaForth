/*
	DELTA Forth - The Forth Compiler
	(C) Valer BOCAN  (vbocan@dataman.ro)
	
	Date of creation:       Thursday,   January   15, 1998
	Date of last update:    Wednesday,  June       2, 1999
	
	Description: Table of constant words
*/

import java.util.Hashtable;

class ConstantWords
{
	private Hashtable conTable;     // Table with user defined constants
	
	// Constructor
	public ConstantWords() {
		conTable = new Hashtable();
	}
	// Adds a user defined constant word into the hashtable
	public int addConstantWord(String uword, int Value) {
		if(uword == null) return -1;
		else conTable.put(uword, new Integer(Value));
		return 0;
	}
	// Returns the value associated with the constant word (0 if no association)
	public int getConstantWord(String resword) {
		Integer tint;
		if(resword == null) return 0;
			else {
				 tint = (Integer)conTable.get(resword);
				 if(tint == null) return 0;
					else return tint.intValue();
			}
	}
	// Returns true if the atom given is a constant word
	public boolean isReserved(String resword) {
		if(resword == null) return false;
			else return conTable.containsKey(resword);
	}
	// Clear all entries
	public void clear() {
		conTable.clear();
	}
}