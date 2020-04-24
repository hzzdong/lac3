package com.linkallcloud.core.lang;

import java.io.Serializable;
import java.util.Date;

import com.linkallcloud.core.json.Json;

public class DateRange implements Serializable{
	private static final long serialVersionUID = -932988938602144803L;
	
	private Date start;
	private Date end;

	public DateRange() {
		super();
	}
	
	public DateRange(String dr) {
		try {
			Date[] darr = Json.fromJsonAsArray(Date.class, dr);
			if(darr!=null && darr.length == 2) {
				this.start = darr[0];
				this.end = darr[1];
			}
        }
        catch (Exception e) {
            throw Lang.makeThrow("Error DateRange format [%s]", dr);
        }
        if (this.start == null || this.end == null)
            throw Lang.makeThrow("Error DateRange format [%s]", dr);
	}

	public DateRange(Date start, Date end) {
		super();
		this.start = start;
		this.end = end;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

}
