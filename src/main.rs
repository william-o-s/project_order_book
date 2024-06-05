/*
Reference:
    IMC Trading Matching Engine
*/

use std::cmp::Ordering;
use std::collections::BTreeMap;
use std::collections::VecDeque;

type Id = u32;
type Username = String;
type Price = f64;
type Volume = u128;
type OrderBook = BTreeMap<Price, VecDeque<Order>>;

enum Side {
    BID,
    OFFER,
}

struct Order {
    id: Id,
    username: Username,
    price: Price,
    volume: Volume,
    side: Side
}

struct Trade {
    buyer: Username,
    seller: Username,
    price: Price,
    volume: Volume
}

fn can_fulfill_order(bids: OrderBook, offers: OrderBook) -> bool {
    let bid = if let Some(b) = bids.first_key_value() { b } else { return false };
    let offer = if let Some(o) = offers.first_key_value() { o } else { return false };

    bid.1.get(0).unwrap().price >= offer.1.get(0).unwrap().price
}

fn send_order(bids: &mut OrderBook, offers: &mut OrderBook, new_order: Order) {
    // Add order to bids or offers
    match new_order.side {
        Side::BID => bids.insert(new_order.price, new_order),
        Side::OFFER => offers.insert(new_order.price, new_order)
    }

    // Fulfill orders while crossing
    while self.can_fulfill_order() {
        let bid = self.bids.peek().unwrap();
        let offer = self.offers.peek().unwrap();

        match bid.quantity.cmp(&offer.quantity) {
            Ordering::Greater => {
                let mut bid = self.bids.peek_mut().unwrap();
                bid.quantity -= offer.quantity;
                self.offers.pop();
            },
            Ordering::Equal => {
                self.bids.pop();
                self.offers.pop();
            },
            Ordering::Less => {
                let mut offer = self.offers.peek_mut().unwrap();
                offer.quantity -= bid.quantity;
                self.bids.pop();
            }
        }
    }
}

fn submit_order() -> Vec<Trade> {
    unimplemented!()
}

fn main() {
    // init
    let bids: BTreeMap<f64, VecDeque<Order>> = BTreeMap::new();
    let offers: BTreeMap<f64, VecDeque<Order>> = BTreeMap::new();

    // update order book
    loop {
        // input order

        // process
    }
}