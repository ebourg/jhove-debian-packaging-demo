/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 *
 * Copyright (c) 2009 by The Regents of the University of California,
 * Ithaka Harbors, Inc., and The Board of Trustees of the Leland Stanford
 * Junior University.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * o Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * o Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * o Neither the name of the University of California/California Digital
 *   Library, Ithaka Harbors/Portico, or Stanford University, nor the names of
 *   its contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package edu.harvard.hul.ois.jhove.module.gzip;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumerated type for GZip supported compression types (extra flags
 * in GZip header).
*/
public class CompressionType {
    /** The list of valid values. */
    private static Map<Integer, CompressionType> values = new HashMap<Integer, CompressionType>();

    /** GZip extra flag value for maximum compression. */
    public final static CompressionType MAXIMUM_COMPRESSION = new CompressionType(2, "Maximum compression", true);
    /** GZip extra flag value for fastest algorithm. */
    public final static CompressionType FASTEST_ALGORITHM = new CompressionType(4, "Fastest algorithm", true);

    /**
     * Initializes the valid values.
     */
    static {
        values.put(MAXIMUM_COMPRESSION.value, MAXIMUM_COMPRESSION);
        values.put(FASTEST_ALGORITHM.value, FASTEST_ALGORITHM);
    }
    
    /** The integer value for the enum instance. */
    public final int value;
    /** The value description. */
    public final String label;
    /** Whether the value is valid. */
    public final boolean valid;

    /** 
     * Constructor.
     * @param value The compression type value.
     * @param label The name of the compression type.
     * @param valid If it is a valid compression type.
     */
    protected CompressionType(int value, String label, boolean valid) {
        this.value = value;
        this.label = label;
        this.valid = valid;
    }

    /**
     * Returns the enumerated value object corresponding to the
     * specified integer value. If the integer value is unknown, an
     * instance marked as not valid is returned.
     * @param  n   the integer value to map.
     *
     * @return a compression type objet, valid if <code>n</code> is
     *         one of the defined valid values.
     */
    public static CompressionType fromValue(int n) {
        CompressionType v = values.get(Integer.valueOf(n));
        if (v == null) {
            v = new CompressionType(n, null, false);
        }
        return v;
    }
}
