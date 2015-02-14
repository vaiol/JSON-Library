import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class JSON {
    private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();
    private static final Set<Character> DELIMETERS = getDelimeters();

    private JSON() {}


    public static String generator(Object object) {
        String result = "";
        if(object instanceof Object[]) {
            result += arrayGenerate(object, 0);
        } else {
            if(object == null) {
                return result;
            } else if(object.getClass().isArray()) {
                result += toSimpleArray(object, 0);
            } else {
                result += objectGenerate(object, 0);
            }
        }
        return result;
    }

    public static boolean validator(String json) {
        boolean b = false;
        try {
            json = json.trim();
            if (!firstValidation(json)) {
                return false;
            }
            json = deleteAllDelimeters(json);
//        System.out.println(json);
            json = deleteAllWords(json);
//        System.out.println(json);
            json = deleteAllKeyWords(json);
//        System.out.println(json);
            json = deleteAllNumbers(json);
//        System.out.println(json);


            String tmp = "";
            b = true;
            while (!json.isEmpty()) {
                if (json.equals(tmp)) {
                    b = false;
                    break;
                }
                tmp = json;
                json = deleteSimpleArrays(json);
                if (json.equals("\"\"")) {
                    break;
                }
//            System.out.println(json);
                json = deleteEmptyElements(json);
                if (json.equals("\"\"")) {
                    break;
                }
//            System.out.println(json);
                json = deleteEmptyObject(json);
                if (json.equals("\"\"")) {
                    break;
                }
//            System.out.println(json);
            }
        } catch (Exception e){}
        return b;
    }

    //===============================VALIDATOR METHOD========================================

    private static boolean firstValidation(String oldString) {
        if(oldString.isEmpty()) {
            return false;
        } else if(oldString.charAt(0) == '{' || oldString.charAt(oldString.length() - 1) == '}') {
            return true;
        } else if(oldString.charAt(0) == '[' || oldString.charAt(oldString.length() - 1) == ']') {
            return true;
        }
        return true;
    }

    private static String deleteAllKeyWords(String oldString) {
        String result = replaceAllElement(oldString, "true", "\"\"");
        result = replaceAllElement(result, "false", "\"\"");
        result = replaceAllElement(result, "null", "\"\"");
        return result;
    }

    private static String deleteAllDelimeters(String oldString) {
        StringBuilder result = new StringBuilder(oldString);


        for(int i = 0; i < result.length(); i++) {
            if(DELIMETERS.contains(result.charAt(i))) {
                result.deleteCharAt(i);
                i--;
            }
        }
        return result.toString();
    }

    private static String deleteAllWords(String oldString) {
        StringBuilder result = new StringBuilder(oldString);
        int index = 0;
        int nextIndex = 0;
        int len = 0;

        while(nextIndex >= 0 && index >= 0) {
            index = result.indexOf("\"", index+len);
            if(index < 0) {
                break;
            }
            nextIndex = result.indexOf("\"", index+1);
            if (nextIndex < 0) {
                throw new RuntimeException("Hello");
            }

            result.replace(index, nextIndex+1, "\"\"");
            len = 2;
        }
        return result.toString();
    }

    private static String deleteAllNumbers(String oldString) {
        StringBuilder result = new StringBuilder(oldString);
        int index = 0;
        int nextIndex = 0;
        int len = 0;

        while(nextIndex >= 0 && index >= 0) {
            index = result.indexOf(":", index+len);
            len = 1;
            if(index < 0) {
                break;
            }
            int indexComma = result.indexOf(",", index+1);
            int indexBracket = result.indexOf("}", index+1);


            if(indexComma < indexBracket) {
                if(indexComma > 0) {
                    nextIndex = indexComma;
                } else if(indexBracket > 0) {
                    nextIndex = indexBracket;
                }
            } else if(indexBracket > 0) {
                nextIndex = indexBracket;
            } else if(indexComma > 0) {
                    nextIndex = indexComma;
            } else {
                nextIndex = - 1;
            }

            if (nextIndex < 0) {
                throw new RuntimeException("Hello");
            }
            String tmp = result.substring(index+1, nextIndex);
            if(determineNumbers(tmp)) {
                result.replace(index+1, nextIndex, "\"\"");
                len = 2;
            }

        }
        return result.toString();
    }

    private static String deleteSimpleArrays(String oldString) {

        StringBuilder result = new StringBuilder(oldString);

        int index = 0;
        int nextIndex = 0;
        int len = 0;

        while(nextIndex >= 0 && index >= 0) {
            index = result.indexOf("[", index+len);
            if(index < 0) {
                break;
            }
            nextIndex = result.indexOf("]", index+1);
            if (nextIndex < 0) {
                throw new RuntimeException("Hello");
            }
            String tmp = result.substring(index, nextIndex+1);
            if(determineSimpleArray(tmp)) {
                result.replace(index, nextIndex+1, "\"\"");
            }
            len = 2;
        }
        return result.toString();


    }

    private static String deleteEmptyElements(String oldString) {
        return replaceAllElement(oldString, "\"\":\"\",", "");
    }

    private static String deleteEmptyObject(String oldString) {
        return replaceAllElement(oldString, "{\"\":\"\"}", "\"\"");
    }

    private static String replaceAllElement(String oldString, String oldElement, String newElement) {
        StringBuilder result = new StringBuilder(oldString);

        int lenOfElement = oldElement.length();
        int index = 0;
        int nextIndex = 0;

        while(nextIndex < result.length() && index>= 0) {
            index = result.indexOf(oldElement, nextIndex);
            nextIndex = index + lenOfElement;
            if(index != -1) {
                result.replace(index, nextIndex, newElement);
                nextIndex = nextIndex-(lenOfElement-newElement.length());
            }
        }
        return result.toString();
    }

    private static Set<Character> getDelimeters() {
        Set<Character> delim = new HashSet<Character>();
        delim.add(' ');
        delim.add('\t');
        delim.add('\n');
        return delim;
    }

    private static int countOfChar(String string, char ch) {
        int count = 0;
        if(string.isEmpty()) {
            return count;
        }
        for(int i  = 0; i < string.length(); i++) {
            if(string.charAt(i) == ch) {
                count++;
            }
        }
        return count;
    }

    private static boolean determineNumbers(String string) {
        boolean b;
        try {
            try {
                Integer.parseInt(string);
            } catch (NumberFormatException e) {
                Double.parseDouble(string);
            }
            b = true;
        } catch (NumberFormatException e) {
            b = false;
        }
        return b;
    }

    private static boolean determineKeywords(String string) {
        if(string.equals("true")) {
            return true;
        } else if(string.equals("false")) {
            return true;
        } else if (string.equals("null")) {
            return true;
        }
        return false;
    }

    private static boolean determineWords(String string) {
        if(string.isEmpty()) {
            return false;
        }
        if(string.charAt(0) != '"' || string.charAt(string.length() - 1) != '"') {
            return false;
        }
        string = string.substring(1, string.length() - 1);
        return (string.indexOf('"') < 0);
    }

    private static boolean determineSimpleArray(String string) {
        if(string.isEmpty()) {
            return false;
        }
        if(string.charAt(0) != '[' || string.charAt(string.length() - 1) != ']') {
            return false;
        }
        string = deleteAllWords(string);
        string = string.substring(1, string.length() - 1);
        int countOfComma = countOfChar(string, ',') + 1;
        String[] elements = string.split(",");
        if(elements.length != countOfComma) {
            return false;
        }
        for(String element : elements) {
            if(determineNumbers(element)) {
                continue;
            } else if(determineKeywords(element)) {
                continue;
            } else if(determineWords(element)) {
                continue;
            }
            return false;
        }
        return true;
    }

    //================================GENERATOR METHOD========================================

    private static String checkToGenerate(Object object, int countOfTab) {
        String result = "";
        if(object instanceof Object[]) {
            result += arrayGenerate(object, countOfTab);
        } else {
            if(object == null) {
                result = "null";
            } else if(object.getClass().isArray()) {
                result += toSimpleArray(object, countOfTab);
            } else {
                String tmp = getWarapperString(object, countOfTab);
                if(!tmp.equals("")) {
                    result += tmp;
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
            String tmp = getWarapperString(objectInArray, countOfTab);
            if(!tmp.equals("")) {
                result += tmp;
            } else {
                result += objectGenerate(objectInArray, countOfTab);
            }
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
        for(int i = 0; i < countOfTab; i++) {
            result += "\t";
        }
        if(object == null) {
            result += "null";
            return result;
        }

        Class myClass = object.getClass();
        Field[] fields = myClass.getDeclaredFields();

        result += "{\n";
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

    private static String toSimpleArray(Object object, int countOfTab) {
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

    private static String getWarapperString(Object object, int countOfTab) {
        String result = "";
        String tmp = "";
        for(int i = 0; i < countOfTab; i++) {
            tmp += "\t";
        }
        if(object == null) {
            result += tmp;
            result += "null";
        }
        if(isWrapperType(object.getClass())) {
            result += tmp;
            if(isCharacterType(object.getClass())) {
                result += "\"" + object + "\"";
            } else {
                result += object;
            }
        }
        return result;
    }

}
