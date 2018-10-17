package main

import (
	"fmt"
	"time"
)

// Greeter defines a greeter type
type Greeter struct {
	Name       string
	WhoToGreet string
}

func printer(greeter Greeter) {
	message := fmt.Sprintf("%s, %s", greeter.Name, greeter.WhoToGreet)
	fmt.Println(fmt.Sprintf("Greeting received (from %s Greeter): %s", greeter.Name, message))
}

func greeter(greeter Greeter) {
	// greet
	go printer(greeter) // launch printer goroutine
}

func main() {

	go greeter(Greeter{"Howdy", "Goroutine"}) // launches goroutine
	go greeter(Greeter{"Howdy", "Google"})
	go greeter(Greeter{"Hello", "Golang"})
	go greeter(Greeter{"Good day", "Gorilla"})

	time.Sleep(3 * time.Second)
}
