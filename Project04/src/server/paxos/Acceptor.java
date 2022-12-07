package server.paxos;

import java.rmi.RemoteException;

public interface Acceptor {
    Promise prepare(Proposal proposal) throws RemoteException;

    int accept(Proposal proposal) throws RemoteException;
}
