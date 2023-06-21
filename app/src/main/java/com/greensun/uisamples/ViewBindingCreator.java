package com.greensun.uisamples;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ViewBindingCreator<VB extends ViewBinding> {

    private static final String TAG = "ViewBindingCreator";

    public VB onCreateView(@NonNull Class<?> clz, @NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        try {
            Class<?> viewBindingType = getViewBindingType(clz);
            Method inflateMethod = viewBindingType
                    .getDeclaredMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
            //noinspection unchecked
            return (VB) inflateMethod.invoke(null, inflater, container, false);
        } catch (Exception e) {
            throw new IllegalStateException("创建ViewBinding失败", e);
        }
    }

    @NonNull
    private Class<?> getViewBindingType(@NonNull Class<?> clz) {
        while (clz != null) {
            Type genericType = clz.getGenericSuperclass();
            clz = clz.getSuperclass();

            if (!(genericType instanceof ParameterizedType)) {
                continue;
            }
            Type[] actualTypeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
            if (actualTypeArguments.length >= 1) {
                for (Type type : actualTypeArguments) {
                    if (type instanceof Class) {
                        Class<?> c = (Class<?>) type;
                        if (ViewBinding.class.isAssignableFrom(c)) {
                            return c;
                        }
                    }
                }
            }
        }
        throw new IllegalStateException("找不到ViewBinding Type");
    }
}
