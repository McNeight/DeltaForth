(
**********************************************************
	DELTA Forth Library    v1.0
	Copyright (C)1999 Valer BOCAN <vbocan@dataman.ro>
	All Rights Reserved

	IO Routines for handling files
	NOTE: IO Java library is required at runtime
**********************************************************
)

EXTERN FINIT 	IO
\	FINIT ( ----- )
\		- Initializes the file system
\

EXTERN FOPEN 	IO
\	FOPEN ( filenameaddr mode ----- fileindex )
\		- Opens a file
\		filenameaddr = address of file name
\		mode	  = 0 for read, 1 for write
\		fileindex = -1 for open error, else file index

EXTERN FREAD 	IO
\	FREAD ( fileindex address count ----- err )
\		- Reads from a file
\		fileindex = index of file
\		address   = address to place data
\		count     = number of bytes to read from file
\		err 	  = -1 for read error, 0 for no error

EXTERN FREADW 	IO
\	FREADW ( fileindex address count ----- err )
\		- Reads from a file (wide version)
\		fileindex = index of file
\		address   = address to place data
\		count     = number of words to read from file
\		err 	  = -1 for read error, 0 for no error

EXTERN FWRITE 	IO
\	FWRITE ( fileindex address count ----- err )
\		- Writes to a file
\		fileindex = index of file
\		address   = address to get data from
\		count     = number of bytes to write to file
\		err 	  = -1 for write error, 0 for no error

EXTERN FWRITEW 	IO
\	FWRITEW ( fileindex address count ----- err )
\		- Writes to a file (wide version)
\		fileindex = index of file
\		address   = address to get data from
\		count     = number of words to write to file
\		err 	  = -1 for write error, 0 for no error

EXTERN FSEEK	IO
\	FSEEK ( fileindex position ----- err )
\		- Sets the file pointer
\		fileindex = index of file
\		position  = new position from the beginning of file (in bytes)
\		err 	  = -1 for write error, 0 for no error

EXTERN FLEN	IO
\	FLEN ( fileindex ----- filelength )
\		- Gets the file length
\		fileindex = index of file
\		filelength= file length, -1 for error

EXTERN FSETLEN	IO
\	FSETLEN ( fileindex length ----- err )
\		- Sets the file length
\		fileindex = index of file
\		position  = new length of file
\		err 	  = -1 for write error, 0 for no error

EXTERN FNEWLINE IO
\	FNEWLINE ( fileindex ----- err )
\		- Writes the CR/LF sequence in the file
\		fileindex = index of file
\		err 	  = -1 for write error, 0 for no error

EXTERN FCLOSE	IO
\	FCLOSE ( fileindex ----- err )
\		- Closes the file
\		fileindex = index of file
\		err       = -1 for error, 0 for no error

\ File access constants
0 CONSTANT READ_ACCESS
1 CONSTANT WRITE_ACCESS

\ Response constants
-1 CONSTANT _ERROR
0  CONSTANT _NOERROR