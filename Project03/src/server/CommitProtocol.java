package server;

public interface CommitProtocol {

  boolean canCommit(Transaction transaction);

  boolean doCommit(Transaction transaction);

  boolean doAbort(Transaction transaction);

  boolean haveCommited(Transaction transaction);

  boolean getDecision(Transaction transaction);

  boolean performTransaction(Transaction transaction);
}
