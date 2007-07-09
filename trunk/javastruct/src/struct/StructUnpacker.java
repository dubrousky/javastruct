package struct;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteOrder;
import java.util.Vector;

/**
 * 
 */
public class StructUnpacker extends StructInputStream {
    private Object objectToUnpack = null;

    public StructUnpacker(Object objectToUnpack, byte[] bufferToUnpack){
        this(objectToUnpack, new ByteArrayInputStream(bufferToUnpack), ByteOrder.BIG_ENDIAN);
    }
    
    public StructUnpacker(Object objectToUnpack, byte[] bufferToUnpack, ByteOrder order){
    	this(objectToUnpack, new ByteArrayInputStream(bufferToUnpack), order);
    }
    
    public StructUnpacker(Object objectToUnpack, InputStream is, ByteOrder order){
    	this.objectToUnpack = objectToUnpack;
    	super.init(is, ByteOrder.BIG_ENDIAN);
    }

    public void unpack() throws StructException{
        readObject(objectToUnpack);
    }

    public void readObject( Object obj) throws StructException{
        if(obj == null)  throw new StructException("Object cannot be null.");
        StructData info = StructUtils.getStructInfo(obj);
        Field[] fields = info.getFields();

        // lenghted Fields.
        Vector<Field> lengthedArrayFields = new Vector<Field>();
        // length Field values
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

            // Modifier mask check (public, private, protected)
            try{
            	if(info.isLenghtedArray(currentField)){
            		lengthedArrayFields.add(currentField);
            		lengthedArrayField = true;
            	}
            	else {
            		Object temp = StructUtils.isLengthtedArray( obj, lengthedArrayFields,
            				lengthedArrayFieldValues,currentField);
            		if(temp != null){
            			Number n = (Number) temp;
            			arrayLength = n.intValue();
            			lengthedArray = true;
            		}
            	}
            	// For private and protected fields, use getFieldName or veya isFieldName
            	if ( fieldData.requiresGetterSetter()){
            		Method getter = fieldData.getGetter();
            		Method setter = fieldData.getSetter();
            		if(getter != null && setter != null){
            			if(lengthedArray && arrayLength >= 0){
            				Object ret = Array.newInstance(currentField.getType().getComponentType(),arrayLength);
            				setter.invoke(obj,new Object[]{ret});
            				if(currentField.getType().getComponentType().isPrimitive() == false){
            					Object[] array = (Object[])ret;
            					for(int j=0; j<arrayLength; j++){
            						array[j] =  currentField.getType().getComponentType().newInstance();
            					}
            				}
            			}
            			if(lengthedArray == false && currentField.getType().isArray()){
            				if(getter.invoke (obj, (Object[])null)== null){
            					throw new StructException("Arrays cannot be null :"+ currentField.getName());
            				}
            			}
            			readField(currentField,getter,setter,obj);
            			if (lengthedArrayField)
            				lengthedArrayFieldValues.add(getter.invoke( obj, (Object[])null));
            		}
            		else{
            			throw new StructException(" getter/setter required for : "+ currentField.getName());
            		}
            	}
            	// If public, use directly.
            	else {
            		if(lengthedArray && arrayLength >= 0) {
            			Object ret = Array.newInstance(currentField.getType().getComponentType(),arrayLength);
            			currentField.set(obj,ret);
            			if(currentField.getType().getComponentType().isPrimitive() == false){
            				Object[] array = (Object[])ret;
            				for(int j=0; j<arrayLength; j++){
            					array[j] =  currentField.getType().getComponentType().newInstance();
            				}
            			}
            		}
            		if(lengthedArray == false &&
            				currentField.getType().isArray()){
            			if(currentField.get(obj)== null){
            				throw new StructException("Lenghted arays ca not be null. : "+ currentField.getName());
            			}
            		}
            		// If length is negative, it means it is null.
            		if(lengthedArray == false || (lengthedArray == true && arrayLength >= 0)){
            			readField(currentField, null, null, obj);
            		}
            		if (lengthedArrayField)
            			lengthedArrayFieldValues.add(currentField.get(obj));
            	}
            }
            catch (Exception e) {
                 throw new StructException(e);
            }
        }
    }

}