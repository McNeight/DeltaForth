/*
	DELTA Forth - The Forth Compiler
	(C) Valer BOCAN  (vbocan@dataman.ro)
	
	Date of creation:       Tuesday,    September 23, 1997
	Date of last update:    Wednesday,  June       2, 1999
	
	Description: Definition of the TCompiler class
*/

import java.io.*;
import java.util.*;

public class TCompiler {
	
	final int CStopExec = 0;
	final int CLoad     = -1;
	final int CIf       = 2;
	final int CElse     = 3;
	final int CThen     = 4;
	final int CBin      = 5;
	final int COct      = 6;
	final int CDec      = 7;
	final int CHex      = 8;
	final int CText     = 9;
	final int CDo       = 10;
	final int CLoop     = 11;
	final int CPLoop    = 12;
	final int CLeave    = 13;
	final int CBegin    = 14;
	final int CAgain    = 15;
	final int CUntil    = 16;
	final int CWhile    = 17;
	final int CRepeat   = 18;
	final int CCase     = 19;
	final int COf       = 20;
	final int CEndof    = 21;
	final int CEndcase  = 22;
	final int CDump     = 23;	
	final int CCount    = 24;
	final int CLessSharp= 25;
	final int CSharp    = 26;
	final int CSharpS   = 27;
	final int CHold     = 28;
	final int CSign     = 29;
	final int CSharpGreater = 30;
	final int CExit	    = 31;
	final int CExternal = 99;
	
	// Internal variables
	boolean inFunction;         // Specifies whether currently parsing a function (default FALSE)
	boolean mainDefined;        // Specifies whether MAIN has already been defined (default FALSE)
	boolean inConversion;	    // Specifies whether in conversion state (between <# and #>)
	int noifs;                  // Number of IF structures (increases and decreases during compilation)
	int nodos;                  // Number of DO structures (increases and decreases during compilation)
	int nobegins;               // Number of BEGIN... structures (increases and decreases during compilation)
	int nowhiles;               // Number of WHILEs in a BEGIN... structure
	int nocases;                // Number of CASE structures
	int noofs;                  // Number of OF statements in a CASE structure
	int noendofs;               // Number of ENDOF statements in a CASE structure
	int lineNum;                // Current line in the input file (default 0)
	RandomAccessFile inFile;    // Input file
	RandomAccessFile LoadQueue[];//Array of files used while proccessing another file
	int iQueue;                 // Pointer in the stack
	TCompiledOutput outFile;    // Output file
	ReservedWords ResWord;      // Hash table containing reserved words
	ConstantWords ConWord;      // Hash table containing constant words
	ConstantWords LocalVars;    // Hash table containing local variables
	static int atomPos;         // Position in string when getting an atom
	static String atomLine;     // Line from the input file
	static int curRadix;        // Current radix
	static int noFunc;          // Number of words compiled in the code file
	static int lastNum;         // Stores the last number loaded into stack
	static int varPoint;        // Stores the address of the next variable to be allocated
				    // Increases with every VARIABLE and ALLOT statement
	
	// Dates for computing compiling time
	Date time1, time2;
	
	// Public functions
	
