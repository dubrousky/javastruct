package mina;

import struct.StructClass;
import struct.StructField;

@StructClass
public class ImageRequest{
	@StructField (order = 0)
    private int width;
	
	@StructField (order = 1)
    private int height;
	
	@StructField (order = 2)
    private int numberOfCharacters;

    public ImageRequest(int width, int height, int numberOfCharacters) {
        this.width = width;
        this.height = height;
        this.numberOfCharacters = numberOfCharacters;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getNumberOfCharacters() {
        return numberOfCharacters;
    }
}
