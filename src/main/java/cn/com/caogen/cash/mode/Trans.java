package cn.com.caogen.cash.mode;


public class Trans {

    private String  businessId;
    private String  cardholder;
    private String  cardNo;
    private String  tradeMoney;
    private String  remark;

    public Trans(){}

    public String getBusinessId() {
        return businessId;
    }



    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getCardholder() {
        return cardholder;
    }

    public void setCardholder(String cardholder) {
        this.cardholder = cardholder;
    }



    public String getCardNo() {
        return cardNo;
    }



    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }



    public String getTradeMoney() {
        return tradeMoney;
    }



    public void setTradeMoney(String tradeMoney) {
        this.tradeMoney = tradeMoney;
    }



    public String getRemark() {
        return remark;
    }



    public void setRemark(String remark) {
        this.remark = remark;
    }



    public Trans(String businessId, String cardHolder, String cardNo, String tradeMoney, String remark) {
        super();
        this.businessId = businessId;
        this.cardholder = cardHolder;
        this.cardNo = cardNo;
        this.tradeMoney = tradeMoney;
        this.remark = remark;
    }

}
