import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParachuteWindow extends Frame {

    private class ParachuteCanvas extends Canvas {
        final static int BIT_NUMBER = 5;

        public ParachuteCanvas() {
            this.setBounds(10, 176, 580, 580);
            this.setBackground(Color.DARK_GRAY);
            this.setVisible(true);
        }

        @Override
        public void paint(Graphics g) {
            // count how many words we have
            int wordCount = 1;
            for (Char c : listOfCharacters) {
                if (c.getCharacter() != ' ')
                    continue;

                wordCount++;
            }

            int startAngle = 0;
            int leftUpperX = 10;
            int leftUpperY = 10;
            int width = 560;
            int height = 560;
            int replacement = height / (wordCount * 2);

            for (Char c : listOfCharacters) {
                // if char is space -> create next layer of sectors
                if (c.getCharacter() == ' ') {

                    while (startAngle % 360 != 0) {
                        g.setColor(Color.blue);
                        g.fillArc(leftUpperX, leftUpperY, width, height, startAngle, -10);
                        startAngle -= 10;
                    }
                    startAngle = 0;
                    g.setColor(Color.black);

                    // go to next layer
                    g.drawOval(leftUpperX, leftUpperY, width, height);
                    leftUpperX += replacement;
                    leftUpperY += replacement;
                    width -= replacement * 2;
                    height -= replacement * 2;
                    g.drawOval(leftUpperX, leftUpperY, width, height);
                    continue;
                }

                // fill sectors wth color corresponding to bit
                for (int j = 0; j < BIT_NUMBER; j++) {
                    if (c.getBinaryRepresentation().charAt(j) == '0') {
                        // draw white arc
                        g.setColor(Color.white);
                    } else {
                        // draw red arc
                        g.setColor(Color.red);
                    }
                    g.fillArc(leftUpperX, leftUpperY, width, height, startAngle, -10);
                    startAngle -= 10;
                }
                g.setColor(Color.black);
                g.fillArc(leftUpperX, leftUpperY, width, height, startAngle, -10);
                startAngle -= 10;

                // if word contains less than 6 chars -> fill other sectors with blue color
                if ((listOfCharacters.get(listOfCharacters.size() - 1) == c)) {
                    while (startAngle % 360 != 0) {
                        g.setColor(Color.blue);
                        g.fillArc(leftUpperX, leftUpperY, width, height, startAngle, -10);
                        startAngle -= 10;
                    }
                }
            }

            // preparation to draw final border and lines for sectors
            width = 560;
            height = 560;
            leftUpperX = 20;
            leftUpperY = 20;
            startAngle = 0;
            int lineStartX = leftUpperX + (height - leftUpperX) / 2;
            int lineStartY = leftUpperY + (width - leftUpperY) / 2;
            int radius = (width - leftUpperY) / 2;
            double scale = 1.04; // scale because we lost some in double-to-int casting
            int arcX = lineStartX + (int) (Math.sin(Math.toRadians(Math.abs(startAngle))) * radius * scale);
            int arcY = lineStartY + (int) (Math.cos(Math.toRadians(Math.abs(startAngle))) * radius * scale);

            // draw outer border
            g.setColor(Color.black);
            g.drawOval(leftUpperX - 10, leftUpperY - 10, width, height);

            // draw lines for sectors
            do {
                g.drawLine(lineStartX, lineStartY, arcX, arcY);
                startAngle += 10;
                arcX = lineStartX + (int) (Math.sin(Math.toRadians(Math.abs(startAngle))) * radius * scale);
                arcY = lineStartY + (int) (Math.cos(Math.toRadians(Math.abs(startAngle))) * radius * scale);
            } while (startAngle % 360 != 0);
        }
    }

    private ArrayList<Char> listOfCharacters;
    boolean illegalNotFound = true;

    private final TextField binaryRepresentation;
    private final String text;

    public ParachuteWindow(String[] newText) {
        this.setSize(600, 766);
        this.setTitle("String to binary parachute!");
        this.setLayout(null);

        String buffText = "";
        for (String s : newText) {
            buffText += s;
            if (s != newText[newText.length - 1]) {
                buffText += " ";
            }
        }
        buffText = buffText.toLowerCase();
        this.text = buffText;

        // Create and add text panel
        Panel textPanel = new Panel();
        textPanel.setBounds(10, 30, 580, 70);
        textPanel.setBackground(Color.GRAY);

        // Add label
        Label textLabel = new Label();
        textLabel.setText("Text to represent to binary (ONLY LATIN LETTERS AND WHITESPACES):");
        textLabel.setVisible(true);

        textPanel.add(textLabel);

        // Add text field
        TextField textField = new TextField(this.text);
        textField.setEditable(false);
        textField.setVisible(true);

        textPanel.add(textField);

        textPanel.setLayout(new GridLayout(2, 1));

        textPanel.setVisible(true);

        this.add(textPanel);

        // Create and add binary representation panel
        Panel binaryPanel = new Panel();
        binaryPanel.setBounds(10, 103, 580, 70);
        binaryPanel.setBackground(Color.GRAY);
        binaryPanel.setVisible(true);

        Label explanatoryLabel = new Label("Binary representation of text:");
        explanatoryLabel.setVisible(true);

        TextField binaryField = new TextField();
        binaryField.setVisible(true);
        binaryField.setSize(300, 30);

        this.binaryRepresentation = binaryField;
        this.binaryRepresentation.setEditable(false);

        if (newText.length > 5) {
            this.binaryRepresentation.setText("Too many words!");
            this.illegalNotFound = false;
        }

        if (illegalNotFound) {
            for (int i = 0; i < newText.length; i++) {
                if (newText[i].length() > 6) {
                    this.binaryRepresentation.setText("Too many letters in word: " + newText[i] + "!");
                    illegalNotFound = false;
                }
            }
        }

        binaryPanel.add(explanatoryLabel);
        binaryPanel.add(binaryField);

        binaryPanel.setLayout(new GridLayout(2, 1));

        this.add(binaryPanel);

        this.setVisible(true);
    }

    public void drawParachute() {
        if (illegalNotFound) {
            for (int i = 0; i < text.length(); i++) {
                if ((text.charAt(i) > 122 || text.charAt(i) < 97) && text.charAt(i) != 32) {
                    this.binaryRepresentation.setText("Text contains illegal characters!!!!");
                    illegalNotFound = false;
                    break;
                }
            }
        }

        if (illegalNotFound/* text.matches("[\\p{L}&&\\w]+") */) {
            this.listOfCharacters = stringToCharList(text);
            this.binaryRepresentation.setText(binaryRepresentationAsString());
            ArrayList<String> words = new ArrayList<>(List.of(text.split(" ")));
            Collections.reverse(words);
            String buffStr = "";
            for (String s : words) {
                buffStr += s;
                if (s != words.get(words.size() - 1)) {
                    buffStr += " ";
                }
            }
            this.listOfCharacters = stringToCharList(buffStr);
            drawParachuteOnCanvas();
        }
    }

    private ArrayList<Char> stringToCharList(String text) {
        ArrayList<Char> list = new ArrayList<>();

        for (int i = 0; i < text.length(); i++) {
            list.add(new Char(text.charAt(i)));
        }

        return list;
    }

    private String binaryRepresentationAsString() {
        StringBuilder result = new StringBuilder();

        for (Char c : this.listOfCharacters) {
            if (c.getCharacter() != ' ')
                result.append(c.getBinaryRepresentation()).append(" ");
        }

        return result.toString();
    }

    private void drawParachuteOnCanvas() {
        // Create and add canvas to draw parachute
        ParachuteCanvas parachuteCanvas = new ParachuteCanvas();

        this.add(parachuteCanvas);
    }
}