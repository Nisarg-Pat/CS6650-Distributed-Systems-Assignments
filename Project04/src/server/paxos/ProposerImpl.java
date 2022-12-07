package server.paxos;

import server.CoordinatorServer;
import server.Server;
import server.command.Command;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

public class ProposerImpl implements Proposer, Serializable {

    public static int proposalID = 0;

    PaxosServer paxosServer;

    public ProposerImpl(PaxosServer server) {
        this.paxosServer = server;
    }

    @Override
    public Object propose(Command command) {
        proposalID++;
        Proposal proposal = new Proposal(proposalID, command);

        int promisedServers = 0;

        int maxProposalNumber = 0;
        Proposal currentProposal  = proposal;

        //Proposal Phase
        try {
            paxosServer.log("Proposing "+proposal);
            List<Server> servers = paxosServer.getCoordinator().getAllServers();
            for(Server server: servers) {
                Promise promise = ((PaxosServer)server).getAcceptor().prepare(proposal);
                if(promise.isPromise()) {
                    promisedServers++;
                    Proposal acceptedProposal = promise.getAcceptedProposal();
                    if(acceptedProposal!=null && acceptedProposal.getProposalNumber() > maxProposalNumber) {
                        maxProposalNumber = acceptedProposal.getProposalNumber();
                        currentProposal = new Proposal(proposalID, acceptedProposal.getCommand());
                    }
                }
            }

            if(promisedServers <= servers.size()/2) {
                return false;
            }

//            int acceptedServers = 0;

            for(Server server: servers) {
                ((PaxosServer)server).getAcceptor().accept(currentProposal);
//                acceptedServers ++;
            }

            int committed = 0;
            for(Server server: servers) {
                boolean response = ((PaxosServer)server).getLearner().learn(currentProposal);
                if(response) {
                    committed++;
                }
            }

            if(committed == servers.size()) {
                return true;
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }


        return false;

    }
}
