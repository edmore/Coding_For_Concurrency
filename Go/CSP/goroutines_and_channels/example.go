package main

import (
	"fmt"
	"time"
)

// Greeter defines a greeter type
type Greeter struct {
	Name       string
	WhoToGreet string
	Greeting   chan string
}

func printerProxy(greeter Greeter) {
	message := fmt.Sprintf("%s, %s", greeter.Name, greeter.WhoToGreet)
	greeter.Greeting <- fmt.Sprintf("Greeting received (from %s Greeter): %s", greeter.Name, message)
}

func greeter(greeter Greeter) {
	// greet
	printerProxy(greeter) // launch printer proxy goroutine
}

func main() {
	var greeting = make(chan string)

	go greeter(Greeter{"Sawubona", "Goroutine", greeting}) // launches goroutine
	go greeter(Greeter{"Sawubona", "Google", greeting})
	go greeter(Greeter{"Hello", "Golang", greeting})
	go greeter(Greeter{"Good day", "Gorilla", greeting})

	// print in main goroutine
	for {
		select {
		case message := <-greeting:
			fmt.Println(message)
		case <-time.After(3 * time.Second):
			fmt.Println("bye ...")
			return
		}
	}
}
