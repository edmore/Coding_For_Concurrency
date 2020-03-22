use std::thread;
use std::sync::mpsc;

fn main() {
    let (sender, receiver) = mpsc::channel();

    thread::spawn(move || {
        let coffee_order = String::from("1, Espresso");

        sender.send(coffee_order).unwrap();

        // Allowing this would be a bad idea: once the value has been sent to another thread,
        // that thread could modify or drop it before we try to use the value again.
        println!("Brewing ... {}", coffee_order);
    });

    let received = receiver.recv().unwrap();
    println!("Brewing ... {}", received);
}