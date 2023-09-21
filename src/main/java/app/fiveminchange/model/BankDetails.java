package app.fiveminchange.model;
public class BankDetails {
    private boolean isTake;
    private boolean isGive;
    private boolean isReferal;
    private String _id;
    private int __v;
    private String useWallet;
    private String code;
    private String iconUrl;
    private String title;
    private String type;
    private String name;

    public boolean isTake() {
        return isTake;
    }

    public void setTake(boolean take) {
        isTake = take;
    }

    public boolean isGive() {
        return isGive;
    }

    public void setGive(boolean give) {
        isGive = give;
    }

    public boolean isReferal() {
        return isReferal;
    }

    public void setReferal(boolean referal) {
        isReferal = referal;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public String getUseWallet() {
        return useWallet;
    }

    public void setUseWallet(String useWallet) {
        this.useWallet = useWallet;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
