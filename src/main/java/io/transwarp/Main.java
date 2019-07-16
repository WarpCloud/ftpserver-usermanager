package io.transwarp;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;

public class Main {

    public static void main(String[] args) throws FtpException {

        ListenerFactory listenerFactory = new ListenerFactory();
        listenerFactory.setPort(8021);

        FtpServerFactory serverFactory = new FtpServerFactory();
        serverFactory.addListener("default", listenerFactory.createListener());

        serverFactory.setUserManager(new GuardianUserManager());
//        serverFactory.setFileSystem();

        FtpServer server = serverFactory.createServer();
        server.start();
    }
}

