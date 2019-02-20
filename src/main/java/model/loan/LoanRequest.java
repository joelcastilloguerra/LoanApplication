package model.loan;

/**
 *
 * This class stores all information about a
 * request that a client submits to get a model.loan.
 *
 */
public class LoanRequest implements java.io.Serializable{

    private int ssn; // unique client number.
    private int amount; // the ammount to borrow
    private int time; // the time-span of the model.loan
    private String cid;

    public LoanRequest() {
        super();
        this.ssn = 0;
        this.amount = 0;
        this.time = 0;
    }

    public LoanRequest(int ssn, int amount, int time) {
        super();
        this.ssn = ssn;
        this.amount = amount;
        this.time = time;
    }

    public LoanRequest(int ssn, int amount, int time, String cid) {
        super();
        this.ssn = ssn;
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

    public int getSsn() {
        return ssn;
    }

    public void setSsn(int ssn) {
        this.ssn = ssn;
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
        return "ssn=" + String.valueOf(ssn) + " amount=" + String.valueOf(amount) + " time=" + String.valueOf(time);
    }
}
