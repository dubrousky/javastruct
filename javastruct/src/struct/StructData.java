package struct;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import struct.Constants.Primitive;

/**
 * This class keeps class metadata for faster access
 *
 */
public class StructData {
	Field[] fields = null;
	Method[] methods = null;
	HashMap<String, Field> lengthedArrayFields = new HashMap<String, Field>();
	HashMap<String, Method> getters = new HashMap<String, Method>();
	HashMap<String, Method> setters = new HashMap<String, Method>();
	HashMap<String, StructFieldData> fieldDataMap = new HashMap<String, StructFieldData>();
	static int ACCEPTED_MODIFIERS =  Modifier.PUBLIC | Modifier.PRIVATE | Modifier.PROTECTED;

	/**
	 * Analyse the class and find getter - setter methods.
	 * 
	 * @param fields
	 * @param methods
	 */
	public StructData(Field[] fields, Method[] methods) throws StructException {
		this.fields = fields;
		this.methods = methods;
		
		for (Field field : fields) {
			if (!((field.getModifiers() & ~ACCEPTED_MODIFIERS) == 0 
				&& (field.getModifiers() | ACCEPTED_MODIFIERS) != 0)) {
				throw new StructException("Field type should be public, private or protected : " + field.getName());
			}
			boolean isArrayLengthMarker = false;
			Field lengthDefinedArrayField = null;
			// find the members whose lengths are given in another field.
			ArrayLengthMarker lengthMarker = field.getAnnotation(ArrayLengthMarker.class);
			if (lengthMarker != null) {
				isArrayLengthMarker = true;
				lengthedArrayFields.put(field.getName(), field);
				for(int i=0; i<fields.length ;i++){
					if (lengthMarker.fieldName().equals(fields[i].getName())) {
						lengthDefinedArrayField = fields[i];
					}
				}
				if (lengthDefinedArrayField == null){
					throw new StructException("Lenght Marker Fields target is not found: " + lengthMarker.fieldName());
				}
			}

			Method getter = null;
			Method setter = null;
			// Find members which requires a getter and setter,
			// also find them, put into a Map for faster access later.
			if (StructUtils.requiresGetterSetter(field.getModifiers())) {
				getter = getGetterName(methods, field);
				getters.put(field.getName(), getter);
				setter = getSetterName(methods, field);
				setters.put(field.getName(), setter);
			}
			boolean requiresGS = getters != null ? true : false;
			
			Primitive type = Primitive.OBJECT; 
			if ( !field.getType().isArray() ){
				type = Constants.getPrimitive(field.getType().getName());
			} else {
				type = Constants.getPrimitive(field.getType().getName().charAt(1));
			}
			
			StructFieldData fieldData = new StructFieldData(field, requiresGS,
					 getter, setter, type, isArrayLengthMarker,lengthDefinedArrayField );
			
			fieldDataMap.put(field.getName(),fieldData);
			
		}
	}
	
	public StructFieldData getFieldData(String fieldName){
		return fieldDataMap.get(fieldName);
	}

	/**
	 * Is there a getter for this field?
	 * 
	 * @param methods
	 * @param field
	 * @return
	 */
	private static final Method getGetterName(Method[] methods, Field field) throws StructException {
		String getterName = "get" + field.getName();
		String booleanGetterName = "is" + field.getName();
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().equalsIgnoreCase(getterName)) {
				return methods[i];
			}
		}
		if (field.getType().getName().equals("boolean")) {
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getName().equalsIgnoreCase(booleanGetterName)) {
					return methods[i];
				}
			}
		}
		throw new StructException("The field needs a getter method, but none supplied. Field: " + field.getName());
	}

	/**
	 * Find setter for a given member.
	 * @param methods : methods
	 * @param field : field
	 * @return : the setter method, if exists, else null.
	 */
	private static final Method getSetterName(Method[] methods, Field field) throws StructException{
		String setterName = "set" + field.getName();
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().equalsIgnoreCase(setterName)) {
				return methods[i];
			}
		}
		throw new StructException("The field needs a setter method, but none supplied. Field: " + field.getName());
	}
    
	public Field[] getFields() {
		return fields;
	}

	public Method[] getMethods() {
		return methods;
	}

	public boolean isLenghtedArray(Field field) {
		return (lengthedArrayFields.get(field.getName()) != null);
	}

	public Field getLenghtedArray(String fieldName) {
		return lengthedArrayFields.get(fieldName);
	}

}
