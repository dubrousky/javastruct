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
        if(obj == null)
        	throw new StructException("Struct classes cant be null.");
        StructData info = StructUtils.getStructInfo(obj);
        Field[] fields = info.getFields();

        Vector<Field> lengthedArrayFields = new Vector<Field>();
        Vector<Object> lengthedArrayFieldValues = new Vector<Object>();
        boolean lengthedArray = false;
        boolean lengthedArrayField = false;
        int arrayLength = 0;

		for (Field currentField : fields) {
			StructFieldData fieldData = info.getFieldData(currentField.getName());
			if(fieldData == null) {
				throw new StructException("Field Data not found for field: " + currentField.getName());
			}
            lengthedArray = false; 
            lengthedArrayField = false; 
            arrayLength = 0;
            try{
            	if(fieldData.isArrayLengthMarker()){
            		lengthedArrayFields.add(currentField);
            		lengthedArrayField = true;
            	}
            	else {
            		if(currentField.getType().isArray()) {
            			Object temp = StructUtils.isLengthtedArray( obj, lengthedArrayFields, lengthedArrayFieldValues, currentField);
            			if(temp != null){
            				arrayLength = ((Number)temp).intValue();
            				lengthedArray = true;
            			}
            		}
            	}
            	if ( fieldData.requiresGetterSetter()){
            		Method getter = fieldData.getGetter();
            		if (lengthedArrayField)
            			lengthedArrayFieldValues.add(getter.invoke( obj, (Object[])null));
            		if(lengthedArray && arrayLength >= 0){
            			writeField(currentField, getter, obj, arrayLength);
            		}
            		else writeField(currentField, getter,obj, -1);
            	}
            	// Field is public. Access directly.
            	else {
            		if (lengthedArrayField)
            			lengthedArrayFieldValues.add(currentField.get(obj));
            		if(lengthedArray){
            			// Array is null if Length is negative.
            			if(arrayLength >= 0){
            				writeField(currentField, null, obj, arrayLength);
            			}
            		}
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
