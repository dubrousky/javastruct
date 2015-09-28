# How to use JavaStruct to read and write Adobe Photoshop Color book files #

JavaStruct is also ideal for reading and writing special binary file formats. In this example we will write a Photoshop Color book file reader-writer. The details of the format can be found [here](http://magnetiq.com/docs/PhotoshopColorBook.txt).

For complete code please go to : http://javastruct.googlecode.com/svn/trunk/javastruct/samples/photoshop/

**Note:** I am aware that writing such a reader - writer is also not very difficult (even shorter) using a nio ByteBuffer. But JavaStruct offers cleaner and more manageable code. And if there are lots of binary file types or messages, hand-writing all encoding and decoding would be very error-prone and problematic.

# Header #
In the header, we have special string fields.
```
  +--------+-----------------------//----------------------------------------+
  | Length | UTF-16 Characters     \\                                        |
  +--------+-----------------------//----------------------------------------+
```

So first we have to represent these strings as a special Struct class, AStruct. We use special **ArrayLengthMarker** annotation.
```
@StructClass
public class AString {
	
	@StructField (order = 0 )
	@ArrayLengthMarker (fieldName = "chars")
	public int length;
	
	@StructField (order = 1)
	public char[] chars;
	
	public AString(String content){
		this.length = content.length();
		this.chars = content.toCharArray();
	}
...
}
```

Here is the actual header.

```
@StructClass
public class ACBHeader {
	
	public static final short RGB  = 0;
	public static final short CMYK = 2;	
	public static final short LAB  = 7;
	
	// Signature is always ASCII characters "8BCB"
	@StructField (order = 0)
	public byte[] signature = {'8', 'B', 'C', 'B'};
	
	//Photoshop 7 only opens files with a version of 1 (0x0001) 
	@StructField (order = 1)
	public short fileVersion;
	
	// Unique identifier..
	@StructField (order = 2)
	public short colorBookIdentifier;
	
	@StructField (order = 3)
	public AString title;
	
	@StructField (order = 4)
	public AString colorNamePrefix;
	
	@StructField (order = 5)
	public AString colorNamePostfix;

	// Generally a copyright notice
	@StructField (order = 6)
	public AString description;
	
	@StructField (order = 7)
	public short colorCount; 

	// Photoshop displays colors page by page. This field specifies the
	// maximum number of colors that can appear on a page.
	@StructField (order = 8)
	public short colorsPerPage; 

	@StructField (order = 9)
	public short pageSelectorOffset;
        
        // RGB, CMYK or Lab	
	@StructField (order = 10)
	public short colorSpaceID;
...

```

# Reading Colors #
Here is the tricky part. According to format, color data and its format is dependent on colorSpaceID field. it can be RGB, CMYK or Lab. So we write one class per Color Space. There are exactly "colorCount" number of color Data.

```
@StructClass
public class RGBColor{
	
	@StructField (order = 0)
	public AString name;
	
	@StructField (order = 1)
	public byte[] code = new byte[6];	
	
	@StructField (order = 2)
	public byte red;

	@StructField (order = 3)
	public byte green;

	@StructField (order = 4)
	public byte blue;
...
}

@StructClass
public class LabColor {
	
	@StructField (order = 0)
	public AString name;
	
	@StructField (order = 1)
	public byte[] code = new byte[6];	
	
	@StructField (order = 2)
	public byte lightness;

	@StructField (order = 3)
	public byte aChrom;

	@StructField (order = 4)
	public byte bChrom;
...
}

```

Note that for each Color class contain name and code fields. This is necessary because currently Struct classes do **not** extend from a strcut base class.

# The Reader Writer #
To read and write acb files, we initiate StructPacker and StructUnpacker classes with FileStreams. After that reading header and color data is quite straightforward.

```
public class AdobeColorBook {
	ACBHeader header;
	RGBColor[] rgbColors = null;
	CMYKColor[] cmykColors = null;
	LabColor[] labColors = null;
	
	public void read(String acbFile){
		try {
			FileInputStream fis = new FileInputStream(new File(acbFile));
			header = new ACBHeader();
			StructUnpacker up = JavaStruct.getUnpacker(fis, ByteOrder.BIG_ENDIAN);
			up.readObject(header);
			switch(header.ColorSpaceID){
				case ACBHeader.RGB :
					rgbColors = new RGBColor[header.colorCount];
					for(int i=0; i<header.colorCount; i++){
						rgbColors[i] = new RGBColor();
						up.readObject(rgbColors[i]);
					}
					break;
				case ACBHeader.CMYK :
					cmykColors = new CMYKColor[header.colorCount];
					for(int i=0; i<header.colorCount; i++){
						cmykColors[i] = new CMYKColor();
						up.readObject(cmykColors[i]);
					}
					break;
				case ACBHeader.LAB :
					labColors = new LabColor[header.colorCount];
					for(int i=0; i<header.colorCount; i++){
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
	
	public void write(String name){
		try{
			FileOutputStream fo = new FileOutputStream(new File(name));
			StructPacker packer = JavaStruct.getPacker(fo, ByteOrder.BIG_ENDIAN);
			packer.writeObject(header);
			switch(header.ColorSpaceID){
				case ACBHeader.RGB :
					for(RGBColor color : rgbColors){
						packer.writeObject(color);
					}
					break;
				case ACBHeader.CMYK :
					for(CMYKColor color : cmykColors){
						packer.writeObject(color);
					}
					break;
				case ACBHeader.LAB :
					for(LabColor color : labColors){
						packer.writeObject(color);
					}
					break;
				default:
			}
			fo.close();
		} catch(StructException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String toString(){
		String str = "";
		if (header != null){
			str += header;
		}
		switch(header.ColorSpaceID){
		case ACBHeader.RGB :
			for(RGBColor color : rgbColors) {
				str += color + "\n";
			}
			break;
		case ACBHeader.CMYK :
			for(CMYKColor color : cmykColors) {
				str += color + "\n";
			}
			break;
		case ACBHeader.LAB :
			for(LabColor color : labColors) {
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
	}
}
```