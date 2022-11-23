package org.example;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.Random;

public class MonsterGame {
    public static void main(String[] args) throws Exception {
        // Goal: Generera en bana med flera mindre hinder. Slumpmässigt
        // OOP för monster
        // Refaktorera i metoder
        // Bug: Monster kan spawna PÅ obstacle
        // Bug: Monster kan deleta obstacle vertikalt
        /*
        Vytis gjort:
        - Skapat moveObject metod, tog bort monstermovement repeterande kod
        - Ändrade så att playerCharacter också är ett Position objekt. Variabel: player
        - Ändrade namnet på hjärtat (spelarens avatar) till playerCharacter
        - Alltså överallt där det var x och y är det nu player.x och player.y
        - Några förkortningar: monsterPosition : monPos
         */


        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Terminal terminal = terminalFactory.createTerminal();
        terminal.setCursorVisible(false);


        Position player = new Position(5,5);
        final char playerCharacter = '\u2661';
        final char block = '\u2588';
        final char monster = '\u2620';
        terminal.setCursorPosition(player.x, player.y);
        terminal.putCharacter(playerCharacter);

        // Create obstacles array
        Position[] obstacles = new Position[50];
        for(int i = 0;i<50;i++){
            obstacles[i] = new Position(10+i, 10);
        }

        // Use obstacles array to print to lanterna
        for (Position p : obstacles) {
            terminal.setCursorPosition(p.x, p.y);
            terminal.putCharacter(block);
        }

        Random r = new Random();
        Position monPos = new Position(r.nextInt(40,80), r.nextInt(24));
        terminal.setCursorPosition(monPos.x, monPos.y);
        terminal.putCharacter(monster);

        terminal.flush();

        boolean continueReadingInput = true;
        while (continueReadingInput) {

            KeyStroke keyStroke;
            do {
                Thread.sleep(5); // might throw InterruptedException
                keyStroke = terminal.pollInput();
            } while (keyStroke == null);

            Character c = keyStroke.getCharacter(); // used Character instead of char because it might be null

            if (c == Character.valueOf('q')) {
                continueReadingInput = false;
                terminal.close();
                System.out.println("quit");
            }

            int oldX = player.x; // save old position x
            int oldY = player.y; // save old position y
            switch (keyStroke.getKeyType()) {
                case ArrowDown -> player.y += 1;
                case ArrowUp -> player.y -= 1;
                case ArrowRight -> player.x += 1;
                case ArrowLeft -> player.x -= 1;
            }
            // detect if playerCharacter tries to run into obsticle

            boolean crashIntoObstacle = checkForObstacleCrash(obstacles, player.x, player.y);

            if (crashIntoObstacle) {
                player.x = oldX;
                player.y = oldY;
            }
            else {
                moveObject(oldX, oldY, player.x, player.y, playerCharacter, terminal);
            }

            // MONSTER MOVEMENT
            int monOldX = monPos.x;
            int monOldY = monPos.y;
            //testade metod för att ta bort lines under men funkade inte..
            //setMonsterCoordinates(monPos.x, monPos.y, player.x, player.y);
            if (monPos.x > player.x) {
                monPos.x--;
            } else if (monPos.x < player.x){
                monPos.x++;
            }
            if (monPos.y > player.y) {
                monPos.y--;
            } else if (monPos.y < player.y){
                monPos.y++;
            }

            boolean monsterCrash = checkForObstacleCrash(obstacles,monPos.x, monPos.y);

            // helps monster move around obstacle when hitting it
            if (monsterCrash) {
                if (monPos.x > player.x) {
                    monPos.x = monOldX-1;
                    monPos.y = monOldY;
                } else if (monPos.x < player.x) {
                    monPos.x = monOldX+1;
                    monPos.y = monOldY;
                } else {
                    monPos.x = monOldX;
                    monPos.y = monOldY;
                }
                moveObject(monOldX, monOldY, monPos.x, monPos.y, monster, terminal);

            } else {
                moveObject(monOldX, monOldY, monPos.x, monPos.y, monster, terminal);
            }

            // check if playerCharacter runs into the monster
            if (monPos.x == player.x && monPos.y == player.y) {
                terminal.close();
                continueReadingInput = false;
            }

            terminal.flush();
        }
    }
    public static void moveObject(int oldX, int oldY, int newX, int newY, char character, Terminal terminal) throws IOException {
        terminal.setCursorPosition(oldX, oldY);
        terminal.putCharacter(' ');
        terminal.setCursorPosition(newX, newY);
        terminal.putCharacter(character);
        terminal.flush();
    }
    public static boolean checkForObstacleCrash(Position[] obstacles, int currentX, int currentY) {
        for (Position p : obstacles) {
            if (p.x == currentX && p.y == currentY) {
                return true;
            }
        }
        return false;
    }
    public static void setMonsterCoordinates(int monX, int monY, int plyrX, int plyrY) {
        if (monX > plyrX) {
            monX--;
        } else if (monX < plyrX){
            monX++;
        }
        if (monY > plyrY) {
            monY--;
        } else if (monY < plyrY){
            monY++;
        }
    }

}
