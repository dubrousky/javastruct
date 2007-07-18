package photoshop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import struct.JavaStruct;
import struct.StructException;

public class ACBWriter {
	
	public static void main(String[] args) {
		int colorCount = 16;
		ACBHeader header = new ACBHeader("Magnetiq", ACBHeader.RGB , colorCount);
		RGBColor[] colors = new RGBColor[colorCount];
		for (int i=0 ; i < 16; i ++){
			colors[i] = new RGBColor(255/(i+1), 255/(i+1), 255/(i+1));
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
}
