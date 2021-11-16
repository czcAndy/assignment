package org.vgcs.assignment.generic;

import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Component
public class Invoker<T> {
    public T call(String menthodName, Object obj, Object... params) {
        try {
            Class<?>[] paramClasses = new Class<?>[params.length];
            for (int i = 0; i < params.length; i++){
                paramClasses[i] = params[i].getClass();
            }

            Method method = obj.getClass().getMethod(menthodName, paramClasses);
            return (T)method.invoke(obj, params);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
