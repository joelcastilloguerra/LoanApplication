package messageGateway;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class MessageSenderGateway<T> {

    public void sendMessage(String subject, T object){

        try{

            String url = ActiveMQConnection.DEFAULT_BROKER_URL;
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
            Connection connection = connectionFactory.createConnection();
            String serializedObject = null;

            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            //Make the QUEUE on the JMS server
            Destination destination = session.createQueue(subject);

            //MessageProducer is used to send messages
            MessageProducer producer = session.createProducer(destination);

            // serialize and send the given class
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(object);
            so.flush();
            serializedObject = Base64.encode(bo.toByteArray());
            // create a text message
            Message msg = session.createTextMessage(serializedObject);
            // send the message
            producer.send(msg);

        }
        catch (JMSException | IOException e){
            e.printStackTrace();
        }

    }

}
