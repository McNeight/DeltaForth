/*
	DELTA Forth - The Forth Interpreter
	(C) Valer BOCAN  (vbocan@dataman.ro)
	
	Date of creation:       Wednesday,  November  12, 1997
	Date of last update:    Tuesday,    January   25, 2000
	
	Description: The DELTA Forth Execution Engine
*/

import java.io.*;
import java.lang.reflect.*;

public class TForthEngine {

	// Constants
	int ForthMemoryLimit;										// Forth Addressable Space (memory is addressed by integer)
	
	final byte VersionMajor = 1;            // Version of the Interpreter
	final byte VersionMinor = 0;
	
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
	
	int initialSP;
	int initialRP;

	// Variables
	int Memory[];			// Forth Addressable Space
	int SP, RP;			// Parameter stack pointer, Return Stack Pointer                             
	RandomAccessFile infile;	// Input file
	int Func[][];     		// Pointer  to array of functions
	int tempFunc[];   		// Temporary array of links (when reading from file)
	int tempFuncCounter;		// Counter of links in the temporary array
	final int MaxTempFunc = 10000;  // Maximum number of links in a function
	int curBase;			// Current radix
	String funcNames[];		// Function names throughout the compiled code
	int ConvBuffer;			// Address of conversion buffer (used with <# and #>)
	int ConvIndex;			// Number of digits placed in the buffer
	boolean ConvNegative;		// TRUE if the number on the stack is negative
	
	TForthEngine(String file, int Space) throws TForthException {
		// Set-up internal constants
		ForthMemoryLimit = Space * 1024;
		initialSP = ForthMemoryLimit - 1024;
	  initialRP = ForthMemoryLimit - 1;

		try {
			infile = new RandomAccessFile(file, "r");
		} catch(IOException ex) {
			throw new TForthException("Could not open file " + file);
		}
		tempFunc = new int[MaxTempFunc];
		try {
			// Check for DELTA Forth logo
			byte temp[] = {32,32,32,32,32};
			infile.read(temp);
			if(!(new String(temp)).equals("DELTA")) throw new TForthException(file + " is not a DELTA Forth executable.");
			// Check version number
			byte localVerMajor, localVerMinor;
			localVerMajor = infile.readByte();
			localVerMinor = infile.readByte();            
			//System.out.println("Code version: " + localVerMajor + "." + localVerMinor);
			if((VersionMajor != localVerMajor) || (VersionMinor != localVerMinor))
				throw new TForthException("Version mismatch (Must be " + VersionMajor + "." + VersionMinor + ")");
			// Read number of functions in file
			int localNum;
			infile.seek(11);
			localNum = infile.readInt();
			funcNames = new String[localNum];
			//System.out.println("Number of functions in file: " + localNum);
			Func = new int[localNum][];
			// Skip to offset where function definitions start
			infile.seek(17);
			for(;;) {   
				// Skip the function name
				//infile.skipBytes(32);
				byte fname[] = new byte[31];
				infile.readFully(fname);
				infile.skipBytes(1);
				// Read the word key (10000+)
				int wordKey = infile.readInt();
				int FuncIndex = wordKey - 10000;
				funcNames[FuncIndex] = (new String(fname)).trim();
				//System.out.println("DEBUG: wordKey: " + wordKey);
				tempFuncCounter = 0;
				int Key = 0, prevKey;
				do {
					prevKey = Key;
					Key = infile.readInt();
					tempFunc[tempFuncCounter++] = Key;
				} while(!((Key == CStopExec) && (prevKey != CLoad)));
				// Copy temporary array into final one
				Func[FuncIndex] = new int[tempFuncCounter];
				for(int i=0; i < tempFuncCounter; i++) {
					Func[FuncIndex][i] = tempFunc[i];
					//System.out.println("DEBUG: Func[" + FuncIndex + "][" + i + "]=" + Func[FuncIndex][i]);
				}
			}
		} catch(IOException ex) {}
		Memory = new int[ForthMemoryLimit];
		// Set Return Stack Pointer
		RP = initialRP;
		// Set Stack Pointer
		SP = initialSP;
		// Set current radix
		curBase = 10;
	}
	// ABS (Abs)
	public void _abs() {
		PushStack(Math.abs(PopStack()));
	}
	// AND (And)
	public void _and() {
		int Value1 = PopStack();
		int Value2 = PopStack();
		PushStack(Value1 & Value2);
	}
	// BASE (Base)
	public void _base() {
		PushStack(curBase);
	}
	// BLANKS (Blanks)
	public void _blanks() {
		int Value1 = PopStack();
		int Value2 = PopStack();
		for(int i=0; i<Value2; i++) Memory[Value1+i] = 32;
	}
	// - (Change Sign)
	public void _chsign() {
		PushStack(-PopStack());
	}
	// CMOVE (memory move)
	public void _cmove() {
		int Value1 = PopStack();
		int Value2 = PopStack();
		int Value3 = PopStack();
		for(int i=0; i<Value3; i++) Memory[Value2+i] = Memory[Value1+i];             
	}
	// CR (CR)
	public void _cr() {
		System.out.println();
	}
	// -DUP (Dash Dup)
	public void _dashdup() {
		int Value = PopStack();
		PushStack(Value);
		if(Value!=0) PushStack(Value);
	}
	// . (Dot)
	public void _dot() {
		ForthOutput(PopStack());
	}
	// DROP (Drop)
	public void _drop() {
		PopStack();
	}
	// DUP (Dup)
	public void _dup() {
		int Value = PopStack();
		PushStack(Value);
		PushStack(Value);
	}
	// EMIT (Emit)
	public void _emit() {
		System.out.print((char)PopStack());
		System.out.flush();
	}
	// = (Equal)
	public void _equal() {
		int Value1 = PopStack();
		int Value2 = PopStack();
		if(Value1 == Value2) PushStack(1);
						else PushStack(0);
	}
	// ERASE (Erase)
	public void _erase() {
		int Value1 = PopStack();
		int Value2 = PopStack();
		for(int i=0; i<Value2; i++) Memory[Value1+i] = 0;
	}
	// EXPECT (Expect)
	public void _expect() throws TForthException {
		int Value1 = PopStack();
		int Value2 = PopStack();
		byte pad[] = new byte[Value1];
		try {
			System.in.read(pad, 0, Value1);
		} catch(IOException e) {
			throw new TForthException("Internal failure in word EXPECT. Please contact the author.");
		}
		for(int i=0; i<Value1; i++) Memory[Value2 + i] = pad[i];
	}

