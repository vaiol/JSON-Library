
public class Example {
    private boolean booleanValue = false;
    private double doubleValue = 15.4;
    private static final byte byteValue = 20;
    private InnerClass none;
    public InnerClass innerClass = new InnerClass();
    protected InnerClass[] array = new InnerClass[3];

    public Example() {
        for (int i = 0; i < array.length; i++) {
            array[i] = new InnerClass();
        }
    }


    private static class InnerClass {
        private static int[] innerArray = {1, 2, 3, 4, 5};
    }
}
