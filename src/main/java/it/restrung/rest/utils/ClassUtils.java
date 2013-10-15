package it.restrung.rest.utils;

import it.restrung.rest.annotations.JsonProperty;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ClassUtils {

    private ClassUtils() {}

    /**
     * Gets a field by name ignoring field not found exceptions
     * @param clazz the clazz
     * @param name the field name
     * @return the found field, null otherwise
     */
    public static Field getField(Class<?> clazz, String name) {
        Field foundField = null;
        try {
            foundField = clazz.getField(name);

        } catch (NoSuchFieldException e) {
            //ignore fields not found
        }
        try {
            foundField = foundField != null ? foundField : clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            //ignore fields not found
        }
        return foundField;
    }

    /**
     * Gets a method by name ignoring field not found exceptions
     * @param clazz the clazz
     * @param name the method name
     * @return the found method, null otherwise
     */
    public static Method getMethod(Class<?> clazz, String name, Class<?> ... args) {
        Method foundMethod = null;
        try {
            foundMethod = clazz.getMethod(name, args);
            foundMethod = foundMethod != null ? foundMethod : clazz.getDeclaredMethod(name, args);
        } catch (NoSuchMethodException e) {
            //ignore fields not found
        }
        return foundMethod;
    }

    /**
     * Gets an array of all fields in a class hierarchy walking up to parent classes
     * @param objectClass the class
     * @return the fields array
     */
    public static Field[] getAllFieldsInHierarchy(Class<?> objectClass) {
        Set<Field> allFields = new HashSet<Field>();
        Field[] declaredFields = objectClass.getDeclaredFields();
        Field[] fields = objectClass.getFields();
        if (objectClass.getSuperclass() != null) {
            Class<?> superClass = objectClass.getSuperclass();
            Field[] superClassFields = getAllFieldsInHierarchy(superClass);
            allFields.addAll(Arrays.asList(superClassFields));
        }
        allFields.addAll(Arrays.asList(declaredFields));
        allFields.addAll(Arrays.asList(fields));
        return allFields.toArray(new Field[allFields.size()]);
    }

    /**
     * Gets an array of all methods in a class hierarchy walking up to parent classes
     * @param objectClass the class
     * @return the methods array
     */
    public static Method[] getAllMethodsInHierarchy(Class<?> objectClass) {
        Set<Method> allMethods = new HashSet<Method>();
        Method[] declaredMethods = objectClass.getDeclaredMethods();
        Method[] methods = objectClass.getMethods();
        if (objectClass.getSuperclass() != null) {
            Class<?> superClass = objectClass.getSuperclass();
            Method[] superClassMethods = getAllMethodsInHierarchy(superClass);
            allMethods.addAll(Arrays.asList(superClassMethods));
        }
        allMethods.addAll(Arrays.asList(declaredMethods));
        allMethods.addAll(Arrays.asList(methods));
        return allMethods.toArray(new Method[allMethods.size()]);
    }

    /**
     * Gets all the setters declared in a class hierarchy
     * @param objectClass the object class
     * @return the setters
     */
    public static Method[] getAllGettersInHierarchy(Class<?> objectClass) {
        Set<Method> allMethods = new HashSet<Method>();
        for (Method method : getAllMethodsInHierarchy(objectClass)) {
            if ((method.getName().startsWith("get") || method.getName().startsWith("is")) && !method.getName().equals("getClass") && method.getName().length() > 3) {
                allMethods.add(method);
            }
        }
        return allMethods.toArray(new Method[allMethods.size()]);
    }

    /**
     * Given a method returns the associated property name
     * @param method
     * @return
     */
    public static String getPropertyName(Method method) {
        String propertyName = method.getName().substring(3);
        return propertyName.substring(0, 1).toLowerCase() + propertyName.substring(1);
    }

    /**
     * Given a getter returns the associated json property name
     * @param method
     * @return
     */
    public static String getJsonPropertyName(Method method) {
        String propertyName = getPropertyName(method);
        Field foundField = ClassUtils.getField(method.getDeclaringClass(), propertyName);
        if (foundField != null && foundField.isAnnotationPresent(JsonProperty.class)) {
            propertyName = foundField.getAnnotation(JsonProperty.class).value();
        }
        return propertyName;
    }



    /**
     * Gets all the setters declared in a class hierarchy
     * @param objectClass the object class
     * @return the setters
     */
    public static Method[] getAllSettersInHierarchy(Class<?> objectClass) {
        Set<Method> allMethods = new HashSet<Method>();
        for (Method method : getAllMethodsInHierarchy(objectClass)) {
            if (method.getParameterTypes().length == 1 && method.getName().startsWith("set") && method.getName().length() > 3) {
                allMethods.add(method);
            }
        }
        return allMethods.toArray(new Method[allMethods.size()]);
    }

    /**
     * Validates that all values are set.
     *
     * @param values a varargs array of arguments
     */
    public static void defenseNotNull(Object... values) {
        if (values == null) {
            throw new IllegalArgumentException("values is null");
        }
        for (int i = 0; i < values.length; i++) {
            Object value = values[i];
            if (value == null) {
                throw new IllegalArgumentException(String.format("values[%d] is null", i));
            }
        }
    }

}
