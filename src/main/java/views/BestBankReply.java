package views;

public class BestBankReply {

    String id;
    Double count;
    Double interest;
    String bank;

    public BestBankReply(String id, Double count) {
        this.id = id;
        this.count = count;
        this.bank = null;
        this.interest = null;
    }

    public BestBankReply() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public Double getInterest() {
        return interest;
    }

    public void setInterest(Double interest) {
        this.interest = interest;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }
}
