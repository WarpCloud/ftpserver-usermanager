package io.transwarp;

/**
 * Created by qls on 18-10-26.
 */
public class Constants {

    //hdfs
    static String HDFS_KERBEROS_PRINCIPAL = System.getenv("HDFS_KERBEROS_PRINCIPAL");
    static String DEFAULT_HDFS_KEYTAB_FILE = "/etc/keytabs/keytab";
    static String DEFAULT_HDFS_SITE_FILE = "/etc/hadoop/conf/hdfs-site.xml";
    static String DEFAULT_HDFS_CORE_SITE_FILE = "/etc/hadoop/conf/core-site.xml";

    //guardian
    static String GUARDIAN_SERVER_ADDRESS = "https://172.26.5.93:8380";
    static String GUARDIAN_CONNECTION_USERNAME = "admin";
    static String GUARDIAN_CONNECTION_PASSWORD = "123";

}
