package data;

/**
 * @author lomofu
 * @desc
 * @create 24/Nov/2021 02:58
 */
public interface DataSourceChannel<T> {
  void onDataChange(T t);

  void subscribe(Class<T> tClass);
}

interface BiDataSourceChannel<T, U> extends DataSourceChannel<T> {
  void subscribe(Class<T> tClass, Class<U> uClass);

  void onDataChange(T t, U e);
}
