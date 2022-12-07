package server.paxos;

import server.command.Command;

public class Proposal {
    private final int proposalNumber;
    private final Command command;

    public Proposal(int proposalNumber, Command command) {
        this.proposalNumber = proposalNumber;
        this.command = command;
    }

    public int getProposalNumber() {
        return proposalNumber;
    }

    public Command getCommand() {
        return command;
    }
}