	// Constructor: Requires input and output filenames
	public TCompiler(String inf, String outf) throws TForthException {
		// Open input file
		try {
			inFile = new RandomAccessFile(inf, "r");
		} catch (IOException ex) {
			throw new TForthException("Could not open input file: " + inf);
		}

		// Create output file
		try {
			outFile = new TCompiledOutput(outf);
		} catch(IOException ex) {
			throw new TForthException("Could not create output file: " + outf);
		}
		// Initialize internal variables
		inFunction = inConversion = mainDefined = false;
		atomPos = -1;   // Signal first usage
		lineNum = 0;
		curRadix = 10;  // Initially radix is set to 10
		noifs = nodos = nobegins = nowhiles = nocases = noofs = noendofs = 0;
		noFunc = 0;
		ResWord = new ReservedWords();  // Instantiate new reserved words hash table
		ConWord = new ConstantWords();  // Instantiate new constant words hash table
		LocalVars = new ConstantWords();// Instantiate new local variables hash table
		LoadQueue = new RandomAccessFile[20];
		lastNum = 0;
		varPoint = 0;                   // Memory upwards available for user's variables
		iQueue = 0;                     // Initially no files are in the LOAD queue
	}
	// Checks whether an atom is literal
	private boolean checkLiteral(String inpstring) {
		int i;
		if(inpstring == null) return false;
		inpstring = inpstring.toUpperCase();
		for(i=0;i<inpstring.length();i++)
			if(inpstring.charAt(i) < 32 || inpstring.charAt(i) > 127) return false;
		return true;
	}
	// Checks whether the input string is a number
	private boolean checkNumber(String inpstring, int radix) {
		int i;
		char tc;
		if(inpstring == null) return false;
		inpstring = inpstring.toUpperCase();
		for(i=0; i<inpstring.length(); i++) {
			if(inpstring.charAt(0) == '-') continue;
			if((tc=inpstring.charAt(i)) < '0') return false;
			switch(radix) {
				case 2 : if(tc > '1') return false;
						 break;
				case 8 : if(tc > '7') return false;
						 break;
				case 10: if(tc > '9') return false;
						 break;
				case 16: if((tc > '9' && tc < 'A') || tc > 'F') return false;
						 break;
				default: return false;
			}
		}
		return true;
	}
	// Destructor
	public void finalize() {
		// Close and destroy input and output file
		try {
			inFile.close();
		} catch(IOException ex) {};
		inFile = null;
		outFile = null;
		ResWord = null;
	}
	// Get an atom from the input file
	private String getAtom() throws TForthException {
		int tPos = 0;
		do {
			if(atomPos == -1 || atomPos >= atomLine.length()-1) {
				try {
					atomLine = inFile.readLine();
					if(atomLine == null) return null;
					atomPos = 0;
					if(iQueue == 0) lineNum++;
				} catch(IOException ex) {
					return null;
				}
			}
			try {
				while(atomLine.charAt(atomPos) == ' ' ||            // avoid spaces
					  atomLine.charAt(atomPos) == 9   ||            // avoid TABs
					  atomLine.charAt(atomPos) == 10  ||            // avoid newlines
					  atomLine.charAt(atomPos) == 13) atomPos++;
			} catch(StringIndexOutOfBoundsException ex) {
				atomPos = -1;
				continue;
			}
			tPos = atomPos;
			try {
				while(atomLine.charAt(atomPos) != ' ' &&
					  atomLine.charAt(atomPos) != 9   &&
					  atomLine.charAt(atomPos) != 10  &&
					  atomLine.charAt(atomPos) != 13) atomPos++;
			} catch(StringIndexOutOfBoundsException ex) { }
			// Convert to uppercase the substring
			String tx = new String(atomLine.substring(tPos, atomPos));
			tx = tx.toUpperCase();
			return tx;
		} while(true);
	}
	
	// Gets a character from the input file
	private char getSourceChar() {
		if(atomPos != -1 && atomPos <= atomLine.length()-1) {
			 return atomLine.charAt(atomPos++);
		}
		else {      // Reload buffer if exhausted
			try {
				atomLine = inFile.readLine();
				if(atomLine.length() == 0) return 0;
				atomPos = 0;
				if(iQueue == 0) lineNum++;
				return atomLine.charAt(atomPos++);
			} catch(IOException ex) {
				return 0;
			}
		}
	}
	// Gets the key associated with the atom
	private int getWordKey(String rword) {
		return ResWord.getWordKey(rword);
	}
	// Private functions
	
	// Checks whether an atom is a reserved word
	private boolean isReserved(String rword) {
		return ResWord.isReserved(rword);
	}

	// Returns the number of occurences of a character in a string
	private int CountString(String str, int ch) {
		int i, count = 0;
		for(i=0;i<str.length();i++) if(str.charAt(i) == ch) count++;
		return count;
	}

