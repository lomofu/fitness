/**
 * @author lomofu
 * @desc
 * @create 05/Dec/2021 23:07
 */
public class DataSourceChannelInfo<T> {
    private DataSourceChannel<?> dataSourceChannel;
    private Class<T> tClass;

    public DataSourceChannelInfo(DataSourceChannel<?> dataSourceChannel, Class<T> tClass) {
        this.dataSourceChannel = dataSourceChannel;
        this.tClass = tClass;
    }

    public DataSourceChannel<?> getDataSourceChannel() {
        return dataSourceChannel;
    }

    public Class<T> gettClass() {
        return tClass;
    }
}
