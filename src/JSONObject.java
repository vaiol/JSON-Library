import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class JSONObject {
    private String object;
    private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();


    public JSONObject(Object object) {
        System.out.println(object.getClass().getCanonicalName() + ":");
        System.out.println(JSONGenerator(object));
    }


    public static String JSONGenerator(Object object) {
        String result = "";
        if(object instanceof Object[]) {
            result += arrayGenerate(object, 0);
        } else {
            result += objectGenerate(object, 0);
        }
        return result;
    }

    //----------------------------------------------------------------------

    private static String checkToGenerate(Object object, int countOfTab) {
        String result = "";
        if(object instanceof Object[]) {
            result += arrayGenerate(object, countOfTab);
        } else {
            if(object == null) {
                result = "null";
            } else if(object.getClass().isArray()) {
                result += toSimpleArray(object);
            } else {
                if(isWrapperType(object.getClass())) {
                    if(isCharacterType(object.getClass())) {
                        result += "\"" + object + "\"";
                    } else {
                        result += object;
                    }
                } else {
                    result += objectGenerate(object, countOfTab);
                }
            }
        }
        return result;
    }


    private static String arrayGenerate(Object object, int countOfTab) {
        String result = "[\n";
        countOfTab++;

        Object[] array = (Object[]) object;
        boolean firstLine = false;
        for(Object objectInArray : array) {
            if(firstLine) {
                result += ",\n";
            }
            firstLine = true;
            result += objectGenerate(objectInArray, countOfTab);
        }

        result += "\n";
        countOfTab--;
        for(int i = 0; i < countOfTab; i++) {
            result += "\t";
        }
        result += "]";

        return result;
    }

    private static String objectGenerate(Object object, int countOfTab) {
        String result = "";
        if(object == null) {
            for(int i = 0; i < countOfTab; i++) {
                result += "\t";
            }
            result += "null";
            return result;
        }


        Class myClass = object.getClass();
        Field[] fields = myClass.getDeclaredFields();

        result = "{\n";
        countOfTab++;
        boolean firstLine = false;
        for(Field field : fields) {
            field.setAccessible(true);

            if(firstLine) {
                result += ",\n";
            }
            firstLine = true;

            for(int i = 0; i < countOfTab; i++) {
                result += "\t";
            }
            result += "\"" + field.getName() + "\": ";
            try {
                result += checkToGenerate(field.get(object), countOfTab);
            } catch (IllegalAccessException e) {
                result += "null";
            }
        }
        countOfTab--;
        result += "\n";
        for(int i = 0; i < countOfTab; i++) {
            result += "\t";
        }
        result += "}";
        return result;
    }

    private static String toSimpleArray(Object object) {
        String result;
        if(object instanceof int[]) {
            int[] array = (int[]) object;
            result = Arrays.toString(array);
        } else if(object instanceof short[]) {
            short[] array = (short[]) object;
            result = Arrays.toString(array);
        } else if(object instanceof long[]) {
            long[] array = (long[]) object;
            result = Arrays.toString(array);
        } else if(object instanceof double[]) {
            double[] array = (double[]) object;
            result = Arrays.toString(array);
        } else if(object instanceof float[]) {
            float[] array = (float[]) object;
            result = Arrays.toString(array);
        } else if(object instanceof char[]) {
            char[] array = (char[]) object;
            result = Arrays.toString(array);
        } else if(object instanceof byte[]) {
            byte[] array = (byte[]) object;
            result = Arrays.toString(array);
        } else {
            result = "null";
        }
        return result;
    }

    private static boolean isWrapperType(Class<?> object) {
        return WRAPPER_TYPES.contains(object);
    }

    private static boolean isCharacterType(Class<?> object) {
        return object.equals(Character.class);
    }

    private static Set<Class<?>> getWrapperTypes() {
        Set<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        return ret;
    }

}
