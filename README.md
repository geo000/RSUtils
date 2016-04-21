# RSUtils

**Remote Sensing Utilities. Version 1.17.04.2016 (Update 04/21/2016)**

**Welcome to RSUtils (Remote Sensing Utilities - Extentions GDAL/OGR)!**

RSUtils is free and open source (GPL) software.
RSUtils - this is a small empowerment tools GDAL command. RSUtils based on the capabilities of the GDAL/OGR library. When developing the used functionality of the libxml2 library.

Copyright (C) 2016, Igor Garkusha. Ukraine, Dnipropetrovsk.

## Previous versions

Version 1.09.03.2016: https://github.com/IgorGarkusha/RSUtils/blob/master/archive/RSUtils_ver_1.09.03.2016.tar.gz

## GDAL/OGR General

In general GDAL/OGR is licensed under an MIT/X style license with the 
following terms:

Permission is hereby granted, free of charge, to any person obtaining a
copy of this software and associated documentation files (the "Software"),
to deal in the Software without restriction, including without limitation
the rights to use, copy, modify, merge, publish, distribute, sublicense,
and/or sell copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included
in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
DEALINGS IN THE SOFTWARE.

## XML toolkit from the GNOME project

Copyright (C) 1998-2012 Daniel Veillard. All Rights Reserved.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is fur-
nished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FIT-
NESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.

## BUILD FOR Version 1.17.04.2013

### RSUtils: build requirements

1. For GNU/Linux: installed GNU GCC/G++ compilers;
   for Mac OS X: installed Xcode;
   for Windows 32/64-bit: installed Windows SDK or Visual Studio with Visual C++ compiler
   or for Windows installed MinGW-32 GNU GCC compiler (for example with IDE Code::Blocks);
   for FreeBSD: installed Clang etc.

2. Installed the library GDAL (only for GNU/Linux, Mac OS X, FreeBSD (with support GEOS!)).

3. Installed the library Libxml2 (only for GNU/Linux, Mac OS X, FreeBSD).

4. Installed OpenJDK 1.8 or Oracle JDK 1.8.

5. Installed Quantum GIS 2.12 or high (necessarily only for MS Windows! For other -- optional).


### RSUtils: building in GNU/Linux, Mac OS X, FreeBSD

1.Add to PATH the location jdk/bin directory.

2.Add to PATH the location RSUtils/bin directory. Create system variable 
RSUTILS_HOME and set her the value in path to RSUtils/bin directory.

For example in GNU/Linux: 

	export RSUTILS_HOME=/home/username/bin/RSUtils/bin
	
	export PATH=$PATH:$RSUTILS_HOME
	
For example in Mac OS X:

	export RSUTILS_HOME=/Users/username/Documents/bin/RSUtils/bin
	
	export PATH=$PATH:$RSUTILS_HOME
	
For FreeBSD -- support GEOS in GDAL library must be enabled!!!

3.Execute from console file build.sh (bash ./build.sh). To clean the build directory to use the file clear.sh (bash ./clear.sh).


### RSUtils: building in Microsoft Windows

1.Add to PATH the location jdk\bin directory.

2.Add to PATH the location RSUtils\bin directory. Create system variable 
RSUTILS_HOME and set her the value in path to RSUtils\bin directory.

For example: 

	RSUTILS_HOME=C:\RSUtils\bin
	
3.Create system variable QGIS_HOME and set her the value in path to QGIS 2.12 (or high version) directory.

For example: 

	QGIS_HOME=C:\QGIS
	
4.Start Windows SDK Command Prompt. Go to the directory RSUtils and execute file 
build_msvc.cmd (for MS Visual C++ compiler) or build_mingw32.cmd (for MinGW-32 GNU GCC).
To clean the build directory to use the file clear_msvc.cmd or clear_mingw32.cmd (depending on the build).
