## Gomoku Game (C) - 2023/2024

<img src="app/src/main/res/drawable/gomoku.png" width="80px" style="display: block; margin-left: auto; margin-right: auto;" alt=""> 

---
### Creators

+ 47269 - **Douglas Incáo**
+ 49475 - **João Nunes** 
+ 46042 - **Maria do Rosário**
  
---
### Introduction  
  
+ This is the Gomoku Game <u>Option C</u>.  
It is a two-player game, and the players take turns to place a stone on the board. The first player to get five stones in a row (horizontally, vertically, or diagonally) wins the game.  
+ The game is played on a <u>15x15</u> or <u>19x19</u> board named **Traditional** or **Renju**, respectively.  
+ The players can only play against each other in wireless.
---
### The "How To Use" Part

> [!TIP]
> Open the console at `.\GOMOKU\app\libs`, execute `java -jar Gomoku.main.jar`. This will start the API to which the webpage and the app will send the requests to. Then initiate the App and either Login or Register yourself to start playing.  

> [!TIP]
> Open the console at `.\GOMOKU\app\webapp`, execute `npm install` (Be sure to have `npm` installed in your computer). This will install the webpack and modules necessary.
> Then, in the same command line execute `npm run start` to initiate the Web-App. (If this doesn't work try changing the directory to `.\GOMOKU\app\webapp\src` and execute the same command).  

> [!IMPORTANT]
> Once you press *`Play`* you can choose your gamemode(*`Traditional`* or *`Renju`*). If there's no one on the auto-queue the app will return you to the home screen but also add you to the *`Wait List`*. You can try again until someone also presses *`Play`*. If not, you'll stay in the menu and in the wait list forever. Totally intended feature by the way.


