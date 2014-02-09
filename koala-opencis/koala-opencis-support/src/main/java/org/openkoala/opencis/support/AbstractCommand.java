package org.openkoala.opencis.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 抽象的命令类，作为SSHCommand和LocalCommand的父类
 * @author zjh
 *
 */
public abstract class AbstractCommand implements Command {

	/**
	 * 转换输入流为字符串输出
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
    protected String readOutput(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }
}
