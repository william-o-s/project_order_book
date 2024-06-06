public class Trade {
    final String buyer;
    final String seller;
    final int price;
    final long volume;

    public Trade(String buyer, String seller, int price, long volume) {
        this.buyer = buyer;
        this.seller = seller;
        this.price = price;
        this.volume = volume;
    }

    public String getBuyer() {
        return buyer;
    }

    public String getSeller() {
        return seller;
    }

    public int getPrice() {
        return price;
    }

    public long getVolume() {
        return volume;
    }
}
