package com.linkallcloud.core.dt;

import java.util.List;

import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.query.Orderby;

public class DtOrder {

	private int column;// column index
	private String dir;// asc desc

	public DtOrder() {
		super();
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public String getDir() {
		if ("ASC".equalsIgnoreCase(dir) || "DESC".equalsIgnoreCase(dir)) {
			return dir;
		} else {
			return "ASC";
		}
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	/**
	 * order sql, 比如：u.id desc
	 * 
	 * @param columns
	 * @param mapUnderscoreToCamelCase
	 * @return
	 */
	public String getOrderSql(List<DtColumn> columns, boolean mapUnderscoreToCamelCase) {
		if (!columns.isEmpty() && columns.size() >= (this.getColumn() + 1)) {
			DtColumn col = columns.get(this.getColumn());
			if (col != null) {
				String dbFieldName = col.getData();
				String aliasName = col.getAlias();
				if (dbFieldName != null && dbFieldName.length() > 0) {
					int idx = dbFieldName.indexOf(" ");
					if (idx == -1) {
						if (mapUnderscoreToCamelCase) {
							dbFieldName = Strings.lowerWord(dbFieldName, '_');
						}
						if (aliasName != null && aliasName.length() > 0) {
							return aliasName + "." + dbFieldName + " " + this.getDir();
						} else {
							return dbFieldName + " " + this.getDir();
						}
					} else {
						// 现在不支持
					}
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param columns
	 * @param mapUnderscoreToCamelCase
	 * @return
	 */
	public Orderby toOrderby(List<DtColumn> columns, boolean mapUnderscoreToCamelCase) {
		if (columns != null && !columns.isEmpty() && columns.size() >= (this.getColumn() + 1)) {
			DtColumn col = columns.get(this.getColumn());
			if (col != null) {
				String fieldName = col.getData();
				String aliasName = col.getAlias();
				if (!Strings.isBlank(fieldName)) {
					int idx = fieldName.indexOf(" ");
					if (idx == -1) {
						if (mapUnderscoreToCamelCase) {
							fieldName = Strings.lowerWord(fieldName, '_');
						}
						if (aliasName != null && aliasName.length() > 0) {
							return new Orderby(aliasName + "." + fieldName, this.getDir());
						} else {
							return new Orderby(fieldName, this.getDir());
						}
					} else {
						// 现在不支持
					}

				}
			}
		}
		return null;
	}

}
