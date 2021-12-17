/**
 * @author Jiaqi Fu
 * <p>
 * This class wraps the parameters required for a subscription
 * The most important things to know is T.class
 */
public class DataSourceChannelInfo<T> {
    private DataSourceChannel<?> dataSourceChannel;
    private Class<T> tClass;

    public DataSourceChannelInfo(DataSourceChannel<?> dataSourceChannel, Class<T> tClass) {
        this.dataSourceChannel = dataSourceChannel;
        this.tClass = tClass;
    }

    //getter
    public DataSourceChannel<?> getDataSourceChannel() {
        return dataSourceChannel;
    }

    public Class<T> gettClass() {
        return tClass;
    }
}
