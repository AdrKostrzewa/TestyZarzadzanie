Feature: Test Cart

Scenario: Add single product to a Cart
Given We have an empty Cart
When We add a new Product into a Cart
Then We obtain Cart with lenght 1

Scenario: Add new product to a Cart
Given We have a non empty Cart
When We add a new Product into a Cart
Then We obtain the same Product on last place in Cart


Scenario: Remove element from a Cart
Given We have an empty Cart
When We add a new Product into a Cart
When We erase a last Product form a Cart
Then We obtain an empty Cart

Scenario Outline: Remove element from a Cart with parameters
Given We have an empty Cart
When We add a new Product "<product1>" into a Cart
When We erase a "<product2>" Product form a Cart
Then We obtain Cart with lenght <answer>

Examples:
	| product1 | product2 | answer |
	| Car      | Car      | 0      |
	| Car      | Doll     | 1      |

Scenario Outline: Remove repeating element from a Cart with parameters
Given We have an empty Cart
When We add a new Product "<product1>" into a Cart
When We add a new Product "<product2>" into a Cart
When We erase a "<product3>" Product form a Cart
Then We obtain Cart with lenght <answer>

Examples:
	| product1 | product2 | product3	|	answer |
	| Car      | Car      | Car			|   0      |
	| Car      | Doll     | Car      	|	1	   |