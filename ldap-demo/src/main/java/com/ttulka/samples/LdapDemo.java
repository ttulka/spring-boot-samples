package com.ttulka.samples;

import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.util.StringUtils;

@SpringBootApplication
public class LdapDemo {

    public static void main(String[] args) {
        if (args.length < 6) {
            System.out.println("Parameters: <server-url> <manager-user> <manager-password> <referral> <search-base> <search-filter> <username> <password>");
            System.exit(-1);
        }
        if (checkConnection(args[0], args[1], args[2], args[3], args[4])) {
            System.out.println("************** Connection OK");
        } else {
            System.out.println("************** Connection FAILED");
        }
        if (checkUsers(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7])) {
            System.out.println("************** Users OK");
        } else {
            System.out.println("************** Users FAILED");
        }
        System.exit(0);
    }

    static boolean checkConnection(String serverUrl, String managerDnUser, String managerPassword, String referral,
                                   String searchBase) {
        try {
            ContextSource contextSource = createLdapContextSource(serverUrl, managerDnUser, managerPassword, referral);

            LdapTemplate ldapTemplate = new LdapTemplate(contextSource);
            System.out.println("USERS: " + ldapTemplate.list(searchBase));
            return true;

        } catch (NamingException e) {
            System.err.println("Exception by checking a connection." + e);
            e.printStackTrace();
            return false;
        }
    }

    static boolean checkUsers(String serverUrl, String managerDnUser, String managerPassword, String referral,
                              String searchBase, String searchFilter,
                              String user, String pass) {
        try {
            LdapContextSource contextSource = createLdapContextSource(serverUrl, managerDnUser, managerPassword, referral);

            BindAuthenticator authenticator = new BindAuthenticator(contextSource);
            FilterBasedLdapUserSearch userSearch = new FilterBasedLdapUserSearch(
                    searchBase, searchFilter, contextSource);
            userSearch.setSearchSubtree(true);
            authenticator.setUserSearch(userSearch);

            authenticator.authenticate(new UsernamePasswordAuthenticationToken(user, pass));
            return true;

        } catch (Exception e) {
            System.err.println("Exception by retrieving user." + e);
            e.printStackTrace();
            return false;
        }
    }

    static private LdapContextSource createLdapContextSource(String serverUrl, String managerDnUser, String managerPassword, String referral) {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(serverUrl);
        contextSource.setUserDn(managerDnUser);
        contextSource.setPassword(managerPassword);

        Map<String, Object> env = new HashMap<String, Object>();
        env.put("com.sun.jndi.ldap.connect.timeout", "300");
        env.put("com.sun.jndi.ldap.read.timeout", "300");

        if (StringUtils.hasLength(referral)) {
            env.put(Context.REFERRAL, referral);
            contextSource.setReferral(referral);
        }
        contextSource.setBaseEnvironmentProperties(env);
        contextSource.afterPropertiesSet();

        return contextSource;
    }
}
