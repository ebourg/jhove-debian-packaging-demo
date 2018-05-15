package com.mcgath.jhove.module.png;

import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.Property;
import edu.harvard.hul.ois.jhove.PropertyType;
import edu.harvard.hul.ois.jhove.RepInfo;

/** The iCCP (color profile) chunk */
public class IccpChunk extends PNGChunk {

	/** Constructor */
	public IccpChunk(int sig, long leng) {
		chunkType = sig;
		length = leng;
		ancillary = true;
		duplicateAllowed = false;
	}
	
	public void processChunk(RepInfo info) throws Exception {
		final String badChunk = "Bad iCCP chunk";
		processChunkCommon(info);
		
		ErrorMessage msg = null;
		if (_module.isPlteSeen()) {
			msg = new ErrorMessage ("iCCP chunk is not allowed after PLTE chunk");
		}
		else if (_module.isIdatSeen()) {
			msg = new ErrorMessage ("iCCP chunk is not allowed after IDAT chunk");
		}
		else if (_module.isChunkSeen(PNGChunk.sRGB_HEAD_SIG)) {
			msg = new ErrorMessage ("iCCP and sRGB chunks are not allowed in the same file");
		}
		if (msg != null) {
			info.setMessage (msg);
			info.setWellFormed (false);
			throw new PNGException (badChunk);
		}

		// The iCCP chunk consists of a name, a null, 
		// a compression type (1 byte), and the profile.
		
		// state: 0 = reading name, 1 = reading compression type,
		//    2 = discarding profile data
		int state = 0;
		int compression = -1;
		StringBuilder nameSB = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int c = readUnsignedByte();
			switch (state) {
			case 0:
				if (c == 0) {
					//null ends the name
					state = 1;
				} else {
					nameSB.append((char) c);
				}
				break;
			case 1:
				// Compression is 1 byte. Save it and go to discarding-profile state
				compression = c;
				state = 2;
				break;
			case 2:
			default:
				// Just throw the profile data away
				break;
			}
		}
		if (state != 2) {
			// If we didn't reach state 2, something's wrong with the chunk structure
			msg = new ErrorMessage ("Malformed iCCP chunk");
			info.setMessage (msg);
			info.setWellFormed (false);
			throw new PNGException (badChunk);
		}
		Property profile = new Property ("ICC Profile name",
				PropertyType.STRING,
				nameSB.toString());
		String cmprsType;
		if (compression == 0) {
			cmprsType = "Deflate";
		}
		else {
			cmprsType = Integer.toString (compression);
		}
		Property profileCompression = new Property ("ICC Profile compression",
				PropertyType.STRING,
				cmprsType);
		_propList.add (profile);
		_propList.add (profileCompression);
	}
}
