package server.paxos;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Learner extends Remote {
    Object learn(Proposal proposal) throws RemoteException;
}
