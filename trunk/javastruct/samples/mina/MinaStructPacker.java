package mina;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import struct.StructException;
import struct.StructOutput;

public class MinaStructPacker extends StructOutput{
	ByteBuffer out;
	
	public MinaStructPacker(){
		out = ByteBuffer.allocate(64, false);
		out.setAutoExpand(true);
	}
	
	public MinaStructPacker(ByteBuffer buffer){
		out = buffer;
	}

	public byte[] pack(Object objectToPack) throws StructException{
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
	
	public final void writeBooleanArray(boolean buffer[], int len) throws IOException {
		if (len == -1)
			len = buffer.length;
		for (int i = 0; i < len; i++)
			out.put(buffer[i] == true ? (byte)1 : (byte)0);
	}

	public final void writeByteArray(byte buffer[], int len) throws IOException {
		if (len == -1)
			len = buffer.length;
		out.put(buffer);
	}

	public final void writeCharArray(char buffer[], int len) throws IOException {
		if (len == -1)
			len = buffer.length;
		for (int i = 0; i < len; i++)
			out.putChar(buffer[i]);
	}

	public final void writeShortArray(short buffer[], int len) throws IOException {
		if (len == -1)
			len = buffer.length;
		for (int i = 0; i < len; i++)
			out.putShort(buffer[i]);
	}

	public final void writeIntArray(int buffer[], int len) throws IOException {
		if (len == -1)
			len = buffer.length;
		for (int i = 0; i < len; i++)
			out.putInt(buffer[i]);
	}

	public final void writeLongArray(long buffer[], int len) throws IOException {
		if (len == -1)
			len = buffer.length;
		for (int i = 0; i < len; i++)
			out.putLong(buffer[i]);
	}

	public final void writeFloatArray(float buffer[], int len) throws IOException {
		if (len == -1)
			len = buffer.length;
		for (int i = 0; i < len; i++)
			out.putFloat(buffer[i]);
	}

	public final void writeDoubleArray(double buffer[], int len) throws IOException {
		if (len == -1)
			len = buffer.length;
		for (int i = 0; i < len; i++)
			out.putDouble(buffer[i]);
	}

	public final void writeObjectArray(Object buffer[], int len) throws IOException,
			IllegalAccessException, InvocationTargetException, StructException {
		if (buffer == null || len == 0)
			return;
		if (len == -1)
			len = buffer.length;
		for (int i = 0; i < len; i++)
			writeObject(buffer[i]);
	}

	@Override
	public final void writeBoolean(boolean value) throws IOException {
		out.put(value == true ? (byte)1 : (byte)0);		
	}

	@Override
	public final void writeByte(byte value) throws IOException {
		out.put(value);
		
	}

	@Override
	public final void writeChar(char value) throws IOException {
		out.putChar(value);
	}

	@Override
	public void writeDouble(double value) throws IOException {
		out.putDouble(value);		
	}

	@Override
	public final void writeFloat(float value) throws IOException {
		out.putFloat(value);		
	}

	@Override
	public final void writeInt(int value) throws IOException {
		out.putInt(value);		
	}

	@Override
	public final void writeLong(long value) throws IOException {
		out.putLong(value);		
	}

	@Override
	public final void writeShort(short value) throws IOException {
		out.putShort(value);		
	}	
}
