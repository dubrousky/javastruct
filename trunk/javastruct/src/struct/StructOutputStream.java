package struct;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteOrder;

public abstract class StructOutputStream extends OutputStream {

	protected DataOutput dataOutput;

	protected StructOutputStream() {
	}

	protected void init(OutputStream outStream, ByteOrder order) {
		if (order == ByteOrder.LITTLE_ENDIAN) {
			dataOutput = new LEDataOutputStream(outStream);
		} else {
			dataOutput = new DataOutputStream(outStream);
		}
	}

	public abstract void writeObject(Object obj) throws StructException;
	
	/**
	 * Write a fields value. Field can be an primitive, array or another object.
	 */
	public void writeField( StructFieldData fieldData, Method getter, Object obj, int len )
	            throws IllegalAccessException, IOException, InvocationTargetException, StructException {
		Field field = fieldData.getField();
		if ( !field.getType().isArray() )
		{
			switch(fieldData.getType()) {
			case BOOLEAN: 
				if(getter != null) writeBoolean((Boolean)getter.invoke(obj, (Object[])null));
				else writeBoolean(field.getBoolean(obj));
				break;
				
			case BYTE:
				if(getter != null) writeByte((Byte)getter.invoke(obj, (Object[])null));
				else writeByte(field.getByte(obj));
				break;
				
			case SHORT:
				if(getter != null) writeShort((Short)getter.invoke(obj, (Object[])null));
				else writeShort(field.getShort(obj));
				break;
				
			case INT: 
				if(getter != null) writeInt((Integer)getter.invoke(obj, (Object[])null));
				else writeInt(field.getInt(obj));
				break;
				
			case LONG: 
				long longValue;
				if(getter != null) longValue = (Long)getter.invoke(obj, (Object[])null);
				else longValue = field.getLong(obj);
				writeLong(longValue);
				break;
				
			case CHAR:
				if(getter != null) writeChar((Character)getter.invoke(obj, (Object[])null));
				else writeChar(field.getChar(obj));
				break;
				
			case FLOAT: 
				if(getter != null) writeFloat((Float)getter.invoke(obj, (Object[])null));
				else writeFloat(field.getFloat(obj));
				break;
				
			case DOUBLE:
				if(getter != null) writeDouble((Double)getter.invoke(obj, (Object[])null));
				else writeDouble(field.getDouble(obj));
				break;
				
			// Object	
			default : 
				if(getter != null) handleObject(field, getter.invoke(obj, (Object[])null));
				else handleObject(field, obj);
				break;
			}
		} else {
			switch(fieldData.getType()) {
			case BOOLEAN:
				if(getter != null) writeBooleanArray((boolean[])getter.invoke(obj, (Object[])null), len);
				else writeBooleanArray((boolean[]) field.get(obj), len);
				break;
				
			case BYTE: 
				if(getter != null) writeByteArray((byte[])getter.invoke(obj, (Object[])null), len);
				else writeByteArray((byte[]) field.get(obj), len);
				break;
				
			case CHAR: 
				if(getter != null) writeCharArray((char[])getter.invoke(obj, (Object[])null), len);
				else writeCharArray((char[]) field.get(obj), len);
				break;
				
			case SHORT: 
				if(getter != null) writeShortArray((short[])getter.invoke(obj, (Object[])null), len);
				else writeShortArray((short[]) field.get(obj), len);
				break;
				
			case INT: 
				if(getter != null) writeIntArray((int[])getter.invoke(obj, (Object[])null), len);
				else writeIntArray((int[]) field.get(obj), len);
				break;
				
			case LONG: 
				if(getter != null) writeLongArray((long[])getter.invoke(obj, (Object[])null), len);
				else writeLongArray((long[]) field.get(obj), len);
				break;
				
			case FLOAT: 
				if(getter != null) writeFloatArray((float[])getter.invoke(obj, (Object[])null), len);
				else writeFloatArray((float[]) field.get(obj), len);
				break;
				
			case DOUBLE:
				if(getter != null) writeDoubleArray((double[])getter.invoke(obj, (Object[])null), len);
				else writeDoubleArray((double[]) field.get(obj), len);
				break;
				
			default: 
				if(getter != null) writeObjectArray((Object[])getter.invoke(obj, (Object[])null), len);
				else writeObjectArray((Object[]) field.get(obj), len);
				break;
			}
		}
	}
	
	public void handleObject(Field field, Object obj)
			throws IllegalArgumentException, StructException,
			IllegalAccessException, IOException {
		writeObject(field.get(obj));
	}

	public void close() throws IOException {
		return;
	}

	public void write(int ch) throws IOException {
		return;
	}

	public void writeBoolean(boolean value) throws IOException {
		dataOutput.writeBoolean(value);
	}

	public void writeByte(byte value) throws IOException {
		dataOutput.writeByte(value);
	}

	public void writeShort(short value) throws IOException {
		dataOutput.writeShort(value);
	}

	public void writeInt(int value) throws IOException {
		dataOutput.writeInt(value);
	}

	public void writeLong(long value) throws IOException {
		dataOutput.writeLong(value);
	}

	public void writeChar(char value) throws IOException {
		dataOutput.writeChar(value);
	}

	public void writeFloat(float value) throws IOException {
		dataOutput.writeFloat(value);
	}

	public void writeDouble(double value) throws IOException {
		dataOutput.writeDouble(value);
	}

	public void writeBooleanArray(boolean buffer[], int len) throws IOException {
		if (len == -1 || len > buffer.length)
			len = buffer.length;
		for (int i = 0; i < len; i++)
			dataOutput.writeBoolean(buffer[i]);
	}

	public void writeByteArray(byte buffer[], int len) throws IOException {
		if (len == 0) {
			return;
		}
		if (len == -1 || len > buffer.length)
			len = buffer.length;
		dataOutput.write(buffer, 0, len);
	}

	public void writeCharArray(char buffer[], int len) throws IOException {
		if (len == -1 || len > buffer.length)
			len = buffer.length;
		for (int i = 0; i < len; i++)
			dataOutput.writeChar(buffer[i]);
	}

	public void writeShortArray(short buffer[], int len) throws IOException {
		if (len == -1 || len > buffer.length)
			len = buffer.length;
		for (int i = 0; i < len; i++)
			dataOutput.writeShort(buffer[i]);
	}

	public void writeIntArray(int buffer[], int len) throws IOException {
		if (len == -1 || len > buffer.length)
			len = buffer.length;
		for (int i = 0; i < len; i++)
			dataOutput.writeInt(buffer[i]);
	}

	public void writeLongArray(long buffer[], int len) throws IOException {
		if (len == -1 || len > buffer.length)
			len = buffer.length;
		for (int i = 0; i < len; i++)
			dataOutput.writeLong(buffer[i]);
	}

	public void writeFloatArray(float buffer[], int len) throws IOException {
		if (len == -1 || len > buffer.length)
			len = buffer.length;
		for (int i = 0; i < len; i++)
			dataOutput.writeFloat(buffer[i]);
	}

	public void writeDoubleArray(double buffer[], int len) throws IOException {
		if (len == -1 || len > buffer.length)
			len = buffer.length;
		for (int i = 0; i < len; i++)
			dataOutput.writeDouble(buffer[i]);
	}

	public void writeObjectArray(Object buffer[], int len) throws IOException,
			IllegalAccessException, InvocationTargetException, StructException {
		if (buffer == null || len == 0)
			return;
		if (len == -1 || len > buffer.length)
			len = buffer.length;
		for (int i = 0; i < len; i++)
			writeObject(buffer[i]);
	}
}