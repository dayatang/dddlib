package org.openkoala.jbpm.core;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.dayatang.domain.AbstractEntity;

@Entity
@Table
public class MessageLog extends AbstractEntity {

	private static final long serialVersionUID = 8881519315514598555L;
	private String text;
	
	private String user;

	@Override
	public String toString() {
		return "MessageLog [text=" + text + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
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
		MessageLog other = (MessageLog) obj;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
}
