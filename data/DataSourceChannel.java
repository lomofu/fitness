package data;

import constant.DataManipulateEnum;

/**
 * @author lomofu
 * <p>
 * This interface defined the observer behavior functions of the observer pattern
 * T: The type of object to be observed needs to be passed into the generic form
 */
public interface DataSourceChannel<T> {
    /**
     * This callback function will be invoked
     * when the data source broadcast and observer subscribe the object they want to observe
     *
     * @param t    The type of object to be observed needs to be passed into the generic form
     * @param flag action see@DataManipulateEnum
     */
    void onDataChange(T t, DataManipulateEnum flag);

    /**
     * The observer should subscribe the data source when they need observe the data change
     *
     * @param tClass Type.class
     */
    void subscribe(Class<T> tClass);
}

