package org.dddlib.codegen.api;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.util.Set;

/**
 * 领域类生成器接口。从各种文件或输入流中读取领域类定义生成Java类源文件。
 * Created by yyang on 2016/12/21.
 */
public interface DomainClassGenerator {

    /**
     * 从文件中读取领域类定义，生成
     * @param file
     * @return
     */
    Set<JavaSourceFile> generate(File file);

    Set<JavaSourceFile> generate(InputStream in);

    Set<JavaSourceFile> generate(Reader in);
}
