package mina;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import struct.ArrayLengthMarker;
import struct.StructClass;
import struct.StructField;

@StructClass
public class ImageResponse implements StructMessage{
	@ArrayLengthMarker (fieldName = "buffer1")
	@StructField (order = 0)
	public int length1;

	@StructField (order = 1)
	public byte[] buffer1;
	
	@ArrayLengthMarker (fieldName = "buffer2")
	@StructField (order = 2)
	public int length2;

	@StructField (order = 3)
	public byte[] buffer2;
	
    public ImageResponse(BufferedImage image1, BufferedImage image2) throws Exception {
    	buffer1 = getBytes(image1);
    	length1 = buffer1.length;
        buffer2 = getBytes(image2);
        length2 = buffer2.length;
    }
    
    private byte[] getBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", baos);
        return baos.toByteArray();
    }

	public int getID() {
		return Messages.IMAGE_RESPONSE;
	}

}
