package struct;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.ByteOrder;

public class StructPacker extends StructOutputStream{
	protected ByteArrayOutputStream bos;

    public StructPacker(){
        this(new ByteArrayOutputStream(), ByteOrder.BIG_ENDIAN);
    }

    public StructPacker(ByteOrder order){
    	this(new ByteArrayOutputStream(), order);
    }

	public StructPacker(OutputStream os, ByteOrder order){
        super.init(os, order);
        bos = (ByteArrayOutputStream)os;
	}

    public byte[] pack(Object objectToPack) throws StructException {
        writeObject(objectToPack);
        return bos.toByteArray();
    }

	/**
	 * Serialize Object as a struct
	 */
	@Override
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
            		if (fieldData.requiresGetterSetter()) {
            			arrayLength = ((Number)fieldData.getGetter().invoke( obj, (Object[])null)).intValue();
            		} else {
            			arrayLength = ((Number)fieldData.getField().get(obj)).intValue();
            		}
            		lengthedArray = true;
            	}
            	if ( fieldData.requiresGetterSetter()){
            		if(lengthedArray && arrayLength >= 0){
            			writeField(fieldData, fieldData.getGetter(), obj, arrayLength);
            		}
            		else writeField(fieldData, fieldData.getGetter(), obj, -1);
            	}
            	// Field is public. Access directly.
            	else {
            		if(lengthedArray && arrayLength >= 0){
            				writeField(fieldData, null, obj, arrayLength);
            		}
            		// Array is null if Length is negative.
            		else {
            			writeField(fieldData, null, obj, -1);
            		}
            	}
            }
            catch (Exception e) {
            	throw new StructException(e);
            }
		}
	}
}