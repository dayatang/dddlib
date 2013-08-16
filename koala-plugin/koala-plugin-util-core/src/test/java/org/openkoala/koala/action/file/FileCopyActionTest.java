package org.openkoala.koala.action.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class FileCopyActionTest {

	@Test
	public void testCopyFile() throws IOException {
		File file = new File("src/test/resources/dir-for-paste/file-for-paste");
		assertFalse(file.exists());
		
		FileCopyAction.copyFile("src/test/resources/dir-for-copy/file-for-copy", "src/test/resources/dir-for-paste/file-for-paste");
		File pasteFile = new File("src/test/resources/dir-for-paste/file-for-paste");
		assertTrue(pasteFile.exists());
		
		File srcFile = new File("src/test/resources/dir-for-copy/file-for-copy");
		assertEquals(getFileContent(srcFile), getFileContent(pasteFile));
		
		pasteFile.delete();
	}

	private String getFileContent(File file) throws IOException {
		StringBuilder result = new StringBuilder();
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String temp;
        while ((temp = bufferedReader.readLine()) != null) {
        	result.append(temp);
        }
        bufferedReader.close();

        return result.toString();
	}
	
	@Test
	public void testCopyDirAndClearDir() throws IOException {
		File directory = new File("src/test/resources/dir-for-paste/dir-for-copy");
		assertFalse(directory.exists());
		
		//测试拷贝目录
		FileCopyAction.copyDir("src/test/resources/dir-for-copy", "src/test/resources/dir-for-paste/dir-for-paste");
		File pasteDirectory = new File("src/test/resources/dir-for-paste/dir-for-paste");
		assertTrue(pasteDirectory.exists());
		assertTrue(pasteDirectory.isDirectory());
		
		//测试目录内容是否拷贝正确
		File srcDirectory = new File("src/test/resources/dir-for-copy");
		assertEquals(getAllContentFromDirectory(srcDirectory), getAllContentFromDirectory(pasteDirectory));
		
		//测试删除目录
		FileCopyAction.clearDir(pasteDirectory.getAbsolutePath());
		assertFalse(pasteDirectory.exists());
		
		//测试带有过滤的拷贝
		FileFilter fileFilter = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (pathname.getName().equals("file-for-filefilter")) {
					return false;
				}
				return true;
			}
		};
		FileCopyAction.copyDir("src/test/resources/dir-for-copy", "src/test/resources/dir-for-paste/dir-for-paste", fileFilter);
		File pasteDirectoryUseFilter = new File("src/test/resources/dir-for-paste/dir-for-paste");
		assertTrue(pasteDirectoryUseFilter.exists());
		assertTrue(pasteDirectoryUseFilter.isDirectory());
		assertTrue(isDonnotContainBeFilteredFile(pasteDirectoryUseFilter));
		
		FileCopyAction.clearDir(pasteDirectoryUseFilter.getAbsolutePath());
	}

	private Set<String> getAllContentFromDirectory(File directory) throws IOException {
		if (!directory.isDirectory()) {
			throw new RuntimeException(directory.getName() + " is not a directory!");
		}
		
		Set<String> results = new HashSet<String>();
		for (File file : directory.listFiles()) {
			results.add(getFileContent(file));
		}
		
		return results;
	}
	
	private boolean isDonnotContainBeFilteredFile(File directory) {
		boolean result = true;
		
		for (File file : directory.listFiles()) {
			if (file.getName().endsWith("file-for-filefilter")) {
				return false;
			}
		}
		
		return result;
	}
}
