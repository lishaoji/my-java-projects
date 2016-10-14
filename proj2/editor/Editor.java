package editor;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.control.ScrollBar;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;



public class Editor extends Application {
    private Rectangle cursor;
    private ScrollBar scrollBar;
    private static linkedTextList allText;
    private Group root;
    private Group textRoot;
    private static final int margin = 5;
    private static int WINDOW_WIDTH;
    private static int WINDOW_HEIGHT;
    private static String[] arguments = new String[2];

    public Editor() {
        // Create a rectangle to surround the text that gets displayed.  Initialize it with a size
        // of 0, since there isn't any text yet.
        cursor = new Rectangle(margin, 0, 0, 0);
        root = new Group();
        textRoot = new Group();
        scrollBar = new ScrollBar();
        root.getChildren().add(textRoot);
    }
    /* create a data structure for all text */
    private class linkedTextList {
        private textList sentinel;
        private textList currentText;
        private int size;
        private String fontName;
        private int fontSize;

        private class textList {
            textList prev;
            Text current;
            textList next;

            public textList(Text t) {
                current = t;
                prev = null;
                next = null;
            }
        }

        public linkedTextList(String fname, int fsize) {
            Text initText = new Text(margin, 0, "");
            this.fontName = fname;
            this.fontSize = fsize;
            initText.setFont(Font.font(fontName, fontSize));
            sentinel = new textList(initText);
            size = 0;
            sentinel.prev = sentinel;
            sentinel.next = sentinel;
            currentText = sentinel;
        }

        private void addAfterCurrentText(String s) {
            size += 1;
            Text tt = new Text(s);
            tt.setFont(Font.font(fontName, fontSize));
            tt.setTextOrigin(VPos.TOP);
            textList toAdd = new textList(tt);

            toAdd.prev = currentText;
            toAdd.next = currentText.next;
            currentText.next.prev = toAdd;
            currentText.next = toAdd;
            currentText = toAdd;

            // wrap the text
//            wrapText();
        }

        private Text removeCurrentText() {
            if (!currentText.current.getText().equals("")) {
                size -= 1;

                //remove the currentText
                textList temp = currentText;
                currentText = currentText.prev;
                currentText.next = currentText.next.next;
                currentText.next.prev = currentText;

                //wrap the text
//                wrapText();
                return temp.current;
            }
            return null;
        }

        private void changeFont(int fsize) {
            textList t = sentinel;
            fontSize = fsize;
            int index = size;
            while (index > 0) {
                t.next.current.setFont(Font.font(fontSize));
                t = t.next;
                index -= 1;
            }
        }

