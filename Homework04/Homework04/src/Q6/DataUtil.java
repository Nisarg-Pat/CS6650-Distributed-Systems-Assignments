package Q6;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DataUtil {
    public static DataSource getDataSource()
            throws SQLException
    {
        SQLServerDataSource xaDS = new SQLServerDataSource();
//        xaDS.setDataSourceName("SQLServer");
        xaDS.setServerName("localhost");
        xaDS.setPortNumber(5432);
        xaDS.setPassword("1807");
        xaDS.setSelectMethod("cursor");
        return xaDS;
    }
}
