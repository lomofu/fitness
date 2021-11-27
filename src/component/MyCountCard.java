package component;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.Consumer;

/**
 * @author lomofu
 * @desc
 * @create 22/Nov/2021 20:00
 */
public class MyCountCard extends JPanel implements MouseListener {
  private final int elevation = 5;
  private final JLabel count = new JLabel("0", JLabel.CENTER);
  private final Consumer<MouseEvent> consumer;
  private MyPanel contentPanel;

  public MyCountCard(String title, int width, int height, Consumer<MouseEvent> consumer) {
    initPanel(width, height);
    initContentPanel(title);
    this.add(contentPanel);
    this.consumer = consumer;
    this.addMouseListener(this);
  }

  public JLabel getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count.setText(Integer.toString(count));
  }

  public MyPanel getContentPanel() {
    return contentPanel;
  }

  private void initPanel(int width, int height) {
    this.setLayout(new GridLayout(1, 1));
    this.setPreferredSize(new Dimension(width, height));
    Border border = BorderFactory.createEmptyBorder(elevation, elevation, elevation, elevation);
    this.setBorder(BorderFactory.createCompoundBorder(this.getBorder(), border));
  }

  private void initContentPanel(String title) {
    this.contentPanel = new MyPanel(new GridLayout(1, 1));
    TitledBorder titledBorder = BorderFactory.createTitledBorder(title);
    titledBorder.setTitleFont(new Font(null, Font.BOLD, 15));
    titledBorder.setTitleJustification(TitledBorder.CENTER);
    this.contentPanel.setBorder(titledBorder);

    initCountNumber();
    this.contentPanel.add(count);
  }

  private void initCountNumber() {
    this.count.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
    Border titleBorder = this.count.getBorder();
    Border margin = new EmptyBorder(0, 0, 20, 0);
    this.count.setBorder(new CompoundBorder(titleBorder, margin));
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

  @Override
  public void mouseClicked(MouseEvent e) {
    consumer.accept(e);
  }

  @Override
  public void mousePressed(MouseEvent e) {}

  @Override
  public void mouseReleased(MouseEvent e) {}

  @Override
  public void mouseEntered(MouseEvent e) {
    MyCountCard component = (MyCountCard) e.getComponent();
    component.getCount().setForeground(Color.WHITE);
    component.setCursor(new Cursor(Cursor.HAND_CURSOR));

    MyPanel myPanel = component.getContentPanel();
    myPanel.setBackground(Color.BLUE);
    myPanel.setForeground(Color.WHITE);

    TitledBorder t = (TitledBorder) myPanel.getBorder();
    t.setTitleColor(Color.WHITE);
  }

  @Override
  public void mouseExited(MouseEvent e) {
    MyCountCard component = (MyCountCard) e.getComponent();
    component.getCount().setForeground(Color.BLACK);

    MyPanel myPanel = component.getContentPanel();
    myPanel.setBackground(Color.WHITE);
    myPanel.setForeground(Color.BLACK);

    TitledBorder t = (TitledBorder) myPanel.getBorder();
    t.setTitleColor(Color.BLACK);
  }
}