        private void wrapText() {
            // Idea is to look at whitespace, calculate word/line length and reset the location of the texts
            int wordLength = 0;
            int lineLength = margin;
            // Create 2 variables to track the location of the text
            int xxPos = margin;
            int yyPos = 0;
            textList prevSpace = sentinel.next;
            textList nextSpace = sentinel.next;
            //Every Iteration is a word
            while (!prevSpace.equals(sentinel)) {
                if (prevSpace.current.getText().equals(" ")) {
                    prevSpace = prevSpace.next;
                    nextSpace.current.setX(xxPos);
                    nextSpace.current.setY(yyPos);
                    xxPos += (int) Math.round(nextSpace.current.getLayoutBounds().getWidth());
                    lineLength += (int) Math.round(nextSpace.current.getLayoutBounds().getWidth());
                    nextSpace = nextSpace.next;
                } else if (prevSpace.current.getText().equals("\r")) {
                    prevSpace = prevSpace.next;
                    nextSpace.current.setX(xxPos);
                    nextSpace.current.setY(yyPos);
                    xxPos = margin;
                    yyPos += (int) Math.round(nextSpace.current.getLayoutBounds().getHeight() / 2);
                    lineLength = margin;
                    nextSpace = nextSpace.next;
                } else {
                    wordLength = 0;
                    while (!(nextSpace.current.getText().equals(" ")
                            && !nextSpace.next.current.getText().equals(" "))
                            && !nextSpace.current.getText().equals("\r")
                            && !nextSpace.current.getText().equals("")) {
                        wordLength += (int) Math.round(nextSpace.current.getLayoutBounds().getWidth());
                        nextSpace = nextSpace.next;
                    }

                    if ((lineLength + wordLength > WINDOW_WIDTH - margin)) {
                        xxPos = margin;
                        yyPos += (int) Math.round(prevSpace.current.getLayoutBounds().getHeight());
                        while (!prevSpace.equals(nextSpace)) {
                            prevSpace.current.setX(xxPos);
                            prevSpace.current.setY(yyPos);
                            xxPos += (int) Math.round(prevSpace.current.getLayoutBounds().getWidth());
                            if (xxPos >= WINDOW_WIDTH - margin) {
                                xxPos = margin;
                                yyPos += (int) Math.round(prevSpace.current.getLayoutBounds().getHeight());
                            }
                            prevSpace = prevSpace.next;
                        }
                        lineLength = xxPos;
                        wordLength = 0;
                    } else {
                        lineLength += wordLength;
                        while (!prevSpace.equals(nextSpace)) {
                            prevSpace.current.setX(xxPos);
                            prevSpace.current.setY(yyPos);
                            xxPos += (int) Math.round(prevSpace.current.getLayoutBounds().getWidth());
                            if (xxPos >= WINDOW_WIDTH - margin
                                    && !prevSpace.next.current.getText().equals("\r")
                                    && !prevSpace.next.current.getText().equals(" ")) {
                                xxPos = margin;
                                yyPos += (int) Math.round(prevSpace.current.getLayoutBounds().getHeight());
                            }
                            prevSpace = prevSpace.next;
                        }
                    }
                }
            }
        }

        private String textToString() {
            String toReturn = "";
            textList head = sentinel.next;
            while (!head.current.getText().equals("")) {
                toReturn = toReturn + head.current.getText();
                head = head.next;
            }
            return toReturn;
        }
    }

	/** An EventHandler to handle keys that get pressed. */
    private class KeyEventHandler implements EventHandler<KeyEvent> {

        private int fontSize = 12;
        private String fontName = "Verdana";
        private int xPos = margin;
        private int yPos = 0;
        private Stack<Text> undo;
        private Stack<Text> redo;
        private int operation_count;

        public KeyEventHandler(int windowWidth, int windowHeight) {
            allText = new linkedTextList(fontName, fontSize);
            cursor.setHeight((int) Math.round(allText.currentText.current.getLayoutBounds().getHeight()));
            cursor.setWidth(1);
            WINDOW_HEIGHT = windowHeight;
            WINDOW_WIDTH = windowWidth;
        }

