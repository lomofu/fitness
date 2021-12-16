/**
 * @author lomofu
 * @desc
 * @create 24/Nov/2021 02:58
 */
public interface DataSourceChannel<T> {
  void onDataChange(T t, DataManipulateEnum flag);

  void subscribe(Class<T> tClass);
}

