enum Side {
    BUY, SELL
}

public class Order {
    private String id;
    private String userName;
    private int price;
    private long volume;
    private Side side;

    public Order(String id, String userName, int price, long volume, Side side) {
        this.id = id;
        this.userName = userName;
        this.price = price;
        this.volume = volume;
        this.side = side;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }
}