# StarWars Invaders

Final project of Advanced Programming

## Student Information

- Name and surname: Sogol Borjloo
- Student number: 40412009

## Project Description

StarWars Invaders is a two-dimensional Java Swing game inspired by Chicken Invaders. The game contains eight levels, four regular enemy types, two boss levels, power-ups, sound settings, user registration and login, high-score storage, and an optional plane store.
In this game you have to destroy all the empire's ships, collect power-ups, survive all 8 levels, defeat both bosses, and finish the galaxy mission.

## Requirements

Java Development Kit (JDK) 25
No external libraries are required
The program must be run from the project root directory so that the `assets` and `data` folders can be found.

## Running the Project in IntelliJ IDEA

1. Open the project folder in IntelliJ IDEA.
2. Set the Project SDK to JDK 25.
3. Open `src/chickenInvaders/GameMain.java`.
4. Run the `main` method in the `GameMain` class.

## Compiling and Running from Terminal

Run the following commands from the project root directory.

### Windows PowerShell / Command Prompt

```bash
javac -d out @sources.txt
java -cp out chickenInvaders.GameMain
```

### Linux / macOS

```bash
javac -d out @sources.txt
java -cp out chickenInvaders.GameMain
```

## Game Controls
| Key | Action |
|---|---|
| `A` or `Left Arrow` | Move left |
| `D` or `Right Arrow` | Move right |
| `W` or `Up Arrow` | Move up |
| `S` or `Down Arrow` | Move down |
| `Space` | Shoot |
| `P` | Pause or resume |
| `Esc` | End the current game and return to the main menu |
| `M` | Open or close sound settings during the game |
| `1` | Toggle background music |
| `2` | Toggle shot sound |
| `3` | Toggle explosion sound |
| `4` | Toggle game-over / win sound |

## Main Features

- User registration and login
- Eight game levels
- Normal, Fast, Zigzag, and Shooter enemies
- Boss battles in levels 4 and 8
- Add Fire, Rapid Fire, Extra Life, Shield, and Freeze Bomb power-ups (special ver)
- Background music, shot, explosion, and end-game sound settings
- High-score table
- Plane store

## Data Storage

The project uses advanced text files instead of SQLite or MySQL. The files are created automatically inside the `data` directory if they do not already exist.

### `data/users.txt`

Stores one user per line with the following fields:

```text
username|password|highScore|lastLevel|soundSettings|selectedPlane
```


The sound settings are stored as four comma-separated values in this order:

```text
backgroundMusic,shotSound,explosionSound,endSound
```

| Value | Meaning |
|---|---|
| `1` | Enabled |
| `0` | Disabled |


### `data/scores.txt`

Stores one record for every completed game:

```text
username|score|levelReached|dateAndTime|soundSettings
```

The **High Scores** page reads these records and displays the highest score for each user.

## Project Structure


```text
StarWarsInvaders/
│
├── src/chickenInvaders/       Java source code
│
├── assets/
│   ├── images/                Game images
│   └── sounds/                Music and sound effects
│
├── data/
│   ├── users.txt              User information
│   └── scores.txt             Game records and scores
│
├── sources.txt                Source file list for terminal compilation
└── README.md                  Project documentation
```

## GitHub Repository

https://github.com/sogolbr/StarWarsInvaders.git

## Notes

- Passwords are stored as plain text because password hashing was not required for this project!
- Keep the `assets` and `data` folders beside the source/output folders when running the game.


## Finally
Enjoy playing **StarWars Invaders** and good luck completing the galaxy mission!
