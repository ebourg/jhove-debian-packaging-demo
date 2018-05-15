package com.mcgath.jhove.module;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.mcgath.jhove.module.png.IdatChunk;
import com.mcgath.jhove.module.png.PNGChunk;

import com.mcgath.jhove.module.png.*;
//import java.util.Vector;

import edu.harvard.hul.ois.jhove.*;

/**
 *   Module for validation and metadata extraction on PNG files.
 *   
 *   @author Gary McGath
 *   
 *   An earlier PNG module was submitted by Gian Uberto Lauri.
 *   A few bits of code are copied from it.
 *   It validates files but doesn't do much metadata
 *   extraction. I noticed it only after making a significant
 *   start on my own version. -- GDM
 */
public class PngModule extends ModuleBase {

	/**
	 * What would constitute a well-formed but invalid PNG? 
	 */
    /******************************************************************
     * DEBUGGING FIELDS.
     * All debugging fields should be set to false for release code.
     ******************************************************************/

	
    /******************************************************************
     * PRIVATE CLASS FIELDS.
     ******************************************************************/

    private static final String NAME = "PNG-gdm";
    private static final String RELEASE = "1.0";
    private static final int [] DATE = {2016, 2, 25};
    private static final String [] FORMAT = {
    	"PNG", " ISO/IEC 15948:2003", "Portable Network Graphics"
    };
    private static final String COVERAGE = 
    	"PNG (ISO/IEC 15948:2003)";
    private static final String [] MIMETYPE = {"image/png"};
    //private static final String [] ALT_MIMETYPE = {"image/x-png"};
	private static final String WELLFORMED = "Put well-formedness criteria here";
	private static final String VALIDITY = "Put validity criteria here";
	private static final String REPINFO = "Put repinfo note here";
	private static final String NOTE = null;
	private static final String RIGHTS = "Copyright 2016 by Gary McGath. " +
			"Released under the GNU Lesser General Public License.";
    private static final String NISO_IMAGE_MD = "NisoImageMetadata";

    /* Checksummer object */
    protected Checksummer _ckSummer;
    
    /* Top-level property list */
    protected List<Property> _propList;
    
    /* List of keyword properties. */
    protected List<Property> _keywordPropList;
    
    /* List of suggested palette properties. */
    protected List<Property> _spltList;

    /* Input stream wrapper which handles checksums */
    protected ChecksumInputStream _cstream;
    
    /* Data input stream wrapped around _cstream */
    protected DataInputStream _dstream;
    
    /* Top-level metadata property */
    protected Property _metadata;
    
    /* NISO metadata for the image */
    NisoImageMetadata _nisoData;
    
    /* Color type. Some chunks need to know this. */
    protected int _colorType;
    
    /* Set of ancillary chunks which aren't allowed to be duplicated
     * and have been encountered.
     */
    private Set<Integer> _ancillaryChunks;
    

    /* Critical chunk flags. */
    private boolean ihdrSeen;		// IHDR chunk has been seen
    private boolean plteSeen;		// PLTE chunk has been seen
    private boolean idatSeen;		// at least one IDAT chunk has been seen
    private boolean idatFinished;	// a different chunk has been seen after an IDAT chunk
    private boolean iendSeen;		// IEND chunk has been seen
    
    /* File signature bytes, found at the beginning of every well-formed PNG files. */
    private final static int _sigBytes[] = { 137, 80, 78, 71, 13, 10, 26, 10 };
    
    /******************************************************************
    * CLASS CONSTRUCTOR.
    ******************************************************************/
    /**
     *  Instantiate a <tt>PngModule</tt> object.
     */
    public PngModule() {
        super (NAME, RELEASE, DATE, FORMAT, COVERAGE, MIMETYPE, WELLFORMED,
                VALIDITY, REPINFO, NOTE, RIGHTS, false);
        Signature sig =
                new InternalSignature (_sigBytes, SignatureType.MAGIC, 
                                       SignatureUseType.MANDATORY, 0,
                                       "");
        _signature.add (sig);
        sig = new ExternalSignature (".png", SignatureType.EXTENSION,
                SignatureUseType.OPTIONAL);
        _signature.add (sig);
	}

