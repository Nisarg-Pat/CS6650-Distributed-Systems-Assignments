package server.paxos;

import server.command.Command;

import java.rmi.RemoteException;

public interface Proposer {

    // will be the coordinator of the paxos algorithm

    Object propose(Command command) throws RemoteException;
}
