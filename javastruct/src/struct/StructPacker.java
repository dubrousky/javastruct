package struct;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteOrder;
import java.util.Vector;

public class StructPacker extends StructOutputStream{
	protected ByteArrayOutputStream bos;
	private Object objectToPack = null;

    public StructPacker(Object objectToPack){
        this(objectToPack, new ByteArrayOutputStream(), ByteOrder.BIG_ENDIAN);
    }

    public StructPacker(Object objectToPack, ByteOrder order){
    	this(objectToPack, new ByteArrayOutputStream(), order);
    }

	public StructPacker(Object objectToPack, OutputStream os, ByteOrder order){
        this.objectToPack = objectToPack ;
        this.bos = (ByteArrayOutputStream)os;
        super.init(bos, order);
	}

    public byte[] pack() throws StructException {
        writeObject(objectToPack);
        return bos.toByteArray();
    }

	/**
	 * Serialize Object as a struct
	 */
	public void writeObject( Object obj) throws StructException{
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
            		if (fieldData.requiresGetterSetter()) {
            			arrayLength = ((Number)fieldData.getGetter().invoke( obj, (Object[])null)).intValue();
            		} else {
            			arrayLength = ((Number)fieldData.getField().get(obj)).intValue();
            		}
            		lengthedArray = true;
            	}
            	if ( fieldData.requiresGetterSetter()){
            		if(lengthedArray && arrayLength >= 0){
            			writeField(currentField, fieldData.getGetter(), obj, arrayLength);
            		}
            		else writeField(currentField, fieldData.getGetter(), obj, -1);
            	}
            	// Field is public. Access directly.
            	else {
            		if(lengthedArray && arrayLength >= 0){
            				writeField(currentField, null, obj, arrayLength);
            		}
            		// Array is null if Length is negative.
            		else {
            			writeField(currentField, null, obj, -1);
            		}
            	}
            }
            catch (Exception e) {
            	throw new StructException(e);
            }
		}
	}
}