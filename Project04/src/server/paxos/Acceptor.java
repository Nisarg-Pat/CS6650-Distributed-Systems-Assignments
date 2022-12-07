package server.paxos;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Acceptor extends Remote {
    Promise prepare(Proposal proposal) throws RemoteException;

    int accept(Proposal proposal) throws RemoteException;
}
