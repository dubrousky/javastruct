package struct;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteOrder;

/**
 * 
 */
public class StructUnpacker extends StructInputStream {

    public StructUnpacker(byte[] bufferToUnpack){
        this(new ByteArrayInputStream(bufferToUnpack), ByteOrder.BIG_ENDIAN);
    }
    
    public StructUnpacker(byte[] bufferToUnpack, ByteOrder order){
    	this(new ByteArrayInputStream(bufferToUnpack), order);
    }
    
    public StructUnpacker(InputStream is, ByteOrder order){
    	super.init(is, order);
    }

    public void unpack(Object objectToUnpack) throws StructException{
        readObject(objectToUnpack);
    }

    @Override
    public void readObject( Object obj) throws StructException{
        if(obj == null)  throw new StructException("Struct objects cannot be null.");
        StructData info = StructUtils.getStructInfo(obj);
        Field[] fields = info.getFields();

        for (Field currentField : fields) {
			//System.out.println("Processing field: " + currentField.getName());
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
            	// For private and protected fields, use getFieldName or isFieldName
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
            		readField(fieldData, getter, setter, obj);
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
            			readField(fieldData, null, null, obj);
            		}
            	}
            }
            catch (Exception e) {
                 throw new StructException(e);
            }
        }
    }
}