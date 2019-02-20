package messageGateway;

import com.owlike.genson.Genson;
import model.loan.LoanRequest;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class MessageReceiverGateway {

    private MessageConsumer consumer;
    private Genson genson;

    public MessageReceiverGateway(String subject) {

        receiveMessage(subject);
        genson = new Genson();

    }

    public MessageConsumer receiveMessage(String subject){

        try{

            // URL of the JMS server
            String url = ActiveMQConnection.DEFAULT_BROKER_URL;
            // default broker URL is : tcp://localhost:61616"

            // Name of the queue we will receive messages from

            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
            Connection connection = connectionFactory.createConnection();

            // Creating session for seding messages
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Getting the queue loanBroker
            Destination destination = session.createQueue(subject);

            // MessageConsumer is used for receiving (consuming) messages
            consumer = session.createConsumer(destination);

            connection.start(); // this is needed to start receiving messages

            consumer.setMessageListener(new MessageListener() {

                @Override
                public void onMessage(Message msg) {

                    try {
                        String messageText = ((TextMessage) msg).getText();

                        //Deserialize
                        Class cls = Class.forName(messageText.substring(messageText.lastIndexOf("!") + 1));
                        String jsonObject = messageText.substring(0, messageText.indexOf("!"));
                        Object deserializedObject = genson.deserialize(jsonObject,cls);

                        messageReceive(deserializedObject);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }

            });

        }
        catch (JMSException e){

            e.printStackTrace();

        }

        return consumer;

    }

    public void messageReceive(Object rr){

    }


}
