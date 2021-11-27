package data;

import bean.Customer;
import utils.CSVUtil;
import utils.UUIDUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

/**
 * @author lomofu
 * @desc
 * @create 23/Nov/2021 11:19
 */
public class DataSource {
  private static final BlockingQueue<DataSourceChannel> queue = new LinkedBlockingQueue<>();
  private static List<Customer> data = new ArrayList<>();

  public static void init() throws IOException {
    data =
        CSVUtil.read("code/customerlist.csv", Customer.class).stream()
            .peek(
                e -> {
                  if ("".equals(e.getId())) {
                    e.setId(UUIDUtil.generate());
                  }
                })
            .collect(Collectors.toList());
  }

  public static List<Customer> getData() {
    return data;
  }

  public static void subscribe(DataSourceChannel channel) {
    if (!queue.offer(channel)) throw new RuntimeException("Cannot subscribe datasource");
  }

  public static void add(Customer customer) {
    data.add(customer);
    save();
    broadcast(customer);
  }

  public static void remove(Customer customer) {
    save();
    broadcast(customer);
  }

  public static void update(Customer customer) {
    Customer updated =
        data.stream()
            .filter(e -> e.getId().equals(customer.getId()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Cannot find customer info to update"));

    save();
    broadcast(customer);
  }

  private static void broadcast(Customer customer) {
    queue.forEach(e -> e.onDataChange(customer));
  }

  private static void save() {}
}
