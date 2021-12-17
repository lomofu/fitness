import javax.swing.Timer;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
//todo
/**
 * @author lomofu
 *
 *
 */
public class MemberTable extends MyTable implements DataSourceChannel<CustomerDto>, ActionListener {
    private AddMemberDialogView addMemberDialogView;
    private AddMainMemberDialogView parent;

    public MemberTable(ClubFrameView clubFrameView, String title, String[] columns, Object[][] data) {
        super(clubFrameView, title, columns, data, UIConstant.MEMBER_SEARCH_FILTER_COLUMNS);
        this.subscribe(CustomerDto.class);
        initTable();
        initTimer();
        initMyEvents();
    }

    public MemberTable(
            AddMemberDialogView addMemberDialogView,
            AddMainMemberDialogView parent,
            String title,
            String[] columns,
            Object[][] data,
            int[] filterColumns,
            boolean selectMode,
            int selectIndex) {
        super(title, columns, data, filterColumns, selectMode, selectIndex);

        TableColumn column = this.jTable.getColumnModel().getColumn(selectIndex);
        column.setMinWidth(60);
        column.setMaxWidth(60);
        column.setResizable(false);
        this.addMemberDialogView = addMemberDialogView;
        this.parent = parent;
    }

    private void initTable() {
        TableColumnModel columnModel = this.jTable.getColumnModel();
        DateRender dateRender = new DateRender();
        StateRender stateRender = new StateRender();
        columnModel.getColumn(3).setCellRenderer(dateRender);
        columnModel.getColumn(9).setCellRenderer(dateRender);
        columnModel.getColumn(10).setCellRenderer(dateRender);
        columnModel.getColumn(12).setCellRenderer(stateRender);
    }

    private void initTimer() {
        Timer timer = new Timer((int) TimeUnit.HOURS.toMillis(1), this);
        timer.start();
    }

