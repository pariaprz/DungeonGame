package unsw.dungeon;

import javafx.scene.text.Text;

/**
 * This is a fix for a JavaFX bug.
 * This is just a linebreak component equivalent to HTML a <br/> tag.
 */
public class LineBreak extends Text {

    public LineBreak() {
        setText("\n");
    }
}
