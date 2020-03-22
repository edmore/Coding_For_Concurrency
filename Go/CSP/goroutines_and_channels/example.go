package main

import (
	"fmt"
	"time"
)

type CoffeeOrder struct {
	HowMany    int
	WhatToMake string
	Order      chan string
}

func processOrder(order CoffeeOrder) {
	message := fmt.Sprintf("%d, %s", order.HowMany, order.WhatToMake)
	order.Order <- fmt.Sprintf("Order : %s", message)
}

func main() {
	var order = make(chan string)

	go processOrder(CoffeeOrder{1, "Espresso", order})
	go processOrder(CoffeeOrder{2, "Flat White", order})

	// Brew
	for {
		select {
		case message := <-order: // synchronization
			fmt.Println("Brewing ... ", message)
		case <-time.After(3 * time.Second):
			fmt.Println("thank you ...")
			return
		}
	}
}
