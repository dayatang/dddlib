package org.openkoala.opencis.trac;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.dayatang.configuration.Configuration;

public class MockConfiguration implements Configuration {

	private Map<String,String> map = null;
	
	public MockConfiguration() {
		// TODO Auto-generated constructor stub
		map = new HashMap<String, String>();
		map.put("HOST", "10.108.1.134");
		map.put("USER", "root");
		map.put("PASSWORD", "password");
		map.put("PERMISSION", "TRAC_ADMIN");
	}
	@Override
	public boolean getBoolean(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getBoolean(String arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Date getDate(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDate(String arg0, Date arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getDouble(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getDouble(String arg0, double arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getInt(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getInt(String arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getLong(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getLong(String arg0, long arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Properties getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getString(String key) {
		// TODO Auto-generated method stub
		return map.get(key);
	}

	@Override
	public String getString(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void load() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBoolean(String arg0, boolean arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDate(String arg0, Date arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDouble(String arg0, double arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setInt(String arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLong(String arg0, long arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setString(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

}
