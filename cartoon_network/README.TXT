------------------------------------------------------------------------

PROJECT TITLE:  Cartoon Network
PURPOSE OF PROJECT:  This project allows open-ended exploration of basic neural networks; it also can provide a brain for a Finch robot.
VERSION or DATE:    v1.0 - First Release
HOW TO START THIS PROJECT:  Type 'a' to enter add neuron mode and then click screen to add neurons; type 'x' to enter stimulation mode and then click some of the neurons; then explore!
AUTHORS:  Bob Calin-Jageman
USER INSTRUCTIONS:

This goal of this simulation is to make it easier to appreciate how simple neural networks can do amazing things.
The project allows users to create networks of neurons with an interface that I hope is very intuitive.
The focus on on ease of use and on enabling interesting circuits to be formed--not on the detailed complexities of real neurons.
This simulation will work with Finch robots from BirdBrain Technologies (http://www.birdbraintechnologies.com/).
A big thank-you to Tom Lauwers at BirdBrain Technologies for some help with code connecting the Finch. 
The code for this project is not the prettiest (I was learning Greenfoot as I went along), but it is available for non-commercial use without restriction.

The simulation works by having different "modes" which you can switch between by typing a command letter or by clicking open the menu on the top-left corner.

Edit network:
a - add mode, clicking screen adds new neurons
d - delete mode, clicking a neuron removes it
g - grow mode, clicking a neuron makes it grow
s - shrink mode, clicking a neuron makes it shrink
m - move mode, clicking a neuron allows it to be dragged to a new position

Experiment:
x - stimulate mode, clicking a neuron stimulates it
n - inhibit mode, clicking a neuron inhibits it

Change neurons:
Edit a neuron's properties:
t - activity type mode - click a neuron to change type: silent at rest, spontaneously active, bursting (BURSTING IS NOT IMPLEMENTED YET)
l - activity level mode - click a neuron to set its activity level --1 can only fire at low frequencies, 10 can fire at high frequencies
w - transmitter type mode - click a neuron to set if it releases glutamate (excitatory) or gaba (inhibitory)
q - transmitter amount mode - click to set amount of nt release (1-40)

to do:
* Implement bursting
* Clean up code...
* Add changes to the mouse pointer for the different modes
* Make a better interface, one that allows translation to different languages and is easier to modify
* Add ability to save/load circuits
* Find other fun things to interface with
* Make it so the screen size and screen elements scale with different monitor resoultions rather than being fixed at a specific resolution
* Add STP/STD?
* Make some of the simulation parameters accessible (e.g. NT decay rate)