package com.robokidz.controller.values;

public class Instructions {
    private String moveRight, moveLeft, moveFront, moveBack;
    private String start, stop;

    private String triangle, circle, cross, square;


    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }


    public String getTriangle() {
        return triangle;
    }

    public void setTriangle(String triangle) {
        this.triangle = triangle;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public String getCross() {
        return cross;
    }

    public void setCross(String cross) {
        this.cross = cross;
    }

    public String getSquare() {
        return square;
    }

    public void setSquare(String square) {
        this.square = square;
    }

    public Instructions() {
        super();
        moveBack = "B";
        moveFront = "F";
        moveLeft = "L";
        moveRight = "R";
        start = "ST";
        stop = "SP";
        triangle = "U";
        circle = "C";
        square = "S";
        cross = "D";
    }

    public String getMoveRight() {
        return moveRight;
    }

    public void setMoveRight(String moveRight) {
        this.moveRight = moveRight;
    }

    public String getMoveLeft() {
        return moveLeft;
    }

    public void setMoveLeft(String moveLeft) {
        this.moveLeft = moveLeft;
    }

    public String getMoveFront() {
        return moveFront;
    }

    public void setMoveFront(String moveFront) {
        this.moveFront = moveFront;
    }

    public String getMoveBack() {
        return moveBack;
    }

    public void setMoveBack(String moveBack) {
        this.moveBack = moveBack;
    }

    public void reset() {
        moveBack = "B";
        moveFront = "F";
        moveLeft = "L";
        moveRight = "R";
        start = "ST";
        stop = "SP";
        triangle = "U";
        circle = "C";
        square = "S";
        cross = "D";
    }
}