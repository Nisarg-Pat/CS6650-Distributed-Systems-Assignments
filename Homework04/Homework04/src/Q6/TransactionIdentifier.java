package Q6;

import javax.transaction.xa.Xid;

public class TransactionIdentifier implements Xid {

    private final int formatId;
    private final byte[] globalTransactionId;
    private final byte[] branchQualifier;

    public TransactionIdentifier(int formatId, byte[] globalTransactionId, byte[] branchQualifier) {
        this.formatId = formatId;
        this.globalTransactionId = globalTransactionId;
        this.branchQualifier = branchQualifier;
    }

    @Override
    public int getFormatId() {
        return formatId;
    }

    @Override
    public byte[] getGlobalTransactionId() {
        return globalTransactionId;
    }

    @Override
    public byte[] getBranchQualifier() {
        return branchQualifier;
    }
}
