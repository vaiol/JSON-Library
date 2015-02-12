import java.lang.reflect.*;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String [] args) {
        Example i = new Example();
        JSONObject jsobject = new JSONObject(i);


    }
    public static boolean isWrapperType(Class<?> clazz)
    {
        return getWrapperTypes().contains(clazz);
    }

    private static Set<Class<?>> getWrapperTypes()
    {
        Set<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(Boolean.class);
        ret.add(Character.class);//sdfsdf
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);//sfsf
        return ret;
    }
}
