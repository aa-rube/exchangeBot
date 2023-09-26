package app.fiveminchange.model;
public class RequestDetails {
    private String status;
    private boolean autorecalculated;
    private String ipAddress;
    private boolean isViewed;
    private String _id;
    private BankDetails fromBank;
    private BankDetails toBank;
    private double fromValue;
    private double toValue;
    private double rateIn;
    private double rateOut;
    private double commission;
    private String requestId;
    private String date;
    private int __v;
    private Object cardImage;
    private String fromAccount;
    private String toAccount;
    private User user;
    private String statusMessage;

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isAutorecalculated() {
        return autorecalculated;
    }

    public void setAutorecalculated(boolean autorecalculated) {
        this.autorecalculated = autorecalculated;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public boolean isViewed() {
        return isViewed;
    }

    public void setViewed(boolean viewed) {
        isViewed = viewed;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public BankDetails getFromBank() {
        return fromBank;
    }

    public void setFromBank(BankDetails fromBank) {
        this.fromBank = fromBank;
    }

    public BankDetails getToBank() {
        return toBank;
    }

    public void setToBank(BankDetails toBank) {
        this.toBank = toBank;
    }

    public double getFromValue() {
        return fromValue;
    }

    public void setFromValue(double fromValue) {
        this.fromValue = fromValue;
    }

    public double getToValue() {
        return toValue;
    }

    public void setToValue(double toValue) {
        this.toValue = toValue;
    }

    public double getRateIn() {
        return rateIn;
    }

    public void setRateIn(double rateIn) {
        this.rateIn = rateIn;
    }

    public double getRateOut() {
        return rateOut;
    }

    public void setRateOut(double rateOut) {
        this.rateOut = rateOut;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public Object getCardImage() {
        return cardImage;
    }

    public void setCardImage(Object cardImage) {
        this.cardImage = cardImage;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
