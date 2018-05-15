/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.pdf;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * The Parser class implements some limited syntactic analysis for PDF.
 * It isn't by any means intended to be a full parser.  Its main job is
 * to track nesting of syntactic elements such as dictionary and array
 * beginnings and ends.
 */
public class Parser
{
    /** The Tokenizer which the parser will use. */
    private Tokenizer _tokenizer;
    /** The number of dictionary starts on stack. */
    private int _dictDepth;
    /** The number of array starts on stack. */
    private int _arrayDepth;
    /** The file's object map. */
    private Map<Long, PdfObject> _objectMap;
    /** True if the document is encrypted. */
    private boolean _encrypted;
    /** PDF/A compliance flag. */
    private boolean _pdfACompliant;


    /**
     * Constructor.  A Parser works with a Tokenizer that feeds it tokens.
     *
     * @param tokenizer  The Tokenizer which the parser will use
     */
    public Parser (Tokenizer tokenizer)
    {
        _tokenizer = tokenizer;
        _pdfACompliant = true;
        reset ();
    }

    /** Sets the object map on which the parser will work. */
    public void setObjectMap (Map<Long, PdfObject> objectMap)
    {
        _objectMap = objectMap;
    }

    /**
     * Clears the state of the parser so that it can start
     * reading at a different place in the file.  Clears the
     * stack and the dictionary and array depth counters.
     */
    public void reset () {
        _dictDepth = 0;
        _arrayDepth = 0;
    }

    /**
     * Clears the state of the parser so that it can start
     * reading at a different place in the file and ignore
     * any nesting errors.  Sets the stack and the dictionary
     * and array depth counters to a large number so that
     * nesting exceptions won't be thrown.
     */
    public void resetLoose () {
        _dictDepth = 1000000;
        _arrayDepth = 1000000;
    }

    /**
     * Gets a token.  Uses Tokenizer.getNext, and keeps track
     * of the depth of dictionary and array nesting.
     */
    public Token getNext () throws IOException, PdfException
    {
        return getNext (0L);
    }

    /**
     * Gets a token.  Uses Tokenizer.getNext, and keeps track
     * of the depth of dictionary and array nesting.
     *
     * @param max  Maximum allowable size of the token
     */
    public Token getNext (long max) throws IOException, PdfException
    {
        Token tok = _tokenizer.getNext (max);
        if (tok instanceof DictionaryStart) {
            ++_dictDepth;
        }
        else if (tok instanceof DictionaryEnd) {
            --_dictDepth;
            if (_dictDepth < 0) {
                throw new PdfMalformedException (MessageConstants.ERR_DICT_DELIMETERS_IMPROPERLY_NESTED);
            }
        }
        if (tok instanceof ArrayStart) {
            ++_arrayDepth;
        }
        else if (tok instanceof ArrayEnd) {
            --_arrayDepth;
            if (_arrayDepth < 0) {
                throw new PdfMalformedException (MessageConstants.ERR_ARRAY_IMPROPERLY_NESTED);
            }
        }
        return tok;
    }

    /**
     * A class-sensitive version of <code>getNext</code>.  The token which is
     * obtained must be of the specified class (or a subclass thereof),
     * or a <code>PdfInvalidException</code> with message <code>errMsg</code>
     * will be thrown.
     */
    public Token getNext (Class<?> clas, String errMsg)
            throws IOException, PdfException
    {
        Token tok = getNext ();
        if (!clas.isInstance (tok)) {
            throw new PdfInvalidException (errMsg);
        }
        if (!tok.isPdfACompliant()) {
            _pdfACompliant = false;
        }
        return tok;
    }

    /**
     * Returns the number of dictionary starts not yet matched by
     * dictionary ends.
     */
    public int getDictDepth ()
    {
        return _dictDepth;
    }

    /**
     * Tells this Parser, and its Tokenizer, whether the file is encrypted.
     */
    public void setEncrypted (boolean encrypted)
    {
        _encrypted = encrypted;
        _tokenizer.setEncrypted (encrypted);
    }

    /**
     * Returns the number of array starts not yet matched by array ends.
     */
    public int getArrayDepth ()
    {
        return _arrayDepth;
    }

    /** Returns the Tokenizer's current whitespace string. */
    public String getWSString () {
        return _tokenizer.getWSString ();
    }

    /** Returns the language code set from the Tokenizer. */
    public Set<String> getLanguageCodes ()
    {
        return _tokenizer.getLanguageCodes ();
    }

    /**
     * Returns false if either the parser or the tokenizer has detected
     * non-compliance with PDF/A restrictions.  A value of <code>true</code>
     * is no guarantee that the file is compliant.
     */
    public boolean getPDFACompliant ()
    {
        if (!_tokenizer.getPDFACompliant ()) {
            _pdfACompliant = false;
        }
        return _pdfACompliant;
    }

    /**
     * Sets the value of the pdfACompliant flag.  This may be used to
     * clear previous detection of noncompliance.  If the parameter
     * has a value of <code>true</code>, the tokenizer's pdfACompliant
     * flag is also set to <code>true</code>.
     */
    public void setPDFACompliant (boolean pdfACompliant)
    {
        _pdfACompliant = pdfACompliant;
        if (pdfACompliant) {
            _tokenizer.setPDFACompliant (true);
        }
    }