	// Parser
	public void Parse() throws TForthException {
		String atom = new String();     // Atom read from the input file
		time1 = new Date();             // Start time
		int inComment = 0;		// Number of open parantheses		
		do {
			atom = getAtom();
			if(atom == null) {
				if(iQueue > 0) {
					inFile = LoadQueue[--iQueue];
					atomPos = -1;
					continue;
				}
				else break;
			}
			// Proccess multi-line comments
			int no1 = CountString(atom, '('); // Number of '('
			inComment += no1;
			int no2 = CountString(atom, ')'); // Number of ')'
			inComment -= no2;
			if(no2 > 0) continue;
			if(inComment > 0) continue;

			// Process single-line comments
			if(atom.charAt(0) == '\\') {
				atomPos = -1;
				continue;
			}
			//System.out.println("Processing: "+atom);
			if(isReserved(atom)) {          // Reserved word
				if(!inFunction) throw new TForthException(atom + " cannot be compiled outside a word. (Line " + lineNum + ")");
				outFile.placeWordLink(ResWord.getWordKey(atom));
			}
			else
			if(atom.equals(":")) {          // Colon
				if(inFunction) throw new TForthException("Nested words are not allowed. (Line " + lineNum + ")");
				else {
					if((atom = getAtom()) == null) throw new TForthException("Unexpected end of file.");
					if(ResWord.isReserved(atom)) throw new TForthException("Redefining reserved words is not allowed. (Line " + lineNum + ")");
					if(ResWord.isUserDefined(atom)) throw new TForthException(atom + " has already been defined. (Line " + lineNum + ")");
					if(!checkLiteral(atom)) throw new TForthException("Expecting a literal identifier. (Line " + lineNum + ")");
					inFunction = true;
					// Clear local variables names
					LocalVars.clear();
					if(atom.length() > 31) atom = atom.substring(1,31);
					if(atom.equals("MAIN")) mainDefined = true;
					int key = ResWord.addUserWord(atom);
					outFile.placeWordHeader(atom, key);
					noFunc++;
					System.out.println("Compiling word '"+atom+"'");
					}
			}                    
			else
			if(atom.equals(";")) {          // Semicolon
				if(!inFunction) throw new TForthException("The semicolon must end a word definition. (Line " + lineNum + ")");
				if(noifs > 0) throw new TForthException("IF ELSE THEN structure must be terminated before semicolon. (Line " + lineNum + ")");
				if(nodos > 0) throw new TForthException("DO LOOP/+LOOP structure must be terminated before semicolon. (Line " + lineNum + ")");
				if(nobegins > 0) throw new TForthException("BEGIN AGAIN/UNTIL/REPEAT structure must be terminated before semicolon. (Line " + lineNum + ")");
				if(nowhiles > 0) throw new TForthException("Unmatched WHILE statement. (Line " + lineNum + ")");
				if(nocases > 0) throw new TForthException("CASE ENDCASE structure must be terminated before semicolon. (Line " + lineNum + ")");
				else {
					inFunction = false;
					// Place link 0 (signal end of links)
					outFile.placeWordLink(0);
					// Clear local variables names
					LocalVars.clear();
					}    
			}
			else
			if(atom.equals("EXTERN")) {          // EXTERN
				if(inFunction) throw new TForthException("Externals must be declared outside words. (Line " + lineNum + ")");
				else {
					if((atom = getAtom()) == null) throw new TForthException("Unexpected end of file.");
					if(ResWord.isReserved(atom)) throw new TForthException("Redefining reserved words is not allowed. (Line " + lineNum + ")");
					if(ResWord.isUserDefined(atom)) throw new TForthException(atom + " has already been defined. (Line " + lineNum + ")");
					if(!checkLiteral(atom)) throw new TForthException("Expecting a literal identifier. (Line " + lineNum + ")");
					if(atom.length() > 31) atom = atom.substring(1,31);
					int key = ResWord.addUserWord(atom);
					outFile.placeWordHeader(atom, key);
					outFile.placeWordLink(CExternal);
					// Read library name
					String libname = new String();
					if((libname = getAtom()) == null) throw new TForthException("Unexpected end of file.");
					outFile.placeString(libname);
					outFile.placeWordLink(0);
					noFunc++;
					System.out.println("Compiling external word '"+atom+"'");
					}
			}                    
			else
			if(atom.equals("BIN")) {    // Radix 2
				if(!inFunction) throw new TForthException(atom + " cannot be compiled outside a word. (Line " + lineNum + ")");
				curRadix = 2;
				outFile.placeWordLink(CBin);   // Place BIN key (5)
			}
			else
			if(atom.equals("OCT")) {    // Radix 8
				if(!inFunction) throw new TForthException(atom + " cannot be compiled outside a word. (Line " + lineNum + ")");
				curRadix = 8;
				outFile.placeWordLink(COct);   // Place OCT key (6)
			}
			else
			if(atom.equals("DEC")) {    // Radix 10
				if(!inFunction) throw new TForthException(atom + " cannot be compiled outside a word. (Line " + lineNum + ")");
				curRadix = 10;
				outFile.placeWordLink(CDec);   // Place DEC key (7)
			}
			else
			if(atom.equals("HEX")) {    // Radix 16
				if(!inFunction) throw new TForthException(atom + " cannot be compiled outside a word. (Line " + lineNum + ")");
				curRadix = 16;
				outFile.placeWordLink(CHex);   // Place HEX key (8)
			}
			else
			if(atom.equals("IF")) {     // IF statement
				if(!inFunction) throw new TForthException("IF must be placed inside a word. (Line " + lineNum + ")");
				noifs++;                // increment IF usage
				int wordLink = noifs << 16;
				wordLink += CIf;
				outFile.placeWordLink(wordLink);   // Place IF key (2)
			}
			else
			if(atom.equals("ELSE")) {   // ELSE statement
				if(!inFunction) throw new TForthException("ELSE must be placed inside a word. (Line " + lineNum + ")");
				if(noifs == 0) throw new TForthException("ELSE without IF. (Line " + lineNum + ")");
				int wordLink = noifs << 16;
				wordLink += CElse;
				outFile.placeWordLink(wordLink);   // Place ELSE key (3)
			}
			else
			if(atom.equals("THEN")) {   // THEN statement
				if(!inFunction) throw new TForthException("THEN must be placed inside a word. (Line " + lineNum + ")");
				if(noifs == 0) throw new TForthException("THEN without IF. (Line " + lineNum + ")");
				int wordLink = noifs << 16;
				wordLink += CThen;
				outFile.placeWordLink(wordLink);   // Place THEN key (4)
				noifs--;                    // decrement IF usage
			}
			else
			if(atom.equals("DO")) {     // DO statement
				if(!inFunction) throw new TForthException("DO must be placed inside a word. (Line " + lineNum + ")");
				nodos++;                // increment DO usage
				int wordLink = nodos << 16;
				wordLink += CDo;
				outFile.placeWordLink(wordLink);   // Place DO key (10)
			}
			else
			if(atom.equals("LOOP")) {   // LOOP statement
				if(!inFunction) throw new TForthException("LOOP must be placed inside a word. (Line " + lineNum + ")");
				if(nodos == 0) throw new TForthException("LOOP without DO. (Line " + lineNum + ")");
				int wordLink = nodos << 16;
				wordLink += CLoop;
				outFile.placeWordLink(wordLink);   // Place LOOP key (11)
				nodos--;                    // decrement DO usage
			}
			else
			if(atom.equals("+LOOP")) {   // +LOOP statement
				if(!inFunction) throw new TForthException("+LOOP must be placed inside a word. (Line " + lineNum + ")");
				if(nodos == 0) throw new TForthException("+LOOP without DO. (Line " + lineNum + ")");
				int wordLink = nodos << 16;
				wordLink += CPLoop;
				outFile.placeWordLink(wordLink);   // Place +LOOP key (12)
				nodos--;                    // decrement DO usage
			}
			else
			if(atom.equals("LEAVE")) {  // LEAVE statement
				if(!inFunction) throw new TForthException("LEAVE must be placed inside a word. (Line " + lineNum + ")");
				if(nodos == 0) throw new TForthException("LEAVE must be placed inside a DO LOOP/+LOOP structure. (Line " + lineNum + ")");
				outFile.placeWordLink(CLeave);
			}
			else
			if(atom.equals("BEGIN")) {     // BEGIN statement
				if(!inFunction) throw new TForthException("BEGIN must be placed inside a word. (Line " + lineNum + ")");
				nobegins++;                // increment BEGIN usage
				int wordLink = nobegins << 16;
				wordLink += CBegin;
				outFile.placeWordLink(wordLink);   // Place BEGIN key (14)
			}
			else
			if(atom.equals("AGAIN")) {   // AGAIN statement
				if(!inFunction) throw new TForthException("AGAIN must be placed inside a word. (Line " + lineNum + ")");
				if(nobegins == 0) throw new TForthException("AGAIN without BEGIN. (Line " + lineNum + ")");
				int wordLink = nobegins << 16;
				wordLink += CAgain;
				outFile.placeWordLink(wordLink);   // Place AGAIN key (15)
				nobegins--;                    // decrement BEGIN usage
			}
			else
			if(atom.equals("UNTIL")) {   // UNTIL statement
				if(!inFunction) throw new TForthException("UNTIL must be placed inside a word. (Line " + lineNum + ")");
				if(nobegins == 0) throw new TForthException("UNTIL without BEGIN. (Line " + lineNum + ")");
				int wordLink = nobegins << 16;
				wordLink += CUntil;
				outFile.placeWordLink(wordLink);   // Place UNTIL key (16)
				nobegins--;                    // decrement BEGIN usage
			}
			else
			if(atom.equals("WHILE")) {  // WHILE statement
				if(!inFunction) throw new TForthException("WHILE must be placed inside a word. (Line " + lineNum + ")");
				if(nobegins == 0) throw new TForthException("WHILE must be placed inside a BEGIN/REPEAT structure. (Line " + lineNum + ")");
				nowhiles++;
				int wordLink = nobegins << 16;
				wordLink += CWhile;
				outFile.placeWordLink(wordLink);
			}
			else
			if(atom.equals("REPEAT")) {   // REPEAT statement
				if(!inFunction) throw new TForthException("REPEAT must be placed inside a word. (Line " + lineNum + ")");
				if(nobegins == 0) throw new TForthException("REPEAT without BEGIN. (Line " + lineNum + ")");
				if(nowhiles == 0) throw new TForthException("REPEAT without WHILE. (Line " + lineNum + ")");
				int wordLink = nobegins << 16;
				wordLink += CRepeat;
				outFile.placeWordLink(wordLink);   // Place REPEAT key (17)
				nobegins--;                        // decrement BEGIN usage
				nowhiles--;                        // decrement WHILE usage
				}
			else
			if(atom.equals("CASE")) {   // CASE statement
				if(!inFunction) throw new TForthException("CASE must be placed inside a word. (Line " + lineNum + ")");
				nocases++;
				int wordLink = nocases << 16;
				wordLink += CCase;
				outFile.placeWordLink(wordLink);   // Place CASE key (19)
				}
			else
			if(atom.equals("OF")) {   // OF statement
				if(!inFunction) throw new TForthException("OF must be placed inside a word. (Line " + lineNum + ")");
				if(nocases == 0) throw new TForthException("OF without CASE. (Line " + lineNum + ")");
				noofs++;
				int wordLink = nocases << 16;
				wordLink += COf;
				outFile.placeWordLink(wordLink);   // Place OF key (20)
				}
			else
			if(atom.equals("ENDOF")) {   // ENDOF statement
				if(!inFunction) throw new TForthException("ENDOF must be placed inside a word. (Line " + lineNum + ")");
				if(nocases == 0) throw new TForthException("ENDOF without CASE. (Line " + lineNum + ")");
				if(noofs <= noendofs) throw new TForthException("ENDOF must have a matching OF. (Line " + lineNum + ")");
				noendofs++;
				int wordLink = nocases << 16;
				wordLink += CEndof;
				outFile.placeWordLink(wordLink);   // Place ENDOF key (21)
				}
			else
			if(atom.equals("LOAD")) {   // LOAD statement
				if(inFunction) throw new TForthException("LOAD directive must be placed outside words. (Line " + lineNum + ")");
				if((atom = getAtom()) == null) throw new TForthException("Unexpected end of file.");
				if(iQueue >= LoadQueue.length) throw new TForthException("LOAD nesting level exceeded. (Line " + lineNum + ")");
				LoadQueue[iQueue++] = inFile;
				try {
					inFile = new RandomAccessFile(atom, "r");
				}catch(IOException e) {
					throw new TForthException("Cannot open file " + atom);    
				}
			}
			else
			
			if(atom.equals("ENDCASE")) {   // ENDCASE statement
				if(!inFunction) throw new TForthException("ENDCASE must be placed inside a word. (Line " + lineNum + ")");
				if(nocases == 0) throw new TForthException("ENDCASE without CASE. (Line " + lineNum + ")");
				int wordLink = nocases << 16;
				wordLink += CEndcase;
				outFile.placeWordLink(wordLink);   // Place ENDCASE key (22)
				nocases--;
				}
			else
			
			if(atom.equals(".\"")) {    // ."
				if(!inFunction) throw new TForthException(".\" must be placed inside a word. (Line " + lineNum + ")");
				outFile.placeWordLink(CText);   // Place PRINT key (9)
				char c;
				do {
					c = getSourceChar();
					outFile.placeWordLink(c);
				}while(c!='\"');
			} else
			if(atom.equals("DUMP")) {    // DUMP
				if(!inFunction) throw new TForthException("DUMP must be placed inside a word. (Line " + lineNum + ")");
				outFile.placeWordLink(CDump);   // Place DUMP key (23)
				char c;
				while((c=getSourceChar()) != '\"');
				do {
					outFile.placeWordLink(c);
					c = getSourceChar();
				}while(c!='\"');
				outFile.placeWordLink(c);
			} else
			if(atom.equals("COUNT")) {    // COUNT
				if(!inFunction) throw new TForthException("COUNT must be placed inside a word. (Line " + lineNum + ")");
				outFile.placeWordLink(CCount);   // Place COUNT key (24)
			} else
			if(atom.equals("EXIT")) {    // EXIT
				if(!inFunction) throw new TForthException("EXIT must be placed inside a word. (Line " + lineNum + ")");
				outFile.placeWordLink(CExit);   // Place EXIT key (31)
			} else
			if(atom.equals("CONSTANT")) {
				if(inFunction) throw new TForthException("Constants cannot be defined inside words. (Line " + lineNum + ")");
				else {
					if((atom = getAtom()) == null) throw new TForthException("Unexpected end of file.");
					if(ResWord.isReserved(atom) || ResWord.isUserDefined(atom)) throw new TForthException("Constants cannot replace reserved words. (Line " + lineNum + ")");
					if(!checkLiteral(atom)) throw new TForthException("Expecting a literal identifier. (Line " + lineNum + ")");
					ConWord.addConstantWord(atom, lastNum);
					//System.out.println("DEBUG: Adding constant "+atom+" (Value:"+lastNum+")");
					}
			}
			else
			if(atom.equals("LOCAL")) {
				if(!inFunction) throw new TForthException("Local variables cannot be defined outside words. Use VARIABLE. (Line " + lineNum + ")");
				else {
					if((atom = getAtom()) == null) throw new TForthException("Unexpected end of file.");
					if(ResWord.isReserved(atom) || ResWord.isUserDefined(atom)) throw new TForthException("Variables cannot redefine reserved words. (Line " + lineNum + ")");
					if(!checkLiteral(atom)) throw new TForthException("Expecting a literal identifier. (Line " + lineNum + ")");
					LocalVars.addConstantWord(atom, varPoint);
					//System.out.println("DEBUG: Adding local variable "+atom+" (Address:"+varPoint+")");
					varPoint++;
					}
			}
			else
			if(atom.equals("VARIABLE")) {
				if(inFunction) throw new TForthException("Global variables cannot be defined inside words. Use LOCAL. (Line " + lineNum + ")");
				else {
					if((atom = getAtom()) == null) throw new TForthException("Unexpected end of file.");
					if(ResWord.isReserved(atom) || ResWord.isUserDefined(atom)) throw new TForthException("Variables cannot redefine reserved words. (Line " + lineNum + ")");
					if(!checkLiteral(atom)) throw new TForthException("Expecting a literal identifier. (Line " + lineNum + ")");
					ConWord.addConstantWord(atom, varPoint);
					//System.out.println("DEBUG: Adding variable "+atom+" (Address:"+varPoint+")");
					varPoint++;
					}
			}
			else
			if(atom.equals("ALLOT")) {
				if(inFunction) throw new TForthException("Cannot reserve space inside words. (Line " + lineNum + ")");
				else {
					//System.out.println("DEBUG: Reserving space (Amount:"+lastNum+" cells)");
					varPoint += lastNum;
					}
			}
			else
			if(atom.equals("<#")) {
				if(!inFunction) throw new TForthException("Conversion cannot be done outside words. (Line " + lineNum + ")");
				if(inConversion) throw new TForthException("Another conversion is already in progress. (Line " + lineNum + ")");
				inConversion = true;
                                outFile.placeWordLink(CLessSharp);
			}
			else
			if(atom.equals("#")) {
				if(!inFunction) throw new TForthException("Conversion cannot be done outside words. (Line " + lineNum + ")");
				if(!inConversion) throw new TForthException("Convesion not yet started. Use <# (Line " + lineNum + ")");
				outFile.placeWordLink(CSharp);
			}
			else
			if(atom.equals("#S")) {
				if(!inFunction) throw new TForthException("Conversion cannot be done outside words. (Line " + lineNum + ")");
				if(!inConversion) throw new TForthException("Convesion not yet started. Use <# (Line " + lineNum + ")");
				outFile.placeWordLink(CSharpS);
			}
			else
			if(atom.equals("HOLD")) {
				if(!inFunction) throw new TForthException("Digit holding be done outside words. (Line " + lineNum + ")");
				if(!inConversion) throw new TForthException("Convesion not yet started. Use <# (Line " + lineNum + ")");
				outFile.placeWordLink(CHold);
			}
			else
			if(atom.equals("SIGN")) {
				if(!inFunction) throw new TForthException("Sign checking cannot be done outside words. (Line " + lineNum + ")");
				if(!inConversion) throw new TForthException("Convesion not yet started. Use <# (Line " + lineNum + ")");
				outFile.placeWordLink(CSign);
			}
			else
			if(atom.equals("#>")) {
				if(!inFunction) throw new TForthException("Conversion cannot be done outside words. (Line " + lineNum + ")");
				if(!inConversion) throw new TForthException("Convesion not yet started. Use <# (Line " + lineNum + ")");
				inConversion = false;
				outFile.placeWordLink(CSharpGreater);
			}
      else
      if(LocalVars.isReserved(atom)) {  // Local variable
				//System.out.println("DEBUG: Placing local variable "+atom);
				outFile.placeWordLink(CLoad);   // Signal number
				outFile.placeWordLink(LocalVars.getConstantWord(atom));
			}
			else
			if(ConWord.isReserved(atom)) {  // Constant word
				//System.out.println("DEBUG: Placing constant "+atom);
				outFile.placeWordLink(CLoad);   // Signal number
				outFile.placeWordLink(ConWord.getConstantWord(atom));
			}
			else
			if(ResWord.isUserDefined(atom)) {       // The atom is user defined
				if(!inFunction) throw new TForthException(atom + " cannot be compiled outside a word.");
				// Place a link to this word in the output file
				int x = ResWord.getUserWordKey(atom);
				outFile.placeWordLink(x);
				//System.out.println("DEBUG: placing key for "+atom+" "+x);
			}
			else
			if(!checkNumber(atom,curRadix)) throw new TForthException(atom + " is a nonsense in DELTA Forth. (Line " + lineNum + ")");
				else {  // The atom is a number
					if(!inFunction) {
						lastNum = Integer.parseInt(atom,curRadix);
						//System.out.println("DEBUG: Outter number " + lastNum);
					} else {
						outFile.placeWordLink(-1);   // Signal number
						outFile.placeWordLink(Integer.parseInt(atom,curRadix));
						//System.out.println("DEBUG: Adding number " + atom);
					}
				}
		} while(true);
	if(inFunction) throw new TForthException("Word definition is not terminated before end of file.");
	if(!mainDefined) throw new TForthException("MAIN word is not defined.");
	// Write out the MAIN key (used for startup)
	outFile.seek(7);
	outFile.placeWordLink(ResWord.getUserWordKey("MAIN"));
	// Write out number of functions compiled
	outFile.seek(11);
	outFile.placeWordLink(noFunc);
	time2 = new Date();
	long elapsed = (time2.getTime() - time1.getTime()) / 1000;
	System.out.println("Source successfully compiled. (" + lineNum + " lines) - " + elapsed + " secs.");
	System.out.println("Total: " + noFunc + " word(s).");
	}
}