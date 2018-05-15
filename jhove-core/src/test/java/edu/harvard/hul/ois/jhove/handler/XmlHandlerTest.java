package edu.harvard.hul.ois.jhove.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import edu.harvard.hul.ois.jhove.NisoImageMetadata;
import edu.harvard.hul.ois.jhove.Rational;
import edu.harvard.hul.ois.jhove.TextMDMetadata;

@RunWith(JUnit4.class)
public class XmlHandlerTest {
	private static final Logger LOGGER = Logger.getLogger(XmlHandlerTest.class
			.getName());

	/* Values needed to generate the expected output */
	private static final int EXPECTED_IMAGE_WIDTH = 8192;
	private static final int EXPECTED_IMAGE_RESOLUTION = 564;
	private static final int EXPECTED_LAST_BYTE = 255;
	private static final int EXPECTED_BYTE_SIZE = 8;
	private static final int EXPECTED_NUMBER_OF_BYTES = 3;
	private static final long EXPECTED_DENOMINATOR = 4294967295L;
	private static final long EXPECTED_WP_NUMENATOR = 1343036288L;
	private static final long EXPECTED_REDX_NUMENATOR = 2748779008L;
	private static final long EXPECTED_REDY_NUMENATOR = 1417339264L;
	private static final long EXPECTED_GREENX_NUMENATOR = 1288490240L;
	private static final long EXPECTED_GREENY_NUMENATOR = 2576980480L;
	private static final long EXPECTED_BLUEX_NUMENATOR = 644245120L;
	private static final long EXPECTED_BLUEY_NUMENATOR = 257698032L;
	
	private static final int EXPECTED_JPEG_COMPRESSION = 6;
	private static final int EXPECTED_EXIF_COLORSPACE = 6;
	private static final int EXPECTED_EXIF_IMAGE_LENGTH = 2322;
	private static final int EXPECTED_EXIF_IMAGE_WIDTH = 4128;
	private static final int EXPECTED_EXIF_IMAGE_RESOLUTION = 72;
	private static final String EXPECTED_EXIF_VERSION = "0220";
	private static final double EXPECTED_EXIF_FOCAL = 4.13;
	private static final int EXPECTED_EXIF_APERTURE_NUM = 228;
	private static final int EXPECTED_EXIF_APERTURE_DEN = 100;
	private static final double EXPECTED_EXIT_FNUMBER = 2.2;

	/* Test instances to be serialized */
	protected static NisoImageMetadata TEST_NISO_IMAGE_MD;
	protected static NisoImageMetadata TEST_NISO_EXIF_MD;
	protected static TextMDMetadata TEST_TEXTMD;

	private File outputFile;
	private XmlHandler handler;

	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		// Define the test instance for NisoImageMetadata to be serialized
		TEST_NISO_IMAGE_MD = new NisoImageMetadata();
		TEST_NISO_IMAGE_MD.setByteOrder(NisoImageMetadata.BYTEORDER[1]);
		TEST_NISO_IMAGE_MD.setCompressionScheme(1);
		TEST_NISO_IMAGE_MD.setImageWidth(EXPECTED_IMAGE_WIDTH);
		TEST_NISO_IMAGE_MD.setImageLength(EXPECTED_IMAGE_WIDTH);
		TEST_NISO_IMAGE_MD.setColorSpace(2);
		TEST_NISO_IMAGE_MD.setProfileName("Adobe RGB (1998)");
		Rational r0 = new Rational(0, 1);
		Rational r255 = new Rational(EXPECTED_LAST_BYTE, 1);
		TEST_NISO_IMAGE_MD.setReferenceBlackWhite(new Rational[] { r0, r255,
				r0, r255, r0, r255 });
		TEST_NISO_IMAGE_MD.setDateTimeCreated("2015-07-15T16:47:26");
		TEST_NISO_IMAGE_MD.setImageProducer("BnF");
		TEST_NISO_IMAGE_MD.setScanningSoftware("Archiver");
		TEST_NISO_IMAGE_MD.setOrientation(1);
		TEST_NISO_IMAGE_MD.setSamplingFrequencyUnit(2);
		TEST_NISO_IMAGE_MD.setXSamplingFrequency(new Rational(
				EXPECTED_IMAGE_RESOLUTION, 1));
		TEST_NISO_IMAGE_MD.setYSamplingFrequency(new Rational(
				EXPECTED_IMAGE_RESOLUTION, 1));
		TEST_NISO_IMAGE_MD.setBitsPerSample(new int[] { EXPECTED_BYTE_SIZE,
				EXPECTED_BYTE_SIZE, EXPECTED_BYTE_SIZE });
		TEST_NISO_IMAGE_MD.setSamplesPerPixel(EXPECTED_NUMBER_OF_BYTES);
		TEST_NISO_IMAGE_MD.setWhitePointXValue(new Rational(
				EXPECTED_WP_NUMENATOR, EXPECTED_DENOMINATOR));
		TEST_NISO_IMAGE_MD.setWhitePointYValue(new Rational(
				EXPECTED_WP_NUMENATOR, EXPECTED_DENOMINATOR));
		TEST_NISO_IMAGE_MD.setPrimaryChromaticitiesRedX(new Rational(
				EXPECTED_REDX_NUMENATOR, EXPECTED_DENOMINATOR));
		TEST_NISO_IMAGE_MD.setPrimaryChromaticitiesRedY(new Rational(
				EXPECTED_REDY_NUMENATOR, EXPECTED_DENOMINATOR));
		TEST_NISO_IMAGE_MD.setPrimaryChromaticitiesGreenX(new Rational(
				EXPECTED_GREENX_NUMENATOR, EXPECTED_DENOMINATOR));
		TEST_NISO_IMAGE_MD.setPrimaryChromaticitiesGreenY(new Rational(
				EXPECTED_GREENY_NUMENATOR, EXPECTED_DENOMINATOR));
		TEST_NISO_IMAGE_MD.setPrimaryChromaticitiesBlueX(new Rational(
				EXPECTED_BLUEX_NUMENATOR, EXPECTED_DENOMINATOR));
		TEST_NISO_IMAGE_MD.setPrimaryChromaticitiesBlueY(new Rational(
				EXPECTED_BLUEY_NUMENATOR, EXPECTED_DENOMINATOR));

