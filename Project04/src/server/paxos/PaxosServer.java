package server.paxos;

import server.Server;

public interface PaxosServer extends Server {
    Proposer getProposer();
    Acceptor getAcceptor();
    Learner getLearner();
}
