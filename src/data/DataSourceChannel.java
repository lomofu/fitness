package data;

import bean.Customer;

/**
 * @author lomofu
 * @desc
 * @create 24/Nov/2021 02:58
 */
public interface DataSourceChannel {
  void onDataChange(Customer customer);

  void subscribe();
}
