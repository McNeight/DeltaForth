/*
**********************************************************
	External DELTA Forth Library    v1.0
	Copyright (C)1999 Valer BOCAN <vbocan@usa.net>
	All Rights Reserved

	IO Routines for handling files



	FINIT ( ----- )
		- Initializes the file system

	FOPEN ( filenameaddr mode ----- fileindex )
		- Opens a file
		filenameaddr = address of file name
		mode      = 0 for read, 1 for write
		fileindex = -1 for open error, else file index

	FREAD ( fileindex address count ----- err )
		- Reads from a file
		fileindex = index of file
		address   = address to place data
		count     = number of bytes to read from file
		err 	  = -1 for read error, 0 for no error

	FREADW ( fileindex address count ----- err )
		- Reads from a file (wide version)
		fileindex = index of file
		address   = address to place data
		count     = number of words to read from file
		err 	  = -1 for read error, 0 for no error

	FWRITE ( fileindex address count ----- err )
		- Writes to a file
		fileindex = index of file
		address   = address to get data from
		count     = number of bytes to write to file
		err 	  = -1 for write error, 0 for no error

	FWRITEW ( fileindex address count ----- err )
		- Writes to a file (wide version)
		fileindex = index of file
		address   = address to get data from
		count     = number of words to write to file
		err 	  = -1 for write error, 0 for no error

	FSEEK ( fileindex position ----- err )
		- Sets the file pointer
		fileindex = index of file
		position  = new position from the beginning of file (in bytes)
		err 	  = -1 for write error, 0 for no error

	FLEN ( fileindex ----- filelength )
		- Gets the file length
		fileindex = index of file
		filelength= file length, -1 for error

	FSETLEN ( fileindex length ----- err )
		- Sets the file length
		fileindex = index of file
		position  = new length of file
		err 	  = -1 for write error, 0 for no error

	FNEWLINE ( fileindex ----- err )
		- Writes the CR/LF sequence in the file
		fileindex = index of file
		err 	  = -1 for write error, 0 for no error

	FCLOSE ( fileindex ----- err )
		- Closes the file
		fileindex = index of file
		err       = -1 for error, 0 for no error

**********************************************************
*/

import java.io.*;

public class IO {

  // Array with file objects
  static RandomAccessFile Files[];
  static int iFiles;
  final int maxFiles = 50;

  // FINIT - initializes the file system
  public void FINIT(int Memory[], int P[]) {
  	iFiles = 0;
	Files = new RandomAccessFile[maxFiles];	// Maximum number of files
  }

  // FOPEN - opens a file
  public void FOPEN(int Memory[], int P[]) {
	// Create an alias for the stack pointers
	int SP = P[0];

	// Get opening mode constant
	int mode = Memory[SP+1];
	// Get address of string
	int address = Memory[SP+2];
	// Clean up the stack
        SP += 2;
	// Retrieve the filename
	String fname = new String();
	while(Memory[address] != 0) fname += (char)Memory[address++];
        // Open file
	String accmode = new String();
	if(mode == 0) accmode = "r";
      		 else accmode = "rw";
	try {
		RandomAccessFile tempf = new RandomAccessFile(fname, accmode);
		// Register file into array
		Files[iFiles++] = tempf;
		// Put index on the stack
		Memory[SP--] = iFiles - 1;
	} catch(FileNotFoundException ex) {
		// Put -1 on the stack (report error)
		Memory[SP--] = -1;
		P[0] = SP;
		return;
	}

	// Modify array just in case the pointers have modified
	P[0] = SP;
  }

  // FREAD - reads from a file
  public void FREAD(int Memory[], int P[]) {
	// Create an alias for the stack pointers
	int SP = P[0];

	// Get number of bytes to read
	int nobytes = Memory[SP+1];
	// Get address of destination
	int address = Memory[SP+2];
	// Get file index
	int index = Memory[SP+3];
	// Clean up the stack
        SP += 3;
	
	try {
		while(nobytes-- > 0) Memory[address++] = Files[index].readByte();
		// Put 0 on the stack (report no error)
		Memory[SP--] = 0;
		P[0] = SP;
	} catch(IOException ex) {
		// Put -1 on the stack (report error)
		Memory[SP--] = -1;
		P[0] = SP;
		return;
	}

	// Modify array just in case the pointers have modified
	P[0] = SP;
  }

  // FREADW - reads from a file (wide version)
  public void FREADW(int Memory[], int P[]) {
	// Create an alias for the stack pointers
	int SP = P[0];

	// Get number of bytes to read
	int nobytes = Memory[SP+1];
	// Get address of destination
	int address = Memory[SP+2];
	// Get file index
	int index = Memory[SP+3];
	// Clean up the stack
        SP += 3;
	
	try {
		while(nobytes-- > 0) Memory[address++] = Files[index].readInt();
		// Put 0 on the stack (report no error)
		Memory[SP--] = 0;
		P[0] = SP;
	} catch(IOException ex) {
		// Put -1 on the stack (report error)
		Memory[SP--] = -1;
		P[0] = SP;
		return;
	}

	// Modify array just in case the pointers have modified
	P[0] = SP;
  }

