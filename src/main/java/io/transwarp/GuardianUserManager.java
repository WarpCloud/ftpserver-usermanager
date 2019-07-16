package io.transwarp;

import io.transwarp.guardian.client.GuardianAdmin;
import io.transwarp.guardian.client.GuardianAdminFactory;
import io.transwarp.guardian.client.GuardianClient;
import io.transwarp.guardian.client.GuardianClientFactory;
import io.transwarp.guardian.common.conf.GuardianConfiguration;
import io.transwarp.guardian.common.exception.GuardianClientException;
import org.apache.ftpserver.ftplet.Authentication;
import org.apache.ftpserver.ftplet.AuthenticationFailedException;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.usermanager.AnonymousAuthentication;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;
import org.apache.ftpserver.usermanager.impl.AbstractUserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public final class GuardianUserManager extends AbstractUserManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuardianUserManager.class);

    private GuardianAdmin guardianAdmin;
    private GuardianClient guardianClient;

    private static String GUARDIAN_SERVER_ADDRESS = Constants.GUARDIAN_SERVER_ADDRESS;
    private static String GUARDIAN_CONNECTION_USERNAME = Constants.GUARDIAN_CONNECTION_USERNAME;
    private static String GUARDIAN_CONNECTION_PASSWORD = Constants.GUARDIAN_CONNECTION_PASSWORD;

    private HashMap userDataProp = new HashMap<String, User>();

    public GuardianAdmin getGuardianAdmin() {
        return guardianAdmin;
    }

    public GuardianClient getGuardianClient() {
        return guardianClient;
    }

    public void setGuardianAdmin(GuardianAdmin guardianAdmin) {
        this.guardianAdmin = guardianAdmin;
    }

    public GuardianUserManager() {

        System.out.println("===== GUARDIAN_SERVER_ADDRESS: " + GUARDIAN_SERVER_ADDRESS);
        System.out.println("===== GUARDIAN_CONNECTION_USERNAME: " + GUARDIAN_CONNECTION_USERNAME);
        System.out.println("===== GUARDIAN_CONNECTION_PASSWORD: " + GUARDIAN_CONNECTION_PASSWORD);

        GuardianConfiguration guardianConfiguration = new GuardianConfiguration();
        guardianConfiguration.set("guardian.server.address", GUARDIAN_SERVER_ADDRESS);
        guardianConfiguration.set("guardian.connection.username", GUARDIAN_CONNECTION_USERNAME);
        guardianConfiguration.set("guardian.connection.password", GUARDIAN_CONNECTION_PASSWORD);
        guardianConfiguration.set("guardian.connection.client.impl", "REST");
        guardianConfiguration.set("guardian.client.cache.enabled", "true");

        try {
            guardianAdmin = GuardianAdminFactory.getInstance(guardianConfiguration);
            guardianClient = GuardianClientFactory.getInstance(guardianConfiguration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getUserByName(String name) throws FtpException {
//        if(this.userDataProp.containsKey(name)){
//            return (User)this.userDataProp.get(name);
//        }
        return null;
    }

    @Override
    public String[] getAllUserNames() throws FtpException {

        List<String> userList = new ArrayList<String>();

        Iterator iter = this.userDataProp.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            userList.add((String) key);
        }

        if(userList !=null && userList.size()>0){
           return userList.toArray(new String[userList.size()]);
        }else{
            return new String[0];
        }

    }

    @Override
    public void delete(String name) throws FtpException {

        if (name != null && this.userDataProp.containsKey(name)) {
            this.userDataProp.remove(name);
        }
    }

    @Override
    public void save(User user) throws FtpException {
        if (user.getName() == null) {
            throw new NullPointerException("User name is null.");
        } else {
            this.userDataProp.put(user.getName(), user);
        }
    }

    @Override
    public boolean doesExist(String name) throws FtpException {

        return this.userDataProp.containsKey(name);
    }

    @Override
    public User authenticate(Authentication authentication) throws AuthenticationFailedException {

        System.out.println("======================");
        if (authentication instanceof UsernamePasswordAuthentication) {
            UsernamePasswordAuthentication upauth = (UsernamePasswordAuthentication) authentication;
            String user = upauth.getUsername();
            String password = upauth.getPassword();
            if (user == null || password == null) {
                throw new AuthenticationFailedException("Authentication failed");
            }

            boolean isGuardianUser;

            try {
                //new client
                isGuardianUser = this.guardianClient.authenticate(user, password);
                if(! isGuardianUser){
                    throw new AuthenticationFailedException("Authentication failed");
                }
            } catch (GuardianClientException e) {
                throw new AuthenticationFailedException("Authentication failed");
            }

            try {
                saveUser(user,password);
            } catch (FtpException e) {
                e.printStackTrace();
            }

//            User var10;
//            try {
//                var10 = this.getUserByName(user);
//            } catch (FtpException var19) {
//                throw new AuthenticationFailedException("Authentication failed", var19);
//            }

            HdfsUser hdfsUser = new HdfsUser();
            hdfsUser.setEnabled(true);
            hdfsUser.setName(user);
            hdfsUser.setPassword(password);
            hdfsUser.setMaxIdleTime(0);
            hdfsUser.setHomeDirectory("/tmp");

            return (User)hdfsUser;

        } else if (authentication instanceof AnonymousAuthentication) {
            try {
                if (this.doesExist("anonymous")) {
                    return this.getUserByName("anonymous");
                } else {
                    throw new AuthenticationFailedException("Authentication failed");
                }
            } catch (AuthenticationFailedException var17) {
                throw var17;
            } catch (FtpException var18) {
                throw new AuthenticationFailedException("Authentication failed", var18);
            }
        } else {
            throw new IllegalArgumentException("Authentication not supported by this user manager");
        }
    }

    private synchronized void saveUser(String name, String password)  throws FtpException {

        HdfsUser user = new HdfsUser();
        user.setEnabled(true);
        user.setName(name);
        user.setPassword(password);
        user.setMaxIdleTime(0);
        user.setHomeDirectory("/tmp");

        this.userDataProp.put(name, (User)user);

    }

}
