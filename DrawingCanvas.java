import java.awt.Canvas;
import java.awt.*;
import java.awt.geom.*;
import java.util.Stack;

public class DrawingCanvas extends Canvas {
    private String drawString;
    private double angleIncrement;

    //The set dimensions of the bottom part of the GUI made
    DrawingCanvas() {
        this.setPreferredSize(new Dimension(400, 400));
    }

    public void setDrawString(String s) {
        drawString = s;
    }

    public void setAngleIncrement(double d) {
        angleIncrement = Math.PI * d/ 180.0;
    }

    //Method to draw the lines
    public void paint (Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        int currentPositionX, currentPositionY, position;
        double currentAngle;
        //Stacks used to save the current states and angles when [ or ] is used
        Stack<Double> savedAngle = new Stack<>();
        Stack<Integer> savedX = new Stack<>();
        Stack<Integer> savedY = new Stack<>();

        //Initial values of the current positions and angle
        //200, 200 is the center of the DrawingCanvas
        currentPositionX = 200;
        currentPositionY = 200;
        currentAngle = 0.0;

        //Will draw the string letter by letter from the drawString
        //If the line is not one of the characters in the if statements below,
        //the nothing will happen and will keep it's current state
        for (position = 0; position < drawString.length(); position++) {
            if (drawString.charAt(position) == 'F') { // Draw 5 units along current direction, F is always draw the line
                //Sets the line positions and angle
                Line2D line = new Line2D.Double(currentPositionX, currentPositionY,
                        currentPositionX - 5.0 * Math.sin(currentAngle),
                        currentPositionY - 5.0 * Math.cos(currentAngle));
                g2.draw(line);

                //Sets the next position for the line, and will have the line wrap around the ends of the dimensions
                //if it exceeds it. This is used so the line does not go off the page forever
                //This is for the x position, same is done for the y
                if(currentPositionX>=0 && currentPositionX<400)
                    currentPositionX = (int) (currentPositionX - 5.0 * Math.sin(currentAngle));
                else if(currentPositionX < 0)
                    currentPositionX = (int) (currentPositionX - 5.0 * Math.sin(currentAngle))+400;
                else
                    currentPositionX = (int) (currentPositionX - 5.0 * Math.sin(currentAngle))-400;
                //Next position for the y
                if(currentPositionY>=0 && currentPositionY<400)
                    currentPositionY = (int) (currentPositionY - 5.0 * Math.cos(currentAngle));
                else if(currentPositionY<0)
                    currentPositionY = (int) (currentPositionY - 5.0 * Math.cos(currentAngle))+400;
                else
                    currentPositionY = (int) (currentPositionY - 5.0 * Math.cos(currentAngle))-400;
            } else if (drawString.charAt(position) == '+') {//Flips the line to the angle entered in
                currentAngle += angleIncrement;
            } else if (drawString.charAt(position) == '-') {//Flips the line to the the other way entered in
                currentAngle -= angleIncrement;
            } else if (drawString.charAt(position) == '[') {//Saves the state of the line and pushes it on the stack
                savedAngle.push(currentAngle);
                savedX.push(currentPositionX);
                savedY.push(currentPositionY);
            } else if (drawString.charAt(position) == ']') {//Pops the state that was saved in the stack, and that is now the current state of the line
                currentAngle = savedAngle.pop();
                currentPositionX = savedX.pop();
                currentPositionY = savedY.pop();
            }
        }
    }
}
