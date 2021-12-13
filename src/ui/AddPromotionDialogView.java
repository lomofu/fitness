package ui;

import bean.Promotion;
import bean.Validation;
import core.PromotionCodeService;
import utils.IDUtil;
import validator.ValidationEnum;
import validator.Validator;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static constant.UIConstant.PROMOTION_TYPES;

/**
 * @author lomofu
 * @desc
 * @create 07/Dec/2021 03:08
 */
public class AddPromotionDialogView extends JDialog implements Validator {
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
    private final JButton close = new JButton("Close");

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
        this.setModal(true);
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

        promotionValueTextField.setDocument(new NumberDocument());

        submit.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        close.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        close.setForeground(Color.RED);
    }

    private JPanel initPanel() {
        JPanel panel = new JPanel(springLayout);
        Box box = getBox(submit, close);
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
        springLayout.putConstraint(SpringLayout.SOUTH, box, - Y_GAP, SpringLayout.SOUTH, panel);
    }

    private void initPromotionIdRow(JPanel panel, Spring childWidth) {
        springLayout.putConstraint(
                SpringLayout.WEST,
                promotionIdLabel,
                - childWidth.getValue() / 2,
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
        close.addActionListener(__ -> this.dispose());
        promotionTypeComboBox.addActionListener(
                e -> {
                    if(promotionTypeComboBox.getSelectedIndex() == 1) {
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

        submit.addActionListener(__ -> {
            if(! "".equals(this.valid())) {
                JOptionPane.showMessageDialog(
                        this, this.valid(), "Invalid Input", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            Promotion promotion = new Promotion();
            promotion.setPromotionId(promotionIdTextField.getText());
            promotion.setPromotionCode(promotionCodeTextField.getText());
            String item = (String) promotionTypeComboBox.getSelectedItem();
            promotion.setPromotionType(item);
            if(PROMOTION_TYPES[0].equals(item)) {
                promotion.setValue(promotionValueTextField.getText());
            } else {
                promotion.setValue(String.valueOf(promotionValueSlider.getScaledValue()));
            }
            PromotionCodeService.createNew(promotion);
            this.dispose();
        });

    }

    @Override
    public String valid() {
        Map<String, Validation> map = new LinkedHashMap<>();
        map.put("Promotion Code", new Validation(promotionCodeTextField.getText(), ValidationEnum.HAS_LEN));
        String item = (String) promotionTypeComboBox.getSelectedItem();
        if(PROMOTION_TYPES[0].equals(item)) {
            map.put("Promotion Value", new Validation(promotionValueTextField.getText(), ValidationEnum.HAS_LEN));
        }
        List<String> errorMsg = ValidationEnum.valid(map);
        if(errorMsg.isEmpty()) {
            return "";
        }
        return errorMsg.get(0);
    }

    private static class DoubleJSlider extends JSlider {
        final int scale;

        public DoubleJSlider(int min, int max, int value, int scale) {
            super(min, max, value);
            this.scale = scale;
        }

        public double getScaledValue() {
            return ((double) super.getValue()) / this.scale;
        }
    }

    private static class NumberDocument extends PlainDocument {
        public void insertString(int var1, String var2, AttributeSet var3)
                throws BadLocationException {
            if(this.isNumeric(var2)) {
                super.insertString(var1, var2, var3);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }

        private boolean isNumeric(String var1) {
            try {
                Long.valueOf(var1);
                return true;
            } catch(NumberFormatException var3) {
                return false;
            }
        }
    }

}

