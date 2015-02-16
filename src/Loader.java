import java.io.*;

public class Loader {

    Object object;

    Loader() {
        object = null;
    }

    Loader(Object object) {
        this.object = object;
    }

    public boolean serialize(String path) {
        if(object == null) {
            return false;
        }

        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            File file = new File(path);
            if(!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            return true;
        } catch (IOException e) {
            return false;
        }   finally {
            try {
                if(oos != null) {
                    oos.flush();
                    oos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Object deserialize(String path) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        Object result = null;
        try {
            fis = new FileInputStream(path);
            ois = new ObjectInputStream(fis);
            result = ois.readObject();
            object = result;
        } catch (IOException e) {
            return result;
        } catch (ClassNotFoundException e) {
            return result;
        } finally {
            try {
                if(ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public boolean load(String path) {
        if(object == null) {
            return false;
        }
        String json = JSON.generator(object);
        File file = new File(path);
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            PrintWriter out = new PrintWriter(file.getAbsoluteFile());
            try {
                out.write(json);
            } finally {
                out.close();
            }
            return true;
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String download(String path) {
        StringBuilder result = new StringBuilder();
        File file = new File(path);

        try {
            BufferedReader in = new BufferedReader(new FileReader( file.getAbsoluteFile()));
            try {
                String s;
                while ((s = in.readLine()) != null) {
                    result.append(s);
                    result.append("\n");
                }
            } finally {
                in.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        return result.toString();
    }

}
