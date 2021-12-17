import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lomofu
 * <p>
 * This class is used to create a new promotion code
 */
public class AddPromotionDialogView extends JDialog implements Validator {
    // define the default gap for each component in the layout
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
    private final JComboBox<String> promotionTypeComboBox = new JComboBox<>(UIConstant.PROMOTION_TYPES);
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

    /**
     * This factory method will create a new dialog when click the "Add" Button on the see @PromotionTable.
     *
     * @param owner the parent component
     */
    public static void showDig(Frame owner) {
        AddPromotionDialogView addPromotionDialogView = new AddPromotionDialogView(owner);
        addPromotionDialogView.setVisible(true);
    }

    private void initDialogSetting(Frame owner) {
        this.setModal(true); // make this dialog on the top and interrupt to operate the parent component
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
        // each new promotion code will be given a prefix of P + current system mills
        promotionIdTextField.setText(IDUtil.generateId("P"));

        // initialize the sliderBox
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

        // add the components to the panel
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
        // use the Spring to calculate the width of the promotionIdLabel width + promotionIdTextField width + const X_GAP (20)
        Spring childWidth =
                Spring.sum(
                        Spring.sum(Spring.width(promotionIdLabel), Spring.width(promotionIdTextField)),
                        Spring.constant(X_GAP));
        // pack the first row
        initPromotionIdRow(panel, childWidth);

        initChildRow(promotionIdLabel, promotionCodeLabel, promotionCodeTextField);
        initChildRow(promotionCodeLabel, promotionTypeLabel, promotionTypeComboBox);
        initChildRow(promotionTypeLabel, promotionValueLabel, promotionValueSwitch);

        // use the spring layout to put constraint
        springLayout.putConstraint(SpringLayout.WEST, box, 0, SpringLayout.WEST, promotionTypeLabel);
        springLayout.putConstraint(SpringLayout.SOUTH, box, -Y_GAP, SpringLayout.SOUTH, panel);
    }

    private void initPromotionIdRow(JPanel panel, Spring childWidth) {
        // horizontal center
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

    /**
     * The function abstract a row with a JLabel and a component (most are the JTextFields)
     *
     * @param refer     the reference label
     * @param label     the label need to put constraint
     * @param component the component need to put constraint
     */
    private void initChildRow(JLabel refer, JLabel label, Component component) {
        springLayout.putConstraint(SpringLayout.EAST, label, 0, SpringLayout.EAST, refer);
        springLayout.putConstraint(SpringLayout.NORTH, label, Y_GAP, SpringLayout.SOUTH, refer);

        springLayout.putConstraint(SpringLayout.NORTH, component, 0, SpringLayout.NORTH, label);
        springLayout.putConstraint(SpringLayout.WEST, component, X_GAP, SpringLayout.EAST, label);
    }

    /**
     * This function manger all the callback events of the components
     */
    private void initListener() {
        close.addActionListener(__ -> this.dispose());

        // the switch container will render different components with two options: discount or voucher
        promotionTypeComboBox.addActionListener(
                e -> {
                    if (promotionTypeComboBox.getSelectedIndex() == 1) {
                        // firstly, remove to exist components in the box
                        promotionValueSwitch.removeAll();
                        // then fill the target components in the box
                        promotionValueSwitch.add(sliderBox);
                    } else {
                        promotionValueSwitch.removeAll();
                        promotionValueSwitch.add(promotionValueTextField);
                    }
                    // invoke the UI repaint
                    promotionValueSwitch.updateUI();
                });

        promotionValueSlider.addChangeListener(
                // update the text value on the right of the slider in real time
                e -> sliderValueLabel.setText(promotionValueSlider.getValue() + "％"));

        // handle the form process of submission
        submit.addActionListener(__ -> {
            // validates all the inputs (if there is an error, it will return a length string)
            if (!"".equals(this.valid())) {
                JOptionPane.showMessageDialog(
                        this, this.valid(), "Invalid Input", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // build a promotion object
            Promotion promotion = new Promotion();
            promotion.setPromotionId(promotionIdTextField.getText());
            promotion.setPromotionCode(promotionCodeTextField.getText());
            String item = (String) promotionTypeComboBox.getSelectedItem();
            promotion.setPromotionType(item);

            // get different value with different strategy of the corresponding components
            if (UIConstant.PROMOTION_TYPES[0].equals(item)) {
                promotion.setValue(promotionValueTextField.getText());
            } else {
                promotion.setValue(String.valueOf(promotionValueSlider.getScaledValue()));
            }
            // call the createNew function
            PromotionCodeService.createNew(promotion);
            this.dispose();
        });

    }

    @Override
    public String valid() {
        // create a validation map, configure an authentication rule for each field
        // The authentication order is determined by the put order
        Map<String, Validation> map = new LinkedHashMap<>();
        map.put("Promotion Code", new Validation(promotionCodeTextField.getText(), ValidationEnum.HAS_LEN));
        String item = (String) promotionTypeComboBox.getSelectedItem();
        // if the type is voucher, check the voucher input has length
        if (UIConstant.PROMOTION_TYPES[0].equals(item)) {
            map.put("Promotion Value", new Validation(promotionValueTextField.getText(), ValidationEnum.HAS_LEN));
        }
        // call the valid method, the return list will be empty if all inputs are correct
        List<String> errorMsg = ValidationEnum.valid(map);
        if (errorMsg.isEmpty()) {
            return "";
        }
        // only get the top message need to be displayed on the dialog
        return errorMsg.get(0);
    }

    // extends the JSlider with double type
    private static class DoubleJSlider extends JSlider {
        final int scale;

        public DoubleJSlider(int min, int max, int value, int scale) {
            super(min, max, value);
            this.scale = scale;
        }

        // calculate the value by the scalar
        public double getScaledValue() {
            return ((double) super.getValue()) / this.scale;
        }
    }

    // this inner class limits the input document only should be numbed type
    private static class NumberDocument extends PlainDocument {
        public void insertString(int var1, String var2, AttributeSet var3)
                throws BadLocationException {
            // only insert the value to the component if it is a number, otherwise call the system prompt
            if (this.isNumeric(var2)) {
                super.insertString(var1, var2, var3);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }

        private boolean isNumeric(String var1) {
            try {
                Long.valueOf(var1);
                return true;
            } catch (NumberFormatException var3) {
                return false;
            }
        }
    }

}