		// Define the test instance of Exif for NisoImageMetadata to be serialized
		TEST_NISO_EXIF_MD = new NisoImageMetadata();
		TEST_NISO_EXIF_MD.setByteOrder(NisoImageMetadata.BYTEORDER[0]);
		TEST_NISO_EXIF_MD.setCompressionScheme(EXPECTED_JPEG_COMPRESSION);
		TEST_NISO_EXIF_MD.setImageWidth(EXPECTED_EXIF_IMAGE_WIDTH);
		TEST_NISO_EXIF_MD.setImageLength(EXPECTED_EXIF_IMAGE_LENGTH);
		TEST_NISO_EXIF_MD.setColorSpace(EXPECTED_EXIF_COLORSPACE);
		TEST_NISO_EXIF_MD.setYCbCrPositioning(1);
		TEST_NISO_EXIF_MD.setDateTimeCreated("2015-02-13T14:06:36");
		TEST_NISO_EXIF_MD.setDigitalCameraManufacturer("SAMSUNG");
		TEST_NISO_EXIF_MD.setDigitalCameraModelName("SM-N9005");
		TEST_NISO_EXIF_MD.setFNumber(EXPECTED_EXIT_FNUMBER);
		TEST_NISO_EXIF_MD.setExposureProgram(2);
		TEST_NISO_EXIF_MD.setExifVersion(EXPECTED_EXIF_VERSION);
		Rational r228 = new Rational(EXPECTED_EXIF_APERTURE_NUM, EXPECTED_EXIF_APERTURE_DEN);
		TEST_NISO_EXIF_MD.setMaxApertureValue(r228);
		TEST_NISO_EXIF_MD.setMeteringMode(2);
		TEST_NISO_EXIF_MD.setFocalLength(EXPECTED_EXIF_FOCAL);
		TEST_NISO_EXIF_MD.setSamplingFrequencyUnit(2);
		Rational r72 = new Rational(EXPECTED_EXIF_IMAGE_RESOLUTION, 1);
		TEST_NISO_EXIF_MD.setXSamplingFrequency(r72);
		TEST_NISO_EXIF_MD.setYSamplingFrequency(r72);
		TEST_NISO_EXIF_MD.setBitsPerSample(new int[] { EXPECTED_BYTE_SIZE,
				EXPECTED_BYTE_SIZE, EXPECTED_BYTE_SIZE });
		TEST_NISO_EXIF_MD.setSamplesPerPixel(EXPECTED_NUMBER_OF_BYTES);

