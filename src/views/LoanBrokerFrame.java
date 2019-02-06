package views;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Properties;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import model.bank.*;
import model.loan.LoanRequest;


public class LoanBrokerFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private DefaultListModel<JListLine> listModel = new DefaultListModel<JListLine>();
	private JList<JListLine> list;

	Connection connection;
	Session session;
	javax.jms.Destination receiveDestination;
	MessageConsumer consumer;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoanBrokerFrame frame = new LoanBrokerFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	/**
	 * Create the frame.
	 */
	public LoanBrokerFrame() {
		setTitle("Loan Broker");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{46, 31, 86, 30, 89, 0};
		gbl_contentPane.rowHeights = new int[]{233, 23, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 7;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		contentPane.add(scrollPane, gbc_scrollPane);
		
		list = new JList<JListLine>(listModel);
		scrollPane.setViewportView(list);

		this.getLoanMessage();
	}
	
	 private JListLine getRequestReply(LoanRequest request){    
	     
	     for (int i = 0; i < listModel.getSize(); i++){
	    	 JListLine rr =listModel.get(i);
	    	 if (rr.getLoanRequest() == request){
	    		 return rr;
	    	 }
	     }
	     
	     return null;
	   }
	
	public void add(LoanRequest loanRequest){		
		listModel.addElement(new JListLine(loanRequest));		
	}
	

	public void add(LoanRequest loanRequest,BankInterestRequest bankRequest){
		JListLine rr = getRequestReply(loanRequest);
		if (rr!= null && bankRequest != null){
			rr.setBankRequest(bankRequest);
            list.repaint();
		}		
	}
	
	public void add(LoanRequest loanRequest, BankInterestReply bankReply){
		JListLine rr = getRequestReply(loanRequest);
		if (rr!= null && bankReply != null){
			rr.setBankReply(bankReply);;
            list.repaint();
		}		
	}

	public void getLoanMessage(){

		try {
			Properties props = new Properties();
			props.setProperty(Context.INITIAL_CONTEXT_FACTORY,
					"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
			props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");

			props.put(("queue.myFirstDestination"), " myFirstDestination");

			Context jndiContext = new InitialContext(props);
			ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext
					.lookup("ConnectionFactory");
			connection = connectionFactory.createConnection();
			session = ((javax.jms.Connection) connection).createSession(false, Session.AUTO_ACKNOWLEDGE);

			// connect to the receiver destination
			receiveDestination = (Destination) jndiContext.lookup("myFirstDestination");
			consumer = session.createConsumer(receiveDestination);

			connection.start(); // this is needed to start receiving messages

		} catch (NamingException | JMSException e) {
			e.printStackTrace();
		}


		try {
			consumer.setMessageListener(new MessageListener() {

				@Override
				public void onMessage(Message msg) {


					// deserialize the object
					try {
						String messageText = ((TextMessage) msg).getText();
						byte b[] = Base64.decode(messageText.getBytes());
						ByteArrayInputStream bi = new ByteArrayInputStream(b);
						ObjectInputStream si = new ObjectInputStream(bi);
						LoanRequest obj = (LoanRequest) si.readObject();

						add(obj);
					} catch (Exception e) {
						System.out.println(e);
					}
				}
			});

		} catch (JMSException e) {
			e.printStackTrace();
		}


	}


}
