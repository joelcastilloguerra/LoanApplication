package AppGateway;

import messageGateway.MessageReceiverGateway;
import messageGateway.MessageSenderGateway;
import model.bank.BankInterestReply;
import model.bank.BankInterestRequest;
import model.loan.LoanReply;
import model.loan.LoanRequest;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;
import views.BestBankReply;

import java.util.*;

public class LoanBroakerAppGateway {

    private MessageSenderGateway<BankInterestRequest> bankInterestRequestMessageSenderGateway;
    private MessageSenderGateway<LoanReply> loanReplyMessageSenderGateway;
    private MessageReceiverGateway messageReceiverGateway;
    private List<BestBankReply> bestBankReplies;

    public LoanBroakerAppGateway() {

        this.bestBankReplies = new ArrayList<>();
        this.bankInterestRequestMessageSenderGateway = new MessageSenderGateway<>();
        this.loanReplyMessageSenderGateway = new MessageSenderGateway<>();
        this.messageReceiverGateway = new MessageReceiverGateway("loanbroker"){

            @Override
            public void messageReceive(Object obj){

                if(obj instanceof LoanRequest){

                    onLoanRequestArrived((LoanRequest)obj);
                    BankInterestRequest bankInterestRequest = new BankInterestRequest(((LoanRequest) obj).getAmount(), ((LoanRequest) obj).getTime(), ((LoanRequest) obj).getCid());
                    List<String> correctBanks = getCorrectBanks(bankInterestRequest);
                    bestBankReplies.add(new BestBankReply(bankInterestRequest.getCid(), (double)correctBanks.size()));
                    sendBankRequest(correctBanks, bankInterestRequest);

                }
                else if(obj instanceof BankInterestReply){

                    onBankResponseArrived((BankInterestReply)obj);

                    if(processBankReply((BankInterestReply)obj)){

                        BestBankReply bestBankReply = getBestBankReply(((BankInterestReply)obj).getCid());
                        LoanReply loanReply = new LoanReply(bestBankReply.getInterest(), bestBankReply.getBank(), bestBankReply.getId());
                        sendLoanReply(loanReply);

                    }



                }


            }

        };

    }

    private void sendBankRequest(List<String> correctBanks, BankInterestRequest bankInterestRequest){

        for(String bank : correctBanks){

            bankInterestRequestMessageSenderGateway.sendMessage(bank, bankInterestRequest);

        }

    }

    private void sendLoanReply(LoanReply loanReply){

        loanReplyMessageSenderGateway.sendMessage("loanReply", loanReply);

    }

    public void onBankResponseArrived(BankInterestReply reply){

    }

    public void onLoanRequestArrived(LoanRequest request){

    }

    private List<String> getCorrectBanks(BankInterestRequest request){

        // AllBanks are............................. all banks.
        HashMap<String, String> allBanks = new HashMap<>();

        // CorrectBanks are the banks that accept the BankInterestRequest
        List<String> correctBanks = new ArrayList<>();

        //Jeval bank rules
        String ING       = "#{amount} <= 100000 && #{time} <= 10";
        String ABN_AMRO  = "#{amount} >= 200000 && #{amount} <= 300000  && #{time} <= 20";
        String RABO_BANK = "#{amount} <= 250000 && #{time} <= 15";

        // Add the bank with rules to the allBanks list
        allBanks.put("ING", ING);
        allBanks.put("ABN_AMRO", ABN_AMRO);
        allBanks.put("RABO_BANK", RABO_BANK);

        Evaluator evaluator = new Evaluator(); // for evaluation of bank rules

        // set values of variables amount and time
        evaluator.putVariable("amount", Integer.toString(request.getAmount()));
        evaluator.putVariable("time", Integer.toString(request.getTime()));

        String result = null; // evaluate ING rule

        // Loop through the list of banks
        for(Map.Entry<String, String> bank : allBanks.entrySet()){

            try {

                result = evaluator.evaluate(bank.getValue());

                if(result.equals("1.0")){

                    // Add the bank to the correctBanks list if the bank accepts the request
                    correctBanks.add(bank.getKey());

                }

            } catch (EvaluationException e) {

                e.printStackTrace();

            }

        }

        return correctBanks;

    }

    public Boolean processBankReply(BankInterestReply reply){

        for (BestBankReply bestBankReply : bestBankReplies){

            //if the HashMap id is the same as the BankInterestReply id
            if(bestBankReply.getId().equals(reply.getCid())){

                //If the idWithInterest interest rate is higher than the BankInterestReply interest rate OR if the idWithInterest interest rate is null
                if(bestBankReply.getInterest() == null || bestBankReply.getInterest() > reply.getInterest()){

                    bestBankReply.setInterest(reply.getInterest());
                    bestBankReply.setBank(reply.getQuoteId());

                }

                //If all the replies came in
                if(bestBankReply.getCount() <= 1){

                    return true;

                }

                //Subtract one from the count
                bestBankReply.setCount(bestBankReply.getCount() - 1);

            }

            return false;

        }

        return false;

    }

    public BestBankReply getBestBankReply(String cid){

        for (BestBankReply bestBankReply : bestBankReplies){

            if(bestBankReply.getId().equals(cid)){

                return bestBankReply;

            }

        }

        return null;

    }

}
