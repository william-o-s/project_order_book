/*
Reference:
    Jane Street Order Book basics
    https://doc.rust-lang.org/beta/std/collections/struct.BinaryHeap.html
    https://doc.rust-lang.org/beta/std/collections/binary_heap/index.html

Background:
    Bid-Offer order pairs are fulfilled when they cross. Thus, the highest bid and lowest offer is
    most likely to cross, and the order book reflects this in priority. Additionally, older
    'resting' orders are prioritised over newer ones, and order pairs are processed at the resting
    price.

Implementation:
    Bids and offers are compared by price (with respective ordering) then by position, which is
    an incrementing counter, with the lowest position being earlier orders.

    Bids are ordered by: max-price
    Offers are ordered by: min-price aka reverse-max-price

Input:
    <stock_identifier> <participant> <order_type> <price> <quantity>
Output:
    "Participant <participant> bought <quantity>x <stock_identifier>
    shares @ $<price>:.2 from Participant <participant>"
Example:
     in> ABC Y bid 15.50 100
     in> ABC A offer 16 100
     in> ABC B offer 15 100
    out> Participant Y bought 100x ABC shares @ $15.50 from Participant B

Input:
    print <stock_identifier>
Example:
    [Stock ABC]
               Bid | Ask
    Y 100 @ $15.50 | B 100 @ $15.00
                   | A 100 @ $16.00
*/

use std::cmp::Ordering;
use std::collections::BinaryHeap;
use std::fmt::{Display, Formatter, Result};
use ordered_float::NotNan;

#[derive(PartialEq, Eq)]
enum OrderType {
    BID,
    OFFER,
}

#[derive(PartialEq, Eq)]
struct Order {
    participant: String,
    order_type: OrderType,
    price: NotNan<f32>,
    quantity: u32,
    position: u32
}

impl Display for Order {
    fn fmt(&self, f: &mut Formatter) -> Result {
        let s = format!("Price: ${:.2} | Quantity: {} shares", self.price, self.quantity);
        s.fmt(f)
    }
}

impl Ord for Order {
    fn cmp(&self, other: &Self) -> Ordering {
        // NOTE: max-heap default
        let sort_by_price = match self.order_type {
            OrderType::BID => self.price.cmp(&other.price),
            OrderType::OFFER => other.price.cmp(&self.price)
        };
        let sort_by_position = other.position.cmp(&self.position);

        sort_by_price.then(sort_by_position)
    }
}

impl PartialOrd for Order {
    fn partial_cmp(&self, other: &Self) -> Option<Ordering> {
        Some(self.cmp(other))
    }
}

struct OrderBook {
    stock_identifier: String,
    bids: BinaryHeap<Order>,
    offers: BinaryHeap<Order>
}

impl OrderBook {
    fn print_book(self) {
        println!("Stock: {}", self.stock_identifier);

        let mut bids = self.bids.into_sorted_vec();
        bids.reverse();
        let mut offers = self.offers.into_sorted_vec();
        offers.reverse();


        println!("[Bids]");
        for order in bids {
            println!("{} {} @ ${}", order.participant, order.quantity, order.price);
        }

        unimplemented!("print order book not implemented!")
    }

    fn can_fulfill_order(&self) -> bool {
        let bid = if let Some(b) = self.bids.peek() { b } else { return false };
        let offer = if let Some(o) = self.offers.peek() { o } else { return false };

        bid.price >= offer.price
    }

    fn send_order(&mut self, new_order: Order) {
        // Add order to bids or offers
        match new_order.order_type {
            OrderType::BID => self.bids.push(new_order),
            OrderType::OFFER => self.offers.push(new_order)
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
}

fn main() {
    // init
    let bids: BinaryHeap<Order> = BinaryHeap::new();
    let offers: BinaryHeap<Order> = BinaryHeap::new();

    // update order book
    loop {
        // input order

        // process
    }
}