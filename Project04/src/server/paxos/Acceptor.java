package server.paxos;

public interface Acceptor {
    Promise promise(Proposal proposal);

}
