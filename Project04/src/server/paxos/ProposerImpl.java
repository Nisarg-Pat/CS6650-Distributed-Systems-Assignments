package server.paxos;

import server.CoordinatorServer;
import server.Server;
import server.command.Command;

import java.rmi.RemoteException;

public class ProposerImpl implements Proposer {

    public static int proposalID = 0;

    PaxosServer paxosServer;

    public ProposerImpl(PaxosServer server) {
        this.paxosServer = server;
    }

    @Override
    public Object propose(Command command) {
        proposalID++;
        Proposal proposal = new Proposal(proposalID, command);

        try {
            CoordinatorServer coordinator = paxosServer.getCoordinator();
            for(Server server: coordinator.getAllServers()) {

            }
        } catch (RemoteException e) {

        }


        return false;

    }
}
