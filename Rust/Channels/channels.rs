use std::thread;
use std::sync::mpsc;

fn main() {
    let (tx, rx) = mpsc::channel();

    thread::spawn(move || {
        let coffee_order = String::from("1, Espresso");

        tx.send(coffee_order).unwrap();

        // Allowing this would be a bad idea: once the value has been sent to another thread,
        // that thread could modify or drop it before we try to use the value again.
        println!("Brewing ... {}", coffee_order);
    });

    let received = rx.recv().unwrap();
    println!("Brewing ... {}", received);
}