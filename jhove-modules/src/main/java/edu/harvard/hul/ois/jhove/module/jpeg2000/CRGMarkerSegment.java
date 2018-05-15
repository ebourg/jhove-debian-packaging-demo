/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2004 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.jpeg2000;

import edu.harvard.hul.ois.jhove.*;
import java.io.*;

/**
 *
 *  Class for the CRG (component registration)
 *  marker segment.
 * 
 * @author Gary McGath
 *
 */
public class CRGMarkerSegment extends MarkerSegment {

    /**
     * Constructor.
     */
    public CRGMarkerSegment() {
        super();
    }


    /**
     * Processes the marker segment.  The DataInputStream
     *  will be at the point of having read the marker code.  The
     *  <code>process</code> method must consume exactly the number
     *  of bytes remaining in the marker segment.
     * 
     *  @param    bytesToEat   The number of bytes that must be consumed.
     *                         If it is 0 for a MarkerSegment, the
     *                         number of bytes to consume is unknown.
     */
    protected boolean process(int bytesToEat) throws IOException 
    {
        if (_ccs.getCurTile () != null) {
            _repInfo.setMessage (new ErrorMessage
                    (MessageConstants.ERR_CRG_MARKER_POSITION_INVALID));
            return false;
        }
        int ncomps = _cs.getNumComponents ();
        if (ncomps * 4 != bytesToEat) {
            _repInfo.setMessage (new ErrorMessage
                    (MessageConstants.ERR_CRG_MARKER_SIZE_INVALID));
        }
        int[] horOffsets = new int[ncomps];
        int[] vertOffsets = new int[ncomps];
        for (int i = 0; i < ncomps; i++) {
            horOffsets[i] = _module.readUnsignedShort (_dstream);
            vertOffsets[i] = _module.readUnsignedShort (_dstream);
        }
        Property[] props = new Property[2];
        props[0] = new Property ("HorizontalOffsets",
                    PropertyType.INTEGER,
                    PropertyArity.ARRAY,
                    horOffsets);
        props[1] = new Property ("VerticalOffsets",
                    PropertyType.INTEGER,
                    PropertyArity.ARRAY,
                    vertOffsets);
        _cs.setCRGProperty (new Property ("ComponentRegistration",
            PropertyType.PROPERTY,
            PropertyArity.ARRAY,
            props));
        
        return true;
    }
}
