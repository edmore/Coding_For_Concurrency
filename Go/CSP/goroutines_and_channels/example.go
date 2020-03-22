package main

import (
	"fmt"
	"time"
)

type Coffee struct {
	HowMany       int
	WhatToMake string
	Order   chan string
}

func processOrder(coffee Coffee) {
	message := fmt.Sprintf("%d, %s", coffee.HowMany, coffee.WhatToMake)
	coffee.Order <- fmt.Sprintf("Order : %s", message)
}

func main() {
	var Order = make(chan string)

	go processOrder(Coffee{1, "Espresso", Order})
	go processOrder(Coffee{2, "Flat White", Order})

	// Brew
	for {
		select {
		case message := <-Order: // synchronization
			fmt.Println("Brewing ... ", message)
		case <-time.After(3 * time.Second):
			fmt.Println("thank you ...")
			return
		}
	}
}
