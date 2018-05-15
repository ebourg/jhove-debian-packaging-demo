RELEASE NOTES
=============
JHOVE - JSTOR/Harvard Object Validation Environment  
Copyright 2003-2009 by JSTOR and the President and Fellows of Harvard College.  
JHOVE is made available under the GNU Lesser General Public License (LGPL;
see the file LICENSE for details).

Versions 1.7 to 1.11 of JHOVE released independently.  
Versions 1.12 onwards released by the Open Preservation Foundation.

JHOVE 1.20
-------------
2018-03-29

### General

- Removed obsolete subsitituion from izpack installer [[#300][]]
- Improved counting accuracy of skipped bytes, allowing better
  EOF detection [[#308][]]

### JPEG Module

- Fixed bug causing JHOVE to skip the wrong number of characters in `APP0`
  segments [[#303][]]

### PDF Module

- Header check for invalid PDF minor version (not > 7) [[#317][]]
- Unit tests for PDF Header parsing conditions [[#317][]]
- Check that document catalog dictionary key `\Type` equals `Catalog` [[#318][]]
- Test that document catalog XRef lookup retrieves the right object
  number [[#319][]]
- Unit tests for document catalog issues [[#318][]]
- Test that page dictionary key `\Type` equals `Pages` [[#322][]]
- Unit tests for page dictionary issues [[#322][]]
- Improved handling of XRef lookup errors for document catalog and pages
  dictonary [[#322][]]
- Added synthetic test files created by @asciim0 for iPres as unit
  test resources ([[#317][]-[#319][]])
- Fixed assignment of `application/pdf` as MIME type for images embedded
  in a PDF [[#324][]]
- Added method to derive MIME type from Filters and assign to NISO metatadata
  and added String constants for Filter names [[#324][]]

### WAVE Module

- Fixed byte skipping issue when parsing Associated Data List chunks [[#309][]]
- Added support for parsing and validating RF64 files [[#308][]]
- Made WAVE parser more resilient to unexpected chunk data [[#308][]]
- Improved reporting of WAVE codecs in WAVEFORMATEXTENSIBLE files [[#308][]]
- Avoids reporting file format and MIME type until signatures have been verified
  and reports extended MIME type information, e.g. `audio/vnd.wave; codec=1`,
  as per RFC 2361 [[#308][]]
- Subformat GUID's are now reported in their standard format, e.g.
  `00000001-0000-0010-8000-00AA00389B71`, instead of as an
  array of byte values [[#308][]]
- Added checks to verify the existence of Data chunks and their appearance 
  after Format chunks [[#308][]]
- Expanded WAVE example corpora to cover more formats and errors [[#308][]]
- Improved truncation detection and reporting [[#308][]]
- Fixed erroneous reporting of Cue Point values and renamed "Cue" report
  property to "CuePoints" [[#308][]]

### Text Handler

- NISO MIX 1.0 output now includes MIME type as `FormatName` [[#323][]]

### XML Handler

- NISO MIX 1.0 output now included mandatory `<FormatDesignation>`
  element [[#323][]]
- Image MIME type output as mandatory `<FormatName>` element [[#323][]]

[#300]: https://github.com/openpreserve/jhove/pull/300
[#303]: https://github.com/openpreserve/jhove/pull/303
[#308]: https://github.com/openpreserve/jhove/pull/308
[#309]: https://github.com/openpreserve/jhove/pull/309
[#317]: https://github.com/openpreserve/jhove/pull/317
[#318]: https://github.com/openpreserve/jhove/pull/318
[#319]: https://github.com/openpreserve/jhove/pull/319
[#322]: https://github.com/openpreserve/jhove/pull/322
[#323]: https://github.com/openpreserve/jhove/pull/323
[#324]: https://github.com/openpreserve/jhove/pull/324


JHOVE 1.18.1
-------------
2017-11-30

### General

- Installation of external modules is now optional [[#292][]]
- Inaccessible files are now reported as of "Unknown" status instead of
  "Not well-formed" [[#257][]]
- Improvements to error handling and uncaught module exceptions,
  increasing resilience during batch processing [[#257][], [#259][]]
- Improved path handling, allowing installation locations and file paths to
  contain spaces, and more exotic characters [[#206][]]
- Error and informational messages have been consolidated into discrete message
  classes for easier maintenance and future improvement [[#120][], [#157][],
  [#283][]–[#285][], [#287][]–[#291][]]
- Increased the minimum version of Java from 1.5 to 1.6 [[#273][]]

### JPEG Module

- Added validation for ICC profiles [[#249][]]
- Fixed handling of Exif profiles [[#253][]]

### PDF Module

- Fixed a false invalid result for some types of encrypted document [[#257][]]
- Fixed incorrect parsing of escaped characters in name objects [[#280][]]
- More detailed error messages for indirect references to non-existent
  destinations [[#123][]]

### PNG Module

- Report invalid NISO color types [[#171][]]

### TIFF Module

- Added validation for ICC profiles [[#249][]]

### WAVE Module

- Added support for reporting BWF v2 fields [[#273][]]
- Simplified BWF profile detection, allowing detection of any future BWF
  versions. All BWF versions will now be reported as "BWF" instead of
  "BWF version #", with any unrecognized versions being flagged [[#273][]]
- Reformatted the BWF UMID field into a hexadecimal string instead of a long
  sequence of numbers [[#273][]]
- Changed property label from "Originator Reference" to "OriginatorReference"
  for consistency and predictability [[#273][]]
- Fixed incorrectly reported format names and `ArrayIndexOutOfBoundsException`
  errors when processing certain non-PCM WAVE files [[#118][]]
- Changed reported MIME type from `audio/x-wave` to `audio/vnd.wave` [[#257][]]

### XML Handler

- Fixed MIX 1.0 and TextMD XML generation for images with certain properties
  [[#220][]]

[#118]: https://github.com/openpreserve/jhove/pull/118
[#120]: https://github.com/openpreserve/jhove/pull/120
[#123]: https://github.com/openpreserve/jhove/pull/123
[#157]: https://github.com/openpreserve/jhove/pull/157
[#171]: https://github.com/openpreserve/jhove/pull/171
[#206]: https://github.com/openpreserve/jhove/pull/206
[#220]: https://github.com/openpreserve/jhove/pull/220
[#249]: https://github.com/openpreserve/jhove/pull/249
[#253]: https://github.com/openpreserve/jhove/pull/253
[#257]: https://github.com/openpreserve/jhove/pull/257
[#259]: https://github.com/openpreserve/jhove/pull/259
[#273]: https://github.com/openpreserve/jhove/pull/273
[#280]: https://github.com/openpreserve/jhove/pull/280
[#283]: https://github.com/openpreserve/jhove/pull/283
[#285]: https://github.com/openpreserve/jhove/pull/285
[#287]: https://github.com/openpreserve/jhove/pull/287
[#291]: https://github.com/openpreserve/jhove/pull/291
[#292]: https://github.com/openpreserve/jhove/pull/292


JHOVE 1.16.7
------------
2017-07-20

### PDF Module

- Fixed: Some PDFs being reported as "Well-formed and valid" while remaining
  largely unchecked [[#258](https://github.com/openpreserve/jhove/pull/258)]

JHOVE 1.16.5
------------
2017-03-20

### General

- Fixed: Core method causing modules to skip more bytes than expected
  [[#194](https://github.com/openpreserve/jhove/pull/194)]

JHOVE 1.16.0
------------
2017-03-16

### General

- Added PDF and WAVE test files submitted by community during JHOVE hack day
- JHOVE Maven artefacts made available on Maven Central in addition to OPF Artifactory
- Improved error reporting for Travis test failures
- Improvements to GitHub pages website
- Formatting improvements to README.md, RELEASENOTES.md and pom.xml

### PDF Module

- Fixed: CrossRefStream incorrectly assumes Index value is a two-element array
- Fixed: Bug in `skipIISBytes` and `PdfModule.getObject`
- Better handling where image heights and widths are PdfIndirectObjects
- Better handling of "empty" hex strings
- Better handling where form-fields are PdfIndirectObjects

### WAVE Module

- Fixed: Validation of WAVE files larger than 2 GB
- Fixed: Skip Bytes issue for WAVE files larger than 100 MB


JHOVE 1.14
----------
2016-05-12

*Version 1.12 was never officially released, so to avoid confusion the
1.12 changes are included with the 1.14 notes below.*

### General

- Ant build replaced with Maven
- Modularised project structure with "fat" JAR packaging
- Java 5 support
- Cross-platform installer
- Travis CI builds
- Maven distribution through OPF Artefactory server
- Updated JHOVE site pages

### New Format Modules

- GZIP Module, ported from JHOVE2 via JWAT by KB
- WARC Module, ported from JHOVE2 via JWAT by KB
- PNG Module, developed by Gary McGath

### UTF-8 Module

- Support for Unicode 7.0.0


JHOVE 1.11
----------
2013-09-30

### General

   1. I've added lots of logging code. Calls at the FINE level and lower
      don't show up no matter what I do, so I've put them at the INFO level.
      The level is set in JhoveBase.java.

   2. All .bat and _bat.tmpl files now have CR-LF line endings. That is, they
      do in the gzip and zip archives you download. I'm not sure how
      SourceForge will treat files that you download individually,
      but hopefully it will have the sense to keep CR-LF when downloading
      to a Windows system.

   3. All .bat files now assume JHOVE_HOME is the directory from which they're
      run. They no longer try to set JAVA_HOME (which was still stuck in
      Java 1.4 and probably wasn't working for many people), instead assuming
      that the `java` command is available on the command line.

   4. All `javac` commands in build.xml files now specify source=1.5 for
      compatibility with more recent compilers.

   5. gdumpwin.bat is deleted. It's redundant with gdump.bat and has bugs
      of its own.

### PDF Module

   1. Fix to PDF module, submitted by willp-bl, may reduce tendency
      to run out of heap space on some files.


JHOVE 1.10
----------
2013-06-10

### General

   1. The amount of logging code has been increased, mostly at the
      DEBUG level.

   2. Further work on generics in Java code.

   3. JhoveView now checks for Java 1.5. Was previously allowing 1.4 even
      though it wouldn't work.

### HTML Module

   1. XHTML files are processed by the HTML module, which invokes the XML
      modules. In this case, the XML module doesn't have the parameters
      specified in the JHOVE configuration file and so won't use local
      copies of schemas. Starting with this version, the parameters of
      the HTML module are passed to the XML module when invoking it.
      However, this doesn't work properly (in either module) for a DTD
      that invokes additional DTDs by relative URLs. Such DTDs should
      be edited to use only absolute URLs.

### PDF Module

   1. Failure to get a page object number wasn't being handled cleanly,
      resulting in a report of an invalid document without an error message
      to explain it (SourceForge bug 49). This has been fixed.

   2. The PDF module unnecessarily uses huge amounts of memory to build
      complex structure trees, when it doesn't need to keep the whole
      tree in memory to validate it. In the new version, it uses memory
      more economically. This should result in the successful processing
      of some PDF files that ran out of memory or took hours to process before.

   3. If an annotation isn't a dictionary object, report that explicitly.
      This happens with some otherwise good files; I can't find any warrant
      for it in the PDF spec.

   4. Some efficiency improvements to PDF parser. Increased buffer size from 4K
      to 64K. Made Parser.collapseObjectVector more efficient. Parser now
      returns pseudo-objects for array and dictionary end instead of throwing
      an exception.

   5. Minor cleanup of error reporting.

   6. If an object uses a compression scheme which JHOVE can't deal with, JHOVE
      will try to give a specific error message.


JHOVE 1.9
---------
2012-12-17

### General

   1. Jhove.java and JhoveView.java now get their version information from
      JhoveBase.java. Before it was redundantly kept in three places, and
      sometimes they didn't all get updated for a new release. Like in 1.8.

   2. ConfigWriter was in the package edu.harvard.hul.ois.jhove.viewer, which
      caused a NoClassDefFoundError if non-GUI configurations didn't include
      JhoveViewer.jar in the classpath. It's been moved to
      edu.harvard.hul.ois.jhove.

   3. Added script packagejhove.sh and made md5.pl part of the CVS repository
      to make packaging for delivery easier.

   4. jhove.bat now simply uses the Java command rather than requiring
      the user to set up the Java path.

   5. JhoveView.jar and jhove (the top level shell script) are now forced
      by ant to be executable so there are no mistakes.

   6. Warning message given on invalid buffer size string, and minimum
      buffer size is 1024.

   7. Configuration file code for adding handlers and giving init strings
      to modules was an awful mess that never could have worked.
      Major repairs done.

### AIFF Module

   1. If an AIFF file was found to be little-endian, the module instance
      would stay in little-endian mode for all subsequent files. This
      has been fixed.

### TIFF Module

   1. TIFF files that had strip or tile offsets but no corresponding byte
      counts were throwing an exception all the way to the top level. Now
      they're correctly being reported as invalid.

### XML Module

   1. Cleaned up reporting of schemas, Added some small classes to replace
      the use of string arrays for information structures. Made URI comparison
      for local schema parameter case-independent. Resolved conflict between
      "s" and "schema" parameters.

### WAVE Module

   1. Some uncaught exceptions caused the module to throw all the way
      back to JhoveBase and not report any result for certain defective
      files. These now report the file as not well-formed.


JHOVE 1.8
---------
2012-11-07

### General

   1. If JHOVE doesn't find a configuration file, it creates a default one.

   2. Generics widely added to clean up the code.

   3. build.xml files fixed to force compilation to Java 1.5.

   4. Shell script "jhove" no longer makes you figure out where JAVA_HOME is.

### PDF Module

   1. Several errors in checking for PDF/A compliance were corrected. Aside from
      fixing some outright bugs, the Contents key for non-text Annotations is
      no longer checked, as its presence is only recommended and not required.

   2. Improved code by Håkan Svenson is now used for finding the trailer.

### TIFF Module

   1. TIFF tag 700 (XMP) now accepts field type 7 (UNDEFINED) as well as 1
      (BYTE), on the basis of Adobe's XMP spec, part 3.

   2. If compression scheme 6 is used in a file, an InfoMessage will report
      that the file uses deprecated compression.

### WAVE Module

   1. The Originator Reference property, found in the Broadcast Wave Extension
      (BEXT) chunk, is now reported.


JHOVE 1.7
---------
2012-08-12

### General

   1. JHOVE 1.7, as well as future releases unless noted otherwise, is
      released independently of Harvard under the GNU General Public License.

   2. JHOVE now will tell you where it was looking for the config file if it
      can't open it. This should help debug configuration problems.

### XML Handler

   1. Changes to XmlHandler.java and NisoImageMetadata.java to correct invalid MIX
      2.0 XML output in the value of grayResponseUnit. It was previously writing
      integers (as in 1.0) rather than the expected enumerated strings.

### PDF Module

   1. A situation that caused an infinite loop and eventual memory exhaustion
      processing in some PDF files with malformed literals has been fixed.


JHOVE 1.6
---------
2011-01-04

### XML Handler and Text Handler

   1. The default version of MIX is now 2.0. In earlier versions it was 0.2.
      However, MIX 2.0 still isn't supported in the text handler, so it will
      produce 1.0 output by default. The XML handler will produce MIX 2.0
      output.

### TIFF Module

   1. JHOVE returned a \"String index out of range: 4\" exceptions during
      TIFF validation for a tiff contains an empty (not NULL) date/time
      field. This has been corrected so that a date/time field with
      the wrong length won't be parsed but will report an error instead.

   2. If text tags contain characters which aren't printable ASCII, these
      are now output as escape sequences so that invalid XML isn't
      output.

### UTF-8 Module

   1.  Updated to Unicode 6.0.0.


JHOVE 1.5
---------
2009-12-17

### PDF Module

   1. An ArrayIndexOutOfBoundsException was thrown on a PDF with an invalid
      object number in the cross-reference stream. In JHOVE 1.5, this is
      correctly reported as a violation of well-formedness.

### UTF-8 Module

   1. With some very simple UTF-8 files, JHOVE handlers would throw an exception
      processing them, and the GUI would fail silently. This happened with files
      using no UTF-8 blocks. This has been fixed.

### TextMD (Multiple Modules)

   1. TextMD metadata can now optionally be reported. To get this, it's
      necessary to edit jhove.conf. TextMD can be enabled on a per-module
      basis for HtmlModule, AsciiModule, Utf8Module, and XmlModule.
      The `<module>` element for each chosen module must contain the element
      `<param>withtextmd=true</param>` (no spaces).

   2. The TextMD feature was added by Thomas Ledoux.


JHOVE 1.4
---------
2009-07-31

### PDF Module

   1. The PDF/A profile has been updated to the final version of
      19005-1:2005(E) and made more thorough. Among the changes:

      a. The set-state and no-op actions disqualify a PDF/A candidate.

      b. The ASCIIHexDecode and ASCII85Decode filters no longer
         disqualify a candidate.

      c. Checking of outlines has been added.

      d. Additional checking of Type 1 fonts and symbolic fonts.

      e. Bug fix in checking type 2 subfonts.

      f. An LZW filter in an image object disqualifies a candidate.

      g. The xpacket processing instruction is checked for attributes
         which disqualify from PDF/A.

      h. Conformity to implementation limits is checked as a condition
         of PDF/A conformity.

### JPEG 2000 Module

   1. The pathological case of an image with no components is checked so
      it won't cause a crash.

### XML Handler

   1. A reset() function has been added so that if the handler is reused,
      it will return to a valid initial state.


JHOVE 1.3
---------
2009-06-04

### General

   1. The build.xml files now force compilation to Java 1.4, preventing
      accidental distributions that aren't 1.4-compatible.

   2. Spaces are allowed in file paths on Windows, if the path is
      enclosed in quotes. This fix had been in version 1.1i, and had been
      lost since then.

### PDF Module

   1. According to the PDF 1.6 specification, table 3.4, parameters for a
      stream filter can be either a dictionary or the null object. The null
      object was treated as an error; it is now allowed.

   2. Object stream handling was seriously buggy, causing rejection of
      well-formed and valid files; it's better now.

   3. In PDF 1.4, an outline dictionary unconditionally must have a "First"
      and a "Last" entry. JHOVE follows this requirement, declaring a file
      invalid if it isn't met. However, PDF 1.6 relaxes the requirement,
      applying it only "if there are any open or closed outline entries."
      Thus, an empty outline dictionary with no "First" or "Last" entry
      is valid. It is now accepted (for all PDF versions).

   4. If a page number tree in a PDF file is missing an expected "Nums"
      entry, this was being reported as an invalid date. A more appropriate
      error message is now given.

### TIFF Module

   1. TIFF tag 33723 (IPTC-NAA) was considered valid only if the data
      type is ASCII or LONG. But according to Aware Systems, the valid
      types are UNDEFINED and BYTE. All four types are now accepted.

### XML Handler

   1. Omissions in MIX 1.0 and 2.0 output have been fixed.


JHOVE 1.2
---------
2009-02-10

### General

   1. A bug has been fixed in CountedInputStream, which could potentially
      have caused infinite recursion in some modules.

### HTML Module

   1. An incompatibility with Java 1.6 has been fixed.

### PDF Module

   1. A null pointer exception would be thrown for PDF documents without a
      document root tree. This has been fixed.

   2. A source of possible false positives in PDF profiles has been fixed.

   3. Certain checks weren't being done to Type 2 fonts, and some PDF/A
      profile violations might have been missed as a result. This has
      been fixed.

### WAVE Module

   1. Sub-chunks of the 'adtl' chunk are now constrained to even byte
      boundaries.

### XML Handler

   1. MIX 2.0 is now supported.

   2. The URL for the MIX 0.2 schema has changed to reflect the change
      on the LOC MIX site.

   3. The handler was sometimes incorrectly reporting whether the
      AESAudioMetadata property had an empty value or not. This has
      been fixed.


JHOVE 1.1
---------
2008-02-22

### Command-line Interface

   1. Allow filenames with internal spaces if they are quoted on the
      command line.

   2. Corrected error setting the Classpath in the Windows Shell script
      (jhove.bat).

   3. Corrected error opening the configuration file using the default
      GCJ parser in the GNU Java Runtime Environment.

### GUI (Swing) Interface (JhoveView)

   1. AES metadata properties displayed in the RepInfo window rearranged
      slightly to make their ordering consistent with the Text and XML
      handlers.

   2. The JhoveView.main() method will now accept a `-c configFile` option
      on the command line.  The GUI interface can now be invoked by:

          java -jar bin/JhoveView.jar -c configFile

   3. Corrected error opening the configuration file using the default
      GCJ parser in the GNU Java Runtime Environment.

   4. Correct recurrent problems with reading the configuration file on
      Windows installations.

### AIFF Module

   1. Correct value for first sample offset by included non-zero offset
      defined in the SSND chunk.

   2. Do not report bitrate reduction data for PCM data.

   3. All non-final instance fields and methods are protected, rather than
      private.

### ASCII Module

   1. A minimal file containing no line-end characters now does not
      produce an empty ASCIIMetadata property, which is invalid against
      the JHOVE schema.

   2. Zero-length files are considered not well-formed.

   3. Issue informative message if file contains no printable characters.

   4. All non-final instance fields and methods are protected, rather than
      private.

### Bytestream Module

   1. All non-final instance fields and methods are protected, rather than
      private.

### GIF Module

   1. All non-final instance fields and methods are protected, rather than
      private.

### HTML Module

   1. The HTMLMetadata block in the module output is only produced if
      there is at least one actual metadata property to report.

   2. All non-final instance fields and methods are protected, rather than
      private.

### JPEG Module

   1. The JPEG module reports the X and Y sampling frequency for files
      meeting the JFIF profile.

   2. The JPEG module reports the pixel aspect ratio for JFIF profile
      files for which it is defined.

   3. File handles were not being properly closed when processing embedded
      EXIF metadata.  In cases where JHOVE was invoked against large
      numbers of objects this was causing a premature crash due to the
      resource leak.

   4. All non-final instance fields and methods are protected, rather than
      private.

   5. Correct parsing of the EXIF "subsecTimeOriginal" (37251) and
      "subsecTimeDigitized" (37522) properties.

   6. Validation errors in embedded EXIF metadata were not being fully
      reported.

### JPEG 2000 Module

   1. All non-final instance fields and methods are protected, rather than
      private.

   2. Files generated by the LuraWave codec are no longer incorrectly identified
      as having unrecognized QCC marker segments.

### PDF Module

   1. Date strings are now parsed with strict conformance to the ASN.1
      syntax.

   2. Destinations defined by indirect references to non-existent objects
      are assumed to have the value "null".  Files containing such
      destinations are reported as "well-formed, but not valid".

   3. No attempt is made to display encrypted outline item title strings are
      not displayed.

   4. Catch error if the Info key of the trailer dictionary is not an
      indirect reference.

   5. Read entire page tree structure, regardless of its internal
      organization.  This error may have caused the under reporting of
      page resources, such as fonts and images.

   6. The NISO Compression Scheme for all images using the CCITTFaxDecode
      compression filter is now reported properly; previously, the scheme
      was always reported as CCITT 1D even if the actual compression
      algorithm was CCITT Group 3 or 4.

   7. Properly parse UTF-16 escape characters encoded in double-byte form.

   8. The module properly stops looking for the header comment after 1024
      bytes.

   9. All non-final instance fields and methods are protected, rather than
      private.

  10. The number of incremental updates is now reported correctly, rather than
      the total number of file trailers, which is one greater than the number
      of updates.

  11. Only up to 1000 fonts will be reported.  After that, an informative
      message will be generated.  The limit can be set using the parameter
      "nxxxx" in the module-specific section of the configuration file:

      ```xml
      <module>
        <class>edu.harvard.hul.ois.jhove.module.PdfModule</class>
        <param>n2000</param>
      </module>
      ```

  12. Subfonts of Type 0 are now being properly reported.

  13. PDF/A-1b profile is now being properly reported.

  14. Permit trailer info key to be optional.

  15. Additional correction for outline recursion.

  16. Fix treatment of indirect object of Actions.

  17. Correctly handle trailer dictionary without Info entry.

  18. Ignore comments within dictionaries.

### TIFF Module

   1. Corrected error parsing pyramidal TIFF using the SubIFDs tag with a
      type of IFD (13) rather than LONG (4).

   7. Correct parsing of the EXIF "subsecTimeOriginal" (37251) and
      "subsecTimeDigitized" (37522) properties.

   2. All sub-IFDs of a pyramidal TIFF are now properly parsed.

   3. The EXIF GainControl tag (41991) is now correctly identified as
      a SHORT, not a RATIONAL, value.

   4. Corrected error in which valid files were reported as being only
      well-formed due to an incorrect parsing of the DateTime (306) tag.

   5. Byte-aligned offsets can be considered well-formed if the module
      parameter "byteoffset=true" is set in the configuration file:

      ```xml
      <module>
        <class>edu.harvard.hul.ois.jhove.module.TiffModule</class>
        <param>byteoffset=true</param>
      </module>
      ```

   6. All non-final instance fields and methods are protected, rather than
      private.

   7. Correct parsing of the EXIF "subsecTimeOriginal" (37251) and
      "subsecTimeDigitized" (37522) properties.

   8. Using the `-s` option, the TIFF module was incorrectly reporting
      signature matches for text files starting with "II".

   9. Validation errors in embedded EXIF metadata were not being fully
      reported.

### UTF-8 Module

   1. Corrected error under which malformed UTF-8 files containing encoding
      sequences starting with a byte value in the range 0xF8 through 0xFF
      were reported as well-formed and valid.

   2. Zero-length files are considered not well-formed.

   3. Issue informative message if file contains no printable characters.

   4. All non-final instance fields and methods are protected, rather than
      private.

### WAVE Module

   1. BWF files now set the correct start time in the AES metadata.

   2. All non-final instance fields and methods are protected, rather than
      private.

   3. "cue" and "adtl" chunks are now properly read.

### XML Module

   1. The DTD is assumed to be the first DOCTYPE system ID in the file with a
      ".dtd" extension.

   2. All non-final instance fields and methods are protected, rather than
      private.

   3. The module correctly handles schemaLocation attributes that do not
      provide two whitespace-separated URIs.

### Text Handler

   1. AES audio metadata properties rearranged slightly to make their
      ordering consistent with the XML schema.

### XML Handler

   1. Correct sample rate formatting in AES Time Code Format (TCF)
      temporal references.

   2. Correct face IDREF in AES metadata.

   3. Disallowed control characters are removed from content.

   4. Null property values no longer generate empty elements.

   5. Image technical metadata can be reported in terms of the MIX 1.0 schema,
      as opposed to the default reporting against MIX 0.2.  To specify the
      1.0 schema include the `<mixVersion>1.0</mixVersion>` directive in the
      configuration file.

### JHOVE API

   1. The process() and processFile() methods of the JhoveBase class are now
      public, to permit direct access to the API by applications.

   2. Checksum calculations now use buffered I/O uniformly for improved
      performance.

   3. All non-final fields and methods in the JhoveBase class are
      protected, rather than private.

   4. When invoked with the `-s` option JHOVE now reports the signature
      matched format and MIME type.

   5. The processing of files in a directory is now performed in an
      alphabetically sorted order.

### ADUMP Utility

   1. Display the field values of known chunks.

### TDUMP Utility

   1. New format that sorts all tag definitions by their byte offset and
      also displays the byte ranges for image data.

   2. Command line flags permit the suppression of BYTE data display (`-b`) and
      and subIFD parsing (`-s`).

### UserHome Utility

   1. A new utility program, UserHome, is available to determine the value
      of the Java user.home property needed to know where to place the
      configuration file.  This utility can be invoked by the driver scripts
      "userhome" (Bourne shell) or "userhome.bat" (Windows).


JHOVE 1.0
---------
2005-05-26

### General

   1. Zero length files are now handled properly in all modules.

   2. Missing start time in audio files is now handled property in all
      audio modules.

   3. Miscellaneous bug fixes, enhancements, and documentation updates.

### AIFF Module

   1. Corrected error causing BitrateReduction to be incorrectly reported
      for uncompressed PCM audio.

### JPEG 2000 Module

   1. The module now validates the enumerated ICC profile types in the
      Color Specification Box. In the JP2 profile, an unrecognized ICC
      profile type marks the file as not well formed; in the JPX, the file
      is merely not valid.

   2. In the beta 3 release certain invalid JPEG 2000 files were
      reported as well formed in the JP2 profile. This has been corrected.

### PDF Module

   1. Following the practice of Acrobat, the PDF module will accept
      the "%PDF-1.n" header comment anywhere in the first 1024 bytes of a
      file (with appropriate notification via an information message),
      rather than requiring that it start at byte offset 0.

   2. The requirements for the PDF/A profile have been brought into
      conformance with the most recent version of the PDF/A specification,
      ISO/DIS 19005-1 of 2004-12-22.

   3. Corrected bug that prevented valid PDF/X-1 files from being
      recognized as such.

### WAVE Module

   1. Corrected error causing BitrateReduction to be incorrectly reported
      for uncompressed PCM audio.

### XML Handler

   1. Dates reported for the NISO Z39.87 `<mix:DateTimeCreated>`
      element are now canonicalized to be in proper ISO 8601 form.

   2. The NISO Z39.87 `<mix:ScannerManufacturer>` element is now
      reported, if known.

### AUDIT Handler

   1. The current working directory is reported as the "home"
      attribute of the `<audit>` element and individual files are reported
      as relative pathnames.


JHOVE 1.0 beta 3
----------------
2005-02-04

### General

   1. The architecture has been modified to simplify the use of JHOVE
      with new "front ends." The new JhoveBase class is used in
      conjunction with the App class to incorporate nearly all the
      work of setting up a JHOVE instance. The main Jhove class and the App
      class are now smaller than before.

   2. Checksums were often being reported with incorrect values due to
      an output formatting error that dropped zeroes. This has been fixed.

   3. New utilities GDUMP and JDUMP created for GIF and JPEG documents.

   4. Error messages are more consistently factored into submessages.
      This allows messages indicating the same type of error to
      be more readily grouped.

   5. Some modules were reporting a MIME type for a document that is
      not well-formed. This no longer occurs.

   6. Duplicate reporting of AES BitDepth has been suppressed.

   7. New module for HTML format. Be sure to update the configuration
      file, jhove/conf/jhove.conf, to include the module:

      ```xml
      <module>
        <class>edu.harvard.hul.ois.jhove.module.HtmlModule</class>
      </module>
      ```

   8. The AES audio metadata representation has been updated to
      conform with schema version 1.02b (pre-release).

   9. New property, sigMatches, has been added to RepInfo. This
      records which module(s) regarded the signature of the document as a
      match, even if the document was not well-formed. This is useful in
      identifying broken documents that are reported as ASCII or Bytestream.

  10. The logging API is supported, permitting the generation of
      debugging messages.

  11. All modules are now non-final, so that they can be subclassed by
      adventurous users.

  12. The `-p` and `-P` arguments of the command line are no longer
      supported.  Instead, the equivalent parameters can be
      provided to all variants of JHOVE (including those which
      don't take a command line) by specifying a `<param>` element
      within the `<module>` element of the configuration file.
      Example:

      ```xml
      <module>
        <class>edu.harvard.hul.ois.jhove.module.PdfModule</class>
        <param>a</param>
        <param>f</param>
        <param>p</param>
      </module>
      ```

### JHOVE Command-line Interface

   1. The JHOVE command-line interface can now accept directory names,
      as well as file pathnames and URIs:

          java jhove [-c config] [-m module] [-h handler] [-e encoding]
                     [-H handler] [-o output] [-x saxclass] [-t tempdir]
                     [-b bufsize] [-l loglevel] [[-krs] dir-file-or-uri [...]]

      All of the files in the directories are processed in a
      depth-first recursive descent.

### JhoveViewer (Swing GUI) Interface

   1. The JhoveViewer class now allows dragging of a directory or of
      multiple files, and the output for all files is presented in a single
      window. This significantly reduces the window clutter.

   2. The JhoveViewer presents the module menu in alphabetical order
      rather than configuration file order.

   3. The JhoveViewer was failing to report some submessages. This is fixed.

   4. The JhoveViewer was failing silently on certain URL errors; it
      now puts up an error alert.

   5. If an empty module class name is added in the Configuration
      dialog, it is ignored.

### AIFF Module

   1. Descriptive properties added.

   2. Checksum was sometimes missing; fixed.

   3. Specification URL added to descriptive information.

   4. Reported MIME type changed to 'audio/x-aiff' from 'application/aiff'.

### GIF Module

   1. BitsPerSample is now reported.

### JPEG Module

   1. Errors occurring when parsing an optional EXIF segment were not
      being reported. This problem manifested itself by incorrectly
      reporting that the JPEG file is not well-formed.

   2. Array size bug in BitsPerSample fixed.

### JPEG 2000 Module

   1. Specification information added for ITU.

   2. Errors in parsing of an EXIF segment are now reported.

### PDF Module

   1. In certain instances the module was inappropriately reporting
      well-formed PDF files as being non-well-formed, indicating
      (incorrectly) that the file does not contain a trailer.

   2. Fixed a NullPointerException being thrown with a defective page
      root tree.

   3. Certain broken cross-reference tables would throw the module
      into a loop. This is fixed.

   4. Problems in XMP data that triggered a SAX error were being
      reported to standard output as a "fatal error." They are now properly
      reported.

   5. Error in offset reporting fixed.

   6. Now reports FontFile2 and FontFile3.

   7. File trailers are now found more reliably.

   8. PDF/A profile updated to latest draft proposal, ISO/CD 19005-1
      (2004-09-20).

   9. Parameters that would have been specified by the `-p` argument
      of the command line are now specified by the `<param>` element
      in the configuration file. The sense of these parameters
      has been reversed; by default, the PDF module presents
      the maximum amount of information unless suppressed by
      including the characters a, p, f, or o in the parameter value(s).

### TIFF Module

   1. Adobe DNG tags are recognized, and a DNG profile has been added.

   2. Bug in DATETIME checking fixed.

   3. Changes in validity tests for PhotometricInterpretation,
      SamplesPerPixel and BitsPerSample.

   4. Corrected spurious null values for some properties.

   5. Tag data type checking was badly broken, now fixed.

### WAVE Module

   1. Type 'exif' recognized in LIST chunk.

   2. Format and signature information updated.

   3. Checksum was sometimes missing; fixed.

   4. Reported MIME type changed to 'audio/x-wave' from 'audio/x-wav'.

### XML Module

   1. Now reports 1.0 and 1.1 as versions rather than profiles.

   2. Reported MIME type changed to 'text/xml' from 'application/xml'.

   3. A base URL for DTD's may now be specified using the
      `<param>` element. The URL must be preceded by the letter `b`
      to distinguish it from potential future parameters, e.g.,

      ```xml
      <module>
        <class>edu.harvard.hul.ois.jhove.module.XmlModule</class>
        <param>bhttp://www.example.com/</param>
      </module>
      ```

### XML Handler

   1. The "xsi" namespace is now defined in the NISO Image Metadata
      `<mix:mix>` and AES Audio Metadata `<aes:audioObject>` elements. This
      allows these segments to validate when extracted from the JHOVE output
      document.

   2. The `<ImagingPerformanceAssessment>` element is properly named; it
      had been improperly displayed as `<ImagePerformanceAssessment>`.

   3. X and YSamplingFrequency are reported as positive integers
      ("600"), not ratios ("600/1"), for consistency with the MIX schema.

   4. An empty Properties element in the XML handler is now suppressed.

### GDUMP Utility

   1. New utility to dump GIF files in human-readable form.

### JDUMP Utility

   1. New utility to dump JPEG files in human-readable form.

### TDUMP Utility

   1. The output format has changed slightly, e.g.

          00000000: "II" (little endian) 42
          00000008: IFD 1 with 15 entries
          00000034: 254 (NewSubFileType) LONG 1 = 0
          00000046: 256 (ImageWidth) LONG 1 = 2948
          00000058: 257 (ImageLength) LONG 1 = 4620
          ...


JHOVE 1.0 beta 2
----------------
2004-07-19

### General

   1. Multiple files can now be specified in command line:

          jhove ... [[-krs] file-or-uri ...]

      A single output document (XML or text) will be generated for a
      set of files specified in a command line.

   2. API version information is now available through methods in the
      App class.

   3. AESAudioMetadata property has been added for sound formats. The
      new PropertyPath class facilitates the extraction of Properties
      by applications that use the JHOVE API.

   4. The ErrorMessage and InfoMessage classes now support a submessage
      string for more flexible message factoring.

   5. The SAX parser class may now be specified in the jhove.properties
      file in the property "edu.harvard.hul.ois.jhove.saxClass".

### Graphical User Interface (JhoveView)

   1. Supports drag and drop of directories; subdirectories are
      processed recursively.

   2. The menu option "File > Close document windows" closes all document
      windows.

### Modules (General)

   1. Performance has been improved in all modules.

   2. New modules for JPEG 2000, AIFF, and WAVE formats.  Be sure to
      update the configuration file, jhove/conf/jhove.conf, to include
      these modules:

      ```xml
      <module>
        <class>edu.harvard.hul.ois.jhove.module.AiffModule</class>
      </module>
      <module>
        <class>edu.harvard.hul.ois.jhove.module.WaveModule</class>
      </module>
      <module>
        <class>edu.harvard.hul.ois.jhove.module.Jpeg2000Module</class>
      </module>
      ```

   3. Bug reading unsigned integers has been fixed.

### PDF Module

   1. More information provided about encryption keys.

   2. UserAccess property now shows "No permissions" if no bits are set.

### GIF Module

   1. Unexpected EOF is now handled cleanly.

### JPEG Module

   1. Exif data exception properly thrown.

### TIFF Module

   1. Identification of Exif profile has been improved.

   2. Photoshop tags 34377 and 50255 are now recognized.

   3. Bug in handling ExtraSamples tag fixed.

   4. Bug in determining valid date/time formats; the range for hours was
      incorrectly constrained to 1-24, rather than 0-24.

### XML Module

   1. If no encoding is specified, encoding is now reported as UTF-8.

   2. Catches and reports UTFDataFormatException.

   3. A greater range of parsers (including Xerces) now will do
      schema validation.

### XML Handler

   1. Omitted values in NisoImageMetadata were being reported in XML
      in some cases as default values (e.g., -1).  These have been
      suppressed.

   2. `<PlanarConfiguration>` element was inappropriately nested underneath
      the `<Segments>` element.

   3. The "subMessage" attribute is now properly defined in the jhove.xsd
      schema.
