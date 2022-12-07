package server.paxos;

import server.command.Command;

public interface Proposer {

    // will be the coordinator of the paxos algorithm

    Object propose(Command command);
}
