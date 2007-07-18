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

public class AdobeColorBook {
	ACBHeader header;
	
	public static void read(String acbFile){
		try {
			FileInputStream fis = new FileInputStream(new File(acbFile));
			ACBHeader header = new ACBHeader();
			StructUnpacker up = new StructUnpacker(header , fis, ByteOrder.BIG_ENDIAN);
			up.readObject(header);
			System.out.println(header);
			LabColor[] colors = new LabColor[header.colorCount];
			for(int i=0; i<header.colorCount; i++){
				colors[i] = new LabColor();
				up.readObject(colors[i]);
			}
			for (int i = 0; i < colors.length; i++) {
				System.out.println(colors[i]);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (StructException e) {
			e.printStackTrace();
		}
	}
	
	public static void write(String name){
		int colorCount = 16;
		ACBHeader header = new ACBHeader("Magnetiq", ACBHeader.RGB , colorCount, "la", "lo", "JavaStruct Test");
		RGBColor[] colors = new RGBColor[colorCount];
		for (int i=0 ; i < 16; i ++){
			colors[i] = new RGBColor(255/(i+1), 255/(i+1), 255/(i+1), "rgb"+i);
		}
		
		try{
			FileOutputStream fo = new FileOutputStream(new File("magnetiq.acb"));
			byte[] headerData = JavaStruct.pack(header);
			fo.write(headerData);
			for(int i=0; i<colorCount; i++){
				byte[] colorData = JavaStruct.pack(colors[i]);
				fo.write(colorData);
			}
			fo.close();
		} catch(StructException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		read("/home/mdakin/Color Books/ANPA Color.acb");
	}
}
