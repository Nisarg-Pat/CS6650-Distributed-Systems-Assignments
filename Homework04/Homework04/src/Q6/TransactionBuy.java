package Q6;

import javax.sql.XAConnection;
import javax.sql.XADataSource;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static Q6.DataUtil.getDataSource;

public class TransactionBuy {

    public static void Transaction_Buy(int user_id, int item_id, int quantity) throws SQLException {
        XADataSource xaDS;
        XAConnection xaCon;
        XAResource xaRes;
        Xid xid;
        Connection con;
        Statement stmt;
        int ret;

        xaDS = (XADataSource) getDataSource();
        xaCon = xaDS.getXAConnection("jdbc_user", "jdbc_password");
        xaRes = xaCon.getXAResource();
        con = xaCon.getConnection();
        stmt = con.createStatement();
        xid = new TransactionIdentifier(100, new byte[]{0x01}, new byte[]{0x02});
        try {
            xaRes.setTransactionTimeout(10);
            xaRes.start(xid, XAResource.TMNOFLAGS);
            stmt.executeUpdate(String.format("UPDATE INVENTORY SET quantity = %d WHERE item_id = %d", quantity, item_id));
            stmt.executeUpdate(String.format("INSERT INTO USER VALUES (%d, %d)", item_id, quantity));
            xaRes.end(xid, XAResource.TMSUCCESS);
            ret = xaRes.prepare(xid);
            if (ret == XAResource.XA_OK) {
                xaRes.commit(xid, false);
            }
        }
        catch (XAException e) {
            e.printStackTrace();
        } finally {
            stmt.close();
            con.close();
            xaCon.close();
        }
    }
}
