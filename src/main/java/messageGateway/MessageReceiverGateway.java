package messageGateway;

import messaging.requestreply.RequestReply;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMessage;
import serialisation.Deserialize;

import javax.jms.*;

public class MessageReceiverGateway {

    private MessageConsumer consumer;
    private serialisation.Deserialize deserializer;

    public MessageReceiverGateway() {

        deserializer = new Deserialize();

    }

    public MessageReceiverGateway(String subject) {

        receiveMessage(subject);
        deserializer = new Deserialize();

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
                        Object deserializedObject = deserializer.deserialize(messageText);
                        messageReceive(deserializedObject);
                    } catch (JMSException e) {
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
