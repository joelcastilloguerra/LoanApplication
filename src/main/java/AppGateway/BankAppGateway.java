package AppGateway;

import messageGateway.MessageReceiverGateway;
import messageGateway.MessageSenderGateway;
import messaging.requestreply.RequestReply;
import model.bank.BankInterestReply;
import model.bank.BankInterestRequest;
import model.loan.LoanReply;
import model.loan.LoanRequest;

public class BankAppGateway {

    private MessageSenderGateway<BankInterestReply> messageSenderGateway;
    private MessageReceiverGateway messageReceiverGateway;

    public BankAppGateway() {

        this.messageSenderGateway = new MessageSenderGateway<>();
        this.messageReceiverGateway = new MessageReceiverGateway("bankRequest"){

            @Override
            public void messageReceive(Object bankRequest){

                onBankRequestArrived((BankInterestRequest)bankRequest);

            }

        };

    }

    public void sendBankInterestReply(BankInterestReply object){

        messageSenderGateway.sendMessage("loanbroker", object);

    }

    public void onBankRequestArrived(BankInterestRequest request){


    }

}
