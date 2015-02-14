public class Main {
    public static void main(String [] args) {
        Example i = new Example();
//        Integer[] i = {1, 2, 3, 4, 5};
//        StringBuilder stb = new StringBuilder("sfdfsd sdf sd f sdf sdf s. sdf sf sdf sdf . sdf sdf!");
//        Text i = new Text(stb);

//        String result = JSONObject.generator(i);
//        System.out.println(result);
//        JSONObject.validator(result);
        System.out.println(JSONObject.determineSimpleArray("[1,null,3,false,\"rwerwer\",,,,,,]"));

        //----------------------------------------------------------------------

//        Map map = new HashMap<String, Object>();
//        map.put("newHashMap", new HashMap<String, Object>());

//        String piu = " sfdf sf sdf sdf s  ";
//        System.out.println(piu + "|");
//        piu = piu.trim();
        System.out.println("END");
    }
}
