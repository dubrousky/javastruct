package struct;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

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

	/**
	 * Analyse the class and fing getter - setter methods.
	 * 
	 * @param fields
	 * @param methods
	 */
	public StructData(Field[] fields, Method[] methods) throws StructException {
		this.fields = fields;
		this.methods = methods;

		for (int i = 0; i < fields.length; i++) {
			// find the members whose lengths are given in another field.
			if (isLengthtedArray(fields[i])) {
				lengthedArrayFields.put(fields[i].getName(), fields[i]);
			}
			// Find members which requires a getter and setter,
			// also find them, put into a Map for faster access later.
			if (StructUtils.requiresGetterSetter(fields[i].getModifiers())) {
				Method getter = getGetterName(methods, fields[i]);
				getters.put(fields[i].getName(), getter);
				Method setter = getSetterName(methods, fields[i]);
				setters.put(fields[i].getName(), setter);
			}
		}
	}

	/**
	 * Is there a getter for this field?
	 * 
	 * @param methods
	 * @param field
	 * @return
	 */
	private static final Method getGetterName(Method[] methods, Field field) {
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
		return null;
	}

	/**
	 * Find setter for a given member.
	 * @param methods : methods
	 * @param field : field
	 * @return : the setter method, if exists, else null.
	 */
	private static final Method getSetterName(Method[] methods, Field field) {
		String setterName = "set" + field.getName();
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().equalsIgnoreCase(setterName)) {
				return methods[i];
			}
		}
		return null;
	}

    /**
     * Is this an array length marker?
     *
     * @param current Field
     * @return boolean
     */
    private static final boolean isLengthtedArray(Field current){
    	return (current.getAnnotation(ArrayLengthMarker.class) != null);
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

	public Method getGetter(Field field) {
		return getters.get(field.getName());
	}

	public Method getSetter(Field field) {
		return setters.get(field.getName());
	}

}
