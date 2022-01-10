package component;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * @author lomofu
 * <p>
 * This class defines a border card with a shadow
 */
public class MyCard extends MyPanel {
    private final MyPanel contentPanel = new MyPanel();

    public MyCard(String title, boolean needItalic) {
        Font font;
        this.setLayout(new GridLayout(1, 1));
        TitledBorder titledBorder = BorderFactory.createTitledBorder(title);
        // let the title style is Italic or not
        if(needItalic) {
            font = new Font(null, Font.ITALIC, 15);
        } else {
            font = new Font(null, Font.BOLD, 15);
        }
        titledBorder.setTitleFont(font);
        titledBorder.setTitleJustification(TitledBorder.CENTER);

        this.contentPanel.setBorder(titledBorder);
        this.add(contentPanel);
    }

    /**
     * This method will let a component be add into the card
     *
     * @param component any component
     */
    public void addC(Component component) {
        this.contentPanel.add(component);
    }

    // set the card size
    public void setCSize(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
    }

    /**
     * This method is used to draw the card shadow
     *
     * @param g graphics
     */
    @Override
    protected void paintComponent(Graphics g) {
        int shade = 0;
        int topOpacity = 70;
        // set shadow elevation
        int ELEVATION = 5;
        for(int i = 0; i < ELEVATION; i++) {
            g.setColor(new Color(shade, shade, shade, ((topOpacity / ELEVATION) * i)));
            // draw rectangle
            g.drawRect(i, i, this.getWidth() - ((i * 2) + 1), this.getHeight() - ((i * 2) + 1));
        }
    }
}
