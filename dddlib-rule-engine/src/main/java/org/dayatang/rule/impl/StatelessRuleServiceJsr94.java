package org.dayatang.rule.impl;

import org.dayatang.rule.RuleRuntimeException;
import org.dayatang.rule.StatelessRuleService;
import org.dayatang.rule.UnSupportedRuleFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.rules.InvalidRuleSessionException;
import javax.rules.RuleRuntime;
import javax.rules.RuleServiceProvider;
import javax.rules.StatelessRuleSession;
import javax.rules.admin.LocalRuleExecutionSetProvider;
import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetCreateException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/**
 * 无状态规则服务的实现类，用JSR94实现。
 *
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>) <a href="mailto:gdyangyu@gmail.com">杨宇</a>
 */
@SuppressWarnings("rawtypes")
public class StatelessRuleServiceJsr94 implements StatelessRuleService {

    private static final long serialVersionUID = -6550908446842944288L;

    private static Logger LOGGER = LoggerFactory.getLogger(StatelessRuleServiceJsr94.class);
    private StatelessRuleSession session;

    private StatelessRuleServiceJsr94(RuleServiceProvider ruleServiceProvider, Map serviceProviderProperties,
                                     Object ruleSource, Map executionSetProperties, Map sessionProperties) {
        //this.ruleServiceProvider = ruleServiceProvider;
        //this.serviceProviderProperties = serviceProviderProperties;
        //this.ruleSource = ruleSource;
        //this.executionSetProperties = executionSetProperties;
        //this.sessionProperties = sessionProperties;
        LOGGER.info("The rule service provider of JSR94 is " + ruleServiceProvider.getClass());

        try {
            RuleAdministrator ruleAdministrator = ruleServiceProvider.getRuleAdministrator();
            LocalRuleExecutionSetProvider ruleExecutionSetProvider = ruleAdministrator.getLocalRuleExecutionSetProvider(serviceProviderProperties);
            RuleRuntime ruleRuntime = ruleServiceProvider.getRuleRuntime();
            RuleExecutionSet ruleExecutionSet = createRuleExecutionSet(ruleExecutionSetProvider, ruleSource, executionSetProperties);
            String packageName = ruleExecutionSet.getName();
            ruleAdministrator.registerRuleExecutionSet(packageName, ruleExecutionSet, null);
            session = (StatelessRuleSession) ruleRuntime.createRuleSession(packageName, sessionProperties, RuleRuntime.STATELESS_SESSION_TYPE);
        } catch (Exception e) {
            throw new RuleRuntimeException(e);
        }
        
    }

    private RuleExecutionSet createRuleExecutionSet(LocalRuleExecutionSetProvider ruleExecutionSetProvider,
                                                    Object ruleSource, Map executionSetProperties) {
        try {
            if (ruleSource instanceof String) {
                Reader reader = new StringReader((String) ruleSource);
                return ruleExecutionSetProvider.createRuleExecutionSet(reader, executionSetProperties);
            }
            if (ruleSource instanceof Reader) {
                Reader reader = (Reader) ruleSource;
                return ruleExecutionSetProvider.createRuleExecutionSet(reader, executionSetProperties);
            }
            if (ruleSource instanceof InputStream) {
                InputStream in = (InputStream) ruleSource;
                return ruleExecutionSetProvider.createRuleExecutionSet(in, executionSetProperties);
            }
            return ruleExecutionSetProvider.createRuleExecutionSet(ruleSource, executionSetProperties);

        } catch (RuleExecutionSetCreateException e) {
            throw new UnSupportedRuleFormatException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List executeRules(List facts) {
        try {
            return session.executeRules(facts);
        } catch (InvalidRuleSessionException e) {
            throw new RuleRuntimeException("Invalid Rule Session.", e);
        } catch (RemoteException e) {
            throw new IllegalStateException(e);
        }
    }

    public void release() {
        try {
            session.release();
        } catch (Exception e) {
            throw new RuleRuntimeException("Cannot release rule session!!", e);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private RuleServiceProvider ruleServiceProvider;
        private Map serviceProviderProperties;
        private Map executionSetProperties;
        private Map sessionProperties;
        private Object ruleSource;

        /**
         * 设置规则服务提供者，例如Drools
         * @param ruleServiceProvider 要设置的规则服务提供者
         * @return
         */
        public Builder ruleServiceProvider(RuleServiceProvider ruleServiceProvider) {
            this.ruleServiceProvider = ruleServiceProvider;
            return this;
        }

        /**
         * 设置规则服务提供者特定的属性
         * @param serviceProviderProperties 要设置的规则服务提供者属性
         * @return
         */
        public Builder serviceProviderProperties(Map serviceProviderProperties) {
            this.serviceProviderProperties = serviceProviderProperties;
            return this;
        }

        /**
         * 设置规则执行集属性(如：source=drl/xml dsl=java.io.Reader)
         * @param executionSetProperties 要设置的规则执行集属性
         * @return
         */
        public Builder executionSetProperties(Map executionSetProperties) {
            this.executionSetProperties = executionSetProperties;
            return this;
        }

        /**
         * 设置会话属性（如全局变量等）
         * @param sessionProperties 要设置的会话属性
         * @return
         */
        public Builder sessionProperties(Map sessionProperties) {
            this.sessionProperties = sessionProperties;
            return this;
        }

        /**
         * 设置规则源
         * @param ruleSource 要设置的规则源
         * @return
         */
        public Builder ruleSource(Object ruleSource) {
            this.ruleSource = ruleSource;
            return this;
        }

        public StatelessRuleServiceJsr94 bulid() {
            if (ruleServiceProvider == null) {
                throw new IllegalStateException("Rule Service Provider not assigned");
            }
            if (ruleSource == null) {
                throw new IllegalStateException("Rule Source not assigned");
            }
            return new StatelessRuleServiceJsr94(ruleServiceProvider, serviceProviderProperties,
                    ruleSource, executionSetProperties, sessionProperties);
        }

    }
}
