import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.IntStream;
import javax.swing.*;

//Author: Dustin D. Digby
//LDS Project 2 L-System
//11/29/2018

public class Project2GUI extends JFrame implements ActionListener {

    private JTextField lhs[], rhs[], angle, startSymbol;
    private JButton drawButton;
    private JSpinner iterationSpinner;
    private JLabel ruleLabels[], angleLabel, startLabel, spinnerLabel;
    private DrawingCanvas myCanvas;
    private Queue<String> extendedList;
    private Rule[] ruleSet;
    private String finalString = "";
    private float angleInc = 0;

    public Project2GUI() {

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(buildGUI());
        this.pack();
        this.setVisible(true);
    }

    //Creates the GUI and then sets the initial states
    private JPanel buildGUI() {
        JPanel ourGUI = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();

        ourGUI.setLayout(new GridBagLayout());

        buildAngle(ourGUI, gbc);
        buildStartSymbol(ourGUI, gbc);

        //Makes the draw button
        drawButton = new JButton("Draw");
        drawButton.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 10;
        gbc.anchor = GridBagConstraints.CENTER;
        ourGUI.add(drawButton, gbc);

        buildSpinner(ourGUI, gbc);

        lhs = new JTextField[5];
        rhs = new JTextField[5];
        ruleLabels = new JLabel[5];

        for (int i = 0; i < 5; i++) {
            buildRules(ourGUI, gbc, i);
        }
        JLabel title = new JLabel("L-System Project");
        title.setSize(20, 200);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 10;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        ourGUI.add(title, gbc);
        myCanvas = new DrawingCanvas();
        myCanvas.setDrawString(finalString);
        myCanvas.setAngleIncrement(angleInc);
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 10;
        gbc.gridheight = 10;
        ourGUI.add(myCanvas, gbc);
        setResizable(false);
        return ourGUI;
    }

    //Makes the Text field and Label for the start symbol
    private void buildStartSymbol(JPanel ourGUI, GridBagConstraints gbc) {
        startLabel = new JLabel("Start Symbol: ");
        startSymbol = new JTextField(2);
        gbc.gridx = 8;
        gbc.gridy = 4;
        ourGUI.add(startLabel, gbc);
        gbc.gridx = 9;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        ourGUI.add(startSymbol, gbc);
        gbc.fill = GridBagConstraints.NONE;
    }

    //Makes the Text field and Label for the Angle
    private void buildAngle(JPanel ourGUI, GridBagConstraints gbc) {
        angleLabel = new JLabel("Angle: ");
        angle = new JTextField(4);
        gbc.gridx = 8;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        ourGUI.add(angleLabel, gbc);
        gbc.gridx = 9;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        ourGUI.add(angle, gbc);
        gbc.fill = GridBagConstraints.NONE;
    }

    //Makes the spinner to get the number of iterations
    private void buildSpinner(JPanel ourGUI, GridBagConstraints gbc) {
        spinnerLabel = new JLabel("Number of Iterations: ");
        gbc.gridx = 8;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        ourGUI.add(spinnerLabel, gbc);
        iterationSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        gbc.gridx = 9;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        ourGUI.add(iterationSpinner, gbc);
        gbc.fill = GridBagConstraints.NONE;
    }

    //Makes the axiom and the line for the rules
    //There will be only 5 rules max only not all need to be used
    private void buildRules(JPanel ourGUI, GridBagConstraints gbc, int i) {
        ruleLabels[i] = new JLabel("Rule " + i + " : ");
        gbc.gridx = 1;
        gbc.gridy = i + 2;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        ourGUI.add(ruleLabels[i], gbc);
        gbc.gridx = 2;
        lhs[i] = new JTextField(2);
        ourGUI.add(lhs[i], gbc);
        gbc.gridwidth = 5;
        gbc.gridx = 3;
        rhs[i] = new JTextField(10);
        ourGUI.add(rhs[i], gbc);
    }

