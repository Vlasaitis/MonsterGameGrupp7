package org.example;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Obstacle {

    public ArrayList<Position> obstacles;

    public Obstacle() {
        this.obstacles = new ArrayList<>();
    }

    public void createBorders() {
        for (int i = 0; i < 81; i++) {
            this.obstacles.add(new Position(i, 0));
        }
        for (int i = 0; i < 81; i++) {
            this.obstacles.add(new Position(i, 24));
        }
        for (int i = 0; i < 25; i++) {
            this.obstacles.add(new Position(0, i));
        }
        for (int i = 0; i < 25; i++) {
            this.obstacles.add(new Position(80, i));
        }
    }

    public void addObstacle() {
        spawnHorizontalObstacle('+');
        spawnHorizontalObstacle('-');
        spawnVerticleObstacle('+');
        spawnVerticleObstacle('-');
    }

    public void spawnHorizontalObstacle(char operator) {
        Random r = new Random();
        int x = r.nextInt(25, 55);
        int y = r.nextInt(3, 21);
        for (int i = 0; i < 20; i++) {
            if (operator == '-') {
                this.obstacles.add(new Position(x - i, y));
            } else if (operator == '+') {
                this.obstacles.add(new Position(x + i, y));
            }
        }
    }
        public void spawnVerticleObstacle (char operator){
            Random r = new Random();
            if (operator == '+') {
                int x = r.nextInt(20, 60);
                int y = r.nextInt(3, 12);
                for (int i = 0; i < 10; i++) {
                    this.obstacles.add(new Position(x, y + i));
                }
            } else if (operator == '-') {
                int x = r.nextInt(20, 60);
                int y = r.nextInt(12, 21);
                for (int i = 0; i < 10; i++) {
                    this.obstacles.add(new Position(x, y - i));
                }

            }
        }
}

//    private static List<Position> generateObstacles(Terminal terminal) {
//        List<Position> obstacles = new ArrayList<>();
//
//
//
//        Position[] obstacles = new Position[50];
//        for(int i = 0;i<50;i++){
//            obstacles[i] = new Position(10+i, 10);
//        }
//
//        // Use obstacles array to print to lanterna
//        for (Position p : obstacles) {
//            terminal.setCursorPosition(p.x, p.y);
//            terminal.putCharacter(block);
//        }
//        return
//    }

