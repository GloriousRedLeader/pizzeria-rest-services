INSERT INTO menu (name, price, available, category, description) VALUES ("X-Large Pizza", 17.99, true, "PIZZA_SIZE", "Feeds 4 adults.");
INSERT INTO menu (name, price, available, category, description) VALUES ("Large Pizza", 12.99, true, "PIZZA_SIZE", "Great for 2 - 3 people.");
INSERT INTO menu (name, price, available, category, description) VALUES ("Medium Pizza", 9.99, true, "PIZZA_SIZE", "Feeds 2 adults.");
INSERT INTO menu (name, price, available, category, description) VALUES ("Personal Pizza", 5.99, true, "PIZZA_SIZE", "Personal Pizza, perfect for a single person.");

INSERT INTO menu (name, price, available, category, description) VALUES ("Jalapenos", 0.99, true, "PIZZA_TOPPING", "Spicy topping.");
INSERT INTO menu (name, price, available, category, description) VALUES ("Anchovies", 0.99, true, "PIZZA_TOPPING", "Gross topping.");
INSERT INTO menu (name, price, available, category, description) VALUES ("Pineapple", 0.99, true, "PIZZA_TOPPING", "A proper, delicious topping. Individuals who don't like this clearly don't like freedom, justice, and liberty.");

INSERT INTO menu (name, price, available, category, description) VALUES ("Deep Dish Style Crust", 0.99, true, "PIZZA_CRUST", "Chicago Style (gross)");
INSERT INTO menu (name, price, available, category, description) VALUES ("New York Style Crust", 0.99, true, "PIZZA_CRUST", "New York does exactly one thing correctly, and that is pizza. Pizza that you can fold.");

INSERT INTO menu (name, price, available, category, description) VALUES ("Mozzarella Cheese", 0.99, true, "PIZZA_CHEESE", "Standard, delicious, proven Mozzarella cheese.");
INSERT INTO menu (name, price, available, category, description) VALUES ("Goat Cheese", 0.99, true, "PIZZA_CHEESE", "Not sure who would want this, but here we are.");

INSERT INTO menu (name, price, available, category, description) VALUES ("Tomato Sauce", 0.99, true, "PIZZA_SAUCE", "Standard sauce made from tomatoes.");
INSERT INTO menu (name, price, available, category, description) VALUES ("Pesto Sauce", 0.99, true, "PIZZA_SAUCE", "Not terrible.");

INSERT INTO menu (name, price, available, category, description) VALUES ("Chimay", 9.99, true, "BEVERAGE", "1 pint Adult Beverage");
INSERT INTO menu (name, price, available, category, description) VALUES ("Coke", 3.99, true, "BEVERAGE", "16 oz glass bottle mexican coke.");
INSERT INTO menu (name, price, available, category, description) VALUES ("Sprite", 0.99, true, "BEVERAGE", "12 oz canned Sprite soda.");

INSERT INTO menu (name, price, available, category, description) VALUES ("Breadsticks", 7.99, true, "SIDE", "8 Breadsticks");
INSERT INTO menu (name, price, available, category, description) VALUES ("Garlic Rolls", 9.99, true, "SIDE", "10 Greasy garlic rolls");

INSERT INTO customers (role, email, username, password, address) VALUES ("ROLE_ADMIN", "burro@churro.com", "burrolover", "$2a$10$LdpscfEJjZv8EHqjpa.ROOPFAjIXDi0MbwqNVsy5qErQp74BOJeU.", "1600 Pennsylvania Avenue NW Washington, D.C");
INSERT INTO customers (role, email, username, password, address) VALUES ("ROLE_USER", "princewillhelm@buckingham.com", "muffinman", "$2y$12$zfU8rfOqNCgiLQjfs0uPVuCIoqy0qDS3DEIqtkzvVjZt58OZmThh6", "Whitehall, Westminster, London SW1A 2ER, United Kingdom");