package model.bank;

/**
 *
 * This class stores all information about an request from a model.bank to offer
 * a model.loan to a specific client.
 */
public class BankInterestRequest implements java.io.Serializable{

    private int amount; // the requested model.loan amount
    private int time; // the requested model.loan period
    private String cid;

    public BankInterestRequest() {
        super();
        this.amount = 0;
        this.time = 0;
    }

    public BankInterestRequest(int amount, int time) {
        super();
        this.amount = amount;
        this.time = time;
    }

    public BankInterestRequest(int amount, int time, String cid) {
        super();
        this.amount = amount;
        this.time = time;
        this.cid = cid;
    }

    public String getCid(){

        return this.cid;

    }

    public void setCid(String cid){

        this.cid = cid;

    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return " amount=" + amount + " time=" + time;
    }
}
