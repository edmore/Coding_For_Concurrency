use std::thread;
use std::sync::mpsc;

fn main() {
    let (sender, receiver) = mpsc::channel(); // async channel - "infinite buffer" - no send will block, recv will blcok until a message is available

    for i in 0..10 {
        let sender = sender.clone(); // cloned so it sends to sam channel multiple times, only one receiver supported

        thread::spawn(move || {
            let answer = i * i;

            sender.send(answer).unwrap();
        });
    }

    for _ in 0..10 {
        println!("{}", receiver.recv().unwrap());
    }
}