    /******************************************************************
     * Parsing methods.
     ******************************************************************/

    /**
     *  Check if the digital object conforms to this Module's
     *  internal signature information.
     *
     *   @param file      A RandomAccessFile, positioned at its beginning,
     *                    which is generated from the object to be parsed
     *   @param stream    An InputStream, positioned at its beginning,
     *                    which is generated from the object to be parsed
     *   @param info      A fresh RepInfo object which will be modified
     *                    to reflect the results of the test
     */
    public void checkSignatures (File file, InputStream stream, RepInfo info) 
        throws IOException
    {
        int i;
        int ch;
        _dstream = getBufferedDataStream (stream, _je != null ?
                    _je.getBufferSize () : 0);
        for (i = 0; i < 8; i++) {
            try {
                ch = readUnsignedByte(_dstream, this);
            }
            catch (Exception e) {
                ch = -1;
            }
            if (ch != _sigBytes[i]) {
                info.setWellFormed (false);
                return;
            }
        }
        info.setModule (this);
        info.setFormat (_format[0]);
        info.setMimeType (_mimeType[0]);
        info.setSigMatch(_name);
    }

    
    /**
     *   Parse the content of a purported JPEG stream digital object and store the
     *   results in RepInfo.
     * 
     *   This function uses the JPEG-L method of detecting a marker following 
     *   a data stream, checking for a 0 high bit rather than an entire 0
     *   byte.  So long at no JPEG markers are defined with a value from 0
     *   through 7F, this is valid for all JPEG files.
     *
     *   @param stream    An InputStream, positioned at its beginning,
     *                    which is generated from the object to be parsed
     *   @param info       A fresh RepInfo object which will be modified
     *                    to reflect the results of the parsing
     *   @param parseIndex  Must be 0 in first call to <code>parse</code>.  If
     *                    <code>parse</code> returns a nonzero value, it must be
     *                    called again with <code>parseIndex</code> 
     *                    equal to that return value.
     */
    public int parse (InputStream stream, RepInfo info, int parseIndex)
        throws IOException
    {
        initParse ();
        info.setFormat (_format[0]);
        info.setMimeType (_mimeType[0]);
        info.setModule (this);
        /* We may have already done the checksums while converting a
        temporary file. */
        _ckSummer = null;
        if (_je != null && _je.getChecksumFlag () &&
        		info.getChecksum ().size () == 0) {
        	_ckSummer = new Checksummer ();
        	_cstream = new ChecksumInputStream (stream, _ckSummer);
        	_dstream = getBufferedDataStream (_cstream, _je != null ?
                 _je.getBufferSize () : 0);
        }
        else {
        	_dstream = getBufferedDataStream (stream, _je != null ?
                 _je.getBufferSize () : 0);
        }
        _propList = new LinkedList<Property> ();
        _metadata = new Property ("PNGMetadata",
             PropertyType.PROPERTY,
             PropertyArity.LIST,
             _propList);
        _nisoData = new NisoImageMetadata();
        Property nisoProp = new Property(NISO_IMAGE_MD,
                PropertyType.NISOIMAGEMETADATA, _nisoData);
        _propList.add(nisoProp);
        _keywordPropList = new LinkedList<Property> ();
        _spltList = new LinkedList<Property> ();
        ErrorMessage msg;
        
        // Check that the file header matching the PNG magic numbers
        for (int i = 0; i < _sigBytes.length; i++) {
        	int byt = readUnsignedByte (_dstream);
        	if (byt != _sigBytes[i]) {
        		msg = new ErrorMessage("File header does not match PNG signature");
        		info.setMessage(msg);
        		info.setWellFormed(false);
        		return 0;
        	}
        }
        
        // Loop through the chunks
        try {
        	for (;;) {
        		PNGChunk chunk = readChunkHead(_dstream);
        		if (chunk == null) {
        			break;
        		}
        		if (iendSeen) {
        			msg = new ErrorMessage ("IEND chunk is not last");
        			info.setMessage (msg);
        			info.setWellFormed (false);
        			return 0;
        		}
        		if (idatSeen && !(chunk instanceof IdatChunk)) {
        			idatFinished = true;
        		}
        		chunk.setModule(this);
        		chunk.setInputStream(_dstream);
        		chunk.setNisoMetadata(_nisoData);
        		chunk.setPropertyList(_propList);
        		chunk.processChunk(info);
        		long storedCRC = chunk.readCRC();
        		long calculatedCRC = chunk.getCRC();
        		if (storedCRC != calculatedCRC) {
        			msg = new ErrorMessage("Incorrect CRC in chunk " + chunk.chunkTypeString());
        			info.setMessage(msg);
        			info.setWellFormed(false);
        			return 0;
        		}
        	}
        }
        catch (PNGException e) {
        	// We've already reported a problem, so we just clean up here.
        	return 0;
        }
        catch (EOFException e) {
        	msg = new ErrorMessage ("Unexpected end of file",
        			_nByte);
        	info.setMessage (msg);
        	info.setWellFormed (false);
        	return 0;
        }
        catch (Exception e) {
        	// Miscellaneous exceptions really shouldn't come here.
        	// But it's better to catch them than let them fall through.
        	// Treat them as bugs.
        	msg = new ErrorMessage ("Exception " + e.getClass().getName());
        	info.setMessage (msg);
        	info.setWellFormed (false);
        	return 0;
        }
        
        /* Check for required chunks. */
        boolean criticalMissing = false;
        if (!ihdrSeen) {
        	msg = new ErrorMessage("No IHDR chunk");
        	info.setMessage (msg);
        	criticalMissing = true;
        }
        if (!idatSeen) {
        	msg = new ErrorMessage("No IDAT chunk");
        	info.setMessage (msg);
        	criticalMissing = true;
        }
        if (!iendSeen) {
        	msg = new ErrorMessage("No IEND chunk");
        	info.setMessage (msg);
        	criticalMissing = true;
        }
        if (criticalMissing) {
        	info.setWellFormed (false);
        	return 0;
		}
        
        /** PLTE is required with color type 3 and forbidden with types 0 and 4 */
        if (_colorType == 3 && !plteSeen) {
        	msg = new ErrorMessage ("No PLTE chunk, required with color type 3");
			info.setMessage (msg);
        	info.setWellFormed (false);
			return 0;
        }
        
        if ((_colorType == 0 || _colorType == 4) && plteSeen) {
			msg = new ErrorMessage ("PLTE chunk found, not allowed with color types 0 and 4");
			info.setMessage (msg);
        	info.setWellFormed (false);
			return 0;
        }

        // Add the keyword property list if it isn't empty.
        if (!_keywordPropList.isEmpty()) {
        	_propList.add(new Property ("Keywords",
        			PropertyType.PROPERTY,
        			PropertyArity.LIST,
        			_keywordPropList));
        			
        }
        
        // Add the splat property list if it isn't empty.
        if (!_spltList.isEmpty()) {
        	_propList.add(new Property ("Suggested palettes",
        			PropertyType.PROPERTY,
        			PropertyArity.LIST,
        			_spltList));
        }
        info.setProperty (_metadata);
        return 0;
    }
    
