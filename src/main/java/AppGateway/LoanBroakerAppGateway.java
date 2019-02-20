package AppGateway;

import messageGateway.MessageReceiverGateway;
import messageGateway.MessageSenderGateway;
import model.bank.BankInterestReply;
import model.bank.BankInterestRequest;
import model.loan.LoanReply;
import model.loan.LoanRequest;
import serialisation.Deserialize;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

public class LoanBroakerAppGateway {

    private MessageSenderGateway<BankInterestRequest> bankInterestRequestMessageSenderGateway;
    private MessageSenderGateway<LoanReply> loanReplyMessageSenderGateway;
    private MessageReceiverGateway messageReceiverGateway;
    private serialisation.Deserialize deserializer;

    public LoanBroakerAppGateway() {

        this.bankInterestRequestMessageSenderGateway = new MessageSenderGateway<>();
        this.loanReplyMessageSenderGateway = new MessageSenderGateway<>();
        this.deserializer = new Deserialize();
        this.messageReceiverGateway = new MessageReceiverGateway("loanbroker"){

            @Override
            public void messageReceive(Object obj){

                if(obj instanceof LoanRequest){

                    onLoanRequestArrived((LoanRequest)obj);
                    BankInterestRequest bankInterestRequest = new BankInterestRequest(((LoanRequest) obj).getAmount(), ((LoanRequest) obj).getTime(), ((LoanRequest) obj).getCid());
                    sendBankRequest(bankInterestRequest);

                }
                else if(obj instanceof BankInterestReply){

                    onBankResponseArrived((BankInterestReply)obj);
                    LoanReply loanReply = new LoanReply(((BankInterestReply) obj).getInterest(), ((BankInterestReply) obj).getQuoteId(), ((BankInterestReply) obj).getCid());
                    sendLoanReply(loanReply);

                }


            }

        };

    }

    private void sendBankRequest(BankInterestRequest bankInterestRequest){

        bankInterestRequestMessageSenderGateway.sendMessage("bankRequest", bankInterestRequest);

    }

    private void sendLoanReply(LoanReply loanReply){

        loanReplyMessageSenderGateway.sendMessage("loanReply", loanReply);

    }

    public void onBankResponseArrived(BankInterestReply reply){

    }

    public void onLoanRequestArrived(LoanRequest request){

    }

}
