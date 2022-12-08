package server.paxos;

import java.io.Serializable;

/**
 * A representation of a Promise sent by Acceptor to Proposer during Paxos.
 */
public class Promise implements Serializable {
    private final boolean promise;
    private final Proposal acceptedProposal;

    public Promise(boolean promise, Proposal acceptedProposal) {
        this.promise = promise;
        this.acceptedProposal = acceptedProposal;
    }

    public boolean isPromise() {
        return promise;
    }

    public Proposal getAcceptedProposal() {
        return acceptedProposal;
    }
}
