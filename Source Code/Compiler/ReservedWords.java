/*
	DELTA Forth - The Forth Compiler
	(C) Valer BOCAN  (vbocan@dataman.ro)
	
	Date of creation:       Wednesday,  September 24, 1997
	Date of last update:    Monday,     February  22, 1999
	
	Description: Table of reserved words
*/

import java.util.Hashtable;

class ReservedWords {
	
	private Hashtable resTable;     // Table with reserved words
	private Hashtable userTable;    // Table with user defined words
	private static int userKey;     // Count key for each user defined function
	
	// Constructor
	public ReservedWords() {
		resTable = new Hashtable();
		userTable = new Hashtable();
		userKey = 10000;    // Starting index for user defined words
		
		// Primitives
		resTable.put("C",       new Integer(100));
		resTable.put("?",       new Integer(101));
		resTable.put("!",       new Integer(102));
		resTable.put("+!",      new Integer(103));
		resTable.put("DUP",     new Integer(104));
		resTable.put("-DUP",    new Integer(105));
		resTable.put("DROP",    new Integer(106));
		resTable.put("SWAP",    new Integer(107));
		resTable.put("OVER",    new Integer(108));
		resTable.put("ROT",     new Integer(109));
		resTable.put(".",       new Integer(110));
		resTable.put("+",       new Integer(111));
		resTable.put("-",       new Integer(112));
		resTable.put("*",       new Integer(113));
		resTable.put("/",       new Integer(114));
		resTable.put(">R",      new Integer(115));
		resTable.put("R>",      new Integer(116));
		resTable.put("I",       new Integer(117));
		resTable.put("MOD",     new Integer(118));
		resTable.put("/MOD",    new Integer(119));
		resTable.put("*/",      new Integer(120));
		resTable.put("*/MOD",   new Integer(121));
		resTable.put("MINUS",   new Integer(122));
		resTable.put("ABS",     new Integer(123));
		resTable.put("MIN",     new Integer(124));
		resTable.put("MAX",     new Integer(125));
		resTable.put("1+",      new Integer(126));
		resTable.put("2+",      new Integer(127));
		resTable.put("0=",      new Integer(128));
		resTable.put("0<",      new Integer(129));
		resTable.put("=",       new Integer(130));
		resTable.put("<",       new Integer(131));
		resTable.put(">",       new Integer(132));
		resTable.put("<>",      new Integer(133));
		resTable.put("AND",     new Integer(134));
		resTable.put("OR",      new Integer(135));
		resTable.put("XOR",     new Integer(136));
		resTable.put("EMIT",    new Integer(137));
		resTable.put("CR",      new Integer(138));
		resTable.put("SPACE",   new Integer(139));
		resTable.put("SPACES",  new Integer(140));        
		resTable.put("TYPE",    new Integer(141));
		resTable.put("FILL",    new Integer(142));
		resTable.put("ERASE",   new Integer(143));
		resTable.put("BLANKS",  new Integer(144));
		resTable.put("CMOVE",   new Integer(145));
		resTable.put("KEY",     new Integer(146));
		resTable.put("EXPECT",  new Integer(147));
		resTable.put("?TERMINAL",new Integer(148));
		resTable.put("PAD",     new Integer(149));
		resTable.put("S0",      new Integer(150));
		resTable.put("R0",      new Integer(151));
		resTable.put("STATE",   new Integer(152));
		resTable.put("BASE",    new Integer(153));
		resTable.put("SP@",     new Integer(154));
		resTable.put("SP!",     new Integer(155));
		resTable.put("RP@",     new Integer(156));
		resTable.put("RP!",     new Integer(157));
		resTable.put("TIB",     new Integer(158));
		resTable.put("QUERY",   new Integer(159));
		resTable.put("INTERPRET",new Integer(160));
	}
	// Adds a user defined word into the hashtable
	public int addUserWord(String uword) {
		if(uword == null) return -1;
		else {
			userTable.put(uword, new Integer(userKey));
			return userKey++;
		}
	}
	// Returns the key associated with the user word (0 if no association)
	public int getUserWordKey(String uword) {
		Integer tint;
		if(uword == null) return 0;
			else {
				 tint = (Integer)userTable.get(uword);
				 if(tint == null) return 0;
					else return tint.intValue();
			}
	}
	// Returns the key associated with the reserved word (0 if no association)
	public int getWordKey(String resword) {
		Integer tint;
		if(resword == null) return 0;
			else {
				 tint = (Integer)resTable.get(resword);
				 if(tint == null) return 0;
					else return tint.intValue();
			}
	}
	// Returns true if the atom given is a reserved word
	public boolean isReserved(String resword) {
		if(resword == null) return false;
			else return resTable.containsKey(resword);
	}
	// Returns true if the atom given is a user word
	public boolean isUserDefined(String resword) {
		if(resword == null) return false;
			else return userTable.containsKey(resword);
	}
}