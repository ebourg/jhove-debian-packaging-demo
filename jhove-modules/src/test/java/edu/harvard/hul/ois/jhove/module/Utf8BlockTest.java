package edu.harvard.hul.ois.jhove.module;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class Utf8BlockTest {

	/** This test makes sure that the code blocks are declared in monotonically
	 *  increasing order without overlap, each one has a positive range,
	 *  and names aren't duplicated.
	 */
	@Test
	public void testBlocks() {
		int lastEnd = -1;
		Set<String> names = new HashSet<String>();
		for (Utf8Block blk : Utf8Block.unicodeBlock) {
			int start = blk.getStart();
			int end = blk.getEnd();
			String name = blk.getName();
			String note = "Bad block: " + name + ",  " + 
					String.format ("%05X", start) + " = " +
					String.format ("%05X", end);
			assertTrue (note, end > start);
			assertTrue (note, start > lastEnd);
			assertFalse ("Duplicate name " + name, names.contains(name));
			lastEnd = end;
			names.add (name);
		}
	}

}
