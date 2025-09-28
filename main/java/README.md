# Bomberman Multiplayer Game

A Java-based multiplayer Bomberman game with network support, featuring enemies with different AI behaviors, power-ups, and multiple levels.

## Features

- **Multiplayer Support**: Host and join games over network
- **Enemy AI**: 4 different enemy types with varying difficulty and behaviors
- **Power-ups**: Various boosters to enhance gameplay
    - Bomb capacity increase/decrease
    - Speed modifications
    - Explosion radius changes
    - Ghost mode (walk through walls)
    - Score modifiers
    - Remote bomb control
- **Level System**: Progressive difficulty with automatic level advancement
- **Real-time Graphics**: Smooth rendering with double buffering

## Project Structure

Bomberman/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/bomberman/
│       │       ├── Main.java              # Application entry point
│       │       ├── constants/              # Game constants
│       │       ├── models/                 # Game entities
│       │       │   ├── enemies/           # Enemy types
│       │       │   ├── boosters/          # Power-ups
│       │       │   └── terrain/           # Map elements
│       │       ├── game/                   # Game logic
│       │       ├── ui/                     # User interface
│       │       ├── network/                # Multiplayer networking
│       │       └── utils/                  # Utility classes
│       └── resources/
│           └── images/                     # Game sprites
└── out/                                    # Compiled classes

## Prerequisites

- Java 8 or higher
- IntelliJ IDEA (recommended) or any Java IDE

## Installation

1. Clone the repository:
```bash
git clone https://github.com/andisheghs/Bomberman.git
cd Bomberman