    private void initMyEvents() {
        this.jTable.getSelectionModel().addListSelectionListener(e -> {
            Component[] jToolBarComponents = jToolBar.getComponents();
            Component editBtn = jToolBarComponents[2];
            Component removeBtn = jToolBarComponents[4];
            Component conBtn = jToolBarComponents[6];
            Component renewBtn = jToolBarComponents[10];

            if (jTable.getSelectedRowCount() > 0) {
                editBtn.setEnabled(false);
                conBtn.setEnabled(false);
                removeBtn.setEnabled(true);
                renewBtn.setEnabled(false);
            }

            if (jTable.getSelectedRowCount() == 1) {
                editBtn.setEnabled(true);
                conBtn.setEnabled(true);
                renewBtn.setEnabled(true);
            }
        });

        this.helpBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this.jScrollPane, UIConstant.HELP_INFO[0], "Help",
                        JOptionPane.QUESTION_MESSAGE, MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[9][1])));
    }

    @Override
    protected void addComponentsToToolBar() {
        if (selectMode) {
            initSelectMode();
            return;
        }
        initCanEditToolBar();
    }

    private void initSelectMode() {
        JButton confirmCourseBtn =
                new TableToolButton("Confirm", MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[0][1]));
        confirmCourseBtn.addActionListener(__ -> {
            DefaultTableModel tableModel = (DefaultTableModel) this.jTable.getModel();
            List<String> collect = tableModel.getDataVector()
                    .stream()
                    .filter(v -> v.get(3) != null)
                    .filter(v -> (Boolean) v.get(3))
                    .map(v -> (String) v.get(0))
                    .collect(Collectors.toList());

            if (collect.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this.parent,
                        "You must choose at least one main member!",
                        "Warning",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (collect.size() > 1) {
                JOptionPane.showMessageDialog(
                        this.parent,
                        "You must choose only one main member!",
                        "Warning",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            // make sure only choose one main member
            String parentId = collect.get(0);
            CustomerDto parent = MembershipService.findCustomerById(parentId);
            this.parent.dispose();
            this.addMemberDialogView.syncParentInfo(parent);
        });

        this.jToolBar.add(confirmCourseBtn);
        this.jToolBar.add(Box.createHorizontalGlue());
        this.jToolBar.add(this.searchBox);
    }

    private void initCanEditToolBar() {
        JButton addMemberBtn = new TableToolButton(UIConstant.TABLE_TOOL_LIST[0][0], MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[0][1]));
        addMemberBtn.addActionListener(e -> AddMemberDialogView.showDig(clubFrameView));

        JButton editMemberBtn = new TableToolButton(UIConstant.TABLE_TOOL_LIST[1][0], MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[1][1]));
        editMemberBtn.setEnabled(false);
        editMemberBtn.addActionListener(
                e -> EditMemberDialogView.showDig(clubFrameView, (String) jTable.getModel().getValueAt(jTable.convertRowIndexToModel(jTable.getSelectedRow()), 0)));

        JButton deleteMemberBtn = new TableToolButton(UIConstant.TABLE_TOOL_LIST[2][0], MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[2][1]));
        deleteMemberBtn.setEnabled(false);
        deleteMemberBtn.addActionListener(__ -> removeMember());

        JButton consumptionBtn = new TableToolButton(UIConstant.TABLE_TOOL_LIST[7][0], MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[7][1]));
        consumptionBtn.setEnabled(false);
        consumptionBtn.addActionListener(__ -> CheckConsumptionDialogView.showDig(clubFrameView, (String) jTable.getModel().getValueAt(jTable.convertRowIndexToModel(jTable.getSelectedRow()), 0)));

        JButton renewBtn = new TableToolButton(UIConstant.TABLE_TOOL_LIST[8][0], MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[8][1]));
        renewBtn.setEnabled(false);
        renewBtn.addActionListener(__ -> RenewMemberDialogView.showDig(clubFrameView, (String) jTable.getModel().getValueAt(jTable.convertRowIndexToModel(jTable.getSelectedRow()), 0)));

        JButton refreshBtn = new TableToolButton(UIConstant.TABLE_TOOL_LIST[6][0], MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[6][1]));
        refreshBtn.addActionListener(__ -> {
            Rectangle rect = jTable.getCellRect(0, 0, true);
            jTable.scrollRectToVisible(rect);
            editMemberBtn.setEnabled(false);
            deleteMemberBtn.setEnabled(false);
            consumptionBtn.setEnabled(false);
            renewBtn.setEnabled(false);
            fetchData();
        });

        JButton filterBtn = new TableToolButton("", MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[3][1]));
        filterBtn.setToolTipText(UIConstant.TABLE_TOOL_LIST[3][0]);
        filterBtn.addActionListener(__ -> {
            if (this.filterBar.isVisible()) {
                filterBtn.setIcon(MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[3][1]));
                filterBtn.setToolTipText(UIConstant.TABLE_TOOL_LIST[3][0]);
                this.filterBar.setVisible(false);
                return;
            }

            filterBtn.setIcon(MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[4][1]));
            filterBtn.setToolTipText(UIConstant.TABLE_TOOL_LIST[4][0]);
            this.filterBar.setVisible(true);
        });

        this.jToolBar.add(addMemberBtn);
        this.jToolBar.addSeparator(new Dimension(10, 0));
        this.jToolBar.add(editMemberBtn);
        this.jToolBar.addSeparator(new Dimension(10, 0));
        this.jToolBar.add(deleteMemberBtn);
        this.jToolBar.addSeparator(new Dimension(10, 0));
        this.jToolBar.add(consumptionBtn);
        this.jToolBar.addSeparator(new Dimension(10, 0));
        this.jToolBar.add(refreshBtn);
        this.jToolBar.addSeparator(new Dimension(10, 0));
        this.jToolBar.add(renewBtn);
        this.jToolBar.add(Box.createHorizontalGlue());
        this.jToolBar.add(filterBtn);
        this.jToolBar.addSeparator(new Dimension(10, 0));
        this.jToolBar.add(this.searchBox);
    }

    private void removeMember() {
        int result = JOptionPane.showConfirmDialog(
                this.parent,
                """
                        Are you sure to remove?
                                                
                        This operation will also:
                        - remove the consumption records of the account.
                        - remove all family members if it is the main account.
                        """,
                "Remove Warning",
                JOptionPane.YES_NO_CANCEL_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            String[] array = Arrays.stream(this.jTable.getSelectedRows())
                    .map(e -> jTable.convertRowIndexToModel(e))
                    .mapToObj(e -> (String) jTable.getModel().getValueAt(e, 0))
                    .toArray(String[]::new);
            MembershipService.remove(array);
        }
    }

    @Override
    protected void addComponentsToFilterBar() {
        Dimension comboBoxDim = new Dimension(100, 40);

        JComboBox<String> memberTypeComboBox =
                new JComboBox<>(Stream.concat(Stream.of(""),
                                Arrays.stream(Arrays.stream(DefaultDataConstant.DEFAULT_MEMBERS).map(Role::getRoleName).toArray()))
                        .toArray(String[]::new));
        JComboBox<String> genderComboBox =
                new JComboBox<>(Stream.concat(Stream.of(""), Arrays.stream(UIConstant.MEMBER_GENDER_LIST)).toArray(String[]::new));

        JComboBox<String> stateComboBox = new JComboBox<>(new String[]{"", CustomerSateEnum.ACTIVE.getName(), CustomerSateEnum.EXPIRED.getName()});


        MyDatePicker dateOfBirthStart = new MyDatePicker(1940, LocalDate.now().getYear());
        dateOfBirthStart.setMaximumSize(new Dimension(300, 20));
        MyDatePicker dateOfBirthEnd = new MyDatePicker(1940, LocalDate.now().getYear());
        dateOfBirthEnd.setMaximumSize(new Dimension(300, 20));

        MyDatePicker startTime = new MyDatePicker(1940, LocalDate.now().getYear());
        startTime.setMaximumSize(new Dimension(300, 20));
        MyDatePicker endTime = new MyDatePicker(1940, LocalDate.now().getYear());
        endTime.setMaximumSize(new Dimension(300, 20));

        JButton searchBtn = new TableToolButton(UIConstant.TABLE_TOOL_LIST[5][0], MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[5][1]));
        searchBtn.setToolTipText(UIConstant.TABLE_TOOL_LIST[5][0]);
        searchBtn.addActionListener(e -> filterSearchBtnEvent(
                memberTypeComboBox,
                genderComboBox,
                stateComboBox,
                dateOfBirthStart,
                dateOfBirthEnd,
                startTime,
                endTime));

        JButton removeFilterBtn = new TableToolButton("Remove Filter", Color.RED);
        removeFilterBtn.addActionListener(e -> {
            memberTypeComboBox.setSelectedIndex(0);
            genderComboBox.setSelectedIndex(0);
            stateComboBox.setSelectedIndex(0);
            startTime.reset();
            endTime.reset();
            dateOfBirthStart.reset();
            dateOfBirthEnd.reset();

            jTable.setRowSorter(new TableRowSorter<TableModel>(this.tableModel));
        });

        Box firstRowBox = initFilterFirstRow(comboBoxDim, memberTypeComboBox, genderComboBox, stateComboBox);
        Box secondRowBox = initFilterSecondRow(dateOfBirthStart, dateOfBirthEnd);
        Box thirdRowBox = initFilterThirdRow(startTime, endTime, searchBtn, removeFilterBtn);

        this.filterBar.add(firstRowBox);
        this.filterBar.add(Box.createVerticalStrut(10));
        this.filterBar.add(secondRowBox);
        this.filterBar.add(Box.createVerticalStrut(10));
        this.filterBar.add(thirdRowBox);
    }

    private void filterSearchBtnEvent(JComboBox<String> memberTypeComboBox, JComboBox<String> genderComboBox,
                                      JComboBox<String> stateComboBox,
                                      MyDatePicker dateOfBirthStart,
                                      MyDatePicker dateOfBirthEnd,
                                      MyDatePicker startTime,
                                      MyDatePicker endTime) {
        List<RowFilter<Object, Object>> filters = new ArrayList<>(2);
        String genderItem = (String) genderComboBox.getSelectedItem();
        String memberTypeItem = (String) memberTypeComboBox.getSelectedItem();
        String stateItem = (String) stateComboBox.getSelectedItem();

        String dateBirthStart = dateOfBirthStart.getDateText();
        String dateBirthEnd = dateOfBirthEnd.getDateText();
        if (!"".equals(dateBirthStart)
                && !"".equals(dateBirthEnd)
                && DateUtil.isAfter(dateBirthStart, dateBirthEnd)) {
            JOptionPane.showMessageDialog(
                    this.clubFrameView,
                    "Please make sure your 'start time' should be before the 'end time' of Date Of Birth range!",
                    "Warning",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String startTimeDateText = startTime.getDateText();
        String endTimeDateText = endTime.getDateText();
        if (!"".equals(startTimeDateText)
                && !"".equals(endTimeDateText)
                && DateUtil.isAfter(startTimeDateText, endTimeDateText)) {
            JOptionPane.showMessageDialog(
                    this.clubFrameView,
                    "Please make sure your 'start time' should be before the 'end time' of Start Time range!",
                    "Warning",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (!"".equals(genderItem)) {
            filters.add(RowFilter.regexFilter("^(?i)" + genderItem + "$", 4));
        }
        if (!"".equals(memberTypeItem)) {
            filters.add(RowFilter.regexFilter("^(?i)" + memberTypeItem + "$", 7));
        }

        if (!"".equals(stateItem)) {
            filters.add(RowFilter.regexFilter("^(?i)" + stateItem + "$", 12));
        }

        if (!"".equals(dateOfBirthStart.getDateText())) {
            filters.add(
                    RowFilter.dateFilter(RowFilter.ComparisonType.AFTER, dateOfBirthStart.getDate(-1), 3));
        }

        if (!"".equals(dateOfBirthEnd.getDateText())) {
            filters.add(
                    RowFilter.dateFilter(RowFilter.ComparisonType.BEFORE, dateOfBirthEnd.getDate(1), 3));
        }

        if (!"".equals(startTimeDateText)) {
            filters.add(RowFilter.dateFilter(RowFilter.ComparisonType.AFTER, startTime.getDate(-1), 9));
        }

        if (!"".equals(endTimeDateText)) {
            filters.add(RowFilter.dateFilter(RowFilter.ComparisonType.BEFORE, endTime.getDate(1), 9));
        }

        if (filters.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this.clubFrameView,
                    "You must add at least one condition to the filter!",
                    "Warning",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(jTable.getModel());
        sorter.setRowFilter(RowFilter.andFilter(filters));
        jTable.setRowSorter(sorter);
    }

    private Box initFilterFirstRow(
            Dimension comboBoxDim,
            JComboBox<String> memberTypeComboBox,
            JComboBox<String> genderComboBox,
            JComboBox<String> stateComboBox) {
        Box box = getBox("Member Type:", memberTypeComboBox, comboBoxDim);
        Box box1 = getBox("Gender:", genderComboBox, comboBoxDim);
        Box box2 = getBox("State:", stateComboBox, comboBoxDim);

        Box comboBox = Box.createHorizontalBox();
        comboBox.add(box);
        comboBox.add(Box.createHorizontalStrut(10));
        comboBox.add(box1);
        comboBox.add(Box.createHorizontalStrut(10));
        comboBox.add(box2);
        comboBox.add(Box.createHorizontalGlue());
        return comboBox;
    }

    private Box getBox(String label, Component component, Dimension dimension) {
        Box box = Box.createHorizontalBox();
        component.setMaximumSize(dimension);
        box.add(new JLabel(label));
        box.add(component);
        return box;
    }

    private Box initFilterSecondRow(MyDatePicker startTime, MyDatePicker endTime) {
        Box box = Box.createHorizontalBox();
        JLabel label = new JLabel("To");
        label.setFont(new Font(label.getFont().getFontName(), Font.BOLD, label.getFont().getSize()));
        box.add(new JLabel("Date Of Birth:"));
        box.add(startTime);
        box.add(Box.createHorizontalStrut(10));
        box.add(label);
        box.add(Box.createHorizontalStrut(10));
        box.add(endTime);
        box.add(Box.createHorizontalGlue());
        return box;
    }

    private Box initFilterThirdRow(
            MyDatePicker startTime, MyDatePicker endTime, JButton searchBtn, JButton removeFilterBtn) {
        Box box = Box.createHorizontalBox();
        JLabel label = new JLabel("To");
        label.setFont(new Font(label.getFont().getFontName(), Font.BOLD, label.getFont().getSize()));
        box.add(new JLabel("Start time:"));
        box.add(startTime);
        box.add(Box.createHorizontalStrut(10));
        box.add(label);
        box.add(Box.createHorizontalStrut(10));
        box.add(endTime);
        box.add(Box.createHorizontalStrut(10));
        box.add(searchBtn);
        box.add(Box.createHorizontalStrut(10));
        box.add(removeFilterBtn);
        box.add(Box.createHorizontalGlue());
        return box;
    }

    @Override
    protected void onRightClick(MouseEvent e) {
        JTable table = (JTable) e.getSource();
        int[] selectedRows = table.getSelectedRows();

        if (selectedRows.length == 0) {
            return;
        }

        Point point = e.getPoint();
        int row = table.rowAtPoint(point);

        if (Arrays.stream(selectedRows).filter(v -> v == row).findAny().isEmpty()) {
            return;
        }

        if (selectedRows.length == 1) {
            table.setRowSelectionInterval(row, row);
            TablePopMenu popMenu = getSingleTablePopMenu();
            popMenu.show(e.getComponent(), e.getX(), e.getY());

            return;
        }

        TablePopMenu popMenu = getMultiTablePopMenu();
        popMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    private TablePopMenu getSingleTablePopMenu() {
        return new TablePopMenu() {
            @Override
            void create() {
                JMenuItem check = new JMenuItem("Personal Info");
                JMenuItem consumption = new JMenuItem("Consumption");
                JMenuItem role = new JMenuItem("Role Info");
                JMenuItem renew = new JMenuItem("Renew");
                JMenuItem edit = new JMenuItem("Edit");
                JMenuItem delete = new JMenuItem("Remove");

                check.addActionListener(__ -> CheckMemberInfoDialogView.showDig(clubFrameView, (String) jTable.getModel().getValueAt(jTable.convertRowIndexToModel(jTable.getSelectedRow()), 0)));
                edit.addActionListener(__ -> EditMemberDialogView.showDig(clubFrameView, (String) jTable.getModel().getValueAt(jTable.convertRowIndexToModel(jTable.getSelectedRow()), 0)));
                delete.addActionListener(__ -> removeMember());
                role.addActionListener(__ -> {
                    String id = (String) jTable.getModel().getValueAt(jTable.convertRowIndexToModel(jTable.getSelectedRow()), 0);
                    String roleIdById = MembershipService.findRoleIdById(id);
                    CheckRoleDialogView.showDig(clubFrameView, roleIdById);
                });
                consumption.addActionListener(__ -> CheckConsumptionDialogView.showDig(clubFrameView, (String) jTable.getModel().getValueAt(jTable.convertRowIndexToModel(jTable.getSelectedRow()), 0)));
                renew.addActionListener(__ -> RenewMemberDialogView.showDig(clubFrameView, (String) jTable.getModel().getValueAt(jTable.convertRowIndexToModel(jTable.getSelectedRow()), 0)));
                this.add(check);
                this.add(consumption);
                this.add(role);
                this.add(renew);
                this.add(edit);
                this.add(delete);
            }
        };
    }

    private TablePopMenu getMultiTablePopMenu() {
        return new TablePopMenu() {
            @Override
            void create() {
                JMenuItem remove = new JMenuItem("Remove");
                remove.addActionListener(__ -> removeMember());
                this.add(remove);
            }
        };
    }

    @Override
    protected void onDoubleClick(MouseEvent e) {
        JTable table = (JTable) e.getSource();
        if (table.getSelectedRow() != -1) {
            CheckMemberInfoDialogView.showDig(clubFrameView, (String) jTable.getModel().getValueAt(jTable.convertRowIndexToModel(jTable.getSelectedRow()), 0));
        }
    }

    private void insert() {
        SwingUtilities.invokeLater(() -> {
            fetchData();
            Rectangle rect = jTable.getCellRect(0, 0, true);
            jTable.scrollRectToVisible(rect);
        });
    }

    private void fetchData() {
        Component[] jToolBarComponents = jToolBar.getComponents();
        Component editBtn = jToolBarComponents[2];
        Component removeBtn = jToolBarComponents[4];
        Component conBtn = jToolBarComponents[6];
        Component renewBtn = jToolBarComponents[10];
        editBtn.setEnabled(false);
        removeBtn.setEnabled(false);
        conBtn.setEnabled(false);
        renewBtn.setEnabled(false);

        DefaultTableModel model = (DefaultTableModel) this.jTable.getModel();
        model.setDataVector(MembershipService.findMembersForTableRender(), UIConstant.MEMBER_COLUMNS);
        model.fireTableDataChanged();
        this.jTable.setModel(model);
        super.setTableStyle();
        initTable();
    }

    @Override
    public void onDataChange(CustomerDto e, DataManipulateEnum flag) {
        switch (flag) {
            case INSERT -> insert();
            case UPDATE, DELETE -> SwingUtilities.invokeLater(this::fetchData);
        }
    }

    @Override
    public void subscribe(Class<CustomerDto> customerClass) {
        DataSource.subscribe(new DataSourceChannelInfo<>(this, customerClass));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(this::fetchData);
        Logger.info("Membership Table updated");
    }

    private static class DateRender extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (row % 2 == 0)
                setBackground(new Color(213, 213, 213));
            else if (row % 2 == 1)
                setBackground(Color.white);
            Component c =
                    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            JLabel original = (JLabel) c;
            original.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
            original.setText(
                    Optional.ofNullable(value)
                            .filter(Date.class::isInstance)
                            .map(Date.class::cast)
                            .map(DateUtil::format)
                            .orElse(""));
            return original;
        }
    }

    private static class StateRender extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c =
                    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            JLabel original = (JLabel) c;
            if (original.getText().equals(CustomerSateEnum.EXPIRED.getName())) {
                original.setBackground(ColorConstant.PANTONE170C);
            } else {
                original.setBackground(ColorConstant.PANTONE359C);
            }
            original.setForeground(Color.WHITE);
            original.setHorizontalAlignment(CENTER);
            Font font = original.getFont();
            original.setFont(new Font(font.getName(), Font.BOLD, font.getSize()));
            return original;
        }
    }
}
