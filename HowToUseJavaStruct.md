# Introduction #

Struct classes can be used to greatly simplfy network protocol codes of Java applications when working with embedded devices and other applications which uses C style structs.

Instead of manually encoding and decoding messages, JavaStruct allows programmers to treat java classes as c structs.

JavaStruct uses Java 5 annotations to mark Classes and fields as structs. JavaStruct is not the first attempt to provide struct like functionality, Jean-Marie Dautelle's [Javolution](http://javolution.org/) library also has an excellent [struct](http://javolution.org/api/javolution/io/Struct.html) implementation. But instead of using special classes in Javolution, POJO approach is preferred JavaStruct.

# General usage #

**JavaStruct** façade class is used to pack and unpack struct classes. Below is a simple unit test method for checking a struct class. Struct fields hasan order value, because Java JVM specification does not tell anything about order of the class members. They are ordered as their appearance in Sun's implementation but it differs on other JVM's. So every Struct field has to supply an order value.

```
@StructClass
public class Foo{

    @StructField(order = 0)
    public byte b;

    @StructField(order = 1)
    public int i;
}

try{
    // Pack the class as a byte buffer
    Foo f = new Foo();
    f.b = (byte)1;
    f.i = 1;
    byte[] b = JavaStruct.pack(f);
    
    // Unpack it into an object
    Foo f2 = new Foo();
    JavaStruct.unpack(f2, b);
}
catch(StructException e){
}
```

Struct operations throws a checked StructException if anything goes wrong.

Struct classes can be used with Streams directly too. Please refer to Photoshop ACB file reader [example](http://code.google.com/p/javastruct/wiki/example_photoshop_acb_file_reader_writer).

```
public void read(String acbFile){
    try {
	FileInputStream fis = new FileInputStream(new File(acbFile));
	header = new ACBHeader();
	StructUnpacker up = JavaStruct.getUnpacker(fis, ByteOrder.BIG_ENDIAN);
	up.readObject(header);
...
```

# Primitives #

Using primitives. Note that private and protected fields requires appropriate getter and setter methods. Transient fields are automatically excluded.

```
@StructClass
public class PublicPrimitives implements Serializable {
	@StructField(order = 0)
	public byte b;

	@StructField(order = 1)
	public char c;

	@StructField(order = 2)
	public short s;

	@StructField(order = 3)
	public int i;

	@StructField(order = 4)
	public long lo;

	@StructField(order = 5)
	protected float f;

	@StructField(order = 6)
	private double d;

	transient int blah;
	transient double foo;


	public float getF() {
		return f;
	}

	public void setF(float f) {
		this.f = f;
	}

	public double getD() {
		return d;
	}

	public void setD(double d) {
		this.d = d;
	}

	public boolean equals(Object o){
		PublicPrimitives other = (PublicPrimitives)o;
		return (this.b == other.b 
			&& this.c == other.c
			&& this.s == other.s
			&& this.i == other.i
			&& this.lo == other.lo
			&& this.f == other.f
			&& this.d == other.d);
	}
}
```

# Arrays #

Arrays have some prerequisites. When unpacking, sufficent space should be reserved in the array. Only arrays, whose lengths are defined in another field using ArrayLengthMarker (see below section) can be null, they are automatically allocated while unpacking. Other than that they can not be null and uninitialized.

```
@StructClass
public class PublicPrimitiveArrays{
	@StructField(order = 0)
	public byte[] b = new byte[5];

	@StructField(order = 1)
	public char[] c = new byte[10];

	@StructField(order = 2)
	public short[] s;

	@StructField(order = 3)
	public int[] i;
}
```

# Array Length Markers #
Array length markers are very useful for the fields whose legth is defined in anoher field. consider the following example.  This is a special String struct which has a length fields and length number of 16 bit characters following it.

```
  +--------+-----------------------//----------------------------------------+
  | Length | UTF-16 Characters     \\                                        |
  +--------+-----------------------//----------------------------------------+
```

In order to process this, we have to represent these strings as a special Struct class, lets say AStruct. The length field should be annotated as "ArrayLengthMarker". This way javastruct can automatically use the value in length field while processing the array field during packing and unpacking operations.

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