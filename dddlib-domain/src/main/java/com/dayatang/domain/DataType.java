package com.dayatang.domain;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

public enum DataType {
	
	STRING {
		@Override
		public String getDefaultValue() {
			return "";
		}

		@Override
		public String getRealValue(String value) {
			return value;
		}
	},
	
	INT {
		@Override
		public Integer getDefaultValue() {
			return 0;
		}

		@Override
		public Integer getRealValue(String value) {
			return Integer.valueOf(value);
		}
	},
	
	DOUBLE {
		@Override
		public Double getDefaultValue() {
			return 0.0;
		}

		@Override
		public Double getRealValue(String value) {
			return Double.valueOf(value);
		}
	},
	
	BIG_DECIMAL {
		@Override
		public BigDecimal getDefaultValue() {
			return BigDecimal.ZERO;
		}

		@Override
		public BigDecimal getRealValue(String value) {
			return new BigDecimal(value);
		}
	},
	
	BOOLEAN {
		@Override
		public Boolean getDefaultValue() {
			return false;
		}

		@Override
		public Boolean getRealValue(String value) {
			return Boolean.valueOf(value);
		}
	},
	
	DATE {
		@Override
		public Date getDefaultValue() {
			try {
				return DateUtils.parseDate("1000-01-01", new String[] {"yyyy-MM-dd"});
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		public Date getRealValue(String value) {
			try {
				return DateUtils.parseDate(value, new String[] {"yyyy-MM-dd"});
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
	},
	
	TIME {
		@Override
		public Date getDefaultValue() {
			try {
				return DateUtils.parseDate("00:00:00", new String[] {"hh:mm:ss"});
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		public Date getRealValue(String value) {
			try {
				return DateUtils.parseDate(value, new String[] {"hh:mm:ss"});
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
	},
	
	DATE_TIME {
		@Override
		public Date getDefaultValue() {
			try {
				return DateUtils.parseDate("1000-01-01 00:00:00", new String[] {"yyyy-MM-dd hh:mm:ss"});
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		public Date getRealValue(String value) {
			try {
				return DateUtils.parseDate(value, new String[] {"yyyy-MM-dd hh:mm:ss"});
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
	};

	public abstract Object getDefaultValue();
	
	public abstract Object getRealValue(String value);
}
