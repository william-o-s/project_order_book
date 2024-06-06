/*
Reference:
    IMC Trading Matching Engine
*/

use std::cmp::Ordering;
use std::collections::BTreeMap;
use std::collections::VecDeque;

// Primitives
type Id = u32;
type Username = String;
type Price = f64;
type Volume = u128;

// Composites
type OrderQueue = VecDeque<Order>;
type OrderBook = BTreeMap<Price, OrderQueue>;

enum Side {
    BID,
    OFFER,
}

pub struct Order {
    id: Id,
    username: Username,
    price: Price,
    volume: Volume,
    side: Side
}

pub struct Trade {
    buyer: Username,
    seller: Username,
    price: Price,
    volume: Volume
}

pub struct MatchingEngine {
    bids: OrderBook,
    offers: OrderBook,
    price_volumes: 
}

impl MatchingEngine {
    pub fn submit_order(&mut self, order: Order) -> Vec<Trade> {
        unimplemented!()
    }

    pub fn get_price_levels(side: Side) -> Vec<Price> {
        unimplemented!()
    }

    pub fn get_volume_at_price(side: Side, price: Price) -> Volume {
        unimplemented!()
    }

    pub fn cancel_order() {
        unimplemented!()
    }
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