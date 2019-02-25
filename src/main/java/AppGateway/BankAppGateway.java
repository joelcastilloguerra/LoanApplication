package AppGateway;

import messageGateway.MessageReceiverGateway;
import messageGateway.MessageSenderGateway;
import model.bank.BankInterestReply;
import model.bank.BankInterestRequest;

abstract public class BankAppGateway {

    private MessageSenderGateway<BankInterestReply> messageSenderGateway;
    private MessageReceiverGateway messageReceiverGateway;

    protected BankAppGateway(String receiverQue) {

        this.messageSenderGateway = new MessageSenderGateway<>();

        this.messageReceiverGateway = new MessageReceiverGateway(receiverQue){

            @Override
            public void messageReceive(Object bankRequest){

                onBankRequestArrived((BankInterestRequest)bankRequest);

            }

        };

    }

    public void sendBankInterestReply(BankInterestReply object){

        messageSenderGateway.sendMessage("loanbroker", object);

    }

    abstract public void onBankRequestArrived(BankInterestRequest request);


}