    /**
     * Reads an object definition, from wherever we are in the stream to
     * the completion of one full object after the obj keyword.
     */
    public PdfObject readObjectDef () throws IOException, PdfException
    {
        Numeric objNumTok = (Numeric) getNext 
            (Numeric.class, MessageConstants.ERR_OBJ_DEF_INVALID);
        return readObjectDef (objNumTok);
    }

    /**
     * Reads an object definition, given the first numeric object, which
     * has already been read and is passed as an argument.  This is called
     * by the no-argument readObjectDef; the only other case in which it
     * will be called is for a cross-reference stream, which can be distinguished
     * from a cross-reference table only once the first token is read.
     */
    public PdfObject readObjectDef (Numeric objNumTok)
            throws IOException, PdfException
    {
        reset ();
        // The start of an object must be <num> <num> obj
        //Numeric objNumTok = (Numeric) getNext (Numeric.class, invDef);
        Numeric genNumTok = (Numeric) getNext (Numeric.class, MessageConstants.ERR_OBJ_DEF_INVALID);
        Keyword objKey = (Keyword) getNext (Keyword.class, MessageConstants.ERR_OBJ_DEF_INVALID);
        if (!"obj".equals (objKey.getValue ())) {
            throw new PdfMalformedException (MessageConstants.ERR_OBJ_DEF_INVALID);
        }
        if (_tokenizer.getWSString ().length () > 1) {
            _pdfACompliant = false;
        }
        PdfObject obj = readObject (false);

        // Now a special-case check to read a stream object, which
        // consists of a dictionary followed by a stream token.
        if (obj instanceof PdfDictionary) {
            Stream strm = null;
            try {
                strm = (Stream) getNext (Stream.class, "");
            }
            catch (Exception e) {
                // If we get an exception, it just means it wasn't a stream
            }
            if (strm != null) {
                // Assimilate the dictionary and the stream token into the
                // object to be returned
                PdfStream strmObj = new PdfStream ((PdfDictionary) obj, strm);
                if (!strmObj.isPdfaCompliant()) {
                    _pdfACompliant = false;
                }
                obj = strmObj;
            }
        }

        obj.setObjNumber (objNumTok.getIntegerValue ());
        obj.setGenNumber (genNumTok.getIntegerValue ());
        return obj;
    }

    /**
     * Reads an object.  By design, this reader has a number
     * of limitations:
     * <ul>
     *   <li>It doesn't retain the contents of streams</li>
     *   <li>It doesn't recognize a stream when it's pointing at
     *       the stream's dictionary; it will just read the
     *       dictionary</li>
     * </ul>
     * Functions which it uses may call it recursively to build up structures.
     * If it encounters a token inappropriate for an object start, it
     * throws a <code>PdfException</code> on which <code>getToken</code>
     * may be called to retrieve that token.
     */
    public PdfObject readObject (boolean allowPseudo) throws IOException, PdfException
    {
        Token tok = getNext ();
        if (tok instanceof ArrayStart) {
            return readArray ();
        }
        else if (tok instanceof DictionaryStart) {
            return readDictionary ();
        }
        // For the end of a dictionary or array, retu
        else if (allowPseudo && tok instanceof ArrayEnd) {
            return new PdfArrayEnd(tok);
        }
        else if (allowPseudo && tok instanceof DictionaryEnd) {
            return new PdfDictionaryEnd(tok);
        }
        else if (tok.isSimpleToken ()) {
            return new PdfSimpleObject (tok);
        }
        else {
            throw new PdfMalformedException 
              (MessageConstants.ERR_OBJ_NOT_PARSABLE, getOffset(), tok);
        }
    }

    /**
     * Reads a PDF array.  When this is called we have already read the
     * ArrayStart token and arrayDepth has been incremented to reflect this.
     */
    public PdfArray readArray () throws IOException, PdfException
    {
        PdfArray arr = new PdfArray ();
        for (;;) {
            PdfObject obj = readObject (true);
            if (!(obj instanceof PdfPseudoObject)) {
                arr.add (obj);
            }
            else if (obj instanceof PdfArrayEnd) {
                // We detect the end of an array by returning a PdfArrayEnd.
                // When we get the end of the array, collapse the vector
                // before returning the object.
                PdfArrayEnd eobj = (PdfArrayEnd) obj;
                Token tok = eobj.getToken();
                if (tok instanceof ArrayEnd) {
                    collapseObjectVector (arr.getContent ());
                    if (!arr.isPdfACompliant()) {
                        _pdfACompliant = false;
                    }
                    return arr;
                }
                throw new PdfMalformedException
                    (MessageConstants.ERR_ARRAY_CONTAINS_UMEXPECTED_TOKEN, getOffset());
            }
        }
    }

