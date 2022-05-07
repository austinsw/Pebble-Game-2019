# Pebble-Game

Multithreaded game & test suite, created using Java and JUnit 4 in 2019.

## Requirements

In this assignment you are to model a game involving multiple competing players, in a thread-safe fashion. The game being modelled has a strictly positive number of players. There are six bags of pebbles in the game, three white bags (A, B and C) and three black bags (X, Y and Z). At the start of the game the white bags are empty and the black bags are full. Each player takes 10 pebbles from a black bag (the black bag each player selects is chosen at random). Each pebble has an integer weight value.

If the weight of pebbles held by a player is 100, then they have won. They should immediately announce this fact to other players, and all other simulated players should stop playing (once they have ensured they only have 10 pebbles in their possession). If a player does not hold a winning hand they should discard a pebble to a white bag. They should then select one of the three black bags at random and draw a new pebble from this bag. This process should continue until either the player has won (has 10 pebbles with a total combined weight of 100), or until another player has won and the game has ended. 

On starting, the command-line program should request the number of players, and after this has been entered it should then request the location of three files in turn containing the weight of each of the pebbles used in the black bags at the start of the game. An example of this is

![ecm2414](https://user-images.githubusercontent.com/42607409/166974107-1ffb565d-0282-4ae6-864f-9c05fcc26dd2.png)

The	legal	format	of	these	files	is	as	a	comma-separated	list	of	integers.

The	program	should perform	 exception-handling	to	cope	with	illegal	player	number	entry	and	 illegal	file	formatting/values	and	request	the	user to	provide	legal	values	until	both	inputs	are	 legal,	or	until	the	user	enters	‘E’ (in	this	case	the	program	should	exit). 

The	simulated	game	should	have	the	following	properties:
- Once	a	black	bag	is	empty,	all	the	pebbles	from	a	white bag	should	be	emptied into	it. Bag	X	should	be	filled	by	bag	A,	bag	Y	filled	by	bag	B,	and	bag	Z	filled	by	bag	C.
- The	process	of	drawing	pebbles	from	a	black	bag	should	be	uniformly	at	random – that	 is	it	should	be	equally	probable	to	draw	any of	the	pebbles	in	the	bag.
- The	 players	 should	 act	 as	 concurrent	threads,	with	 the	 threading	 commencing	before drawing	their	initial	pebbles.
- Players	should	be	implemented	as	nested	classes	within a PebbleGame application.
- Drawing	and	discarding	should	be	an	atomic	action	– additionally,	the	bag	a	pebble	is	 discarded	to,	must	be	the	paired	white	bag	of	the	black	bag	that	the	last	pebble	draw was	from.	Specifically,	if	the	last	pebble	was	drawn	from	X,	the	next	discard	should	be to	A,	if	the	last	pebble	drawn	was	from	Y,	the	next	discard	should	be	to	B,	and	if	the	last	pebble	drawn	was	from	Z,	the	next	discard	should	be	to	C.
- On	 loading	 the	 bag	 files,	 the	 program	 should	 ensure	 that	 each	 black	 bag	 contains	 at least	11	times	as many	pebbles	as	players. For	example,	if	there	are	three	players	then	there	must	be	at	least	33	pebbles.
- If	 a	 player	 attempts	 to	 draw	 from	 an	 empty black bag,	 the	 player	 should	 attempt	 to	 select	another	bag	until	they	select	a	bag	with	pebbles.
- A	 player	 should	 never	 find	 an	 empty	 black	 bag	 if	 its	 paired	 white	 bag	 has	 pebbles	 within	it.
- Pebbles	must	have	a	strictly	positive	weight	– therefore	the	program	should	detect	and	 warn	the	user	if	they	are	trying	to	use	files	where	this	is	not	the	case. 

After	each	draw	and	discard	a	player	should	write	to	a	file	the	drawn/discarded	value	and	the associated	 bag,	 and	 then	 the	 current	 values	 of	 the	 pebbles	 the	 player	 holds.	 I.e.,	if	 player	 1 draws	 a	 pebble	 of	 weight	 17	 the	 following	 two	 lines	 should	 be	 written	 to 
```
player1_output.txt: 
  player1 has drawn a 17 from bag Y 
  player1 hand is 1, 2, 45, 6, 7, 8, 56, 23, 12, 17
```
and	if	subsequently	player	1	discards	a	pebble	of	weight	45	the	following	two	lines	should	be	written	to
```
player1_output.txt:
  player1 has discarded a 45 to bag B
  player1 hand is 1, 2, 6, 7, 8, 56, 23, 12, 17
```
There	are	no	restrictions	on	the	standard	Java	libraries	that	you	may	use.

## Simplified Requirements

Multiple players (multiple class?) (+ no. of players)

6 bags (ABC white; XYZ black)

<br />

t=0:

white bags empty; black bags full

Each player, select random black bag, 10 pebbles (integer weight, +)

<br />

t=terminate

Player’s pebbles’ weight = 100. -> announcement

Once player ensure hv 10 pebbles, stop.

Each player without weight 100, put 1 pebble into white.

Select black randomly, draw 1. 

Repeat, til another player win.

<br />

t=0:

Command request no. of players

request locations of three files (weight of black pebbles)

<br />

Exception to handle illegal no. of players, file format & values: Ask till valid/ E.

<br />

If black bag == empty -> pebbles from white put there: X<-A; Y<-B; Z<-C

Prob. drawing a pebble, shared evenly

Player: concurrent thread: threading commencing before initial draw

Player, a nested class, within PebbleGame

Drawing & discarding atomic action. Discard pebble to white, by pairs above

In loading, ensure each black hv >= 11 x no. of players

If player draws from empty black, select another bag bag till that hv pebbles

Player cannot select an empty black if its pair white has pebbles

<br />

Draw & discard value & bag, into a file file for each player
```
player1_output.txt:						
     player1 has drawn a 17 from bag Y			
     player1 hand is 1, 2, 45, 6, 7, 8, 56, 23, 12, 17
```
```
player1_output.txt:
     player1 has discarded a 45 to bag B
     player1 hand is 1, 2, 6, 7, 8, 56, 23, 12, 17
```
No restrictions on standard Java lib.

![ecm2414](https://user-images.githubusercontent.com/42607409/166974107-1ffb565d-0282-4ae6-864f-9c05fcc26dd2.png)

## Run

Make sure that the current directory is the pebblesTest directory. In order to run the testsuite, 
- if on a Mac run the command:
  `java -cp junit-4.12.jar:. TestRunner`
- if on Windows run the command:
  `java -cp junit-4.12.jar;. TestRunner`

The expected output of the test is that all the tests will be passed

