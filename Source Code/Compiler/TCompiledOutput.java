/*
	DELTA Forth - The Forth Compiler
	(C) Valer BOCAN  (vbocan@dataman.ro)
	
	Date of creation:   	Thursday,   October    9, 1997
	Date of last update:    Tuesday,    January   25, 2000
	
	Description: Definition of the TCompiledOutput
*/

import java.io.*;

class TCompiledOutput {
	
	final byte VersionMajor = 1;
	final byte VersionMinor = 0;
	
	RandomAccessFile compout;       // Handler for the output file

	public TCompiledOutput(String outfile) throws IOException {
		// Delete outfile first
		File temp = new File(outfile);
		temp.delete();
		temp = null;
		// Create and initialize outfile
		compout = new RandomAccessFile(outfile, "rw");
		compout.writeBytes("DELTA");
		compout.writeByte(VersionMajor);
		compout.writeByte(VersionMinor);
		compout.writeBytes("          ");
	}
	// Writes a word header in the output file
	public void placeWordHeader(String oword, int okey)
	{
		int olen = oword.length();
		try {
			compout.writeBytes(oword);
			if(olen < 31) for(int i=0;i<(31-olen);i++) compout.writeByte(32);
			compout.writeByte(0);
			compout.writeInt(okey);
		} catch(IOException ex) {}
	}
	// Writes a link in the output file
	public void placeWordLink(int link) {
		try {
			compout.writeInt(link);
		} catch(IOException ex) {}
	}
	// Writes a string in the output file
	public void placeString(String str) {
		try {
			for(int i=0; i<str.length(); i++) {
				int value = 0;
				value = (int)str.charAt(i);
				compout.writeInt(value);
			}
		} catch(IOException ex) {}
	}
	// Seek method
	public void seek(long pos) {
		try {
			compout.seek(pos);
		} catch(IOException ex) {}
	}
}