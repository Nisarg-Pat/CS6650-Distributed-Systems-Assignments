package server.paxos;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface for acceptor task of Paxos
 */
public interface Acceptor extends Remote {
    /**
     * Prepares a given proposal to promise
     *
     * @param proposal Proposal that Proposer proposed
     * @return A promise that it will not accept a proposal number smaller than this
     * @throws RemoteException
     */
    Promise prepare(Proposal proposal) throws RemoteException;

    /**
     * Accepts the given proposal
     *
     * @param proposal The proposal to accept
     * @return The min number of promised or accepted proposal
     * @throws RemoteException
     */
    Integer accept(Proposal proposal) throws RemoteException;
}
