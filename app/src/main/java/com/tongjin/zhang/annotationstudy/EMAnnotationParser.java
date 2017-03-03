package com.tongjin.zhang.annotationstudy;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by zhang on 2017/3/3.
 */

public class EMAnnotationParser {
    public static void injectActivity(Activity activity) {
        if (null == activity) {
            return;
        }
        Class<Activity> activityClass = (Class<Activity>) activity.getClass();
        if (isEMLayoutBinder(activityClass)) {
            EMLayoutBinder layout = activityClass.getAnnotation(EMLayoutBinder.class);
            activity.setContentView(layout.value());
        }
        View decorView = activity.getWindow().getDecorView();
        initViews(activityClass.getDeclaredFields(), decorView, activity);
        initOnClick(activityClass.getDeclaredMethods(), decorView, activity);
    }


    private static boolean isEMLayoutBinder(Class<Activity> c) {
        return c.isAnnotationPresent(EMLayoutBinder.class);
    }

    private static boolean isEMViewBinder(Field field) {
        return field.isAnnotationPresent(EMViewBinder.class);
    }

    private static boolean isEMOnClickBinder(Method method) {
        return method.isAnnotationPresent(EMOnClickBinder.class);
    }

    private static void initViews(Field[] declaredFields, View decorView, Object activity) {
        View view1;
        for (Field field :
                declaredFields) {
            if (isEMViewBinder(field)) {
                EMViewBinder emViewBinder = field.getAnnotation(EMViewBinder.class);
                view1 = decorView.findViewById(emViewBinder.value());
                if (null != view1) {
                    try {
                        field.setAccessible(true);
                        field.set(activity, view1);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void initOnClick(Method[] allMethod, View root, Object activity) {
        for (Method method :
                allMethod) {
            if (isEMOnClickBinder(method)) {
                EMOnClickBinder emOnClickBinder = method.getAnnotation(EMOnClickBinder.class);
                MyOnClickListener clickListener = new MyOnClickListener(method, activity);
                int[] ids = emOnClickBinder.value();
                for (int id :
                        ids) {
                    root.findViewById(id).setOnClickListener(clickListener);
                }
            }
        }
    }

    static class MyOnClickListener implements View.OnClickListener {
        private Method mMethod;
        private Object mRecceiver;

        public MyOnClickListener(Method mMethod, Object mRecceiver) {
            this.mMethod = mMethod;
            this.mRecceiver = mRecceiver;
        }

        @Override
        public void onClick(View v) {
            try {
                mMethod.setAccessible(true);
                mMethod.invoke(mRecceiver, v);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


}
