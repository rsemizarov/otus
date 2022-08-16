package homework;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import homework.annotations.After;
import homework.annotations.Before;
import homework.annotations.Test;

public class TestExecutor {
    public static void execute(Class<?> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        HashMap<Class<? extends Annotation>, ArrayList<Method>> annotatedMethods = getAnnotatedMethods(clazz);
        int successCount = 0;
        int failedCount = 0;

        if (annotatedMethods.containsKey(Test.class)) {
            for (Method method : annotatedMethods.get(Test.class)) {
                if (executeSingleTest(clazz, annotatedMethods, method)) {
                    successCount++;
                } else {
                    failedCount++;
                }
            }
        }

        System.out.println("Executed tests: " + (successCount + failedCount));
        System.out.println("Succeeded: " + successCount);
        System.out.println("Failed: " + failedCount);

    }

    private static boolean executeSingleTest(Class<?> clazz, HashMap<Class<? extends Annotation>, ArrayList<Method>> annotatedMethods, Method method) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Object instance = clazz.getConstructor().newInstance();
        System.out.println(method.getName());
        executeAll(instance, Before.class, annotatedMethods);
        boolean success = false;
        try {
            method.invoke(instance);
            success = true;
        } catch (InvocationTargetException exception) {
            System.out.println("Test " + clazz.getName() + ":" + method.getName() + " failed: " +
                    exception.getTargetException()
            );
        } finally {
            executeAll(instance, After.class, annotatedMethods);
        }

        return success;
    }

    private static HashMap<Class<? extends Annotation>, ArrayList<Method>> getAnnotatedMethods(Class<?> clazz) {
        HashMap<Class<? extends Annotation>, ArrayList<Method>> annotatedMethods = new HashMap<>();
        for (Method method : clazz.getDeclaredMethods()) {
            addMethodAnnotation(Test.class, method, annotatedMethods);
            addMethodAnnotation(Before.class, method, annotatedMethods);
            addMethodAnnotation(After.class, method, annotatedMethods);
        }
        return annotatedMethods;
    }

    private static void addMethodAnnotation(Class<? extends Annotation> annotation, Method method, HashMap<Class<? extends Annotation>, ArrayList<Method>> hashMap) {
        Annotation testAnnotation = method.getAnnotation(annotation);
        if (testAnnotation == null) {
            return;
        }
        if (!hashMap.containsKey(annotation)) {
            hashMap.put(annotation, new ArrayList<>());
        }
        hashMap.get(annotation).add(method);
    }

    private static void executeAll(Object test, Class<? extends Annotation> annotation, HashMap<Class<? extends Annotation>, ArrayList<Method>> hashMap) throws InvocationTargetException {
        if (!hashMap.containsKey(annotation)) {
            return;
        }
        for (Method m : hashMap.get(annotation)) {
            execute(m, test);
        }
    }

    private static void execute(Method method, Object clazz) throws InvocationTargetException {
        try {
            method.invoke(clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
