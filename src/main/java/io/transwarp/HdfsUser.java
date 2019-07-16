package io.transwarp;

import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.AuthorizationRequest;
import org.apache.ftpserver.ftplet.User;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Implemented User to add group persmissions
 */
public class HdfsUser implements User, Serializable {

	private static final long serialVersionUID = -47371353779731294L;

	private String name = null;

	private String password = null;

	private int maxIdleTimeSec = 0; // no limit

	private String homeDir = null;

	private boolean isEnabled = true;

	private ArrayList<String> groups = new ArrayList<String>();

	private Logger log = Logger.getLogger(HdfsUser.class);

	/**
	 * Default constructor.
	 */
	public HdfsUser() {
	}

	/**
	 * Copy constructor.
	 */
	public HdfsUser(User user) {
		name = user.getName();
		password = user.getPassword();
		maxIdleTimeSec = user.getMaxIdleTime();
		homeDir = user.getHomeDirectory();
		isEnabled = user.getEnabled();
	}

	public ArrayList<String> getGroups() {
		return groups;
	}

	/**
	 * Get the main group of the user
	 *
	 * @return main group of the user
	 */
	public String getMainGroup() {
		if (groups.size() > 0) {
			return groups.get(0);
		} else {
			log.error("User " + name + " is not a memer of any group");
			return "error";
		}
	}

	/**
	 * Checks if user is a member of the group
	 *
	 * @param group to check
	 * @return true if the user id a member of the group
	 */
	public boolean isGroupMember(String group) {
		for (String userGroup : groups) {
			if (userGroup.equals(group)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Set users' groups
	 *
	 * @param groups to set
	 */
	public void setGroups(ArrayList<String> groups) {
		if (groups.size() < 1) {
			log.error("User " + name + " is not a memer of any group");
		}
		this.groups = groups;
	}

	/**
	 * Get the user name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set user name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the user password.
	 */
	public String getPassword() {
		return password;
	}

	@Override
	public List<? extends Authority> getAuthorities() {
		return null;
	}

	@Override
	public List<? extends Authority> getAuthorities(Class<? extends Authority> aClass) {
			return null;
	}

	/**
	 * Set user password.
	 */
	public void setPassword(String pass) {
		password = pass;
	}

	/**
	 * Get the maximum idle time in second.
	 */
	public int getMaxIdleTime() {
		return maxIdleTimeSec;
	}

	/**
	 * Set the maximum idle time in second.
	 */
	public void setMaxIdleTime(int idleSec) {
		maxIdleTimeSec = idleSec;
		if (maxIdleTimeSec < 0) {
			maxIdleTimeSec = 0;
		}
	}

	/**
	 * Get the user enable status.
	 */
	public boolean getEnabled() {
		return isEnabled;
	}

	/**
	 * Set the user enable status.
	 */
	public void setEnabled(boolean enb) {
		isEnabled = enb;
	}

	/**
	 * Get the user home directory.
	 */
	public String getHomeDirectory() {
		return homeDir;
	}

	/**
	 * Set the user home directory.
	 */
	public void setHomeDirectory(String home) {
		homeDir = home;
	}

	/**
	 * String representation.
	 */
	public String toString() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	public AuthorizationRequest authorize(AuthorizationRequest request) {
		return null;
	}

}