        @Override
        public void handle(KeyEvent keyEvent) {
            if (keyEvent.getEventType() == KeyEvent.KEY_TYPED) {
                // Use the KEY_TYPED event rather than KEY_PRESSED for letter keys, because with
                // the KEY_TYPED event, javafx handles the "Shift" key and associated
                // capitalization.
                String characterTyped = keyEvent.getCharacter();
                if (characterTyped.length() > 0 && characterTyped.charAt(0) != 8 && !keyEvent.isShortcutDown()) {
                    // Ignore control keys, which have non-zero length, as well as the backspace key, which is
                    // represented as a character of value = 8 on Windows.

                    allText.addAfterCurrentText(characterTyped);
                    allText.wrapText();
                    Text charTyped = allText.currentText.current;
                    textRoot.getChildren().add(charTyped);

                    // Calculate xPos and yPos for cursor
                    if (allText.currentText.current.getText().equals("\r")) {
                        xPos = margin;
                        yPos = (int) Math.round(allText.currentText.current.getY())
                                + (int) Math.round(allText.currentText.current.getLayoutBounds().getHeight() / 2);

                    } else {
                        xPos = (int) Math.round(allText.currentText.current.getX())
                                + (int) Math.round(allText.currentText.current.getLayoutBounds().getWidth());
                        yPos = (int) Math.round(allText.currentText.current.getY());
                    }

                    updateCursor();
                    keyEvent.consume();
                }

            } else if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
                // Arrow keys should be processed using the KEY_PRESSED event, because KEY_PRESSED
                // events have a code that we can check (KEY_TYPED events don't have an associated
                // KeyCode).
                KeyCode code = keyEvent.getCode();
                if (keyEvent.isShortcutDown()) {
                    if (code == KeyCode.P) {
                        System.out.println((int)cursor.getX() + ", " + (int)cursor.getY());
                    }
                    if (code == KeyCode.PLUS || code == KeyCode.EQUALS) {
                        fontSize += 4;
                        allText.changeFont(fontSize);
                        allText.wrapText();
                        xPos = (int) Math.round(allText.currentText.current.getX())
                                + (int) Math.round(allText.currentText.current.getLayoutBounds().getWidth());
                        yPos = (int) Math.round(allText.currentText.current.getY());
                        updateCursor();

                    } else if (code == KeyCode.MINUS) {
                        fontSize = Math.max(0, fontSize - 4);
                        allText.changeFont(fontSize);
                        allText.wrapText();
                        xPos = (int) Math.round(allText.currentText.current.getX())
                                + (int) Math.round(allText.currentText.current.getLayoutBounds().getWidth());
                        yPos = (int) Math.round(allText.currentText.current.getY());
                        updateCursor();

                    } else if (code == KeyCode.S) {
                        //"save file"
                        if (!arguments[1].equals("")) {
                            String outputFileName = arguments[1];
                            try {
                                FileWriter writer = new FileWriter(outputFileName);
                                String textString = allText.textToString();
                                int textIndex = 0;
                                while (textIndex < allText.size) {
                                    char charWrite = textString.charAt(textIndex);
                                    writer.write(charWrite);
                                    textIndex += 1;
                                }
                                writer.close();
                            } catch (IOException ioException) {
                                System.out.println("Error when copying; exception was: " + ioException);
                            }
                        }
                    } else if (code == KeyCode.Z) {
                        // "Undo"
                    } else if (code == KeyCode.Y) {
                        // "Redo"
                    }
                } else if (code == KeyCode.BACK_SPACE) {
                    Text temp = allText.removeCurrentText();
                    allText.wrapText();
                    // update the position of cursor
                    xPos = (int) Math.round(allText.currentText.current.getX())
                            + (int) Math.round(allText.currentText.current.getLayoutBounds().getWidth());
                    yPos = (int) Math.round(allText.currentText.current.getY());
                    textRoot.getChildren().remove(temp);
                    updateCursor();

                } else if (code == KeyCode.UP) {
                    if (!allText.currentText.current.getText().equals("") && yPos > margin) {
                        yPos -= (int) Math.round(cursor.getHeight());
                        while ((allText.currentText.current.getX() > xPos
                                || allText.currentText.current.getY() > yPos)
                                && !allText.currentText.current.getText().equals("")
                                && allText.currentText.next.current.getY() >= yPos) {
                            allText.currentText = allText.currentText.prev;
                        }
                        if (Math.abs(allText.currentText.current.getX() - xPos)
                                > Math.abs(allText.currentText.next.current.getX() - xPos)) {
                            xPos = (int) Math.round(allText.currentText.next.current.getX());
                        } else {
                            xPos = (int) Math.round(allText.currentText.current.getX());
                            if (!allText.currentText.prev.current.getText().equals("\n")) {
                                allText.currentText = allText.currentText.prev;
                            } else {
                                allText.currentText = allText.currentText.prev.prev;
                            }
                        }
                        updateCursor();
                    }
                } else if (code == KeyCode.DOWN) {
                    if (!allText.currentText.next.current.getText().equals("")
                            && yPos < (int) Math.round(allText.sentinel.prev.current.getY())) {
                        yPos += (int) Math.round(cursor.getHeight());
                        while ((allText.currentText.current.getX() < xPos
                                || allText.currentText.current.getY() < yPos)
                                && !allText.currentText.next.current.getText().equals("")
                                && allText.currentText.next.current.getY() <= yPos) {
                            allText.currentText = allText.currentText.next;
                        }
                        if (Math.abs(allText.currentText.current.getX() - xPos)
                                > Math.abs(allText.currentText.prev.current.getX() - xPos)) {
                            xPos = (int) Math.round(allText.currentText.prev.current.getX());
                            allText.currentText = allText.currentText.prev.prev;
                        } else {
                            xPos = (int) Math.round(allText.currentText.current.getX());
                            if (!allText.currentText.prev.current.getText().equals("\n")) {
                                allText.currentText = allText.currentText.prev;
                            } else {
                                allText.currentText = allText.currentText.prev.prev;
                            }
                        }
                        updateCursor();
                    }

                } else if (code == KeyCode.LEFT) {
                    // Update database
                    if (!allText.currentText.current.getText().equals("")) {
                        // update cursor
                        xPos = (int) Math.round(allText.currentText.current.getX());
                        yPos = (int) Math.round(allText.currentText.current.getY());
                        // update the current nodes
                        allText.currentText = allText.currentText.prev;
                        if (allText.currentText.current.getText().equals("\n")) {
                            allText.currentText = allText.currentText.prev;
                        }
                        updateCursor();
                    }
                } else if (code == KeyCode.RIGHT) {
                    //update database
                    if (!allText.currentText.next.current.getText().equals("")) {
                        allText.currentText = allText.currentText.next;
                        if (allText.currentText.current.getText().equals("\n")) {
                            allText.currentText = allText.currentText.next;
                        }
                        //update cursor
                        if (allText.currentText.current.getText().equals("\r")) {
                            xPos = margin;
                            yPos += (int) Math.round(allText.currentText.current.getLayoutBounds().getHeight() / 2);
                        } else {
                            xPos = (int) Math.round(allText.currentText.current.getX()) +
                                    (int) Math.round(allText.currentText.current.getLayoutBounds().getWidth());
                            yPos = (int) Math.round(allText.currentText.current.getY());
                        }
                        updateCursor();
                    }
                }
            }
            // Snapping back to the cursor position
            if (cursor.getY() + cursor.getLayoutY() < 0) {
                cursor.setLayoutY(-cursor.getY());
                textRoot.setLayoutY(-cursor.getY());
                scrollBar.setValue(cursor.getY());
            } else if (cursor.getY() + cursor.getLayoutY() + cursor.getHeight() > WINDOW_HEIGHT) {
                cursor.setLayoutY(WINDOW_HEIGHT - cursor.getY() - cursor.getHeight());
                textRoot.setLayoutY(WINDOW_HEIGHT - cursor.getY()- cursor.getHeight());
                scrollBar.setValue(cursor.getY() + cursor.getHeight() - WINDOW_HEIGHT);
            }
        }


