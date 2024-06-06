import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class MatchingEngineTest {
    MatchingEngine engine;

    @Before
    public void init_engine() {
        engine = new MatchingEngine();
    }

    @Test
    public void submit_one_bid() {
        engine.submitOrder(new Order("1", "a", 10, 20, Side.BUY));

        // Check BUY side
        assertArrayEquals(Arrays.asList(10).toArray(), engine.getPriceLevels(Side.BUY).toArray());
        assertEquals(20, engine.getVolumeAtLevel(Side.BUY, 10));

        // Check SELL side
        assertArrayEquals(Arrays.asList().toArray(), engine.getPriceLevels(Side.SELL).toArray());
        assertEquals(0, engine.getVolumeAtLevel(Side.SELL, 10));
    }

    @Test
    public void submit_two_bids() {
        engine.submitOrder(new Order("1", "a", 10, 20, Side.BUY));
        engine.submitOrder(new Order("2", "a", 20, 20, Side.BUY));
        engine.submitOrder(new Order("3", "b", 10, 20, Side.BUY));

        // Check BUY side
        assertArrayEquals(Arrays.asList(20, 10).toArray(), engine.getPriceLevels(Side.BUY).toArray());
        assertEquals(40, engine.getVolumeAtLevel(Side.BUY, 10));
    }

    @Test
    public void submit_one_offer() {
        engine.submitOrder(new Order("1", "a", 20, 30, Side.SELL));

        // Check SELL side
        assertArrayEquals(Arrays.asList(20).toArray(), engine.getPriceLevels(Side.SELL).toArray());
        assertEquals(30, engine.getVolumeAtLevel(Side.SELL, 20));

        // Check BUY side
        assertArrayEquals(Arrays.asList().toArray(), engine.getPriceLevels(Side.BUY).toArray());
        assertEquals(0, engine.getVolumeAtLevel(Side.BUY, 20));
    }

    @Test
    public void submit_two_offers() {
        engine.submitOrder(new Order("1", "a", 20, 30, Side.SELL));
        engine.submitOrder(new Order("2", "a", 20, 30, Side.SELL));
        engine.submitOrder(new Order("3", "b", 40, 30, Side.SELL));

        // Check SELL side
        assertArrayEquals(Arrays.asList(20, 40).toArray(), engine.getPriceLevels(Side.SELL).toArray());
        assertEquals(60, engine.getVolumeAtLevel(Side.SELL, 20));
    }

    @Test
    public void cancel_one_bid() {
        engine.submitOrder(new Order("1", "a", 20, 30, Side.BUY));
        assertTrue(engine.cancelOrder("a", "1"));

        assertArrayEquals(Arrays.asList().toArray(), engine.getPriceLevels(Side.BUY).toArray());
        assertEquals(0, engine.getVolumeAtLevel(Side.BUY, 0));
    }

    @Test
    public void cancel_two_bids() {
        engine.submitOrder(new Order("1", "a", 10, 20, Side.BUY));
        engine.submitOrder(new Order("2", "a", 10, 20, Side.BUY));
        engine.submitOrder(new Order("3", "b", 20, 20, Side.BUY));

        assertTrue(engine.cancelOrder("a", "1"));
        assertArrayEquals(Arrays.asList(20, 10).toArray(), engine.getPriceLevels(Side.BUY).toArray());
        assertEquals(20, engine.getVolumeAtLevel(Side.BUY, 20));

        assertTrue(engine.cancelOrder("b", "3"));
        assertArrayEquals(Arrays.asList(10).toArray(), engine.getPriceLevels(Side.BUY).toArray());
        assertEquals(0, engine.getVolumeAtLevel(Side.BUY, 20));
    }

    @Test
    public void match_one_bid() {
        List<Trade> trades;

        trades = engine.submitOrder(new Order("1", "a", 10, 20, Side.BUY));
        assertTrue(trades.isEmpty());

        trades = engine.submitOrder(new Order("2", "a", 10, 20, Side.SELL));
        assertArrayEquals(
            Arrays.asList(new Trade("a", "b", 10, 20)).toArray(),
            trades.toArray());

        assertEquals(0, engine.getVolumeAtLevel(Side.BUY, 10));
        assertEquals(0, engine.getVolumeAtLevel(Side.SELL, 10));
    }

    @Test
    public void match_multiple_bids() {
        List<Trade> trades;

        // Test time priority
        engine.submitOrder(new Order("1", "a", 10, 20, Side.BUY));
        engine.submitOrder(new Order("2", "b", 10, 30, Side.BUY));
        engine.submitOrder(new Order("3", "c", 10, 10, Side.BUY));

        trades = engine.submitOrder(new Order("4", "d", 10, 55, Side.SELL));
        assertArrayEquals(
            Arrays.asList(
                new Trade("a", "d", 10, 20),
                new Trade("b", "d", 10, 30),
                new Trade("c", "d", 10, 5)).toArray(),
            trades.toArray());

        assertArrayEquals(Arrays.asList(10).toArray(), engine.getPriceLevels(Side.BUY).toArray());
        assertArrayEquals(Arrays.asList().toArray(), engine.getPriceLevels(Side.SELL).toArray());
        assertEquals(5, engine.getVolumeAtLevel(Side.BUY, 10));

        // Test price priority
        engine.submitOrder(new Order("5", "e", 5, 10, Side.BUY));

        trades = engine.submitOrder(new Order("6", "d", 5, 20, Side.SELL));
        assertArrayEquals(
            Arrays.asList(
                new Trade("e", "d", 5, 10),
                new Trade("c", "d", 10, 5)).toArray(),
            trades.toArray());

        assertArrayEquals(Arrays.asList().toArray(), engine.getPriceLevels(Side.BUY).toArray());
        assertArrayEquals(Arrays.asList(5).toArray(), engine.getPriceLevels(Side.SELL).toArray());
        assertEquals(5, engine.getVolumeAtLevel(Side.SELL, 5));
    }
}
