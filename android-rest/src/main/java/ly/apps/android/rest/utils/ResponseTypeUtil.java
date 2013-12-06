package ly.apps.android.rest.utils;

import ly.apps.android.rest.client.Callback;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;


public class ResponseTypeUtil {

    public static Type parseResponseType(Method method) {
        Type responseObjectType = null;
        // Asynchronous methods should have a Callback type as the last argument.
        Type lastArgType = null;
        Type[] parameterTypes = method.getGenericParameterTypes();
        if (parameterTypes.length > 0) {
            lastArgType = parameterTypes[parameterTypes.length - 1];
        }
        lastArgType = Types.getSupertype(lastArgType, Types.getRawType(lastArgType), Callback.class);
        if (lastArgType instanceof ParameterizedType) {
            responseObjectType = ResponseTypeUtil.getParameterUpperBound((ParameterizedType) lastArgType);
            return responseObjectType;
        }
        throw new IllegalArgumentException("Callback is not parameterized");
    }

    public static Type getParameterUpperBound(ParameterizedType type) {
        Type[] types = type.getActualTypeArguments();
        for (int i = 0; i < types.length; i++) {
            Type paramType = types[i];
            if (paramType instanceof WildcardType) {
                types[i] = ((WildcardType) paramType).getUpperBounds()[0];
            }
        }
        return types[0];
    }

}