        private void updateCursor() {
            // Figure out the size of the current text.
            int textHeight;
            int textWidth = 1;
            if (allText.currentText.current.getText().equals("\r")) {
                textHeight = (int) Math.round(allText.currentText.current.getLayoutBounds().getHeight() / 2);
            } else {
                textHeight = (int) Math.round(allText.currentText.current.getLayoutBounds().getHeight());
            }


            // Re-size and re-position the cursor.
            cursor.setHeight(textHeight);
            cursor.setWidth(textWidth);

            // For rectangles, the position is the upper left hand corner.
            cursor.setX(xPos);
            cursor.setY(yPos);
            // Many of the JavaFX classes have implemented the toString() function, so that
            // they print nicely by default.
        }
    }

    /** An EventHandler to handle changing the color of the rectangle. */
    private class RectangleBlinkEventHandler implements EventHandler<ActionEvent> {
        private int currentColorIndex = 0;
        private Color[] boxColors =
                {Color.WHITE, Color.BLACK};

        public RectangleBlinkEventHandler() {
            // Set the color to be the first color in the list.
            changeColor();
        }

        private void changeColor() {
            cursor.setFill(boxColors[currentColorIndex]);
            currentColorIndex = (currentColorIndex + 1) % boxColors.length;
        }

        @Override
        public void handle(ActionEvent event) {
            changeColor();
        }
    }

    /** Makes the text bounding box change color periodically. */
    public void makeRectangleColorChange() {
        // Create a Timeline that will call the "handle" function of RectangleBlinkEventHandler
        // every 1 second.
        final Timeline timeline = new Timeline();
        // The rectangle should continue blinking forever.
        timeline.setCycleCount(Timeline.INDEFINITE);
        RectangleBlinkEventHandler cursorChange = new RectangleBlinkEventHandler();
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), cursorChange);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    /** An event handler that displays the current position of the mouse whenever it is clicked. */
    private class MouseClickEventHandler implements EventHandler<MouseEvent> {
        /** A Text object that will be used to print the current mouse position. */
        Text positionText;

        MouseClickEventHandler(Group root) {
            // For now, since there's no mouse position yet, just create an empty Text object.
            positionText = new Text("");
            // We want the text to show up immediately above the position, so set the origin to be
            // VPos.BOTTOM (so the x-position we assign will be the position of the bottom of the
            // text).
            positionText.setTextOrigin(VPos.BOTTOM);

            // Add the positionText to root, so that it will be displayed on the screen.
            root.getChildren().add(positionText);
        }


        @Override
        public void handle(MouseEvent mouseEvent) {
            // Because we registered this EventHandler using setOnMouseClicked, it will only called
            // with mouse events of type MouseEvent.MOUSE_CLICKED.  A mouse clicked event is
            // generated anytime the mouse is pressed and released on the same JavaFX node.
            double mousePressedX = mouseEvent.getX();
            double mousePressedY = mouseEvent.getY();

            // Display text right above the click.
            positionText.setText("(" + mousePressedX + ", " + mousePressedY + ")");
            positionText.setX(mousePressedX);
            positionText.setY(mousePressedY);
            allText.currentText = allText.sentinel.next;
            while ((allText.currentText.current.getY() <= mousePressedY
                    && allText.currentText.current.getY() + cursor.getHeight() <= mousePressedY)
                    || allText.currentText.current.getX() <= mousePressedX) {
                allText.currentText = allText.currentText.next;
            }
            cursor.setX(allText.currentText.current.getX());
            cursor.setY(allText.currentText.current.getY());
            allText.currentText = allText.currentText.prev;
        }
    }

    @Override
    public void start(Stage primaryStage) {
    	// Create a Node that will be the parent of all things displayed on the screen.
//        root = new Group();
        // The Scene represents the window: its height and width will be the height and width
        // of the window displayed.

        int windowWidth = 500;
        int windowHeight = 500;
        Scene scene = new Scene(root, windowWidth, windowHeight, Color.WHITE);

        // Set on mouse click
        scene.setOnMouseClicked(new MouseClickEventHandler(root));

        // Make a vertical scroll bar on the right side of the screen.
//        ScrollBar scrollBar = new ScrollBar();
        scrollBar.setOrientation(Orientation.VERTICAL);
        // Set the height of the scroll bar so that it fills the whole window.
        scrollBar.setPrefHeight(windowHeight);

        // Add the scroll bar to the scene graph, so that it appears on the screen.
        root.getChildren().add(scrollBar);

        // Move the scrollbar to the right
        windowWidth = windowWidth - (int) Math.round(scrollBar.getLayoutBounds().getWidth());
        scrollBar.setLayoutX(windowWidth);

        // To get information about what keys the user is pressing, create an EventHandler.
        // EventHandler subclasses must override the "handle" function, which will be called
        // by javafx.
        EventHandler<KeyEvent> keyEventHandler = new KeyEventHandler(windowWidth, windowHeight);
        if (!arguments[0].equals("")) {
            if (arguments[1].equals("debug")) {
                System.out.println("I don't know what to print here");
            } else {
                String inputFilename = arguments[0];
                try {
                    File inputFile = new File(inputFilename);
                    // Check to make sure that the input file exists!
                    if (!inputFile.exists()) {
                        System.out.println("Unable to load because file with name " + inputFilename
                                + " does not exist");
                        return;
                    }
                    FileReader reader = new FileReader(inputFile);
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    int intRead = -1;
                    while ((intRead = bufferedReader.read()) != -1) {
                        // The integer read can be cast to a char, because we're assuming ASCII.
                        char charRead = (char) intRead;
                        allText.addAfterCurrentText(String.valueOf(charRead));
                        Text charTyped = allText.currentText.current;
                        textRoot.getChildren().add(charTyped);
                    }
                    allText.currentText = allText.currentText.next;
                    bufferedReader.close();
                } catch (FileNotFoundException fileNotFoundException) {
                    System.out.println("File not found! Exception was: " + fileNotFoundException);
                } catch (IOException ioException) {
                    System.out.println("Error when copying; exception was: " + ioException);
                }
            }
        }

        // Register the event handler to be called for all KEY_PRESSED and KEY_TYPED events.
        scene.setOnKeyTyped(keyEventHandler);
        scene.setOnKeyPressed(keyEventHandler);

        // All new Nodes need to be added to the root in order to be displayed.
        root.getChildren().add(cursor);
        makeRectangleColorChange();

        // Set the range of the scroll bar.
//        scrollBar.setVisibleAmount(WINDOW_HEIGHT);
        scrollBar.setMin(0);
        scrollBar.setMax(Math.max(Math.round(allText.sentinel.prev.current.getY())
                        + Math.round(allText.sentinel.prev.current.getLayoutBounds().getHeight())
                        - WINDOW_HEIGHT, 0));


        // Resize
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldScreenWidth,
                    Number newScreenWidth) {
                // Re-compute WIN_WIDTH & reset scrollbar
                WINDOW_WIDTH = newScreenWidth.intValue() - (int) Math.round(scrollBar.getLayoutBounds().getWidth());
                scrollBar.setLayoutX(WINDOW_WIDTH);
                // Reset all texts
                allText.wrapText();
//                scrollBar.setVisibleAmount(WINDOW_HEIGHT);
                scrollBar.setMin(0);
                scrollBar.setMax(Math.max(Math.round(allText.sentinel.prev.current.getY())
                                + Math.round(allText.sentinel.prev.current.getLayoutBounds().getHeight())
                                - WINDOW_HEIGHT, 0));
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldScreenHeight,
                    Number newScreenHeight) {
                WINDOW_HEIGHT = newScreenHeight.intValue();
                scrollBar.setPrefHeight(WINDOW_HEIGHT);
                allText.wrapText();
//                scrollBar.setVisibleAmount(WINDOW_HEIGHT);
                scrollBar.setMin(0);
                scrollBar.setMax(Math.max(Math.round(allText.sentinel.prev.current.getY())
                                + Math.round(allText.sentinel.prev.current.getLayoutBounds().getHeight())
                                - WINDOW_HEIGHT, 0));
            }
        });

        //Set the text and cursor according to scrollbar move
        scrollBar.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldValue,
                    Number newValue) {
                // newValue describes the value of the new position of the scroll bar. The numerical
                // value of the position is based on the position of the scroll bar, and on the min
                // and max we set above. For example, if the scroll bar is exactly in the middle of
                // the scroll area, the position will be:
                //      scroll minimum + (scroll maximum - scroll minimum) / 2
//                System.out.println(scrollBar.getValue());
                textRoot.setLayoutY(-scrollBar.getValue());
                cursor.setLayoutY(-scrollBar.getValue());
            }
        });

        primaryStage.setTitle("Editor");

        // This is boilerplate, necessary to setup the window where things are displayed.
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        if (args.length == 2) {
            arguments[0] = args[0];
            arguments[1] = args[1];
        } else if (args.length == 1) {
            arguments[0] = args[0];
            arguments[1] = "";
        } else {
            arguments[0] = "";
            arguments[1] = "";
        }
        launch(args);
    }
}
