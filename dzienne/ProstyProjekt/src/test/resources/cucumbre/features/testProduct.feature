Feature: Product Tests

Scenario: Compare test
Given We have a product "Car" with price 1.12
Given We have a product "Car" with price 1.12
When We compare both products
Then We obtain true