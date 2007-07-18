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
        if(obj == null)  throw new StructException("Struct objects cannot be null.");
        StructData info = StructUtils.getStructInfo(obj);
        Field[] fields = info.getFields();

        for (Field currentField : fields) {
			StructFieldData fieldData = info.getFieldData(currentField.getName());
			if(fieldData == null) {
				throw new StructException("Field Data not found for field: " + currentField.getName());
			}        
            int arrayLength = -1;
            boolean lengthedArray = false;
            try{
            	if(info.isLenghtedArray(currentField)){
            		Field f = info.getLenghtedArray(currentField.getName());
            		StructFieldData lengthMarker = info.getFieldData(f.getName());
            		if (lengthMarker.requiresGetterSetter()) {
            			arrayLength = ((Number)lengthMarker.getGetter().invoke( obj, (Object[])null)).intValue();
            		} else {
            			arrayLength = ((Number)lengthMarker.getField().get(obj)).intValue();
            		}
	       			lengthedArray = true;
            	}
            	// For private and protected fields, use getFieldName or veya isFieldName
            	if ( fieldData.requiresGetterSetter()){
            		Method getter = fieldData.getGetter();
            		Method setter = fieldData.getSetter();
            		
            		if(getter == null || setter == null){
            			throw new StructException(" getter/setter required for : "+ currentField.getName());
            		}
            		
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
            			if(getter.invoke (obj, (Object[])null) == null){
            				throw new StructException("Arrays can not be null :"+ currentField.getName());
            			}
            		}
            		readField(currentField,getter,setter,obj);
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
            		if(lengthedArray == false && currentField.getType().isArray()){
            			if(currentField.get(obj)== null){
            				throw new StructException("Arrays can not be null. : "+ currentField.getName());
            			}
            		}
            		if(lengthedArray == false || (lengthedArray == true && arrayLength >= 0)){
            			readField(currentField, null, null, obj);
            		}
            	}
            }
            catch (Exception e) {
                 throw new StructException(e);
            }
        }
    }
}