package photoshop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;

import struct.JavaStruct;
import struct.StructException;
import struct.StructPacker;
import struct.StructUnpacker;

/**
 * A class for reading and writing Adobe Photoshop Color Book format. Format
 * details is given in http://magnetiq.com/docs/PhotoshopColorBook.txt Thanks to
 * Ates Goral for his help.
 * 
 * JavaStruct makes it very easy to read header information. But reading color
 * datais tricky, because format changes depending to Color space idenifier
 * (RGB, CMYK or Lab) we have to read them conditionally, and there is some
 * repeated code.There could be a better way, but this is simpler to understand.
 * 
 * Two example acb files are included in the project. They are used in the main
 * method of this class.
 * 
 * @author mdakin
 */
public class AdobeColorBook {
	ACBHeader header;
	RGBColor[] rgbColors = null;
	CMYKColor[] cmykColors = null;
	LabColor[] labColors = null;

	public void read(String acbFile) {
		try {
			FileInputStream fis = new FileInputStream(new File(acbFile));
			header = new ACBHeader();
			StructUnpacker up = JavaStruct.getUnpacker(fis,
					ByteOrder.BIG_ENDIAN);
			up.readObject(header);
			switch (header.ColorSpaceID) {
			case ACBHeader.RGB:
				rgbColors = new RGBColor[header.colorCount];
				for (int i = 0; i < header.colorCount; i++) {
					rgbColors[i] = new RGBColor();
					up.readObject(rgbColors[i]);
				}
				break;
			case ACBHeader.CMYK:
				cmykColors = new CMYKColor[header.colorCount];
				for (int i = 0; i < header.colorCount; i++) {
					cmykColors[i] = new CMYKColor();
					up.readObject(cmykColors[i]);
				}
				break;
			case ACBHeader.LAB:
				labColors = new LabColor[header.colorCount];
				for (int i = 0; i < header.colorCount; i++) {
					labColors[i] = new LabColor();
					up.readObject(labColors[i]);
				}
				break;
			default:
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (StructException e) {
			e.printStackTrace();
		}
	}

	public void write(String name) {
		try {
			FileOutputStream fo = new FileOutputStream(new File(name));
			StructPacker packer = JavaStruct
					.getPacker(fo, ByteOrder.BIG_ENDIAN);
			packer.writeObject(header);
			switch (header.ColorSpaceID) {
			case ACBHeader.RGB:
				for (RGBColor color : rgbColors) {
					packer.writeObject(color);
				}
				break;
			case ACBHeader.CMYK:
				for (CMYKColor color : cmykColors) {
					packer.writeObject(color);
				}
				break;
			case ACBHeader.LAB:
				for (LabColor color : labColors) {
					packer.writeObject(color);
				}
				break;
			default:
			}
			fo.close();
		} catch (StructException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String toString() {
		String str = "";
		if (header != null) {
			str += header;
		}
		switch (header.ColorSpaceID) {
		case ACBHeader.RGB:
			for (RGBColor color : rgbColors) {
				str += color + "\n";
			}
			break;
		case ACBHeader.CMYK:
			for (CMYKColor color : cmykColors) {
				str += color + "\n";
			}
			break;
		case ACBHeader.LAB:
			for (LabColor color : labColors) {
				str += color + "\n";
			}
			break;
		}
		return str;
	}

	public static void main(String[] args) {
		AdobeColorBook anpaBook = new AdobeColorBook();
		anpaBook.read("ANPA Color.acb");
		anpaBook.write("ANPA_my_Color.acb");
		System.out.println(anpaBook);

		AdobeColorBook focoltoneBook = new AdobeColorBook();
		focoltoneBook.read("FOCOLTONE.acb");
		System.out.println(focoltoneBook);
	}
}
