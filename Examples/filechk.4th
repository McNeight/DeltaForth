( DELTA Forth Example File  - Copyright 1999 Valer BOCAN <vbocan@dataman.ro> )

\ Using external Java libraries
\ This program generates a report in a text file, based on the information
\ collected from a source file.
\ NOTE! Library IO.class must be copied in interpreter's directory

LOAD LIB\IO.4TH			\ Load file access library

VARIABLE FNAME  19 ALLOT	\ Reserve space for file name
VARIABLE ARRAY 255 ALLOT	\ Reserve 256 cells for counting the number of
				\ occurences of each byte in the source file
VARIABLE BUFF   10 ALLOT	\ Temporary storage buffer
VARIABLE TEMP			\ Temporary storage buffer

: MAIN
	FNAME DUMP "file.txt"		\ Fill variable FNAME with the file name
	ARRAY 256 ERASE			\ Erase array

	FINIT				\ Initialize file system
	FNAME READ_ACCESS FOPEN		\ Open the file with READ ONLY access
	DUP
	_ERROR = IF   ." Could not open source file. (file.txt)" EXIT
		ELSE ." Source file opened successfully."
		THEN
	CR

	DUP FLEN 0			\ Scan entire file
	DO
		DUP BUFF 1 FREAD DROP	\ Read a character from the file
		ARRAY BUFF C +		\ The content of the address on the
					\ stack has to be increased by 1
		DUP C 1+ SWAP !		\ Store the result back into array
	LOOP
	FCLOSE DROP			\ Close the file

        FNAME DUMP "stat.txt"		\ Fill variable FNAME with the file name
	FNAME WRITE_ACCESS FOPEN	\ Open the file with READ/WRITE access
	DUP
	_ERROR = IF   ." Could not create destination file. (stat.txt)" EXIT
		ELSE ." Destination file created successfully."
		THEN
	CR

	DUP 0 FSETLEN DROP		\ Truncate file (set length to 0)

	BUFF DUMP " ----> "		\ Fill buffer with text

	256 0
	DO
		ARRAY I + C
		0 <> IF			\ If number of occurences is 0, give up
			I TEMP !
			DUP TEMP 1 FWRITE DROP			\ Write the character
			DUP BUFF DUP COUNT FWRITE DROP		\ Write the text in BUFF
			DUP ARRAY I + C <# #S #> FWRITE DROP	\ Write number of occurences
			DUP FNEWLINE DROP			\ Write a new line
		     THEN
	LOOP
	FCLOSE				\ Close the file
;