    public void actionPerformed(ActionEvent event) {

        //What happens when the button for draw is pressed
        if (event.getSource() == drawButton) {

            finalString ="";
            //Reads and saves the starting symbol as start
            String start = startSymbol.getText().toUpperCase();
            //Boolean that is used to see if the drawing can be made and has all correct parts filled in.
            boolean startTruth = true;
            //Gets the number of iterations from the spinner
            int iteration = (int) iterationSpinner.getValue();
            //The queue that will be used to extend the line from the rules.
            extendedList = new LinkedList<>();

            //Reads in all the rules and sets them to an object, with an axiom and a line
            //All the rules even if null are added to the ruleSet array of 5
            //Since there can be no more then 5 rules the array size doesn't need to change, otherwise a dynamic
            //array would then be made and used.
            ruleSet = new Rule[5];
            for (int i = 0; i < ruleSet.length; i++) {
                if (!lhs[i].getText().equals("") && !rhs[i].getText().equals("")) {
                    Rule rule = new Rule(lhs[i].getText().trim().toUpperCase(), rhs[i].getText().toUpperCase().trim());
                    ruleSet[i] = rule;
                } else {
                    Rule rule = new Rule();
                    ruleSet[i] = rule;
                }
            }

            //Error message for the start symbol being incorrect. If the start symbol is incorrect startTruth will then be false.
            if (start.length() > 1) {
                JOptionPane.showMessageDialog(this,
                        "Start Symbol must be a single character!  Multiple characters found.",
                        "Start Symbol Error",
                        JOptionPane.ERROR_MESSAGE);
                startTruth = false;
            } else if (start.length() == 0) {
                JOptionPane.showMessageDialog(this,
                        "You must have a start symbol!  No start symbol found.",
                        "Start Symbol Error",
                        JOptionPane.ERROR_MESSAGE);
                startTruth = false;
            }

            //Error message for the angle being incorrect. If the angle is incorrect startTruth will then be false.
            try {
                angleInc = Float.parseFloat(angle.getText());
                myCanvas.setAngleIncrement(angleInc);
            } catch (NumberFormatException e) {  // bad angle -- should notify user and try again...
                JOptionPane.showMessageDialog(this,
                        "Angle must be a valid floating point number.  Try Again.",
                        "Angle Error",
                        JOptionPane.ERROR_MESSAGE);
                startTruth = false;
            }


            //Will add the start symbol to the front of the queue if there is a correct value for it
            if (startTruth)
                extendedList.add(start);

            //If there is at least one rule the string will be iterated the number of times from the spinner
            //The start symbol can not be expanded if there are no rules. This will have only the start symbol be drawn on the canvas
            //due to it being the only thing on the queue
            if (!ruleSet[0].axiom.equals(""))
                for (int i = 0; i < iteration; i++)
                    iterate();

            //After the queue is iterated the correct number of times the queue will have the front polled off one by one
            //and then added to a string.
            while (!extendedList.isEmpty()) {
                finalString += extendedList.poll();
            }
            //It will draw the string if there is a start to the string. If not it will not print anything and will have the user try again.
            //"" will give in an empty string to be drawn which would draw nothing to the string.
            if (startTruth)
                myCanvas.setDrawString(finalString);
            else
                myCanvas.setDrawString("");
            //This resets the DrawingCanvas class redraws the next drawing
            myCanvas.repaint();

        }
    }

    //Method to expand the queue
    private void iterate() {
        //! is the symbol used to detect the end of the queue. The end of the queue means that it
        //has been expanded all the way. After the string is expanded all the way it will break the method
        extendedList.add("!");

        for (int i = 0; i < extendedList.size(); i++) {
            String current = extendedList.poll();//The first value of the queue
            if (!current.equals("!")) {//Will test to find the end of the queue
                if (ruleMatch(current)) {//Method to see if any of the rules matches the current head of the queue
                    for (Rule aRuleSet : ruleSet) {
                        if (aRuleSet.axiom.equals("")) {
                            break;
                        }
                        if (current.equals(aRuleSet.axiom)) {
                            //The rule's line will be split letter by letter then added to the queue one by one.
                            String[] split = aRuleSet.line.split("");
                            IntStream.range(0, split.length).forEach(h -> extendedList.add(split[h]));
                        }
                    }
                } else
                    extendedList.add(current);//If no rule is found for the current head it will be put back onto the tail of the queue.
            } else {
                break;
            }
        }
    }

    //Method to see if the current head of the queue matches a rule's axiom
    private boolean ruleMatch(String current) {
        for (int j = 0; j < ruleSet.length; j++) {
            if (ruleSet[j].axiom.equals("")) {
                break;
            }
            if (current.equals(ruleSet[j].axiom)) {
                return true;
            }
        }
        return false;
    }

    //Main method of the code
    public static void main(String[] args) {
        Project2GUI project2 = new Project2GUI();
    }
}