use std::thread;
use std::sync::mpsc;

fn main() {
    let (sender, receiver) = mpsc::channel();

    thread::spawn(move || {
        let mut mutable_coffee_order =  String::from("1, Espresso");
        let mutable_coffee_order_ref = &mut mutable_coffee_order;
        *mutable_coffee_order_ref =  String::from("1, Flat White");

        println!("Preparing to brew ... {}", mutable_coffee_order);

        sender.send(mutable_coffee_order).unwrap();

        // once the value has been sent to another thread,
        // that thread could modify it, so we should not be using it
        // println!("Brewing ... {}", mutable_coffee_order);
    });

    let received = receiver.recv().unwrap();
    println!("Brewing ... {}", received);
}