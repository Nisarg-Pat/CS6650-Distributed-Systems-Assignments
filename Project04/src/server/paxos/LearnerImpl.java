package server.paxos;

import server.KeyValueStore;
import server.command.Command;
import util.KeyValueDB;

import java.io.Serializable;
import java.rmi.RemoteException;

public class LearnerImpl implements Learner, Serializable {

    KeyValueStore store;
    PaxosServer paxosServer;

    public LearnerImpl(PaxosServer server, KeyValueStore store) {
        this.paxosServer = server;
        this.store = store;
    }

    @Override
    public boolean learn(Proposal proposal) throws RemoteException {
        Command command = proposal.getCommand();
        paxosServer.log("Got Learn request for "+proposal+": Learned");
        return (boolean)command.execute(store);
    }
}
