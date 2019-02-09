package serialisation;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class Deserialize{
    public Object deserialize(String input){

        Object obj = null;

        try {
            byte b[] = Base64.decode(input.getBytes());
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            obj = (Object) si.readObject();

        } catch (Exception e) {
            System.out.println(e);
        }

        return obj;

    }
}
