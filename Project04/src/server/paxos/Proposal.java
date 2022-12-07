package server.paxos;

import server.command.Command;

import java.io.Serializable;

public class Proposal implements Serializable {
    private final int proposalNumber;
    private final Command command;

    public Proposal(int proposalNumber, Command command) {
        this.proposalNumber = proposalNumber;
        this.command = command;
    }

    public Proposal(Proposal proposal) {
        this.proposalNumber = proposal.getProposalNumber();
        this.command = proposal.getCommand();
    }

    public int getProposalNumber() {
        return proposalNumber;
    }

    public Command getCommand() {
        return command;
    }

    @Override
    public String toString() {
        return "Proposal: ("+proposalNumber+", "+command+")";
    }
}
