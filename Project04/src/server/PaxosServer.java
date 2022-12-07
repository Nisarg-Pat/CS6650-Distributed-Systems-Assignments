package server;

import server.CoordinatorServer;
import server.KeyValueStore;
import server.Server;
import server.paxos.Acceptor;
import server.paxos.Learner;
import server.paxos.Proposer;
import util.KeyValueDB;

import java.rmi.RemoteException;

public interface PaxosServer extends Server, Acceptor, Learner {

    KeyValueStore replicate() throws RemoteException;

    CoordinatorServer getCoordinator() throws RemoteException;
}
