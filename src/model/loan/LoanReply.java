package model.loan;

/**
 *
 * This class stores all information about a model.bank offer
 * as a response to a client model.loan request.
 */
public class LoanReply implements java.io.Serializable{

        private double interest; // the interest that the model.bank offers
        private String bankID; // the unique quote identification
    private String cid;

    public LoanReply() {
        super();
        this.interest = 0;
        this.bankID = "";
    }

    public LoanReply(double interest, String quoteID, String cid) {
        super();
        this.interest = interest;
        this.bankID = quoteID;
        this.cid = cid;
    }

    public String getCid(){

        return this.cid;

    }

    public void setCid(String cid){

        this.cid = cid;

    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public String getQuoteID() {
        return bankID;
    }

    public void setQuoteID(String quoteID) {
        this.bankID = quoteID;
    }
    
    @Override
    public String toString(){
        return " interest="+String.valueOf(interest) + " quoteID="+String.valueOf(bankID);
    }
}
