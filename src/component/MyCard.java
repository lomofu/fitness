package component;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * @author lomofu
 * @desc
 * @create 24/Nov/2021 19:16
 */
public class MyCard extends MyPanel {
  private final int elevation = 5;
  private final MyPanel contentPanel = new MyPanel();

  public MyCard(String title, boolean needItalic) {
    Font font;
    this.setLayout(new GridLayout(1, 1));
    TitledBorder titledBorder = BorderFactory.createTitledBorder(title);
    if (needItalic) {
      font = new Font(null, Font.ITALIC, 15);
    } else {
      font = new Font(null, Font.BOLD, 15);
    }
    titledBorder.setTitleFont(font);
    titledBorder.setTitleJustification(TitledBorder.CENTER);

    this.contentPanel.setBorder(titledBorder);
    this.add(contentPanel);
  }

  public void addC(Component component) {
    this.contentPanel.add(component);
  }

  public void setCSize(int width, int height) {
    this.setPreferredSize(new Dimension(width, height));
  }

  public void setCLayout(LayoutManager layout) {
    this.contentPanel.setLayout(layout);
  }

  @Override
  protected void paintComponent(Graphics g) {
    int shade = 0;
    int topOpacity = 70;
    for (int i = 0; i < elevation; i++) {
      g.setColor(new Color(shade, shade, shade, ((topOpacity / elevation) * i)));
      g.drawRect(i, i, this.getWidth() - ((i * 2) + 1), this.getHeight() - ((i * 2) + 1));
    }
  }
}
