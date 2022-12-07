package server.paxos;

public class Promise {
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
