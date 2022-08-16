package homework;

import java.lang.reflect.InvocationTargetException;

public class Demo {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        if (args.length == 0) {
            return;
        }
        String className = args[0];
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            System.out.println("Class \"" + className + "\" does not exist");
            return;
        }
        TestExecutor.execute(clazz);
    }
}
