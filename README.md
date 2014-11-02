Can't Take It Anymore
======================

2d top-down shooter w/ rpg elements.
Prepare for a boss fight when your time runs out, if you aren't prepared, you can reset but with increased difficulty.

For: UMD Game Dev Club's Game Jam, Theme: Countdown

Developers:
Shiliang Zhang
Zhehao Chen

=======================

###For Developers
* Java 7 
* Modify classes in the **ctia.game** package, not the **ctia.engine** package

###Engine Design
####Game is frame-based
* Game logic steps with each game frame **dt()** and redraws screen with each game frame **draw()**

####Game uses a container hierarchy for modularity
* **Entity** is an individual game object, like a player, an enemy, a projectile, or an obstacle
* **Level** contains multiple entities and handles interaction between them such as collision detection
* **Scene** is a game screen that contains a level and forwards user input to player, handles level transitions, and draws the HUD
* **Runner** is a Swing window that runs the scene/game screens and transitions between screens
* **dt()** and **draw()** are called on the **Runner**, passed to the contained **Scene** which draws the HUD, passed to the contained **Level** which draws the background, passed to the contained **Entities** which have their logic
* So, each individual **Entity** acts as its own module

##Development Plan

| # | Task | Done on |
|---|------|------------:|
| 00|Start project|10/29|
|   |**GENERIC TOP-DOWN GAME PROTOTYPE**||
| A1|Visible, player-controllable entity|11/1|
| A2|Scrolling/panning level||
| A3|Projectile firing||
| A4|Destroyable enemies||
| A5|Enemies that attack||
|   |**RPG ELEMENTS**||
| B1|Brainstorm RPG elements to add||
| B2|Add RPG elements||
|   |**SPECIFIC GAME ELEMENTS**||
| C1|Implement boss - ai and other mechanics||
| C2|Add reset mechanic||
|   |**POLISH**||
| D1|Add walls and obstacles||
| D2|Tweak mechanics and numbers||
| D3|Improve user interface||
| D4|Improve sprites and animations||
