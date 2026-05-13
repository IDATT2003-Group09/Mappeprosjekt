<h1 align="center">MILLIONS - the stock game</h1>
<img width="2728" height="1752" alt="image" src="https://github.com/user-attachments/assets/e2aaa1e4-bd13-428e-8625-2ec7154bf9a2" />

Millions is s open source stock trading game. It was developed at NTNU in accordance with the subject code IDATT2003.

<h3 align="center">How to install and start playing</h3>

To start playing the game, simply clone the repository:
```
git clone
```
Also ensure you have java 25 and mvn 25 installed as specified in the pom.xml
After this you can launch it from the termial using cd command to enter the directory where you cloned the game.

When you are ready to launch the game simply do:
```
mvn javafx:run
```
And the application will launch. Have fun :) !

<h3 align="center">Testing</h3>
The game also comes with junit tests to ensure the main structure of the game if users want to edit the source code.
To run the tests simply run:

```
mvn test
```
