package org.openkoala.koala.codechecker.vo;

/**
 * 在代码质量检查上有问题的VO对象
 * @author lingen
 *
 */
public class CodeCheckerVO {

	/**
	 * 有问题的代码文件名，全路径
	 */
	private String filename;
	
	/**
	 * 有问题的代码所在的行数
	 */
	private int line;
	
	/**
	 * 有问题的代码所在的列
	 */
	private int column;
	
	/**
	 * 有问题的代码的描述
	 */
	private String message;

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "CodeCheckerVO [filename=" + filename + ", line=" + line
				+ ", column=" + column + ", message=" + message + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result
				+ ((filename == null) ? 0 : filename.hashCode());
		result = prime * result + line;
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CodeCheckerVO other = (CodeCheckerVO) obj;
		if (column != other.column)
			return false;
		if (filename == null) {
			if (other.filename != null)
				return false;
		} else if (!filename.equals(other.filename))
			return false;
		if (line != other.line)
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		return true;
	}

	public CodeCheckerVO(String filename, int line, int column, String message) {
		super();
		this.filename = filename;
		this.line = line;
		this.column = column;
		this.message = message;
	}

	public CodeCheckerVO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	
	
}
