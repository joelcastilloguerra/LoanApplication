package messageGateway;

import com.owlike.genson.Genson;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class MessageSenderGateway<T> {

    private Genson genson;

    public void sendMessage(String subject, T object){

        try{

            String url = ActiveMQConnection.DEFAULT_BROKER_URL;
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
            Connection connection = connectionFactory.createConnection();

            genson = new Genson();
            String serializedObject = genson.serialize(object);
            serializedObject += "!" + object.getClass().getName();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            //Make the QUEUE on the JMS server
            Destination destination = session.createQueue(subject);

            //MessageProducer is used to send messages
            MessageProducer producer = session.createProducer(destination);

            // create a text message
            Message msg = session.createTextMessage(serializedObject);
            // send the message
            producer.send(msg);

        }
        catch (JMSException e){
            e.printStackTrace();
        }

    }

}
