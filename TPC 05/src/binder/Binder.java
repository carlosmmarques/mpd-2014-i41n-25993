package binder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Binder {
	
    private static BinderFields binderF = new BinderFields();
    private static BinderProps binderP = new BinderProps();

    /**
	 * getFieldsValues 
	 * 
	 * @param o - The instance of the object upon which to perform the operation;
	 * @return Map<String, Object> with the names and values of the fields of
	 * an instance of an object.
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Map<String, Object> getFieldsValues(Object o) throws IllegalArgumentException, IllegalAccessException{
		if (o == null) throw new IllegalArgumentException("O objecto não pode ser nulo");
		HashMap<String, Object> map = new HashMap<String, Object>();
		Class<?> klass = o.getClass();
		for (Field f : klass.getDeclaredFields()){
			if (!f.isAccessible()) f.setAccessible(true);
			map.put(f.getName(), f.get(o));
		}
		return map;
	}
	
	/**
	 * 
	 * checkForParameterlessConstructor
	 * 
	 * @param targetClass - The class to check. If it does not 
	 * @throws RuntimeException
	 */
	public static <T> void checkForParameterlessConstructor(Class<T> targetClass) throws RuntimeException {
		for (Constructor<?> ctor : targetClass.getDeclaredConstructors()){
			if (ctor.getGenericParameterTypes().length == 0){
				if (!ctor.isAccessible()) ctor.setAccessible(true);
			} else {
				throw new RuntimeException("Class does not have a parameterless constructor.");
			}
		}
	}
	
	/**
	 * 
	 * bindField
	 * 
	 * @param target - An instance of type <T> upon which we will try to bind a value of
	 * 	the given "fields" map to a matching field "f";
	 * @param targetField - The field of the class
	 * @param fields - The map of Fields (Expects a CASE INSENSITIVE KeySet)
	 * @return true if bind to Field was successful, otherwise returns false
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static <T> boolean bindField(T target, Field targetField, Map<String, Object> fields) throws IllegalArgumentException, IllegalAccessException{
		String targetFieldName = targetField.getName();
		if (fields.containsKey(targetFieldName)){
			Object fieldValue = fields.get(targetFieldName);
			Class<?> targetFieldType = targetField.getType();
			if (targetFieldType.isPrimitive()){ 
				targetFieldType = targetField.get(target).getClass(); // get wrapper via boxing of the primitive type.
			}
			if (targetFieldType.isAssignableFrom(fields.get(targetFieldName).getClass())){
				targetField.set(target, fieldValue);
				return true;
			} 
		}
		return false;
	}
	
	/**
	 * 
	 * bindToFields
	 * 
	 * @param targetClass - The class of a DTO object
	 * @param vals - A Map of fields values of a given instance of an Object
	 * @return <T>T - An instance of a DTO object whose fields have been filled with 
	 * 	the values for the matching keyset.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
    public static <T> T bindToFields(Class<T> targetClass, Map<String, Object> vals) 
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
            {
        return binderF.bindTo(targetClass, vals);
    }
	
	/**
	 * 
	 * bindProps - Tries to Bind a value to a property via invocation of the respective Setter Method.
	 * 
	 * @param target - An instance of type <T> upon which we will try to bind a value of
	 * 	the given "fields" map to a matching property via invocation of the propper accessor Method;
	 * @param propAccessorMethod - The Accessor Method
	 * @param fields - The map of Fields (Expects a CASE INSENSITIVE KeySet)
	 * @return TODO
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static <T> boolean bindProps(T target, Method propAccessorMethod, Map<String, Object> fields) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		String targetPropertyName = propAccessorMethod.getName().substring(3);
		if (propAccessorMethod.getName().matches("[sS]et.*") && fields.containsKey(targetPropertyName)){
			Object value = fields.get(targetPropertyName);
			Class<?> targetPropertyType = WrapperUtils.toWrapper(propAccessorMethod.getParameterTypes()[0]);
			if (targetPropertyType.isAssignableFrom(value.getClass())){
				propAccessorMethod.setAccessible(true);
				propAccessorMethod.invoke(target, value);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * bindToProperties
	 * 
	 * @param targetClass - The class of a DTO object
	 * @param vals - A Map of fields values of a given instance of an Object
	 * @return <T>T - An instance of a DTO object whose properties have been set with 
	 * 	the values for the matching keyset.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
    public static <T> T bindToProperties(Class<T> targetClass, Map<String, Object> vals)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return binderP.bindTo(targetClass, vals);

    }
    
    /**
     * 
     * bindToFieldsAndProperties - Binds both Fields and Properties to a new instance of a specified type and returns 
     * the object.
     * 
     * @param targetClass - Type of object to attempt binding to
     * @param vals - a Map of values with Field and/or Property Names and corresponding objects.
     * @return - An instance of the specified  class type.
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static <T> T bindToFieldsAndProperties(Class<T> targetClass, Map<String, Object> vals)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return new BinderPropsAndFields().bindTo(targetClass, vals);
    }

}
