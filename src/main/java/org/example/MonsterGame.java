package org.example;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.util.Random;

public class MonsterGame {
    public static void main(String[] args) throws Exception {
        // Goal: Generera en bana med flera mindre hinder. Slumpmässigt
        // OOP för monster
        // Refaktorera i metoder

        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Terminal terminal = terminalFactory.createTerminal();
        terminal.setCursorVisible(false);

        int x = 5;
        int y = 5;
        final char player = '\u2661';
        final char block = '\u2588';
        final char monster = '\u2620';
        terminal.setCursorPosition(x, y);
        terminal.putCharacter(player);

        // Create obsticles array
        Position[] obsticles = new Position[50];
        for(int i = 0;i<50;i++){
            obsticles[i] = new Position(10+i, 10);
        }

        // Use obsticles array to print to lanterna
        for (Position p : obsticles) {
            terminal.setCursorPosition(p.x, p.y);
            terminal.putCharacter(block);
        }

        Random r = new Random();
        Position monsterPosition = new Position(r.nextInt(40,80), r.nextInt(24));
        terminal.setCursorPosition(monsterPosition.x, monsterPosition.y);
        terminal.putCharacter(monster);

        terminal.flush();


        // Task 12
        boolean continueReadingInput = true;
        while (continueReadingInput) {

            KeyStroke keyStroke;
            do {
                Thread.sleep(5); // might throw InterruptedException
                keyStroke = terminal.pollInput();
            } while (keyStroke == null);


            KeyType type = keyStroke.getKeyType();
            Character c = keyStroke.getCharacter(); // used Character instead of char because it might be null

            System.out.println("keyStroke.getKeyType(): " + type
                    + " keyStroke.getCharacter(): " + c);

            if (c == Character.valueOf('q')) {
                continueReadingInput = false;
                terminal.close();
                System.out.println("quit");
            }

            int oldX = x; // save old position x
            int oldY = y; // save old position y
            switch (keyStroke.getKeyType()) {
                case ArrowDown -> y += 1;
                case ArrowUp -> y -= 1;
                case ArrowRight -> x += 1;
                case ArrowLeft -> x -= 1;
            }

            // detect if player tries to run into obsticle
            boolean crashIntoObsticle = false;
            for (Position p : obsticles) {
                if (p.x == x && p.y == y) {
                    crashIntoObsticle = true;
                    break;
                }
            }

            if (crashIntoObsticle) {
                x = oldX;
                y = oldY;
            }
            else {
                terminal.setCursorPosition(oldX, oldY); // move cursor to old position
                terminal.putCharacter(' '); // clean up by printing space on old position
                terminal.setCursorPosition(x, y);
                terminal.putCharacter(player);
            }

            // MONSTER MOVEMENT
            int monsterOldX = monsterPosition.x;
            int monsterOldY = monsterPosition.y;

            if (monsterPosition.x > x) {
                monsterPosition.x--;
            } else if (monsterPosition.x < x){
                monsterPosition.x++;
            }
            if (monsterPosition.y > y) {
                monsterPosition.y--;
            } else if (monsterPosition.y < y){
                monsterPosition.y++;
            }

            boolean monsterCrashIntoObsticle = false;
            for (Position p : obsticles) {
                if (p.x == monsterPosition.x && p.y == monsterPosition.y) {
                    monsterCrashIntoObsticle = true;
                    break;
                }
            }

            // helps monster move around obstacle when hitting it
            if (monsterCrashIntoObsticle) {
                if (monsterPosition.x > x) {
                    monsterPosition.x = monsterOldX-1;
                    monsterPosition.y = monsterOldY;
                } else if (monsterPosition.x < x) {
                    monsterPosition.x = monsterOldX+1;
                    monsterPosition.y = monsterOldY;
                } else {
                    monsterPosition.x = monsterOldX;
                    monsterPosition.y = monsterOldY;
                }
                //moveObject(int oldY, int oldX, int newX, int newY, char character, Terminal terminal);
                terminal.setCursorPosition(monsterOldX, monsterOldY);
                terminal.putCharacter(' ');
                terminal.setCursorPosition(monsterPosition.x, monsterPosition.y);
                terminal.putCharacter(monster);
                terminal.flush();
            } else {
                terminal.setCursorPosition(monsterOldX, monsterOldY);
                terminal.putCharacter(' ');
                terminal.setCursorPosition(monsterPosition.x, monsterPosition.y);
                terminal.putCharacter(monster);
                terminal.flush();
            }

            // check if player runs into the monster
            if (monsterPosition.x == x && monsterPosition.y == y) {
                terminal.close();
                continueReadingInput = false;
            }

            terminal.flush();
        }
    }
}
