package main

import (
	"fmt"
	"time"
)

type CoffeeOrder struct {
	HowMany    int
	WhatToMake string
	Order      chan *string
}

func processOrder(order CoffeeOrder) {
	customerOrder := fmt.Sprintf("%d, %s", order.HowMany, order.WhatToMake)
	order.Order <- &customerOrder

	// what if we then update the customer's order?
	customerOrder = "1, Cappuccino"
}

func main() {
	var order = make(chan *string)

	go processOrder(CoffeeOrder{1, "Espresso", order})
	go processOrder(CoffeeOrder{2, "Flat White", order})

	// Brew
	for {
		select {
		case customerOrder := <-order:
			fmt.Println("Brewing ... ", *customerOrder)
		case <-time.After(3 * time.Second):
			fmt.Println("oops ...")
			return
		}
	}
}
