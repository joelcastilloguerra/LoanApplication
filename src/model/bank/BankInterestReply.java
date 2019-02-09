package model.bank;

/**
 * This class stores information about the model.bank reply
 *  to a model.loan request of the specific client
 * 
 */
public class BankInterestReply implements java.io.Serializable{

    private double interest; // the model.loan interest
    private String bankId; // the nunique quote Id
    private String cid;
    
    public BankInterestReply() {
        this.interest = 0;
        this.bankId = "";
    }
    
    public BankInterestReply(double interest, String quoteId) {
        this.interest = interest;
        this.bankId = quoteId;
    }

    public BankInterestReply(double interest, String quoteId, String cid) {
        this.interest = interest;
        this.bankId = quoteId;
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

    public String getQuoteId() {
        return bankId;
    }

    public void setQuoteId(String quoteId) {
        this.bankId = quoteId;
    }

    public String toString() {
        return "quote=" + this.bankId + " interest=" + this.interest;
    }
}
