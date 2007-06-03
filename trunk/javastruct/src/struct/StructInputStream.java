package struct;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteOrder;

import struct.Constants.Primitive;

public abstract class StructInputStream extends InputStream {

    DataInput dataInput;

  protected void init( InputStream inStream, ByteOrder order) {
      if ( order == ByteOrder.LITTLE_ENDIAN){
          dataInput = new LEDataInputStream(inStream);
      }
      else {
          dataInput = new DataInputStream( inStream );
      }
  }

  public abstract void readObject(Object obj) throws StructException;

  public void readField( Field field, Method getter, Method setter, Object obj )
              throws IOException, InvocationTargetException, InstantiationException, IllegalAccessException, StructException {
      String typeString = field.getType().getName();
      if ( !field.getType().isArray() ){
    	  Primitive p = Constants.getPrimitive(typeString);
    	  switch(p) {
    	  case BOOLEAN: 
    		  if(setter != null) setter.invoke(obj, new Object[]{readBoolean()});
    		  else field.setBoolean(obj, readBoolean());
    		  break;

    	  case BYTE   : 
    		  if(setter != null) setter.invoke(obj, new Object[]{readByte()});
    		  else field.setByte(obj, readByte());
    		  break;

    	  case SHORT  : 
    		  if(setter != null) setter.invoke(obj, new Object[]{readShort()});
    		  else field.setShort(obj, readShort());
    		  break;

    	  case INT    : 
    		  if(setter != null) setter.invoke(obj, new Object[]{readInt()});
    		  else field.setInt(obj, readInt());
    		  break;

    	  case LONG   :
    		  if(setter != null) setter.invoke(obj, new Object[]{readLong()});
    		  else field.setLong(obj, readLong());
    		  break;

    	  case CHAR   : 
    		  if(setter != null) setter.invoke(obj, new Object[]{readChar()});
    		  else field.setChar(obj, readChar());
    		  break;

    	  case FLOAT  : 
    		  if(setter != null) setter.invoke(obj, new Object[]{readFloat()});
    		  else field.setFloat(obj, readFloat());
    		  break;

    	  case DOUBLE : 
    		  if(setter != null) setter.invoke(obj, new Object[]{readDouble()});
    		  else field.setDouble(obj, readDouble());
    		  break;

    	  default: 
    		  if(setter != null){
    			  Object object = getter.invoke(obj,(Object[])null);
    			  if(object == null){
    				  if(field.getName().endsWith("FixedString")){
    					  throw new StructException("FixedString objects should be initialized :" + field.getName());
    				  }
    				  object = field.getType().newInstance();
    			  }
    			  readObject(object);
    			  setter.invoke(obj, new Object[]{object});
    		  }
    		  else handleObject(field, obj);
    	  break;
    	  }
      }
      else {
    	  Primitive p = Constants.getPrimitive(typeString.charAt(1));
    	  if ( getter!= null && getter.invoke(obj,(Object[])null) == null )
    		  throw new StructException("Arrays ca not be null : " + field.getName());
    	  switch(p) {
    	  case BOOLEAN: 
    		  if(getter != null) readBooleanArray((boolean[])getter.invoke(obj, (Object[])null));
    		  else readBooleanArray((boolean[])field.get(obj));
    		  break;

    	  case BYTE: 
    		  if(getter != null) readByteArray((byte[])getter.invoke(obj,(Object[]) null));
    		  else readByteArray((byte[])field.get(obj));
    		  break;

    	  case CHAR: 
    		  if(getter != null) readCharArray((char[])getter.invoke(obj, (Object[])null));
    		  else readCharArray((char[])field.get(obj));
    		  break;

    	  case SHORT    : 
    		  if(getter != null) readShortArray((short[])getter.invoke(obj, (Object[])null));
    		  else readShortArray((short[])field.get(obj));
    		  break;

    	  case INT      : 
    		  if(getter != null) readIntArray((int[])getter.invoke(obj, (Object[])null));
    		  else readIntArray((int[])field.get(obj));
    		  break;

    	  case LONG     : 
    		  if(getter != null) readLongArray((long[])getter.invoke(obj, (Object[])null));
    		  else readLongArray((long[])field.get(obj));
    		  break;

    	  case FLOAT: 
    		  if(getter != null) readFloatArray((float[])getter.invoke(obj, (Object[])null));
    		  else readFloatArray((float[])field.get(obj));
    		  break;

    	  case DOUBLE: 
    		  if(getter != null) readDoubleArray((double[])getter.invoke(obj, (Object[])null));
    		  else readDoubleArray((double[])field.get(obj));
    		  break;

    	  default: 
    		  if(getter != null) readObjectArray((Object[])getter.invoke(obj, (Object[])null));
    		  else readObjectArray((Object[])field.get(obj));
    	  break;
    	  }
      }
  }  

  /**
   * @param field
   * @param obj
   * @throws IllegalArgumentException
   * @throws StructException
   * @throws IllegalAccessException
   * @throws IOException
   */
  public void handleObject(Field field, Object obj)
			throws IllegalArgumentException, StructException, IOException,
			InstantiationException, IllegalAccessException {
		if (field.get(obj) == null) {
			if (field.getType().getName().endsWith("FixedString")) {
				throw new StructException("FixedString objects should be initialized before unpacking :"
								+ field.getName());
			}
			field.set(obj, field.getType().newInstance());
		}
		readObject(field.get(obj));
	}  

  public void close() throws IOException {
  }

  public int read() throws IOException {
    return -1;
  }

  protected boolean readBoolean() throws IOException {
    return dataInput.readBoolean();
  }

  protected byte readByte() throws IOException {
    return dataInput.readByte();
  }

  protected short readShort() throws IOException {
    return dataInput.readShort();
  }

  protected int readInt() throws IOException {
    return dataInput.readInt();
  }

  protected long readLong() throws IOException {
    return dataInput.readLong();
  }

  protected char readChar() throws IOException {
    return dataInput.readChar();
  }

  protected float readFloat() throws IOException {
    return dataInput.readFloat();
  }

  protected double readDouble() throws IOException {
    return dataInput.readDouble();
  }

  protected void readBooleanArray(boolean buffer[]) throws IOException {
    for ( int i=0; i<buffer.length; i++)
      buffer[i] = readBoolean();
  }

  protected void readByteArray( byte buffer[] ) throws IOException,StructException {
      dataInput.readFully(buffer);
  }

  protected void readCharArray( char buffer[] ) throws IOException {
    for ( int i=0; i<buffer.length; i++)
      buffer[i] = readChar();
  }

  protected void readShortArray( short buffer[] ) throws IOException {
    for ( int i=0; i<buffer.length; i++)
      buffer[i] = readShort();
  }

  protected void readIntArray( int buffer[] ) throws IOException {
    for ( int i=0; i<buffer.length; i++)
      buffer[i] = readInt();
  }

  protected void readLongArray( long buffer[] ) throws IOException {
    for ( int i=0; i<buffer.length; i++)
      buffer[i] = readLong();
  }

  protected void readFloatArray( float buffer[] ) throws IOException {
    for ( int i=0; i<buffer.length; i++)
      buffer[i] = readFloat();
  }

  protected void readDoubleArray( double buffer[] ) throws IOException {
    for ( int i=0; i<buffer.length; i++)
      buffer[i] = readDouble();
  }

  protected void readObjectArray( Object objects[] )
                 throws IOException, IllegalAccessException, InvocationTargetException, StructException {
    for ( int i=0; i<objects.length; i++)
      readObject( objects[i] );
  }

}
