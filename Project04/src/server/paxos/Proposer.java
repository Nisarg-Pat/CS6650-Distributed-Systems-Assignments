package server.paxos;

public interface Proposer {

    // will be the coordinator of the paxos algorithm

    Object propose();
}