		// Define the test instance for TextMD to be serialized
		TEST_TEXTMD = new TextMDMetadata();
		TEST_TEXTMD.setCharset(TextMDMetadata.CHARSET_UTF8);
		TEST_TEXTMD.setByte_order(TextMDMetadata.BYTE_ORDER_LITTLE);
		TEST_TEXTMD.setByte_size("" + EXPECTED_BYTE_SIZE);
		TEST_TEXTMD.setCharacter_size("variable");
		TEST_TEXTMD.setLinebreak(TextMDMetadata.LINEBREAK_LF);
		TEST_TEXTMD.setMarkup_basis("XML");
		TEST_TEXTMD.setMarkup_basis_version("1.0");
		TEST_TEXTMD
				.setMarkup_language("http://www.web3d.org/specifications/x3d-3.1.xsd");
		TEST_TEXTMD.setMarkup_language_version("3.1");
	}

	@Before
	public void setUp() throws IOException {
		// Prepare for a new test
		this.outputFile = File.createTempFile("jhove_", ".xml");
		PrintWriter writer = new PrintWriter(outputFile);
		this.handler = new XmlHandler();
		this.handler.setWriter(writer);
	}

	@After
	public void tearDown() {
		if (this.outputFile != null && this.outputFile.exists()) {
			this.outputFile.delete();
		}
	}

	@Test
	public void testShowNisoImageMetadata02() throws IOException {
		File mix02File = new File(this.getClass().getResource("mix02_output.xml").getPath());
		LOGGER.info("testShowNisoImageMetadata02 with file " + mix02File);
		assertTrue(mix02File.isFile());
		String expectedMix02 = readXmlFile(mix02File);

		this.handler.showNisoImageMetadata02(TEST_NISO_IMAGE_MD);
		this.handler.close();

		String generatedMix = readXmlFile(outputFile);
		assertEquals("Mix v0.2 generated not conformant", expectedMix02,
				generatedMix);
	}

	@Test
	public void testShowNisoImageMetadata10() throws IOException {
		File mix10File = new File(this.getClass().getResource("mix10_output.xml").getPath());
		LOGGER.info("testShowNisoImageMetadata10 with file " + mix10File);
		assertTrue(mix10File.isFile());
		String expectedMix10 = readXmlFile(mix10File);

		this.handler.showNisoImageMetadata10(TEST_NISO_IMAGE_MD);
		this.handler.close();

		String generatedMix = readXmlFile(outputFile);
		assertEquals("Mix v1.0 generated not conformant", expectedMix10,
				generatedMix);
	}

	@Test
	public void testShowNisoImageMetadata20() throws IOException {
		File mix20File = new File(this.getClass().getResource("mix20_output.xml").getPath());
		LOGGER.info("testShowNisoImageMetadata20 with file " + mix20File);
		assertTrue(mix20File.isFile());
		String expectedMix20 = readXmlFile(mix20File);

		this.handler.showNisoImageMetadata20(TEST_NISO_IMAGE_MD);
		this.handler.close();

		String generatedMix = readXmlFile(outputFile);
		assertEquals("Mix v2.0 generated not conformant", expectedMix20,
				generatedMix);
	}

	@Test
	public void testShowNisoExifMetadata02() throws IOException {
		File mix02File = new File(this.getClass().getResource("exif_mix02.xml").getPath());
		LOGGER.info("testShowNisoExifMetadata02 with file " + mix02File);
		assertTrue(mix02File.isFile());
		String expectedMix02 = readXmlFile(mix02File);

		this.handler.showNisoImageMetadata02(TEST_NISO_EXIF_MD);
		this.handler.close();

		String generatedMix = readXmlFile(outputFile);
		assertEquals("Exif Mix v0.2 generated not conformant", expectedMix02,
				generatedMix);
	}

	@Test
	public void testShowNisoExifMetadata10() throws IOException {
		File mix10File = new File(this.getClass().getResource("exif_mix10.xml").getPath());
		LOGGER.info("testShowNisoExifMetadata10 with file " + mix10File);
		assertTrue(mix10File.isFile());
		String expectedMix10 = readXmlFile(mix10File);

		this.handler.showNisoImageMetadata10(TEST_NISO_EXIF_MD);
		this.handler.close();

		String generatedMix = readXmlFile(outputFile);
		assertEquals("Exif Mix v1.0 generated not conformant", expectedMix10,
				generatedMix);
	}

	@Test
	public void testShowNisoExifMetadata20() throws IOException {
		File mix20File = new File(this.getClass().getResource("exif_mix20.xml").getPath());
		LOGGER.info("testShowNisoExifMetadata20 with file " + mix20File);
		assertTrue(mix20File.isFile());
		String expectedMix20 = readXmlFile(mix20File);

		this.handler.showNisoImageMetadata20(TEST_NISO_EXIF_MD);
		this.handler.close();

		String generatedMix = readXmlFile(outputFile);
		assertEquals("Exif Mix v2.0 generated not conformant", expectedMix20,
				generatedMix);
	}

	@Test
	public void testShowTextMDMetadata() throws IOException {
		File textMD30File = new File(this.getClass().getResource("text30_output.xml").getPath());
		LOGGER.info("testShowTextMDMetadata with file " + textMD30File);
		assertTrue(textMD30File.isFile());
		String expectedText30 = readXmlFile(textMD30File);

		this.handler.showTextMDMetadata(TEST_TEXTMD);
		this.handler.close();

		String generatedTextMD = readXmlFile(outputFile);
		assertEquals("TextMD v3.0 generated not conformant", expectedText30,
				generatedTextMD);
	}

	
	/**
	 * Reads an XML file into an one line string and eliminates the multiple spaces.  
	 * @param f the xml file
	 * @return one line with the content
	 * @throws IOException
	 */
	private static String readXmlFile(File f) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new FileReader(f));
		try {
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
		} finally {
			br.close();
		}
		return sb.toString().replaceAll("\\s+", " ");
	}
}
