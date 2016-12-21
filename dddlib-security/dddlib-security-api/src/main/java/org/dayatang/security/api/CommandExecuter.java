package org.dayatang.security.api;

/**
 * Created by yyang on 2016/12/2.
 */
public interface CommandExecuter {

    <T> T execute(Command<T> command);

}
