package pers.zr.magic.dao.utils;

import java.lang.reflect.*;
import java.util.*;

/**
 * created by zhurong 2016/4/30
 */
public class ClassUtil {

    public static Type[] getGenericTypes(Class<?> clazz) {

        Class<?> myClass = clazz;

        if (!(clazz.getGenericSuperclass() instanceof ParameterizedType)) {
            myClass = clazz.getSuperclass();
        }

        Type superClass = myClass.getGenericSuperclass();
        ParameterizedType type = (ParameterizedType) superClass;
        return type.getActualTypeArguments();
    }

    public static Set<Field> getAllFileds(Class<?> entityClass) {

        // 获取本类的所有字段
        Set<Field> fs = new HashSet<Field>();
        Collections.addAll(fs, entityClass.getFields());
        Collections.addAll(fs, entityClass.getDeclaredFields());

        // 递归获取父类的所有字段
        Class<?> superClass = entityClass.getSuperclass();
        if (!superClass.equals(Object.class)) {
            fs.addAll(getAllFileds(superClass));
        }

        return fs;
    }


    public static Set<Method> getAllMethods(Class<?> entityClass) {

        // 获取本类的所有的方法
        Set<Method> ms = new HashSet<Method>();
        Collections.addAll(ms, entityClass.getMethods());
        Collections.addAll(ms, entityClass.getDeclaredMethods());

        // 递归获取父类的所有方法
        Class<?> superClass = entityClass.getSuperclass();
        if (!superClass.equals(Object.class)) {
            ms.addAll(getAllMethods(superClass));
        }

        return ms;
    }

    public static boolean isBasicType(Class<?> clazz) {
        return !Collection.class.isAssignableFrom(clazz) &&
                (clazz.equals(Integer.class) || clazz.equals(Integer.TYPE) ||
                 clazz.equals(Long.class) || clazz.equals(Long.TYPE) ||
                 clazz.equals(Boolean.class) || clazz.equals(Boolean.TYPE) ||
                 clazz.equals(Float.class) || clazz.equals(Float.TYPE) ||
                 clazz.equals(Double.class) || clazz.equals(Double.TYPE) ||
                 clazz.equals(Byte.class) || clazz.equals(Byte.TYPE) ||
                 clazz.equals(String.class) ||
                 Date.class.isAssignableFrom(clazz));

    }



}
