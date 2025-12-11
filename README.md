# Game Overview
## This game is called Escape Hunt West. The player will start on the fourth level, and have to solve puzzles and enter codes to progress to lower levels, eventually escaping to the Outside World. This game is geared towards NCSSM students (specifically Hunt West residents) as the game has all kinds of Easter eggs only they would understand. The game is interesting because it features 8 unique minigames which the user must complete in order to a receive a special code needed for level advancement. 

# Feature List
## 1. 8 unique minigames for the player to complete
## 2. 4 custom designed hallways and 8 custom designed rooms for gameplay
## 3. Takes player arrow-key input to move a character on a 2D display
## 4. Player can interact with objects in the game (doors for going in and out of rooms, as well as "trigger" to initiate a minigame)
## 5. Easter eggs for the player to recognize on each hallway and each room
## 6. Simulates collisions between the player and walls, triggers, and doors to keep game within the map
## 7. Instead of games being played in the terminal, all games are played within the GUI itself for a more immersive experience for the player
## 8. Instead of having a checklist for what needs to be done, there is a door to the next hallway that requires a code given from the other games. The user must win the games to get the codes and enter it.

# Known Bugs / Limitations
## 1. The Hallway and Room tags are buggy, so they don't always show up on screen at the right time. This doesn't matter for the game experience itself as the game can be completed without knowing the names of the rooms and halls, but it's a feature that's nice to have for players familiar with Hunt West.
## 2. When the bot wins the TicTacToe game in Raghav's room, it doesn't automatically return the the window of the 2W Hallway. The user has to do it manually. The game can continue, but it's just an issue in terms of user experience.

# Step-by-Step User Guide:
## 1. Run the code using Visual Studio Code, and a new screen will pop up. The game will be played mainly in that screen.
## 2. The first hallway is 4th West, and you can move around using the arrow keys.
## 3. Go to either door and enter the room. In each room, there is an object called a trigger, which initiates the minigame. You don't know what the trigger is, so you have to move around and explore the room until it pops up.
## 4. The games are relatively simple to figure out so there are no instructions. Once the game pops up, complete the game. If you win, you will be given a code and be returned to the hallway. If you lose, you will just return to the hallway and repeat the previous steps again.
## 5. The games either take trackpad input or keyboard input, and it is up to the user to figure out which one (should be simple enough to figure out).
## 6. If you accidently enter a room, you can go back to the hallway through the door in the room.
## 7. Complete both games on the hallway, and get the 1 letter code from each game and combine them. Collide into the door that asks for the code, and enter that code in ALPHABETICAL ORDER. If the code is right, you will be taken to the next level. If the code is wrong, you will stay on the current level you are on.
## 8. Repeat this process until you are on the final level and have to choose between the right and left door. One door causes you to start over and the other door leads to the "Outside World". 
