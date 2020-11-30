# Design Notes

The goal is to create an exhaustive set of models that represent data in a fictitious online pizza ordering service. These data models need to cover all use cases. 

The basic requirements are:

1. I want to order one cheese pizza with no toppings, and two veggie pizzas with A,B,C,D and E toppings etc
2. The application should remember who I am, so next time I visit the site my information is prepopulated (including the option to store my payment information)
3. Ability to email the order confirmation that contains the order details
4. Any other enhancement(s) you can think of

Please see the image in this folder titled "design.png" for the entity relationships.

### Thought Process

Here I will justify my decisions.

##### Menus & Orders

Original design idea was to represent each piece of data as a model, which quickly turned into an unnessarily confusing mess. There were models for Cheese, Crust, Sauce, and a few others - which all did the same thing and had the same properties. So these were consolidated into a single MenuItem, where other, non-pizza items could co-exist. For example, the basic pieces of information we need are:

* ID: Some identifier like an integer or SKU
* Name: This could be 12oz soda, or a type of crust used in a complex, pizza item "New York Style Crust"
* Price: Every item should have a related price
* Description: Pretty named used for presentation layer. Every MenuItem can benefit from this. "Delicious beverage!", "Our Marinara sauce is glorious"
* Available: Whether it is in stock

There is a clear distinction between an item on the menu, and an item in the shopping cart (and eventually in an order). An OrderItem must be able to accomodate combinations of MenuItems for complex types, or even just a single item for simple types like an order of cheese sticks. It is the responsibility of the service layer to validate MenuItem combinations in an OrderItem.

The most complex piece of this operation is an Order - which is in fact the end goal of the business. I think we've actually accomplished this quite elegantly. The root of an order is the Order entity, which is a composite of all of the other entities we've been working with so far. It contains:

* CustomerDetails: Our customer!
* Address: The address to deliver to.
* PaymentTransaction: The successful payment transaction used to place the order
* List\<OrderItem\>: A list of items that make up the order
* OrderStatusEvent: Used to track orders from the time of placement to fullfillment.

An OrderItem is simply a named collection of MenuItem entities. This allows us the flexibility to create custom pizzas from a combination of ingredients. It also lets us sell simple items like beverages and 

This is an example of a valid OrderItem, note that it has two toppings. And we don't have a price set for basic ingredients like cheese. More luxurious accoutrements would certainly incur additional fees.

What we're looking at is an **OrderItem** for a completely customized extra large, new york style pizza with two toppings:

```
{ 
    "name": "Valid X Large Pizza",
    "menuItems": [
        { "category": "PIZZA_SIZE", "name": "X-Large", "price": 19.99 },
        { "category": "PIZZA_CRUST", "name": "New York", "price": 0.00 },
        { "category": "PIZZA_SAUCE", "name": "Pesto", "price": 0.00 },
        { "category": "PIZZA_CHEESE", "name": "Mozarella", "price": 0.00 },
        { "category": "PIZZA_TOPPING", "name": "Jalapeno", "price": 0.99 },
        { "category": "PIZZA_TOPPING", "name": "Pineapple", "price": 0.99 }
    ]
}

```

We could write a service to pre-compute some exciting combinations like Hawaiin pizzas, or even reserve that for a frontend developer if we're extra lazy.

The service layer must ensure that this kind of **OrderItem** is invalid:

```
{
    "name": "Two pizzas in one with a sprite topping?!",
    "menuItems": [
        { "category": "PIZZA_SIZE", "name": "X-Large", "price": 19.99 },
        { "category": "PIZZA_SIZE", "name": "Medium", "price": 12.99 },
        { "category": "BEVERAGE", "name": "12oz soda", "price": 0.99 }
    ]
    ...
}
```

Another great feature of our order design is that we can track, and subscribe to different stages of the order process and take action. Theoretical steps might be placing of an order successful payment of an order (these two may not be together if user opts to pay in cash later), chef has started work on order, food is cooked, order delivery has started, order has been fulfilled. With each of these steps we will log them as an **OrderStatusEvent** pertaining to this order. Interested services could subscribe to these events and take action like send emails or SMS. An example might be an email to a customer informing them the order is out for delivery.

##### Payments

Though I've never worked on enterprise software that exeutes financial transactionos, my assumption is that this information for different payment methods is distinct, and therefore warrants its own entities. In our fake pizzeria we have outlined three **PaymentMethod** entities, each with unique properties.

* Cash: Traditional pizza eater who wants to pay the delivery man in cash
* CreditCard: Here we store perhaps card type and card number (optionally store cvv and exp date, but this may be discouraged)
* Paypal: Might just need to store the email address and prompt user to complete transaction on paypal. Not sure if there is a way to generate a token to allow the user to make additional purchases.

An attempted charge on a PaymentMethod is logged as a **PaymentTransaction** entity, which denotes success/failure, method, and date/time. 

