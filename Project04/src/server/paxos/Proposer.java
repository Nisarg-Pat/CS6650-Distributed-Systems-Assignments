package server.paxos;

import server.command.Command;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * A Proposer task of the Paxos
 */
public interface Proposer extends Remote {

    /**
     * Proposes a Command and carries the Paxos algorithm
     *
     * @param command The command to propose
     * @return The return value of the coomand if executed
     * @throws RemoteException
     */
    Object propose(Command command) throws RemoteException;
}
