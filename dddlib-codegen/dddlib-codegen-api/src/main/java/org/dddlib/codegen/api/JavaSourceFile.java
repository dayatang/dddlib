package org.dddlib.codegen.api;

import java.io.File;
import java.io.OutputStream;

/**
 * Created by yyang on 2016/12/21.
 */
public interface JavaSourceFile {

    void writeToStream(OutputStream out);

    void writeToDirectory(String directory);

    void writeToFile(File outputFile);
}
