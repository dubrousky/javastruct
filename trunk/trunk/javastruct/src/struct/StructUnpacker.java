package struct;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Vector;

/**
 * 
 */
public class StructUnpacker extends StructInputStream {
    private Object objectToUnpack = null;

	public StructUnpacker(InputStream inStream) {
		super(inStream);
	}

    public static final int REQUIRE_GETTER_SETTER =  Modifier.PRIVATE | Modifier.PROTECTED;
    ByteArrayInputStream bis;

    public StructUnpacker(Object objectToUnpack, byte[] bufferToUnpack){
        this(objectToUnpack,bufferToUnpack,true);
    }

    public StructUnpacker(Object objectToUnpack, byte[] bufferToUnpack, boolean isLittleEndian){
        this.objectToUnpack = objectToUnpack;
        bis = new ByteArrayInputStream(bufferToUnpack);
        super.init(bis,isLittleEndian);
        super.modifiers = Modifier.PUBLIC | Modifier.PRIVATE | Modifier.PROTECTED ;
    }

    public StructUnpacker(Object objectToUnpack, InputStream is){
        super(is);
    }

    public void unpack() throws StructException{
        readObject(objectToUnpack);
    }

    public static void unpack(Object object, byte[] buffer) throws StructException {
        StructUnpacker unpacker = new StructUnpacker(object, buffer);
        unpacker.unpack();
    }

	public static void createFromByteArray(Object object, byte[] buffer) throws RuntimeException {
		try {
			StructUnpacker unpacker = new StructUnpacker(object, buffer);
			unpacker.unpack();
		} catch (StructException e) {
			throw new RuntimeException(e);
		}
	}

	public static void createFromByteArray(Object object, byte[] buffer, boolean isLittleEndian) throws RuntimeException {
		try {
			StructUnpacker unpacker = new StructUnpacker(object, buffer, isLittleEndian);
			unpacker.unpack();
		} catch (StructException e) {
			throw new RuntimeException(e);
		}
	}

    public void readObject( Object obj) throws StructException{
        if(obj == null)
            throw new StructException("Object cannot be null.");
         //Is this Object a Struct?
        StructUtils.implementsStruct(obj);
        StructUtils.isAccessible(obj);

        StructData info = StructUtils.getStructInfo(obj);
        Field[] fields = info.getFields();

        Field currentField = null;
        // lenghted Fields.
        Vector<Field> lengthedArrayFields = new Vector<Field>();
        // length Field values
        Vector<Object> lengthedArrayFieldValues = new Vector<Object>(); 
        boolean lengthedArray = false;
        boolean lengthedArrayField = false;
        int arrayLength = 0;

        for ( int i=0; i<fields.length; i++) {
            lengthedArray = false; 
            lengthedArrayField = false; 
            arrayLength = 0;

            currentField = fields[i];
            // Modifier mask check (public, private, protected)
            try{
                if (isFieldValid(currentField.getModifiers())){

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
                    if ( StructUtils.requiresGetterSetter(currentField.getModifiers()) ){
                          Method getter = info.getGetter(currentField);
                          Method setter = info.getSetter(currentField);
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
            }
            catch (Exception e) {
                 throw new StructException(e);
            }
        }
    }

    public boolean isFieldValid(int modifier){
        return (modifier == 0) || ((modifier & ~modifiers) == 0 && (modifier | modifiers) != 0);
    }
}
