package struct.mina;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import struct.Constants;
import struct.StructData;
import struct.StructException;
import struct.StructFieldData;
import struct.StructUtils;
import struct.Constants.Primitive;

public class StructOutput {
	ByteBuffer out;
	
	public StructOutput(){
	}
	
	public byte[] pack(Object objectToPack) throws StructException{
		out = ByteBuffer.allocate(64, false);
		out.setAutoExpand(true);
		writeObject(objectToPack);
		return out.array();
	}
	
	public void reset(){
		out.flip();
		out.rewind();
	}
	
	public void setByteOrder(ByteOrder order){
		out.order(order);
	}
	
	/**
	 * Serialize Object as a struct
	 */
	public void writeObject(Object obj) throws StructException{
        if(obj == null)	throw new StructException("Struct classes cant be null. ");
        StructData info = StructUtils.getStructInfo(obj);
        
        boolean lengthedArray = false;
        int arrayLength = 0;

		for (Field currentField : info.getFields()) {
			//System.out.println("Processing field: " + currentField.getName());
			StructFieldData fieldData = info.getFieldData(currentField.getName());
			if(fieldData == null) {
				throw new StructException("Field Data not found for field: " + currentField.getName());
			}
            lengthedArray = false; 
            arrayLength = 0;
            try{
            	if(fieldData.isArrayLengthMarker()){
            		arrayLength = ((Number)fieldData.getField().get(obj)).intValue();
            		lengthedArray = true;
            	}
            	if(lengthedArray && arrayLength >= 0){
            		writeField(fieldData, obj, arrayLength);
            	}
            	else {
            		writeField(fieldData, obj, -1);
            	}
            }
            catch (Exception e) {
            	throw new StructException(e);
            }
		}
	}	

	/**
	 * Write a fields value. Field can be an primitive, array or another object.
	 */
	public void writeField( StructFieldData fieldData, Object obj, int len )
	            throws IllegalAccessException, IOException, InvocationTargetException, StructException {
		Field field = fieldData.getField();
		field.setAccessible(true);
		if ( !field.getType().isArray() )
		{
			switch (fieldData.getType()) {
			case BOOLEAN:
				out.put(field.getBoolean(obj) == true ? (byte)1 : (byte)0);
				break;

			case BYTE:
				out.put(field.getByte(obj));
				break;

			case SHORT:
				out.putShort(field.getShort(obj));
				break;

			case INT:
				out.putInt(field.getInt(obj));
				break;

			case LONG:
				out.putLong(field.getLong(obj));
				break;

			case CHAR:
				out.putChar(field.getChar(obj));
				break;

			case FLOAT:
				out.putFloat(field.getFloat(obj));
				break;

			case DOUBLE:
				out.putDouble(field.getDouble(obj));
				break;

			// Object
			default:
				writeObject(field.get(obj));
				break;
			}
		} else {
			Primitive p = Constants.getPrimitive(field.getType().getName().charAt(1));
			switch(p) {
			case BOOLEAN:
				writeBooleanArray((boolean[]) field.get(obj), len);
				break;
				
			case BYTE: 
				writeByteArray((byte[]) field.get(obj), len);
				break;
				
			case CHAR: 
				writeCharArray((char[]) field.get(obj), len);
				break;
				
			case SHORT: 
				writeShortArray((short[]) field.get(obj), len);
				break;
				
			case INT: 
				writeIntArray((int[]) field.get(obj), len);
				break;
				
			case LONG: 
				writeLongArray((long[]) field.get(obj), len);
				break;
				
			case FLOAT: 
				writeFloatArray((float[]) field.get(obj), len);
				break;
				
			case DOUBLE:
				writeDoubleArray((double[]) field.get(obj), len);
				break;
				
			default: 
				writeObjectArray((Object[]) field.get(obj), len);
				break;
			}
		}
	}
	
	public void writeBooleanArray(boolean buffer[], int len) throws IOException {
		if (len == -1)
			len = buffer.length;
		for (int i = 0; i < len; i++)
			out.put(buffer[i] == true ? (byte)1 : (byte)0);
	}

	public void writeByteArray(byte buffer[], int len) throws IOException {
		if (len == -1)
			len = buffer.length;
		out.put(buffer);
	}

	public void writeCharArray(char buffer[], int len) throws IOException {
		if (len == -1)
			len = buffer.length;
		for (int i = 0; i < len; i++)
			out.putChar(buffer[i]);
	}

	public void writeShortArray(short buffer[], int len) throws IOException {
		if (len == -1)
			len = buffer.length;
		for (int i = 0; i < len; i++)
			out.putShort(buffer[i]);
	}

	public void writeIntArray(int buffer[], int len) throws IOException {
		if (len == -1)
			len = buffer.length;
		for (int i = 0; i < len; i++)
			out.putInt(buffer[i]);
	}

	public void writeLongArray(long buffer[], int len) throws IOException {
		if (len == -1)
			len = buffer.length;
		for (int i = 0; i < len; i++)
			out.putLong(buffer[i]);
	}

	public void writeFloatArray(float buffer[], int len) throws IOException {
		if (len == -1)
			len = buffer.length;
		for (int i = 0; i < len; i++)
			out.putFloat(buffer[i]);
	}

	public void writeDoubleArray(double buffer[], int len) throws IOException {
		if (len == -1)
			len = buffer.length;
		for (int i = 0; i < len; i++)
			out.putDouble(buffer[i]);
	}

	public void writeObjectArray(Object buffer[], int len) throws IOException,
			IllegalAccessException, InvocationTargetException, StructException {
		if (buffer == null || len == 0)
			return;
		if (len == -1)
			len = buffer.length;
		for (int i = 0; i < len; i++)
			writeObject(buffer[i]);
	}	
}
