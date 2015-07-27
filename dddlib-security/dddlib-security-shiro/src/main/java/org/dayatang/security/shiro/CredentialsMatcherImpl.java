package org.dayatang.security.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

import javax.inject.Inject;

/**
 * Created by yyang on 14/11/11.
 */
public class CredentialsMatcherImpl implements CredentialsMatcher {

    private PasswordEncoder encoder;

    @Inject
    public CredentialsMatcherImpl(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        PasswordEncoder passwordEncoder = this.ensurePasswordEncoder();
        String submittedPassword = this.getSubmittedPassword(usernamePasswordToken);
        Object storedCredentials = this.getStoredPassword(info);
        this.assertStoredCredentialsType(storedCredentials);
        String formatted = (String) storedCredentials;
        return passwordEncoder.encodePassword(submittedPassword).equals(formatted);
    }

    private PasswordEncoder ensurePasswordEncoder() {
        if (encoder == null) {
            String msg = "Required PasswordService has not been configured.";
            throw new IllegalStateException(msg);
        } else {
            return encoder;
        }
    }

    protected String getSubmittedPassword(UsernamePasswordToken token) {
        return token != null ? new String(token.getPassword()) : null;
    }

    private void assertStoredCredentialsType(Object credentials) {
        if (!(credentials instanceof String)) {
            String msg = "Stored account credentials are expected to be either a formatted hash String.";
            throw new IllegalArgumentException(msg);
        }
    }

    protected Object getStoredPassword(AuthenticationInfo storedAccountInfo) {
        return storedAccountInfo != null ? storedAccountInfo.getCredentials() : null;
    }

}
