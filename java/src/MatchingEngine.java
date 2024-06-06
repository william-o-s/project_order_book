import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class MatchingEngine {
    private final TreeMap<Integer, LinkedHashMap<UID, Order>> bids = new TreeMap<>(Comparator.reverseOrder());;
    private final TreeMap<Integer, LinkedHashMap<UID, Order>> offers = new TreeMap<>();;
    private HashMap<Integer, Long> bid_volumes = new HashMap<>();
    private HashMap<Integer, Long> offer_volumes = new HashMap<>();
    private final HashMap<UID, Order> orders = new HashMap<>();

    public MatchingEngine() {}

    private boolean canFulfillOrders(Order thisOrder, Order otherOrder) {
        assert(thisOrder.getSide() != otherOrder.getSide());
        return thisOrder.getSide() == Side.BUY
            ? thisOrder.getPrice() >= otherOrder.getPrice()
            : otherOrder.getPrice() >= thisOrder.getPrice();
    }

    public List<Trade> submitOrder(Order order) {
        TreeMap<Integer, LinkedHashMap<UID, Order>> thisOrderBook = order.getSide() == Side.BUY ? bids : offers;
        TreeMap<Integer, LinkedHashMap<UID, Order>> otherOrderBook = order.getSide() == Side.BUY ? offers : bids;
        HashMap<Integer, Long> thisVolumes = order.getSide() == Side.BUY ? bid_volumes : offer_volumes;
        HashMap<Integer, Long> otherVolumes = order.getSide() == Side.BUY ? offer_volumes : bid_volumes;

        // Loop until either order volume is 0 or no match present
        while (order.getVolume() > 0) {


            if (order.getSide() == Side.BUY && otherOrderBook.firstEntry().getValue().)
        }

        // Add order if volume remaining
        if (order.getVolume() > 0) {
            UID newUid = new UID(order.getUserName(), order.getId());

            // Add to: order book, volumes, orders
            thisOrderBook.compute(
                order.getPrice(),
                (k, v) -> (v == null) ? new LinkedHashMap<UID, Order>().put(newUid, order));
            thisVolumes.compute(order.getPrice(), (k, v) -> (v == null) ? order.getVolume() : v + order.getVolume());
            orders.put(newUid, order);
        }
    }

    public boolean cancelOrder(String userName, String id) {
        UID uid = new UID(userName, id);

        if (!orders.containsKey(uid)) return false;

        Order order = orders.remove(uid);
        if (order.getSide() == Side.BUY)
            order = bids.get(order.getPrice()).remove(uid);
        else
            order = offers.get(order.getPrice()).remove(uid);

        return !(order == null);
    }

    public List<Integer> getPriceLevels(Side side) {
        TreeMap<Integer, LinkedHashMap<UID, Order>> orderBook = side == Side.BUY ? bids : offers;

        return orderBook
                .entrySet()
                .stream()
                .filter(order -> !order.getValue().isEmpty())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public long getVolumeAtLevel(Side side, int price) {
        return side == Side.BUY
            ? bid_volumes.getOrDefault(price, 0L)
            : offer_volumes.getOrDefault(price, 0L);
    }
}