	// DELTA Forth primitives coded in Java
	// C (Fetch)
	public void _fetch() {
		PushStack(Peek(PopStack()));        
	}
	// FILL (Fill)
	public void _fill() {
		int Value1 = PopStack();
		int Value2 = PopStack();
		int Value3 = PopStack();
		for(int i=0; i<Value2; i++) Memory[Value3+i] = Value1;
	}
	// > (Greater)
	public void _greater() {
		int Value1 = PopStack();
		int Value2 = PopStack();
		if(Value2 > Value1) PushStack(1);
					   else PushStack(0);
	}
	// I (I)
	public void _I() {
		PushStack(Memory[RP+1]);
	}
	// INTERPRET (Interpret)
	public void _interpret() {
		int tib = SP - 16384;
		byte temp[] = new byte[80];
		for(int i = 0; i<80; i++) temp[i] = (byte)Memory[tib+i];
		
		String num = new String(temp);
		int len = 0;
		while(num.charAt(len) != 0) len++;
		num = num.substring(0, len-2);
		Integer value;
		
		try {
			value = new Integer(num);
			PushStack(value.intValue());
		} catch(NumberFormatException e) {
			System.out.print("WARNING: Cannot convert " + num + " to a valid number.");
			System.out.println();
			PushStack(0);
		}
	}
	// KEY (Key)
	public void _key() throws TForthException {
		try {
			PushStack(System.in.read());
		} catch(IOException e) {
			throw new TForthException("Internal failure in word KEY. Please contact the author.");
		}
	}
	// < (Less)
	public void _less() {
		int Value1 = PopStack();
		int Value2 = PopStack();
		if(Value2 < Value1) PushStack(1);
					   else PushStack(0);
	}
	// MAX (Max)
	public void _max() {
		int Value1 = PopStack();
		int Value2 = PopStack();
		PushStack(Value1 > Value2 ? Value1 : Value2);
	}
	// MIN (Min)
	public void _min() {
		int Value1 = PopStack();
		int Value2 = PopStack();
		PushStack(Value1 < Value2 ? Value1 : Value2);
	}
	// - (Minus)
	public void _minus() {
		int Value1 = PopStack();
		int Value2 = PopStack();
		PushStack(Value2-Value1);
	}
	// MOD (Mod)
	public void _mod() {
		int Value1 = PopStack();
		int Value2 = PopStack();
		PushStack(Value2%Value1);
	}
	// <> (Not Equal)
	public void _notequal() {
		int Value1 = PopStack();
		int Value2 = PopStack();
		if(Value1 != Value2) PushStack(1);
						else PushStack(0);
	}
	// 1+ (One Plus)
	public void _oneplus() {
		PushStack(PopStack()+1);
	}
	// OR (Or)
	public void _or() {
		int Value1 = PopStack();
		int Value2 = PopStack();
		PushStack(Value1 | Value2);
	}
	// OVER (Over)
	public void _over() {
		int Value1 = PopStack();
		int Value2 = PopStack();
		PushStack(Value2);
		PushStack(Value1);
		PushStack(Value2);
	}
	// PAD (Pad)
	public void _pad() {
		PushStack(SP - 8192);
	}
	// + (Plus)
	public void _plus() {
		int Value1 = PopStack();
		int Value2 = PopStack();
		PushStack(Value1+Value2);
	}
	// +! (Plus Store)
	public void _plusstore() {
		int Addr = PopStack();
		int Value = Peek(Addr);
		Poke(Addr,PopStack()+Value);
	}
	// ? (Question Mark)
	public void _qmark() {
		ForthOutput(Peek(PopStack()));
	}
	// QUERY (Query)
	public void _query() throws TForthException {
		int tib = SP - 16384;
		byte pad[] = new byte[80];
		try {
			System.in.read(pad, 0, 80);
		} catch(IOException e) {
			throw new TForthException("Internal failure in word QUERY. Please contact the author.");
		}
		for(int i=0; i<80; i++) Memory[tib + i] = pad[i];
	}
	// R0 (Return stack origin)
	public void _r0() {
		PushStack(initialRP);
	}
	// R> (R From)
	public void _Rfrom() {
		PushStack(Memory[++RP]);
	}
	// ROT (Rot)
	public void _rot() {
		int Value1 = PopStack();
		int Value2 = PopStack();
		int Value3 = PopStack();
		PushStack(Value2);
		PushStack(Value1);
		PushStack(Value3);
	}
	// RP@ (Return Stack Pointer Fetch)
	public void _rpfetch() {
		PushStack(RP);
	}
	// RP! (Return Stack Pointer Store)
	public void _rpstore() {
		RP = initialRP;
	}
	// S0 (Parameter stack origin)
	public void _s0() {
		PushStack(initialSP);
	}
	// / (Slash)
	public void _slash() {
		int Value1 = PopStack();
		int Value2 = PopStack();
		PushStack(Value2/Value1);
	}
	// /MOD (Slash Mod)
	public void _slashmod() {
		int Value1 = PopStack();
		int Value2 = PopStack();
		PushStack(Value2%Value1);
		PushStack(Value2/Value1);
	}
	// SPACE (Space)
	public void _space() {
		System.out.print(" ");
		System.out.flush();
	}
	// SPACES (Spaces)
	public void _spaces() {
		int i;
		int sp = PopStack();
		for(i=0;i<sp;i++) {
			System.out.print(" ");
			System.out.flush();
		}
	}
	// SP@ (Stack Pointer Fetch)
	public void _spfetch() {
		PushStack(SP);
	}
	// SP! (Stack Pointer Store)
	public void _spstore() {
		SP = initialSP;
	}
	// * (Star)
	public void _star() {
		int Value1 = PopStack();
		int Value2 = PopStack();
		PushStack(Value1*Value2);
	}
	// */ (Star Slash)
	public void _starslash() {
		int Value1 = PopStack();
		int Value2 = PopStack();
		int Value3 = PopStack();
		PushStack(Value3*Value2/Value1);
	}
	// */MOD (Star Slash Mod)
	public void _starslashmod() {
		int Value1 = PopStack();
		int Value2 = PopStack();
		int Value3 = PopStack();
		PushStack((Value3*Value2)%Value1);
		PushStack(Value3*Value2/Value1);
	}
	// STATE (State)
	public void _state() {
		PushStack(0);
	}
	// ! (Store)
	public void _store() {
		Poke(PopStack(),PopStack());
	}
	// SWAP (Swap)
	public void _swap() {
		int Value1 = PopStack();
		int Value2 = PopStack();
		PushStack(Value1);
		PushStack(Value2);
	}
	// ?TERMINAL (Query Terminal)
	public void _terminal() throws TForthException {
		try {
			if(System.in.available() != 0) PushStack(1);
						  else PushStack(0);
		} catch(IOException e) {
			throw new TForthException("Internal failure in word ?TERMINAL. Please contact the author.");
		}
	}
	// TIB (Terminal Input Buffer)
	public void _tib() {
		PushStack(SP - 16384);
	}
	// >R (To R)
	public void _toR() {
		Memory[RP--] = PopStack();
	}
	// 2+ (Two Plus)
	public void _twoplus() {
		PushStack(PopStack()+2);
	}
	// TYPE (Type)
	public void _type() {
		int Value1 = PopStack();
		int Value2 = PopStack();
		for(int i=0; i<Value1; i++) System.out.print((char)Memory[Value2+i]);
		System.out.flush();
	}
	// XOR (Xor)
	public void _xor() {
		int Value1 = PopStack();
		int Value2 = PopStack();
		PushStack(Value1 ^ Value2);
	}
	// 0= (Zero Equal)
	public void _zeroequal() {
		if(PopStack() == 0) PushStack(1);
					   else PushStack(0);
	}
	// 0< (Zero Less)
	public void _zeroless() {
		if(PopStack() < 0) PushStack(1);
					  else PushStack(0);
	}