  // FWRITE - writes to a file
  public void FWRITE(int Memory[], int P[]) {
	// Create an alias for the stack pointers
	int SP = P[0];

	// Get number of bytes to write
	int nobytes = Memory[SP+1];
	// Get address of source
	int address = Memory[SP+2];
	// Get file index
	int index = Memory[SP+3];
	// Clean up the stack
        SP += 3;
	
	try {
		while(nobytes-- > 0) Files[index].writeByte(Memory[address++]);
		// Put 0 on the stack (report no error)
		Memory[SP--] = 0;
		P[0] = SP;
	} catch(IOException ex) {
		// Put -1 on the stack (report error)
		Memory[SP--] = -1;
		P[0] = SP;
		return;
	}

	// Modify array just in case the pointers have modified
	P[0] = SP;
  }

  // FWRITEW - writes to a file (wide version)
  public void FWRITEW(int Memory[], int P[]) {
	// Create an alias for the stack pointers
	int SP = P[0];

	// Get number of bytes to write
	int nobytes = Memory[SP+1];
	// Get address of source
	int address = Memory[SP+2];
	// Get file index
	int index = Memory[SP+3];
	// Clean up the stack
        SP += 3;
	
	try {
		while(nobytes-- > 0) Files[index].writeInt(Memory[address++]);
		// Put 0 on the stack (report no error)
		Memory[SP--] = 0;
		P[0] = SP;
	} catch(IOException ex) {
		// Put -1 on the stack (report error)
		Memory[SP--] = -1;
		P[0] = SP;
		return;
	}

	// Modify array just in case the pointers have modified
	P[0] = SP;
  }

  // FSEEK - sets the file pointer
  public void FSEEK(int Memory[], int P[]) {
	// Create an alias for the stack pointers
	int SP = P[0];

	// Get the new position
	int position = Memory[SP+1];
	// Get file index
	int index = Memory[SP+2];
	// Clean up the stack
        SP += 2;
	
	try {
		Files[index].seek(position);
		// Put 0 on the stack (report no error)
		Memory[SP--] = 0;
		P[0] = SP;
	} catch(IOException ex) {
		// Put -1 on the stack (report error)
		Memory[SP--] = -1;
		P[0] = SP;
		return;
	}

	// Modify array just in case the pointers have modified
	P[0] = SP;
  }

  // FLEN - gets the length of a file
  public void FLEN(int Memory[], int P[]) {
	// Create an alias for the stack pointers
	int SP = P[0];

	// Get file index
	int index = Memory[SP+1];
	// Clean up the stack
        SP ++;
	
	try {
		// Put length on the stack
		Memory[SP--] = (int)Files[index].length();
		P[0] = SP;
	} catch(IOException ex) {
		// Put -1 on the stack (report error)
		Memory[SP--] = -1;
		P[0] = SP;
		return;
	}

	// Modify array just in case the pointers have modified
	P[0] = SP;
  }

  // FCLOSE - closes the file
  public void FCLOSE(int Memory[], int P[]) {
	// Create an alias for the stack pointers
	int SP = P[0];

	// Get file index
	int index = Memory[SP+1];
	// Clean up the stack
        SP ++;
	
	try {
		Files[index].close();
		iFiles--;
		// Put 0 on the stack (no error)
		Memory[SP--] = 0;
		P[0] = SP;
	} catch(IOException ex) {
		// Put -1 on the stack (report error)
		Memory[SP--] = -1;
		P[0] = SP;
		return;
	}

	// Modify array just in case the pointers have modified
	P[0] = SP;
  }

  // FNEWLINE - writes the CR/LF sequence
  public void FNEWLINE(int Memory[], int P[]) {
	// Create an alias for the stack pointers
	int SP = P[0];

	// Get file index
	int index = Memory[SP+1];
	// Clean up the stack
        SP ++;
	
	try {
		Files[index].writeByte('\r');
		Files[index].writeByte('\n');
		// Put 0 on the stack (no error)
		Memory[SP--] = 0;
		P[0] = SP;
	} catch(IOException ex) {
		// Put -1 on the stack (report error)
		Memory[SP--] = -1;
		P[0] = SP;
		return;
	}

	// Modify array just in case the pointers have modified
	P[0] = SP;
  }

  // FSETLEN - sets the file file
  public void FSETLEN(int Memory[], int P[]) {
	// Create an alias for the stack pointers
	int SP = P[0];

	// Get the new size
	int size = Memory[SP+1];
	// Get file index
	int index = Memory[SP+2];
	// Clean up the stack
        SP += 2;
	
	try {
		Files[index].setLength(size);
		// Put 0 on the stack (report no error)
		Memory[SP--] = 0;
		P[0] = SP;
	} catch(IOException ex) {
		// Put -1 on the stack (report error)
		Memory[SP--] = -1;
		P[0] = SP;
		return;
	}

	// Modify array just in case the pointers have modified
	P[0] = SP;
  }
}