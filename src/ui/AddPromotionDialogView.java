package ui;

import utils.IDUtil;

import javax.swing.*;
import java.awt.*;

import static constant.UIConstant.PROMOTION_TYPES;

/**
 * @author lomofu
 * @desc
 * @create 07/Dec/2021 03:08
 */
public class AddPromotionDialogView extends JDialog {
  private static final int X_GAP = 20;
  private static final int Y_GAP = 20;
  private final SpringLayout springLayout = new SpringLayout();
  private final JLabel promotionIdLabel = new JLabel("Promotion ID");
  private final JLabel promotionCodeLabel = new JLabel("Promotion Code");
  private final JLabel promotionTypeLabel = new JLabel("Promotion Type");
  private final JLabel promotionValueLabel = new JLabel("Promotion Value");
  private final JLabel sliderValueLabel = new JLabel("100%");

  private final JTextField promotionIdTextField = new JTextField(20);
  private final JTextField promotionCodeTextField = new JTextField(20);
  private final JComboBox<String> promotionTypeComboBox = new JComboBox<>(PROMOTION_TYPES);
  private final JPanel promotionValueSwitch = new JPanel();
  private final JTextField promotionValueTextField = new JTextField(20);
  private final DoubleJSlider promotionValueSlider = new DoubleJSlider(0, 100, 100, 100);
  private final Box sliderBox = Box.createHorizontalBox();

  private final JButton submit = new JButton("Submit");
  private final JButton reset = new JButton("Reset");

  public AddPromotionDialogView(Frame owner) {
    initDialogSetting(owner);
    initFormElements();
    JPanel panel = initPanel();
    initListener();
    this.add(panel);
  }

  public static void showDig(Frame owner) {
    AddPromotionDialogView addPromotionDialogView = new AddPromotionDialogView(owner);
    addPromotionDialogView.setVisible(true);
  }

  private void initDialogSetting(Frame owner) {
    this.setTitle("New Promotion");
    this.setSize(600, 300);
    this.setPreferredSize(new Dimension(600, 300));
    this.setResizable(true);
    this.setLocationRelativeTo(owner);
  }

  private void initFormElements() {
    promotionIdTextField.setEditable(false);
    promotionIdTextField.setFocusable(false);
    promotionIdTextField.setBackground(new Color(0, 0, 0, 0));
    promotionIdTextField.setBorder(null);
    promotionIdTextField.setText(IDUtil.generateId("P"));

    promotionValueSlider.setInverted(true);
    sliderBox.add(promotionValueSlider);
    sliderBox.add(Box.createHorizontalStrut(10));
    sliderBox.add(sliderValueLabel);

    promotionValueSwitch.add(promotionValueTextField);
    submit.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
    reset.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
    reset.setForeground(Color.RED);
  }

  private JPanel initPanel() {
    JPanel panel = new JPanel(springLayout);
    Box box = getBox(submit, reset);
    initSpringLayout(panel, box);

    panel.add(promotionIdLabel);
    panel.add(promotionCodeLabel);
    panel.add(promotionTypeLabel);
    panel.add(promotionValueLabel);
    panel.add(promotionIdTextField);
    panel.add(promotionCodeTextField);
    panel.add(promotionTypeComboBox);
    panel.add(promotionValueSwitch);

    panel.add(box);
    return panel;
  }

  private Box getBox(Component component, Component component1) {
    Box box = Box.createHorizontalBox();
    box.add(component);
    box.add(Box.createHorizontalStrut(X_GAP));
    box.add(component1);
    return box;
  }

  private void initSpringLayout(JPanel panel, Box box) {
    Spring childWidth =
        Spring.sum(
            Spring.sum(Spring.width(promotionIdLabel), Spring.width(promotionIdTextField)),
            Spring.constant(X_GAP));
    initPromotionIdRow(panel, childWidth);
    initChildRow(promotionIdLabel, promotionCodeLabel, promotionCodeTextField);
    initChildRow(promotionCodeLabel, promotionTypeLabel, promotionTypeComboBox);
    initChildRow(promotionTypeLabel, promotionValueLabel, promotionValueSwitch);

    springLayout.putConstraint(SpringLayout.WEST, box, 0, SpringLayout.WEST, promotionTypeLabel);
    springLayout.putConstraint(SpringLayout.SOUTH, box, -Y_GAP, SpringLayout.SOUTH, panel);
  }

  private void initPromotionIdRow(JPanel panel, Spring childWidth) {
    springLayout.putConstraint(
        SpringLayout.WEST,
        promotionIdLabel,
        -childWidth.getValue() / 2,
        SpringLayout.HORIZONTAL_CENTER,
        panel);
    springLayout.putConstraint(
        SpringLayout.NORTH, promotionIdLabel, Y_GAP, SpringLayout.NORTH, panel);

    springLayout.putConstraint(
        SpringLayout.NORTH, promotionIdTextField, 0, SpringLayout.NORTH, promotionIdLabel);
    springLayout.putConstraint(
        SpringLayout.WEST, promotionIdTextField, X_GAP, SpringLayout.EAST, promotionIdLabel);
  }

  private void initChildRow(JLabel refer, JLabel label, Component component) {
    springLayout.putConstraint(SpringLayout.EAST, label, 0, SpringLayout.EAST, refer);
    springLayout.putConstraint(SpringLayout.NORTH, label, Y_GAP, SpringLayout.SOUTH, refer);

    springLayout.putConstraint(SpringLayout.NORTH, component, 0, SpringLayout.NORTH, label);
    springLayout.putConstraint(SpringLayout.WEST, component, X_GAP, SpringLayout.EAST, label);
  }

  private void initListener() {
    promotionTypeComboBox.addActionListener(
        e -> {
          if (promotionTypeComboBox.getSelectedIndex() == 1) {
            promotionValueSwitch.removeAll();
            promotionValueSwitch.add(sliderBox);
          } else {
            promotionValueSwitch.removeAll();
            promotionValueSwitch.add(promotionValueTextField);
          }
          promotionValueSwitch.updateUI();
        });

    promotionValueSlider.addChangeListener(
        e -> sliderValueLabel.setText(promotionValueSlider.getValue() + "％"));
  }
}

class DoubleJSlider extends JSlider {

  final int scale;

  public DoubleJSlider(int min, int max, int value, int scale) {
    super(min, max, value);
    this.scale = scale;
  }

  public double getScaledValue() {
    return ((double) super.getValue()) / this.scale;
  }
}
