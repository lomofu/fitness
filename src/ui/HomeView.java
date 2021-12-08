package ui;

import bean.DataSourceChannelInfo;
import component.MyCountCard;
import component.MyLabel;
import component.MyPanel;
import data.DataSource;
import data.DataSourceChannel;
import dto.CustomerDto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

/**
 * @author lomofu
 * @desc
 * @create 23/Nov/2021 10:48
 */
public class HomeView extends MyPanel implements DataSourceChannel<CustomerDto> {
  private final MyCountCard countCard;
  private final MyCountCard countCard1;
  private final JTabbedPane jTabbedPane;

  public HomeView(FlowLayout flowLayout, int frameWidth, int frameHeight, JTabbedPane tabbedPane) {
    super(flowLayout);
    this.subscribe(CustomerDto.class);

    this.jTabbedPane = tabbedPane;
    var panelWidth = (int) (frameWidth * 0.8);
    var cardWidth = (int) (panelWidth * 0.3);
    var cardHeight = (int) (frameHeight * 0.3);

    initHomeViewPanel(frameHeight, panelWidth);

    MyLabel label = initAdminTitle(panelWidth);

    countCard =
        initCountCard(
            "User Statistics",
            DataSource.getCustomerList().size(),
            cardWidth,
            cardHeight,
            e -> this.jTabbedPane.setSelectedIndex(1));

    countCard1 =
        initCountCard(
            "New Members Today",
            DataSource.getCustomerList().size(),
            cardWidth,
            cardHeight,
            e -> this.jTabbedPane.setSelectedIndex(1));

    this.add(label);
    this.add(countCard);
    this.add(countCard1);
  }

  private void initHomeViewPanel(int frameHeight, int panelWidth) {
    this.setSize(panelWidth, frameHeight);
    this.setMargin(20, 10, 0, 0);
  }

  private MyLabel initAdminTitle(int panelWidth) {
    MyLabel label = new MyLabel("Hello, Admin", JLabel.RIGHT);
    label.setPreferredSize(new Dimension(panelWidth, 10));
    label.setMargin(0, 0, 0, 20);
    return label;
  }

  private MyCountCard initCountCard(
      String title, int number, int cardWidth, int cardHeight, Consumer<MouseEvent> consumer) {
    MyCountCard countCard = new MyCountCard(title, cardWidth, cardHeight, consumer);
    countCard.setCount(number);
    countCard.addVetoableChangeListener(e -> this.jTabbedPane.setSelectedIndex(1));
    return countCard;
  }

  @Override
  public void onDataChange(CustomerDto customer) {
    countCard.setCount(DataSource.getCustomerList().size());
    countCard1.setCount(DataSource.getCustomerList().size());
  }

  @Override
  public void subscribe(Class<CustomerDto> customerClass) {
    DataSource.subscribe(new DataSourceChannelInfo<>(this, customerClass));
  }
}
