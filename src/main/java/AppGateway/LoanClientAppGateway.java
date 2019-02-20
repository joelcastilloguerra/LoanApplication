package AppGateway;

import messageGateway.MessageReceiverGateway;
import messageGateway.MessageSenderGateway;
import messaging.requestreply.RequestReply;
import model.bank.BankInterestReply;
import model.loan.LoanReply;
import model.loan.LoanRequest;

import javax.jms.*;

public class LoanClientAppGateway {

    private MessageSenderGateway<LoanRequest> messageSenderGateway;
    private MessageReceiverGateway messageReceiverGateway;

    public LoanClientAppGateway() {

        this.messageSenderGateway = new MessageSenderGateway<>();
        this.messageReceiverGateway = new MessageReceiverGateway("loanReply"){

            @Override
            public void messageReceive(Object loanReply){

                onLoanReplyArrived((LoanReply)loanReply);

            }

        };

    }

    public void applyForLoan(LoanRequest object){

        messageSenderGateway.sendMessage("loanbroker", object);

    }

    public void onLoanReplyArrived(LoanReply reply){


    }

}
