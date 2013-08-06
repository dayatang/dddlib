package org.openkoala.koala.codechecker;

import java.util.List;

import org.openkoala.koala.codechecker.vo.CodeCheckerVO;

/**.
 * 代码检查的接口.
 * @author lingen.
 *
 */
public interface CodeCheckerProcess {

	/**.
	 * 对代码进行检查，返回CodeCheckerVO的List信息
	 * @param filePath 目录
	 * @return List<CodeCheckerVO>
	 */
	List<CodeCheckerVO> process(String filePath);
}
