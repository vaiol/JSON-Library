import forTesting.Text;

public class Main {
    public static void main(String [] args) {
        Example i = new Example();
//        Integer[] i = {1, 2, 3, 4, 5};
//        StringBuilder stb = new StringBuilder("sfdfsd sdf sd f sdf sdf s. sdf sf sdf sdf . sdf sdf!");
//        Text i = new Text(stb);

        String result = JSON.generator(i);

        System.out.println(result);
        System.out.println(JSON.validator(result));

        //----------------------------------------------------------------------

//        Map map = new HashMap<String, Object>();
//        map.put("newHashMap", new HashMap<String, Object>());

//        String piu = " sfdf sf sdf sdf s  ";
//        System.out.println(piu + "|");
//        piu = piu.trim();
        System.out.println("END");
    }
}
