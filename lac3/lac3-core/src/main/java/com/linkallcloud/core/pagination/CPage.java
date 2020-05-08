package com.linkallcloud.core.pagination;

import com.alibaba.fastjson.annotation.JSONField;
import com.linkallcloud.core.face.message.request.PageFaceRequest;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.query.WebQuery;

import java.util.ArrayList;
import java.util.List;

public class CPage<E> extends Query {
    private static final long serialVersionUID = -7193513033881352225L;

    private int draw;
    // 当前记录位置
    private int start;
    // 每页记录数
    private int length;

    // 总记录数
    private long recordsTotal;
    private long recordsFiltered;
    // 查询结果
    private List<E> data = new ArrayList<E>();

    /* 是否重新查询总记录数（默认 true） */
    @JSONField(serialize = false)
    private boolean searchCount = true;

    // private boolean lastPage;

    public CPage() {
        super();
    }

    public CPage(int start, int length) {
        super();
        this.start = start < 0 ? 0 : start;
        this.length = length < 1 ? 1 : length;
    }

    public CPage(PageFaceRequest pfr) {
        this(pfr.getStart(), pfr.getLength());
        this.setSearchCount(pfr.isSearchCount());
        WebQuery.copyWebQueryFields2Query(pfr.getQuery(), this);
    }

    public boolean isLastPage() {
        return gePageSize() <= getPageNum();
    }

    public long gePageSize() {
        long mod = this.recordsTotal % this.length;
        return mod == 0 ? (recordsTotal / length) : (recordsTotal / length + 1);
    }

    public void checkPageParameters() {
        if (this.getRecordsTotal() > 0) {
            if (this.gePageSize() < this.getPageNum()) {
                throw new RuntimeException("分页参数错误。");
            }
        }
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public long getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public List<E> getData() {
        return data;
    }

    public void setData(List<E> data) {
        this.data = data;
    }

    public boolean isSearchCount() {
        return searchCount;
    }

    public void setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
    }

    public int getPageNum() {
        if (this.start <= 0) {
            return 1;
        } else {
            return this.start / this.length + 1;
        }
    }

    public void addDataAll(List<E> d) {
        if (d == null || d.isEmpty()) {
            return;
        }

        if (this.data == null) {
            this.data = new ArrayList<E>();
        }
        this.data.addAll(d);
    }

    @SuppressWarnings("unchecked")
	public void addData(Object d) {
        if (d == null) {
            return;
        }
        if (this.data == null) {
            this.data = new ArrayList<E>();
        }
        this.data.add((E) d);
    }

    @JSONField(serialize = false)
    public int getOffsetCurrent() {
        return this.start;
    }

    @JSONField(serialize = false)
    protected int offsetCurrent(int current, int size) {
        if (current > 0) {
            return (current - 1) * size;
        }
        return 0;
    }

}
