# What is Javastruct #
javastruct is a library for using java objects as C or C++ structs.

# What is it used for? #
[For details please refer to Wiki page](http://code.google.com/p/javastruct/wiki/HowToUseJavaStruct)

This library could be useful for java applications communicating with embedded devices or other C, C++ applications. It could also be used as a simple but space efficent serialization method.

Primitives, arrays, C Strings and Nested classes are supported. Big Endian and Little Endian byte orders are also supported. javastruct can also handle fields whose length is defined in other fields, using ArrayLengthMarker annotation. Please refer to examples and documentation in wiki section.

# How to use JavaStruct #
Classes should be marked as StructClass annotation and fields must be annotated to be used as struct fields.

Look at the test classes to learn usage in detail.
A Simple Example:
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

# Peformance #
For simple classes , JavaStruct is faster than Java serialization, For complex and nested objects, generally same performance as Java serialization. Naturally JavaStruct produces 2-3 times smaller output.

# Future Work #
  * Better naming and unified Façade class
  * Detailed documentation
  * ByteBuffer based struct serialization (Currently it is stream based)
  * Better performance
  * More unit tests
  * Data alignment support
  * Bitfields
  * Unions.

# Requirements #
- Any Os with Java 5 or upper JVM

# Similar Projects #
Jean-Marie Dautelle's [Javolution](http://www.javolution.org) also has an excellent struct implementation. Javolution structs uses special classes for representing fields, JavaStruct has a different approach and uses POJO's and Java 5 annotations.