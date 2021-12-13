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
public class MyTextCard extends JPanel implements MouseListener {
    private final int elevation = 5;
    private final JLabel count = new JLabel("0", JLabel.CENTER);
    private final Consumer<MouseEvent> consumer;
    private MyPanel contentPanel;
    private Color backgroundColor = Color.WHITE;
    private Color foregroundColor = Color.BLACK;
    private Color hoverColor = new Color(18, 95, 161, 229);
    private Color hoverForegroundColor = Color.WHITE;
    private Color borderColor = Color.BLACK;
    private Color hoverBorderColor = Color.WHITE;

    public MyTextCard(String title, int width, int height, Consumer<MouseEvent> consumer) {
        initPanel(width, height);
        initContentPanel(title);
        this.add(contentPanel);
        this.consumer = consumer;
        this.setBackground(backgroundColor);
        this.addMouseListener(this);
    }

    public JLabel getCount() {
        return count;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.setBackground(backgroundColor);
        this.contentPanel.setBackground(backgroundColor);
    }

    public void setHoverColor(Color hoverColor) {
        this.hoverColor = hoverColor;
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
        this.contentPanel.setForeground(foregroundColor);
        this.getCount().setForeground(foregroundColor);
    }

    public void setHoverForegroundColor(Color hoverForegroundColor) {
        this.hoverForegroundColor = hoverForegroundColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        TitledBorder t = (TitledBorder) contentPanel.getBorder();
        t.setTitleColor(hoverBorderColor);
    }

    public void setHoverBorderColor(Color hoverBorderColor) {
        this.hoverBorderColor = hoverBorderColor;
    }

    public void setValue(int count) {
        this.count.setText(Integer.toString(count));
    }

    public void setValue(String count) {this.count.setText(count);}

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
        this.contentPanel.setBackground(Color.WHITE);

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
        for(int i = 0; i < elevation; i++) {
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
        MyTextCard component = (MyTextCard) e.getComponent();
        component.getCount().setForeground(hoverForegroundColor);
        component.setCursor(new Cursor(Cursor.HAND_CURSOR));

        MyPanel myPanel = component.getContentPanel();
        myPanel.setBackground(hoverColor);
        myPanel.setForeground(hoverForegroundColor);

        TitledBorder t = (TitledBorder) myPanel.getBorder();
        t.setTitleColor(hoverBorderColor);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        MyTextCard component = (MyTextCard) e.getComponent();
        component.getCount().setForeground(foregroundColor);

        MyPanel myPanel = component.getContentPanel();
        myPanel.setBackground(backgroundColor);
        myPanel.setForeground(foregroundColor);

        TitledBorder t = (TitledBorder) myPanel.getBorder();
        t.setTitleColor(borderColor);
    }
}