    /** This lets the module skip over the remainder of a chunk, not
     *  including the name, length,and CRC. It updates the CRC. */
    public void eatChunk(PNGChunk chnk) throws IOException {
    	chnk.skipBytes ((int) chnk.getLength());	
    }

	/** Add a keyword and value. Creating arbitrary properties
	 *  on the fly doesn't go well with JHOVE's approach, so
	 *  we make each property a Map, with keys Keyword, Value,
	 *  and optionally Language.
	 */
	public void addKeyword(String keywd, String val) {
		addKeyword (keywd, null, val, null);
	}

	/** Add a keyword, value, and language. */
	public void addKeyword(String keywd, String translatedKeywd, String val, String language) {
		//HashMap<String, String> map = new HashMap<String, String>();
		List<Property> props = new ArrayList<Property>();
		Property prop = new Property ("Keyword",
				PropertyType.PROPERTY,
				PropertyArity.LIST,
				props);
		props.add (new Property ("Key",
				PropertyType.STRING,
				keywd));
		props.add (new Property ("Value",
				PropertyType.STRING,
				val));
		if (language != null) {
			props.add (new Property ("Language",
					PropertyType.STRING,
					language));
		}
		if (translatedKeywd != null) {
			props.add (new Property ("Translated key",
					PropertyType.STRING,
					translatedKeywd));
		}
		_keywordPropList.add(prop);
	}
    
