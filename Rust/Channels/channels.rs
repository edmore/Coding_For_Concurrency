use std::thread;
use std::sync::mpsc;

fn main() {
    let (tx, rx) = mpsc::channel(); // async channel - "infinite buffer" - no send will block, recv will blcok until a message is available

    // we’re using thread::spawn to create a new thread and then using move to move tx into the closure so the spawned thread owns tx
    // The spawned thread needs to own the transmitting end of the channel to be able to send messages through the channel.
    thread::spawn(move || {
        let greeting = String::from("hi");

        // must be considered safe to send between threads
        // unwrap is used to panic if there is an error, in this case - should be handled correctly in a real application
        tx.send(greeting).unwrap();

        // Allowing this would be a bad idea: once the value has been sent to another thread,
        // that thread could modify or drop it before we try to use the value again.
        // println!("greeting is {}", greeting);
    });

    // The receiving end of a channel has two useful methods: recv and try_recv. 
    // We’re using recv, short for receive, which will block the main thread’s execution and wait until a value is sent down the channel.
    let received = rx.recv().unwrap();
    println!("Got: {}", received);
}