// package component;
//
// import bean.Customer;
// import data.DataSource;
// import data.DataSourceChannel;
// import ui.AddMemberDialogView;
// import ui.ClubFrameView;
// import ui.EditMemberDialogView;
//
// import javax.swing.*;
// import javax.swing.event.DocumentEvent;
// import javax.swing.event.DocumentListener;
// import javax.swing.table.*;
// import java.awt.*;
// import java.awt.event.MouseAdapter;
// import java.awt.event.MouseEvent;
// import java.awt.event.MouseListener;
// import java.awt.geom.Rectangle2D;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.List;
//
// import static constant.UIConstant.COLUMNS;
//
// /**
//  * @author lomofu
//  * @desc
//  * @create 23/Nov/2021 16:42
//  */
// public class MemberTable implements DataSourceChannel {
//   private final DefaultTableModel tableModel = new DefaultTableModel();
//   private ClubFrameView clubFrameView;
//   private JScrollPane jScrollPane;
//   private JToolBar jToolBar;
//   private JTable jTable;
//   private JTextField searchTextField;
//
//   public MemberTable(ClubFrameView clubFrameView) {
//     this.subscribe();
//     this.clubFrameView = clubFrameView;
//     initToolBar();
//     initTable();
//     initScrollPane();
//     initEvents();
//   }
//
//   public JScrollPane getjScrollPane() {
//     return jScrollPane;
//   }
//
//   public JToolBar getjToolBar() {
//     return jToolBar;
//   }
//
//   private void initToolBar() {
//     this.jToolBar = new JToolBar();
//     this.jToolBar.setLayout(new BoxLayout(this.jToolBar, BoxLayout.X_AXIS));
//     this.jToolBar.setFloatable(false);
//     this.jToolBar.setPreferredSize(new Dimension(0, 40));
//
//     JButton addMemberBtn =
//         new TableToolButton("Add", MyImageIcon.build("code/assets/table/plus.png"));
//
//     JButton editMemberBtn =
//         new TableToolButton("Edit", MyImageIcon.build("code/assets/table/edit.png"));
//     editMemberBtn.setEnabled(false);
//
//     JButton deleteMemberBtn =
//         new TableToolButton("Remove", MyImageIcon.build("code/assets/table/remove.png"));
//     deleteMemberBtn.setEnabled(false);
//
//     Box searchBox = Box.createHorizontalBox();
//     searchTextField = new JTextField(20);
//     searchTextField.setMaximumSize(new Dimension(100, 200));
//     searchBox.add(new JLabel("Search"));
//     searchBox.add(Box.createHorizontalStrut(10));
//     searchBox.add(searchTextField);
//
//     this.jToolBar.add(addMemberBtn);
//     this.jToolBar.addSeparator(new Dimension(10, 0));
//     this.jToolBar.add(editMemberBtn);
//     this.jToolBar.addSeparator(new Dimension(10, 0));
//     this.jToolBar.add(deleteMemberBtn);
//     this.jToolBar.add(Box.createHorizontalGlue());
//     this.jToolBar.add(searchBox);
//   }
//
//   private void initTable() {
//     bindData();
//     setTableStyle();
//   }
//
//   private void setTableStyle() {
//     this.jTable.setGridColor(new Color(227, 227, 227));
//     this.jTable.setShowHorizontalLines(false);
//     this.jTable.setRowHeight(30);
//     this.jTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
//
//     JTableHeader tableHeader = jTable.getTableHeader();
//     tableHeader.setFont(new Font(null, Font.PLAIN, 14));
//
//     DefaultTableCellRenderer defaultTableHeaderRenderer =
//         (DefaultTableCellRenderer) tableHeader.getDefaultRenderer();
//     defaultTableHeaderRenderer.setHorizontalAlignment(SwingConstants.CENTER);
//     Arrays.stream(COLUMNS)
//         .forEachOrdered(
//             e -> jTable.getColumn(e).setCellRenderer(new MyTableCellRender(this.searchTextField)));
//   }
//
//   private void bindData() {
//     String[][] data =
//         DataSource.getData().stream()
//             .map(
//                 e ->
//                     new String[] {
//                       e.getId(),
//                       e.getFirstName(),
//                       e.getLastName(),
//                       e.getDateOfBirth(),
//                       e.getGender(),
//                       e.getHomeAddress(),
//                       e.getPhoneNumber(),
//                       e.getType(),
//                       e.getStartDate(),
//                       e.getExpireTime()
//                     })
//             .toArray(size -> new String[size][COLUMNS.length]);
//
//     tableModel.setDataVector(data, COLUMNS);
//     this.jTable = new JTable(tableModel);
//   }
//
//   private void initScrollPane() {
//     this.jScrollPane = new JScrollPane(jTable);
//   }
//
//   private void initEvents() {
//     ((JButton) jToolBar.getComponents()[0])
//         .addActionListener(e -> AddMemberDialogView.showDig(clubFrameView));
//     ((JButton) jToolBar.getComponents()[2])
//         .addActionListener(
//             e ->
//                 EditMemberDialogView.showDig(
//                     clubFrameView, DataSource.getData().get(jTable.getSelectedRow())));
//
//     this.jTable =
//         new JTable(this.tableModel) {
//           @Override
//           public boolean isCellEditable(int row, int column) {
//             return false;
//           }
//         };
//
//     this.jTable.addMouseListener(
//         new MouseAdapter() {
//           @Override
//           public void mousePressed(MouseEvent e) {
//             JTable table = (JTable) e.getSource();
//             Point point = e.getPoint();
//             int row = table.rowAtPoint(point);
//             Customer data = DataSource.getData().get(row);
//             if (e.getButton() == MouseEvent.BUTTON1
//                 && e.getClickCount() == 2
//                 && table.getSelectedRow() != -1) {
//               Customer customer = EditMemberDialogView.showDig(clubFrameView, data);
//             }
//           }
//
//           @Override
//           public void mouseClicked(MouseEvent e) {
//             if (e.getButton() == MouseEvent.BUTTON3) {
//               JTable table = (JTable) e.getSource();
//               int[] selectedRows = table.getSelectedRows();
//
//               if (selectedRows.length == 0) {
//                 return;
//               }
//
//               Point point = e.getPoint();
//               int row = table.rowAtPoint(point);
//
//               if (Arrays.stream(selectedRows).filter(v -> v == row).findAny().isEmpty()) {
//                 return;
//               }
//
//               if (selectedRows.length == 1) {
//                 table.setRowSelectionInterval(row, row);
//                 Customer data = DataSource.getData().get(row);
//                 TablePopMenu popMenu = new TablePopMenu(clubFrameView, data);
//                 popMenu.show(e.getComponent(), e.getX(), e.getY());
//
//                 return;
//               }
//
//               TablePopMenu popMenu = new TablePopMenu(clubFrameView, selectedRows);
//               popMenu.show(e.getComponent(), e.getX(), e.getY());
//             }
//           }
//         });
//
//     this.jTable
//         .getSelectionModel()
//         .addListSelectionListener(
//             e -> {
//               Component editBtn = jToolBar.getComponents()[2];
//               Component removeBtn = jToolBar.getComponents()[4];
//               if (jTable.getSelectedRowCount() > 0) {
//                 editBtn.setEnabled(false);
//                 removeBtn.setEnabled(true);
//               }
//               if (jTable.getSelectedRowCount() == 1) {
//                 editBtn.setEnabled(true);
//               }
//             });
//
//     searchTextField
//         .getDocument()
//         .addDocumentListener(
//             new DocumentListener() {
//               @Override
//               public void insertUpdate(DocumentEvent e) {
//                 update();
//               }
//
//               @Override
//               public void removeUpdate(DocumentEvent e) {
//                 update();
//               }
//
//               @Override
//               public void changedUpdate(DocumentEvent e) {
//                 update();
//               }
//
//               private void update() {
//                 TableRowSorter<TableModel> sorter = new TableRowSorter<>(jTable.getModel());
//                 String text = searchTextField.getText();
//                 if (text.trim().length() == 0) {
//                   jTable.setRowSorter(null);
//                   return;
//                 }
//                 sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
//                 jTable.setRowSorter(sorter);
//               }
//             });
//   }
//
//   @Override
//   public void subscribe() {
//     DataSource.subscribe(this);
//   }
//
//   @Override
//   public void onDataChange(Customer e) {
//     this.tableModel.addRow(
//         new String[] {
//           e.getId(),
//           e.getFirstName(),
//           e.getLastName(),
//           e.getDateOfBirth(),
//           e.getGender(),
//           e.getHomeAddress(),
//           e.getPhoneNumber(),
//           e.getType(),
//           e.getHealthCondition(),
//           e.getStartDate(),
//           e.getExpireTime()
//         });
//     this.jTable.validate();
//     this.jTable.updateUI();
//   }
//
//   private static class MyTableCellRender extends DefaultTableCellRenderer {
//     private JTextField searchField;
//
//     public MyTableCellRender(JTextField searchField) {
//       this.searchField = searchField;
//     }
//
//     @Override
//     public Component getTableCellRendererComponent(
//         JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//       if (row % 2 == 0) setBackground(new Color(213, 213, 213));
//       else if (row % 2 == 1) setBackground(Color.white);
//       if (column != 5) setHorizontalAlignment(SwingConstants.CENTER);
//       Component c =
//           super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//       JLabel original = (JLabel) c;
//       LabelHighlighted label = new LabelHighlighted();
//       label.setFont(original.getFont());
//       label.setText(original.getText());
//       label.setBackground(original.getBackground());
//       label.setForeground(original.getForeground());
//       label.setHorizontalTextPosition(original.getHorizontalTextPosition());
//       label.highlightText(searchField.getText());
//       return label;
//       // return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
//       // column);
//     }
//   }
//
//   private static class TableToolButton extends JButton implements MouseListener {
//     public TableToolButton(String label, ImageIcon imageIcon) {
//       super(label, imageIcon);
//       this.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
//       this.setBorderPainted(false);
//       this.setIconTextGap(10);
//       this.setCursor(new Cursor(Cursor.HAND_CURSOR));
//       this.addMouseListener(this);
//     }
//
//     @Override
//     public void mouseClicked(MouseEvent e) {}
//
//     @Override
//     public void mousePressed(MouseEvent e) {}
//
//     @Override
//     public void mouseReleased(MouseEvent e) {}
//
//     @Override
//     public void mouseEntered(MouseEvent e) {
//       JButton btn = (JButton) e.getComponent();
//       btn.setBorderPainted(true);
//       btn.setRolloverEnabled(true);
//     }
//
//     @Override
//     public void mouseExited(MouseEvent e) {
//       JButton btn = (JButton) e.getComponent();
//       btn.setBorderPainted(false);
//     }
//   }
//
//   private static class TablePopMenu extends JPopupMenu {
//     public TablePopMenu(Frame owner, Customer data) {
//       JMenuItem edit = new JMenuItem("Edit");
//       JMenuItem remove = new JMenuItem("Remove");
//
//       edit.addActionListener(e -> EditMemberDialogView.showDig(owner, data));
//
//       this.add(edit);
//       this.add(remove);
//     }
//
//     public TablePopMenu(Frame owner, int[] rows) {
//       JMenuItem remove = new JMenuItem("Remove");
//       this.add(remove);
//     }
//   }
//
//   private static class LabelHighlighted extends JLabel {
//     private final List<Rectangle2D> rectangles = new ArrayList<>();
//     private final Color colorHighlight = Color.YELLOW;
//
//     public void reset() {
//       rectangles.clear();
//       repaint();
//     }
//
//     public void highlightText(String textToHighlight) {
//       if (textToHighlight == null) {
//         return;
//       }
//       reset();
//
//       final String textToMatch = textToHighlight.toLowerCase().trim();
//       if (textToMatch.length() == 0) {
//         return;
//       }
//       textToHighlight = textToHighlight.trim();
//
//       final String labelText = getText().toLowerCase();
//       if (labelText.contains(textToMatch)) {
//         FontMetrics fm = getFontMetrics(getFont());
//         float w = -1;
//         final float h = fm.getHeight() - 1;
//         int i = 0;
//         while (true) {
//           i = labelText.indexOf(textToMatch, i);
//           if (i == -1) {
//             break;
//           }
//           if (w == -1) {
//             String matchingText = getText().substring(i, i + textToHighlight.length());
//             w = fm.stringWidth(matchingText);
//           }
//           String preText = getText().substring(0, i);
//           float x = fm.stringWidth(preText);
//           rectangles.add(new Rectangle2D.Float(x, 8, w, h));
//           i = i + textToMatch.length();
//         }
//         repaint();
//       }
//     }
//
//     @Override
//     protected void paintComponent(Graphics g) {
//       g.setColor(getBackground());
//       g.fillRect(0, 0, getWidth(), getHeight());
//       if (rectangles.size() > 0) {
//         Graphics2D g2d = (Graphics2D) g;
//         Color c = g2d.getColor();
//         for (Rectangle2D rectangle : rectangles) {
//           g2d.setColor(colorHighlight);
//           g2d.fill(rectangle);
//           g2d.setColor(Color.LIGHT_GRAY);
//           g2d.draw(rectangle);
//         }
//         g2d.setColor(c);
//       }
//       super.paintComponent(g);
//     }
//   }
// }
