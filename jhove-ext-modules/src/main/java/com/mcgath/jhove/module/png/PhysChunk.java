package com.mcgath.jhove.module.png;

import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.Property;
import edu.harvard.hul.ois.jhove.PropertyType;
import edu.harvard.hul.ois.jhove.RepInfo;

/** The pHYS chunk, which indicates pixel aspect ratio or size.
 * 
 */
public class PhysChunk extends PNGChunk {

	/** Constructor */
	public PhysChunk(int sig, long leng) {
		chunkType = sig;
		length = leng;
		ancillary = true;
		duplicateAllowed = false;
	}
	
	/** Process the data in the chunk.
	 *  
	 */
	public void processChunk(RepInfo info) throws Exception {
		final String badChunk = "Bad pHYS chunk";
		processChunkCommon(info);
		ErrorMessage msg = null;
		if (_module.isIdatSeen()) {
			msg = new ErrorMessage ("pHYS chunk is not allowed after IDAT chunk");
		}
		if (length < 9) {
			msg = new ErrorMessage ("pHYS chunk too short");
		}
		if (msg != null) {
			info.setMessage (msg);
			info.setWellFormed (false);
			throw new PNGException (badChunk);
		}
		long xPixelsPerUnit = readUnsignedInt();
		long yPixelsPerUnit = readUnsignedInt();
		int unit = readUnsignedByte();
		Property prop = new Property ("X pixels per unit",
				PropertyType.INTEGER,
				xPixelsPerUnit);
		info.setProperty (prop);
		prop = new Property ("Y pixels per unit",
				PropertyType.INTEGER,
				yPixelsPerUnit);
		info.setProperty (prop);
		String unitStr;
		switch (unit) {
		case 0:
			unitStr = "Undefined";
			break;
		case 1:
			// Stick with British spelling, as in the spec
			unitStr = "Metre";
			break;
		default:
			// Should this even be allowed?
			unitStr = Integer.toString (unit);
			break;
		}
		prop = new Property ("Pixel unit",
				PropertyType.STRING,
				unitStr);
		info.setProperty (prop);
		
		// Discard any extra
		for (int i = 0; i < length - 9; i++) {
			readUnsignedByte();
		}
	}
}
