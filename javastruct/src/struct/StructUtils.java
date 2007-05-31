package struct;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Vector;

/**
 * Common functions
 *
 */
public class StructUtils {
    private static HashMap<String, StructData> structInfoCache = new HashMap<String, StructData>();

    /**
     * Put class metadata and required info to a cache
     *
     * @param obj Object
     * @return telays.util.struct.StructInfo
     * @throws StructException
     */
    public static StructData getStructInfo(Object obj) throws StructException{
        StructData info = structInfoCache.get(obj.getClass().getName());
        if(info != null) {
            return info;
        }
         //Annotated member check.
        if(obj.getClass().getAnnotation(StructClass.class) != null){
            Field annotatedFields[] = obj.getClass().getDeclaredFields();
            Field tmpStructFields[] = new Field[annotatedFields.length];
            int annotatedFieldCount = 0;
            for(Field f : annotatedFields){
                StructField sf = f.getAnnotation(StructField.class);
                if(sf != null){
                    int order = sf.order();
                    if(order < 0 || order >= annotatedFields.length) {
                        throw new StructException("Order is illegal for StructField : " + f.getName());
                    }
                    annotatedFieldCount ++ ;
                    tmpStructFields[order] = f;
                }
            }
            Field structFields[] = new Field[annotatedFieldCount];

            for(int i = 0; i<annotatedFieldCount ; i++){
                if(tmpStructFields[i] == null){
                    throw new StructException("Order error for annotated fields! : " + obj.getClass().getName());
                }
                structFields[i] = tmpStructFields[i];
            }
            info = new StructData(structFields, obj.getClass().getDeclaredMethods());
            structInfoCache.put(obj.getClass().getName(), info);
            return info;
        }
        throw new StructException("No struct Annotation found for " + obj.getClass().getName());
    }

    /**
     *
     * @param obj Object
     * @return if class is marked as Struct
     */
    public static boolean implementsStruct(Object obj) throws StructException {
    	return obj.getClass().getAnnotation(StructClass.class) != null ;
    }

    /**
     * Is object accessable?
     *
     * @param obj Object
     * @throws StructException 
     */
    public static void isAccessible(Object obj) throws StructException{
        int modifiers = obj.getClass().getModifiers();
        if ((modifiers & Modifier.PUBLIC) == 0)
            throw new StructException("Struct operations are only accessible for public classes. Class: " + obj.getClass().getName());
        if ((modifiers & (Modifier.INTERFACE | Modifier.ABSTRACT)) != 0)
            throw new StructException("Struct operations are not accessible for abstract classes and ınterfaces. Class: " + obj.getClass().getName());
    }

    /**
     * TODO: discard this by the power of annotations
     *
     * @param currentObject Object
     * @param lengthFields Vector
     * @param lengthValues Vector
     * @param current Field
     * @return java.lang.Object
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static Object isLengthtedArray(Object currentObject, Vector lengthFields, Vector lengthValues, Field current)
                                         throws IllegalArgumentException, IllegalAccessException{
        for(int i=0; i<lengthFields.size(); i++){
            Field lengthField = (Field)lengthFields.elementAt(i);
            if (current.getType().isArray()){
                String name = lengthField.getName();
                if (((name.endsWith("Length") && name.substring(0,name.length()-6).equals(current.getName()))
                    || (name.endsWith("Boy") && name.substring(0,name.length()-3).equals(current.getName())))
                    ) {
                    return lengthValues.elementAt(i);
                 }
              }
         }
      return null;
   }


    /**
     * Does this field reauires a getter or setter?.
     *
     * @param modifier , modifier mask
     * @return : true if field requires getter - setter. false otherwise
     */
    public static boolean requiresGetterSetter(int modifier){
       return (modifier == 0) || (modifier & (Modifier.PRIVATE | Modifier.PROTECTED)) != 0 ;
   }
}
