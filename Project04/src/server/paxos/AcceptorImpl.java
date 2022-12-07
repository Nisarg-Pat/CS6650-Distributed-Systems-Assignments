package server.paxos;

import java.io.Serializable;
import java.rmi.RemoteException;

public class AcceptorImpl implements Acceptor, Serializable {

    PaxosServer paxosServer;
    int minProposal;
    Proposal acceptedProposal;

    public AcceptorImpl(PaxosServer server) {
        this.paxosServer = server;
        minProposal = -1;
        acceptedProposal = null;
    }

    @Override
    public Promise prepare(Proposal proposal) throws RemoteException {
        if(proposal.getProposalNumber() > minProposal) {
            minProposal = proposal.getProposalNumber();
            paxosServer.log("Got Preparation request for "+proposal+": Promised");
            return new Promise(true, acceptedProposal);
        } else {
            paxosServer.log("Got Preparation request for "+proposal+": Denied");
            return new Promise(false, null);
        }
    }

    @Override
    public int accept(Proposal proposal) throws RemoteException {
        if(proposal.getProposalNumber() >= minProposal) {
            minProposal = proposal.getProposalNumber();
            acceptedProposal = new Proposal(proposal);
            paxosServer.log("Got Accept request for "+proposal+": Accepted");
//            acceptedProposal.getCommand().execute(store);
        } else {
            paxosServer.log("Got Accept request for "+proposal+": Rejected");
        }
        return minProposal;
    }
}
