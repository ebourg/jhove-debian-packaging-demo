@ECHO OFF
REM JHOVE - JSTOR/Harvard Object Validation Environment
REM Copyright 2003-2006 by JSTOR and the President and Fellows of Harvard College
REM JHOVE is made available under the GNU General Public License (see the
REM file LICENSE for details)
REM
REM This is like jhove.bat, except that it uses the jhove.conf file within
REM the installation directory, instead of the one in the user's top-level
REM JHOVE directory. This allows "out of the box" execution if you don't
REM need a custom configuration file.
REM
REM Usage: jhove_local_conf [-m module] [-h handler] [-e encoding]
REM              [-H handler] [-o output] [-x saxclass] [-t tempdir]
REM              [-b bufsize] [-l loglevel] [[-krs] dir-file-or-uri [...]]
REM
REM where -c config   Configuration file pathname
REM       -m module   Module name
REM       -h handler  Output handler name (defaults to TEXT)
REM       -e encoding Character encoding of output handler (defaults to UTF-8)
REM       -H handler  About handler name
REM       -o output   Output file pathname (defaults to standard output)
REM       -x saxclass SAX parser class (defaults to J2SE 1.4 default)
REM       -t tempdir  Temporary directory in which to create temporary files
REM       -b bufsize  Buffer size for buffered I/O (defaults to J2SE default)
REM       -l loglevel Logging level
REM       -k          Calculate CRC32, MD5, and SHA-1 checksums
REM       -r          Display raw data flags, not textual equivalents
REM       -s          Format identification based on internal signatures only
REM       dir-file-or-uri Directory, file pathname, or URI of formatted content
REM
REM Configuration constants:
REM JHOVE_HOME Jhove installation directory
REM JAVA_HOME  Java JRE directory
REM JAVA       Java interpreter
REM EXTRA_JARS Extra jar files to add to CLASSPATH

REM Change JHOVE_HOME if location of JHOVE is different from the current directory
SET JHOVE_HOME=%~dp0

SET EXTRA_JARS=

REM NOTE: Nothing below this line should be edited
REM #########################################################################


SET CP=%JHOVE_HOME%\bin\JhoveApp.jar
IF "%EXTRA_JARS%"=="" GOTO FI
  SET CP=%CP%:%EXTRA_JARS
:FI

REM Retrieve a copy of all command line arguments to pass to the application

SET ARGS= -c %JHOVE_HOME%\conf\jhove.conf
:WHILE
IF %1x==x GOTO LOOP
  SET ARGS=%ARGS% %1
  SHIFT
  GOTO WHILE
:LOOP


REM Set the CLASSPATH and invoke the Java loader
java -classpath %CP% Jhove %ARGS%
