package photoshop;

import struct.StructClass;
import struct.StructField;

/**
 * ACBHeader struct for Adobe Photoshop Color book (acb) files. For complete
 * format information please look at:
 * http://magnetiq.com/docs/PhotoshopColorBook.txt
 * 
 * @author mdakin
 * 
 */
@StructClass
public class ACBHeader {

	public static final short RGB = 0;
	public static final short CMYK = 2;
	public static final short LAB = 7;

	// Signature is always ASCII characters "8BCB"
	@StructField(order = 0)
	public byte[] signature = { '8', 'B', 'C', 'B' };

	// Photoshop 7 only opens files with a version of 1 (0x0001)
	@StructField(order = 1)
	public short fileVersion;

	// Unique identifier..
	@StructField(order = 2)
	public short colorBookIdentifier;

	@StructField(order = 3)
	public AString title;

	@StructField(order = 4)
	public AString colorNamePrefix;

	@StructField(order = 5)
	public AString colorNamePostfix;

	// Generally a copyright notice
	@StructField(order = 6)
	public AString description;

	@StructField(order = 7)
	public short colorCount;

	// Photoshop displays colors page by page. This field specifies the
	// maximum number of colors that can appear on a page.
	@StructField(order = 8)
	public short colorsPerPage;

	@StructField(order = 9)
	public short pageSelectorOffset;

	/*
	 * Color Space Identifier: 0 = RGB 2 = CMYK 7 = Lab ...
	 */
	@StructField(order = 10)
	public short ColorSpaceID;

	public ACBHeader(String title, short colorSpace, int colorCount,
			String colorNamePrefix, String colorNamePostfix, String description) {
		this.fileVersion = 0x0001;
		this.title = new AString(title);
		this.colorNamePrefix = new AString(colorNamePrefix);
		this.colorNamePostfix = new AString(colorNamePostfix);
		this.description = new AString(description);
		this.ColorSpaceID = colorSpace;
		this.colorCount = (short) colorCount;
		this.colorsPerPage = 16;
		this.pageSelectorOffset = 0;
	}

	public ACBHeader() {
	}

	public String toString() {
		String str = "ACB Color Book\n";
		str += "Title: " + this.title + "\n";
		str += "Color Name Prefix: " + this.colorNamePrefix + "\n";
		str += "Color Name Postfix: " + this.colorNamePostfix + "\n";
		str += "Description: " + this.description + "\n";
		str += "Color count: " + this.colorCount + "\n";
		str += "Color per Page: " + this.colorsPerPage + "\n";
		str += "Page Selector Offset: " + this.pageSelectorOffset + "\n";
		str += "Color Space ID: " + this.ColorSpaceID + "\n";
		return str;
	}
}
