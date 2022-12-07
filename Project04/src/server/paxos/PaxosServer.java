package server.paxos;

import server.CoordinatorServer;
import server.KeyValueStore;
import server.Server;
import util.KeyValueDB;

import java.rmi.RemoteException;

public interface PaxosServer extends Server {
    Proposer getProposer() throws RemoteException;
    Acceptor getAcceptor() throws RemoteException;
    Learner getLearner() throws RemoteException;

    KeyValueStore replicate() throws RemoteException;

    CoordinatorServer getCoordinator() throws RemoteException;
}
