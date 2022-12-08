package server.paxos;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * A Learner task for Paxos
 */
public interface Learner extends Remote {
    /**
     * Learns the proposal
     *
     * @param proposal The proposal to learn
     * @return The value of the proposal learnt
     * @throws RemoteException
     */
    Object learn(Proposal proposal) throws RemoteException;
}