    /** Add a suggested palette */
	public void addSplt (String name, int sampleDepth, int numSamples) {
		List<Property> props = new ArrayList<Property>();
		Property prop = new Property ("Suggested palette",
				PropertyType.PROPERTY,
				PropertyArity.LIST,
				props);
		props.add (new Property ("Name",
				PropertyType.STRING,
				name));
		props.add (new Property ("Sample depth",
				PropertyType.INTEGER,
				sampleDepth));
		props.add (new Property ("Number of samples",
				PropertyType.INTEGER,
				numSamples));
		_spltList.add (prop);
	}
	
	
    /**
     *   Initializes the state of the module for parsing.
     */
    protected void initParse ()
    {
        super.initParse ();
        ihdrSeen = false;
        plteSeen = false;
        idatSeen = false;
        idatFinished = false;
        iendSeen = false;
        _ancillaryChunks = new HashSet<Integer>();
    }
    
    /* readChunkHead reads the type and length of a chunk and
     * leaves the rest to the specific chunk processing. */
    private PNGChunk readChunkHead(DataInputStream dstrm) throws IOException {
    	long chunkLength;
    	//String typeStr;
    	
    	try {
    		chunkLength = readUnsignedInt(dstrm, true);
    	}
    	catch (EOFException e) {
    		// If we get an EOF here, we're done reading chunks.
    		return null;
    	}
    	int sig = (int) readUnsignedInt(dstrm, true);
    	
    	return PNGChunk.makePNGChunk(chunkLength, sig);
    }

    
    /**********************************************************************
     * Chunk-specific functions.
     **********************************************************************/

    /** Note that an IHDR chunk has been seen */
    public void setIhdrSeen (boolean b) {
    	ihdrSeen = b;
    }
    
    /** Returns true if IHDR chunk has been seen */
    public boolean isIhdrSeen () {
    	return ihdrSeen;
    }
    
    /** Note that an IDAT chunk has been seen */
    public void setIdatSeen (boolean b) {
    	idatSeen = b;
    }
    
    /** Note that a PLTE chunk has been seen */
	public void setPlteSeen (boolean b) {
		plteSeen = b;
	}
    
	/** Note that an IEND chunk has been seen */
	public void setIendSeen (boolean b) {
		iendSeen = b;
	}
    
	/** Return true if IDAT chunk has been seen */
	public boolean isIdatSeen () {
		return idatSeen;
	}
    
	/** Return true if a non-IDAT chunk has been seen after an IDAT chunk */
	public boolean isIdatFinished () {
		return idatFinished;
	}
    
	/** Return true if PLTE chunk has been seen */
	public boolean isPlteSeen () {
		return plteSeen;
	}
	
	/* Record that an ancillary chunk has been seen. This is used
	 * only for chunks whose names start with a lower-case letter. */
	public void setChunkSeen (int chunkType) {
		_ancillaryChunks.add (chunkType);
	}
	
	/* Return true if setChunkSeen has been called for the specified
	 * chunk type. */
	public boolean isChunkSeen (int chunkType) {
		return _ancillaryChunks.contains(chunkType);
	}
    
	/** Set the color type. The IHDR processing will set this for
	 *  the benefit of chunks that need it.
	 */
	public void setColorType (int ct) {
		_colorType = ct;
	}
    
	/** Get the color type that was recorded from the IHDR chunk. */
	public int getColorType () {
		return _colorType;
    }
        
}