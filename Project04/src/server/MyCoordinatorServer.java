package server;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import server.command.Command;
import server.paxos.*;
import util.Log;

/**
 * Implementation of Coordinator Server. It's function is to store the details of all the servers in the application.
 * Should run before any other server starts
 */
public class MyCoordinatorServer extends UnicastRemoteObject implements CoordinatorServer {

    private final int port;
    private final ServerHeader header;


    Set<Server> serverSet;
    private Server tempServer;
    boolean foundServer;

    private static final int GET_TIMEOUT = 1000;

    protected MyCoordinatorServer(String host) throws RemoteException {
        super();
        this.port = CoordinatorServer.PORT;
        this.header = new ServerHeader(host, this.port);
        serverSet = new HashSet<>();
    }

    @Override
    public void addServer(Server server) throws RemoteException {
        serverSet.add(server);
        Log.logln("Adding Server "+server.getServerHeader()+", Server Set Size: "+serverSet.size());
    }

    @Override
    public List<Server> getAllServers() throws RemoteException {
        //Temporary set of not accessible servers
        //To get accurate list of running servers
        Set<Server> removedSet = new HashSet<>();
        for (Server server : serverSet) {
            foundServer = false;
            Thread lookupTimeOut = new Thread(() -> {
                try {
                    server.getServerHeader();
                    foundServer = true;
                } catch (RemoteException e) {
                    foundServer = false;
                }
            });
            long timeoutTime = System.currentTimeMillis() + GET_TIMEOUT;
            lookupTimeOut.start();
            while (lookupTimeOut.isAlive()) {
                long currentTime = System.currentTimeMillis();
                if (currentTime >= timeoutTime) {
                    lookupTimeOut.interrupt();
                    break;
                }
            }
            if (!foundServer) {
                //If unable to find the server, remove it
                removedSet.add(server);
            }
        }
        for (Server server : removedSet) {
            //Removing the servers
            serverSet.remove(server);
        }
        //Printing the current server size.
//        Log.logln("Current server size: " + serverSet.size());
        return new ArrayList<>(serverSet);
    }

    @Override
    public void start() throws RemoteException {
        try {
            Registry registry = LocateRegistry.createRegistry(port);
            registry.rebind(SERVER_LIST_SERVICE, this);
            Log.logln("CoordinatorServer started at host: " + header.getHost() + ", port: " + header.getPort());
        } catch (Exception e) {
            Log.logln("Error while starting: " + e);
        }
    }

    @Override
    public ServerHeader getServerHeader() throws RemoteException {
        return header;
    }


    public static int proposalID = 0;

    @Override
    public Object propose(Command command) throws RemoteException{
        proposalID++;
        Proposal proposal = new Proposal(proposalID, command);

        int promisedServers = 0;

        int maxProposalNumber = 0;
        Proposal currentProposal = proposal;

        //Proposal Phase
        try {
            Log.logln("Proposing " + proposal);
            List<Server> servers = getAllServers();
            for (Server server : servers) {
                Promise promise = ((Acceptor) server).prepare(proposal);
                if (promise.isPromise()) {
                    promisedServers++;
                    Proposal acceptedProposal = promise.getAcceptedProposal();
                    if (acceptedProposal != null && acceptedProposal.getProposalNumber() > maxProposalNumber) {
                        maxProposalNumber = acceptedProposal.getProposalNumber();
                        currentProposal = new Proposal(proposalID, acceptedProposal.getCommand());
                    }
                }
            }

            if (promisedServers <= servers.size() / 2) {
                return false;
            }

//            int acceptedServers = 0;

            for (Server server : servers) {
                ((Acceptor) server).accept(currentProposal);
//                acceptedServers ++;
            }

            int committed = 0;
            for (Server server : servers) {
                boolean response = ((Learner) server).learn(currentProposal);
                if (response) {
                    committed++;
                }
            }

            if (committed == servers.size()) {
                return true;
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }


        return false;

    }
}
