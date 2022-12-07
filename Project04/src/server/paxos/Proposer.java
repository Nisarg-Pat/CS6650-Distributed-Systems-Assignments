package server.paxos;

import server.command.Command;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Proposer extends Remote {

    // will be the coordinator of the paxos algorithm

    Object propose(Command command) throws RemoteException;
}