    /**
     * Reads a PDF dictionary.  When this is called, we have already read the
     * DictionaryStart token, and dictDepth has been incremented to reflect
     * this.  Only for use in this special case, where we're picking up
     * a dictionary in midstream.
     */
    public PdfDictionary readDictionary () throws IOException, PdfException
    {
        PdfDictionary dict = new PdfDictionary ();
        // Create a vector as a temporary holding place for the objects
        Vector<PdfObject> vec = new Vector<PdfObject> ();

        for (;;) {
            PdfObject obj = readObject (true);
            // Comments within a dictionary need to be ignored.
            if (obj instanceof PdfSimpleObject
                    && ((PdfSimpleObject) obj).getToken() instanceof Comment) {
                continue;
            }
            if (!(obj instanceof PdfPseudoObject)) {
                vec.add (obj);
            }
            else if (obj instanceof PdfDictionaryEnd) {
                // When we get the end of the dictionary,
                // collapse the vector before returning the object.
                PdfDictionaryEnd eobj = (PdfDictionaryEnd) obj;
                Token tok = eobj.getToken ();
                if (tok instanceof DictionaryEnd) {
                    collapseObjectVector (vec);
                    // The collapsed vector must contain an even number of objects
                    int vecSize = vec.size ();
                    if ((vecSize % 2) != 0) {
                        throw new PdfMalformedException (MessageConstants.ERR_VECTOR_OBJ_COUNT_NOT_EVEN + vecSize, getOffset ());
                    }
                    for (int i = 0; i < vecSize; i += 2) {
                        try {
                            Name key = (Name) ((PdfSimpleObject)
                                    vec.elementAt (i)).getToken ();
                            PdfObject value = vec.elementAt (i + 1);
                            dict.add (key.getValue (), value);
                        }
                        catch (Exception f) {
                            throw new PdfMalformedException (MessageConstants.ERR_DICT_MALFORMED, getOffset ());
                        }
                    }
                    if (!dict.isPdfACompliant()) {
                        _pdfACompliant = false;    // Exceeds implementation limit for PDF/A
                    }
                    return dict;
                }
                throw new PdfMalformedException
                (MessageConstants.ERR_DICT_CONTAINS_UNEXPECTED_TOKEN, getOffset());
            }
        }
    }

    /** Returns the current offset into the file. */
    public long getOffset ()
    {
        return _tokenizer.getOffset ();
    }

    /**
     * Positions the file to the specified offset, and
     * resets the state for a new token stream.
     */
    public void seek (long offset) throws IOException, PdfException
    {
        _tokenizer.seek (offset);
        reset ();
    }

    /**
     * PDF has a wacky grammar which must be a legacy of
     * PostScript's postfix syntax.  A keyword of R means that
     * the two previous objects are really part of an indirect object
     * reference.  This means that when a vector of objects is complete,
     * it has to be read backwards so that indirect object references can
     * be collapsed out.  In the case of a dictionary, this has to be done
     * before the content can be interpreted as key-value pairs.
     */
    private void collapseObjectVector (Vector<PdfObject> v) throws PdfException
    {
        int lowestChanged = -1;
        for (int i = v.size() - 1; i >= 2; i--) {
            PdfObject obj = v.elementAt (i);
            if (obj instanceof PdfSimpleObject) {
                Token tok = ((PdfSimpleObject) obj).getToken ();
                if (tok instanceof Keyword) {
                    if ("R".equals (((Keyword)tok).getValue ())) {
                        // We're in the key of 'R'.  The two previous tokens
                        // had better be Numerics.  Three objects in the Vector
                        // are replaced by one.
                        try {
                            PdfSimpleObject nobj =
                                    (PdfSimpleObject) v.elementAt (i - 2);
                            Numeric ntok = (Numeric) nobj.getToken ();
                            int objNum = ntok.getIntegerValue ();
                            nobj = (PdfSimpleObject) v.elementAt (i - 1);
                            ntok = (Numeric) nobj.getToken ();
                            int genNum = ntok.getIntegerValue ();
                            v.set (i - 2, new PdfIndirectObj
                                    (objNum, genNum, _objectMap));
                            //v.removeElementAt (i);
                            //v.removeElementAt (i - 1);
                            // Put in null as placeholder, to be removed below
                            v.set(i, null);
                            v.set(i - 1, null);
                            lowestChanged = i - 1;
                            i -= 2;
                        }
                        catch (Exception e) {
                            throw new PdfMalformedException 
                                (MessageConstants.ERR_INDIRECT_OBJ_REF_MALFORMED);
                        }
                    }
                }
            }
        }
        // Now remove all the positioned that were nulled.
        if (lowestChanged > 0) {
            int i, j;
            for (i = lowestChanged, j = lowestChanged; i < v.size(); i++) {
                PdfObject elem = v.elementAt(i);
                if (elem != null) {
                    v.set(j++, elem);
                }
            }
            v.setSize(j);
        }
    }

    /**
     * If <code>true</code>, do not attempt to parse non-whitespace
     * delimited tokens, e.g., literal and hexadecimal strings.
     *
     * @param flag  Scan mode flag
     */
    public void scanMode (boolean flag)
    {
        _tokenizer.scanMode (flag);
    }
}
