package server.paxos;

import java.rmi.RemoteException;

public interface Learner {
    boolean learn(Proposal proposal) throws RemoteException;
}