	// Private Methods
		
	// This is the core of the DELTA Forth system.
	//  Input:  wordKey - the index in the Func array representing the function to execute
	//          stopKey - the code of the key marking the end of executon, usually 0. If
	// the execution is needed up to a point, for instance up to a THEN or ELSE, stopKey
	// will contain that code.
	//          wPos - starting position, usually 0. Modify this if you need to begin
	// execution from another point.
	// Output:  an integer containing the new position in execution. Normally not needed
	// when called directly except when executing pieces of code, like IF ELSE THEN.
	private int Execute(int FuncIndex, int wPos, int stopKey) throws TForthException {
		for(;;) {
			int Key = Func[FuncIndex][wPos];
			if((Key == stopKey) || (Key == 0)) return wPos;    // Return from execution
			if(Key != CLoad) Key = Key & 0x0000ffff;     
			if(Key < 100) {     // Basic words
				wPos = ExecuteBasic(Key, FuncIndex, wPos);
			}
			else
			if(Key < 10000) {   // Primitives
				wPos = ExecutePrimitive(Key, FuncIndex, wPos);
			}
			else Execute(Key - 10000, 0, stopKey);   // Recursive call           
			wPos++;
		}
	}
	// Executes basic primitives (1 - 99)
	private int ExecuteBasic(int Key, int FuncIndex, int wPos) throws TForthException {
		switch(Key) {
			// External words execution
			case CExternal:
					String libname = new String();
					wPos++;
					while(Func[FuncIndex][wPos] != 0) libname += (char)Func[FuncIndex][wPos++];
					ExecuteExternal(libname, funcNames[FuncIndex]);
					wPos--;
					break;
			// Push value into stack
			case  CLoad:
					PushStack(Func[FuncIndex][++wPos]);
					break;
			// IF-ELSE-THEN structure
			case  CIf:
					int bif = PopStack();
					int Level = Func[FuncIndex][wPos] & 0x0ffff0000;    // Level for this statement
					int tElse = Level + CElse;  // Else with current level
					int tThen = Level + CThen;  // Then with current level
					if(bif != 0) { // Execute the TRUE variant
						wPos++;
						int tempPos = wPos;
						int tx;
						for(;;) {
							tx = Func[FuncIndex][tempPos++];
							if(tx == tElse || tx == tThen) break;
						}
						wPos = Execute(FuncIndex, wPos, tx);   // Execute up to Else or Then
						if(Func[FuncIndex][wPos] == CStopExec) return wPos-1;	// Return immediately (EXIT word encountered)
						if(tx == tElse) {   // We must skip the false variant
							while(Func[FuncIndex][++wPos] != tThen);
						} 
					}
					else {  // Execute the FALSE variant
						// We must skip the true variant
						int tx;
						for(;;) {
							tx = Func[FuncIndex][++wPos];
							if(tx == tElse || tx == tThen) break;
						}
						if(tx == tElse) {
							wPos = Execute(FuncIndex, wPos+1, tThen);   // Execute up to Then
							if(Func[FuncIndex][wPos] == CStopExec) return wPos-1;	// Return immediately (EXIT word encountered)
						}
					}
					break;
			case CElse: // In case of multiple ELSEs, they are ignored
					break;
			// BIN
			case CBin:
					curBase = 2;
					break;
			// OCT
			case COct:
					curBase = 8;
					break;
			// DEC
			case CDec:
					curBase = 10;
					break;
			// HEX
			case CHex:
					curBase = 16;
					break;
			// EXIT
			case CExit:
					while(Func[FuncIndex][++wPos] != CStopExec);
					wPos--;
					break;
			// ."
			case CText:
					String ts = new String();
					char c;
					++wPos;
					do {
						c = (char)Func[FuncIndex][++wPos];
						if(c!='\"') ts += c;
					}while(c!='\"');
					System.out.print(ts);
					System.out.flush();
					break;
			// DUMP
			case CDump:
					char d;
					++wPos;
					int Address = PopStack();
					do {
						d = (char)Func[FuncIndex][++wPos];
						if(d!='\"') Poke(Address++, (int)d);
					}while(d!='\"');
					Poke(Address, (int)0); // Place \0 as a string terminator
					break;
			// COUNT
			case CCount:
					int Len = 0, Addr = PopStack();
					while(Memory[Addr++] != 0) Len++;
					PushStack(Len);
					break;
			// <#
			case CLessSharp:
					// Set the buffer where digits will be stored
					_tib();
					ConvBuffer = PopStack() + 64;
					ConvIndex = 0;
					int stval = PopStack();
					ConvNegative = (stval < 0) ? true : false;
					PushStack(Math.abs(stval));
					break;
			// #
			case CSharp:
					// Convert a digit
					int val = PopStack();
					Memory[ConvBuffer-(ConvIndex++)] = val%curBase + '0';
					val /= curBase;
					PushStack(val);
					
					break;
			// #S
			case CSharpS:
					// Convert entire number
					int num = PopStack();
					do {
						Memory[ConvBuffer-(ConvIndex++)] = num%curBase + '0';
						num /= curBase;
					} while(num > 0);
					PushStack(num);
					break;
			// HOLD
			case CHold:
					// Put in the buffer the ASCII code from the stack
					Memory[ConvBuffer-(ConvIndex++)] = PopStack();
					break;
			// SIGN
			case CSign:
					// Place "-" in the buffer if the number is negative
					if(ConvNegative) Memory[ConvBuffer-(ConvIndex++)] = '-';
					break;
			// #>
			case CSharpGreater:
					// Replace value with address and length of buffer
					PopStack();
					PushStack(ConvBuffer-ConvIndex+1);
					PushStack(ConvIndex);
					break;
			// DO LOOP/+LOOP structure
			case CDo:
					int DoLevel = Func[FuncIndex][wPos] & 0x0ffff0000;    // Level for this statement
					int tLoop = DoLevel + CLoop;    // LOOP with current level
					int tPLoop = DoLevel + CPLoop;  // +LOOP with current level                  
					_swap();
					_toR();
					_toR();
					wPos++;
					int tempPos = wPos;
					int tx;
					for(;;) {
						tx = Func[FuncIndex][tempPos++];
						if(tx == tLoop || tx == tPLoop) break;
						}
					int afterPos;   // Position after executing the Loop
					for(;;) {
						afterPos = Execute(FuncIndex, wPos, tx);   // Execute up to LOOP or +LOOP
						if(Func[FuncIndex][afterPos] == CStopExec) return afterPos-1;	// Return immediately (EXIT word encountered)
						if(tx == tLoop) {
							Memory[RP+1] += 1;
						}
						else {
							Memory[RP+1] += PopStack();
						}
						if(Memory[RP+1] >= Memory[RP+2]) break;
					};
					wPos = afterPos;
					RP += 2;        // Clear return stack
					break;
			// LEAVE (from DO LOOP/+LOOP)
			case CLeave:
					Memory[RP+1] = Memory[RP+2];
					break;
			// BEGIN AGAIN/UNTIL/REPEAT
			case CBegin:
					int BeginLevel = Func[FuncIndex][wPos] & 0x0ffff0000;    // Level for this statement
					int tAgain = BeginLevel + CAgain;    // AGAIN with current level
					int tUntil = BeginLevel + CUntil;    // UNTIL with current level
					int tWhile = BeginLevel + CWhile;    // WHILE with current level
					int tRepeat = BeginLevel + CRepeat;  // REPEAT with current level
					
					wPos++;
					int beginPos = wPos;
					int txx;
					for(;;) {
						txx = Func[FuncIndex][beginPos++];
						if(txx == tAgain || txx == tUntil || txx == tWhile) break;
					}
					int aftPos;
					for(;;) {
						aftPos = Execute(FuncIndex, wPos, txx);   // Execute up to AGAIN, UNTIL or WHILE
						if(Func[FuncIndex][aftPos] == CStopExec) return aftPos-1;	// Return immediately (EXIT word encountered)
						if(txx == tUntil)                // UNTIL
							if(PopStack() > 0) break;
						if(txx == tWhile)                // WHILE
							if(PopStack() > 0) {
								aftPos = Execute(FuncIndex, aftPos+1, tRepeat);
								if(Func[FuncIndex][aftPos] == CStopExec) return aftPos-1;	// Return immediately (EXIT word encountered)
							}
							else {
								while(Func[FuncIndex][aftPos++] != tRepeat);
								aftPos--;
								break;
							}
					}
					wPos = aftPos;
					break;
			// CASE ENDCASE
			case CCase:
					int CaseLevel = Func[FuncIndex][wPos] & 0x0ffff0000;    // Level for this statement
					int tOf = CaseLevel + COf;          // OF with current level
					int tEndof = CaseLevel + CEndof;    // ENDOF with current level
					int tEndcase = CaseLevel + CEndcase;// ENCASE with current level
					wPos++;
					int bcase = PopStack();                    
					for(;;) {
						int casePos = wPos;
						int ty;
						for(;;) {
							ty = Func[FuncIndex][casePos++];
							if(ty == tOf || ty == tEndcase) break;
						}
						if(ty == tOf) {
							wPos = Execute(FuncIndex, wPos, tOf);
							if(Func[FuncIndex][wPos] == CStopExec) return wPos-1;	// Return immediately (EXIT word encountered)
							int bof = PopStack();
							if(bcase == bof) {
								wPos = Execute(FuncIndex, wPos+1, tEndof);
								if(Func[FuncIndex][wPos] == CStopExec) return wPos-1;	// Return immediately (EXIT word encountered)
								while(Func[FuncIndex][wPos++] != tEndcase);
								wPos--;
								}
								else while(Func[FuncIndex][wPos++] != tEndof);
						} else {
							wPos = Execute(FuncIndex, wPos, tEndcase);
							if(Func[FuncIndex][wPos] == CStopExec) return wPos-1;	// Return immediately (EXIT word encountered)
						}
						if(Func[FuncIndex][wPos] == tEndcase) break;
					};
					break;
					
			default: throw new TForthException("Invalid opcode encountered.");
		}
		return wPos;
	}
	// Executes primitives (100 - 9999)
	private int ExecutePrimitive(int Key, int FuncIndex, int wPos) throws TForthException {
		switch(Key) {
			
			case 100: _fetch();
					break;
			case 101: _qmark();
					break;
			case 102: _store();
					break;  
			case 103: _plusstore();
					break;
			case 104: _dup();
					break;
			case 105: _dashdup();
					break;
			case 106: _drop();
					break;
			case 107: _swap();
					break;
			case 108: _over();
					break;
			case 109: _rot();
					break;
			case 110: _dot();
					break;
			case 111: _plus();
					break;
			case 112: _minus();
					break;
			case 113: _star();
					break;
			case 114: _slash();
					break;
			case 115: _toR();
					break;
			case 116: _Rfrom();
					break;
			case 117: _I();
					break;
			case 118: _mod();
					break;
			case 119: _slashmod();
					break;
			case 120: _starslash();
					break;
			case 121: _starslashmod();
					break;
			case 122: _chsign();
					break;
			case 123: _abs();
					break;
			case 124: _min();
					break;
			case 125: _max();
					break;
			case 126: _oneplus();
					break;
			case 127: _twoplus();
					break;
			case 128: _zeroequal();
					break;      
			case 129: _zeroless();
					break;
			case 130: _equal();
					break;
			case 131: _less();
					break;
			case 132: _greater();
					break;
			case 133: _notequal();
					break;
			case 134: _and();
					break;
			case 135: _or();
					break;
			case 136: _xor();
					break;
			case 137: _emit();
					break;
			case 138: _cr();
					break;
			case 139: _space();
					break;
			case 140: _spaces();
					break;
			case 141: _type();
					break;
			case 142: _fill();
					break;
			case 143: _erase();
					break;
			case 144: _blanks();
					break;
			case 145: _cmove();
					break;
			case 146: _key();
					break;
			case 147: _expect();
					break;
			case 148: _terminal();
					break;
			case 149: _pad();
					break;
			case 150: _s0();
					break;
			case 151: _r0();
					break;
			case 152: _state();
					break;
			case 153: _base();
					break;
			case 154: _spfetch();
					break;
			case 155: _spstore();
					break;
			case 156: _rpfetch();
					break;
			case 157: _rpstore();
					break;
			case 158: _tib();
					break;
			case 159: _query();
					break;
			case 160: _interpret();
					break;
			default: throw new TForthException("Invalid opcode encountered.");
		}
		return wPos;
	}
	protected void finalize() {
		Memory = null;
		Func = null;
		tempFunc = null;
		try {
			infile.close();
		} catch(IOException ex){}
	}
	// Display function for Forth output
	private void ForthOutput(int Value) {
		int Rem[] = new int[256];   // Here we store the remainders
		int iRem=0;                 // Pointer in Rem array
		String str = new String();
		if(curBase == 10) {
			System.out.print(Value);
			System.out.flush();
			return;
		}
		do {
			Rem[iRem++] = Value % curBase;
			Value = Value / curBase;
		} while(Value >= curBase);
		Rem[iRem] = Value;
		for(;iRem>=0;iRem--) {
			if(Rem[iRem] < 10) str+=Rem[iRem];
				      else str+=(char)(Rem[iRem]+55);
		}
		System.out.print(str);
		System.out.flush();
	}
	// Launches execution (calls Execute with the wordkey for MAIN)
	public void LaunchExecution() throws TForthException {
		// Read wordkey for MAIN
		int MainKey;
		try {
			infile.seek(7);
			MainKey = infile.readInt();
		} catch(IOException ex) {
			throw new TForthException("File cannot be executed.");
		}
		//System.out.println("DEBUG: MAIN is "+MainKey);
		Execute(MainKey - 10000, 0, CStopExec);
	}
	// Executes an external function from a Java library
	private void ExecuteExternal(String libname, String funcname) throws TForthException {
	    try {
		Class c;
		c = Class.forName(libname);
		Class[] paramtype = new Class[2];
		paramtype[0] = int[].class;
		paramtype[1] = int[].class;
	
		int P[] = new int[2];
		P[0] = SP;
		P[1] = RP;
		Object[] args = new Object[2];
        	args[0] = Memory;	// Pass addressable space as the first parameter
		args[1] = P;		// Pass SP and RP as the second parameter

		Method m = c.getMethod(funcname,paramtype);
		Object o = c.newInstance();
		m.invoke(o,args);
		// Store the stack pointers to reflect changes
		SP = P[0];
		RP = P[1];
            }
		catch(NoSuchMethodException ex) {
			throw new TForthException("No such function: '"+funcname+"'.");
		}
		catch(ClassNotFoundException ex) {
			throw new TForthException("No such library: '"+libname+"'.");
		}
		catch(IllegalAccessException ex) {
			throw new TForthException("No access rights calling function '"+funcname+"' in library '"+libname+"'.");
		}
		catch(InvocationTargetException ex) {
			throw new TForthException("Function '"+funcname+"' has thrown an exception.");
		}
		catch(InstantiationException ex) {
			throw new TForthException("Could not call function '"+funcname+"' in library '"+libname+"'.");
		}
		catch(NoClassDefFoundError ex) {
			throw new TForthException("No such library '"+libname+"'.");
		}
	      
	}
	// Retrieves a value in the addressable space
	private int Peek(int Address) {
		return Memory[Address];
	}
	// Modifies a value in the addressable space
	private void Poke(int Address, int Value) {
		Memory[Address] = Value;
	}
	// Push & Pop functions for the parameter stack
	private void PushStack(int Value) {
		Memory[SP--] = Value;
	}
	private int PopStack() {
		return Memory[++SP];
	}
	private int PeekStack() {
		return Memory[SP+1];
	}

}