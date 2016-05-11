/**
 * beidou-core-493#com.baidu.beidou.common.utils.ClassUtil.java
 * 下午3:40:26 created by Darwin(Tianxin)
 */
package pers.zr.magic.dao.utils;

import java.lang.reflect.*;
import java.util.*;

public class ClassUtil {

    public final static Type[] getGenericTypes(Class<?> clazz) {

        Class<?> myClass = clazz;

        if (!(clazz.getGenericSuperclass() instanceof ParameterizedType)) {
            myClass = clazz.getSuperclass();
        }

        Type superClass = myClass.getGenericSuperclass();
        ParameterizedType type = (ParameterizedType) superClass;
        return type.getActualTypeArguments();
    }

    public static Set<Field> getAllFiled(Class<?> entityClass) {

        // 获取本类的所有字段
        Set<Field> fs = new HashSet<Field>();
        for (Field f : entityClass.getFields()) {
            fs.add(f);
        }
        for (Field f : entityClass.getDeclaredFields()) {
            fs.add(f);
        }

        // 递归获取父类的所有字段
        Class<?> superClass = entityClass.getSuperclass();
        if (!superClass.equals(Object.class)) {
            Set<Field> superFileds = getAllFiled(superClass);
            fs.addAll(superFileds);
        }

        return fs;
    }


    public static Set<Method> getAllMethod(Class<?> entityClass) {

        // 获取本类的所有的方法
        Set<Method> ms = new HashSet<Method>();
        for (Method m : entityClass.getMethods()) {
            ms.add(m);
        }
        for (Method m : entityClass.getDeclaredMethods()) {
            ms.add(m);
        }

        // 递归获取父类的所有方法
        Class<?> superClass = entityClass.getSuperclass();
        if (!superClass.equals(Object.class)) {
            Set<Method> superFileds = getAllMethod(superClass);
            ms.addAll(superFileds);
        }

        return ms;
    }

    public static boolean isBasicType(Class<?> clazz) {
        if (Collection.class.isAssignableFrom(clazz)) {
            return false;
        }
        
        if (clazz.equals(Integer.class) || clazz.equals(Integer.TYPE)) {
            return true;
        }

        if (clazz.equals(Long.class) || clazz.equals(Long.TYPE)) {
            return true;
        }

        if (clazz.equals(Boolean.class) || clazz.equals(Boolean.TYPE)) {
            return true;
        }

        if (clazz.equals(Float.class) || clazz.equals(Float.TYPE)) {
            return true;
        }

        if (clazz.equals(Double.class) || clazz.equals(Double.TYPE)) {
            return true;
        }

        if (clazz.equals(Byte.class) || clazz.equals(Byte.TYPE)) {
            return true;
        }

        if (clazz.equals(String.class)) {
            return true;
        }

        if (Date.class.isAssignableFrom(clazz)) {
            return true;
        }

        return false;

    }



}
