package com.linkallcloud.core.face.message.request;

import com.alibaba.fastjson.annotation.JSONField;

public class PageFaceRequest extends ListFaceRequest {
    private static final long serialVersionUID = 3813877585305706005L;

    private int draw;

    // 当前记录位置，与pageNum二选一必填
    private int start;
    // 当前页码，与start二选一必填
    private int pageNum;

    // 每页记录数
    private int length;

    // 是否重新查询总记录数（默认 true）
    private boolean searchCount = true;

    public PageFaceRequest() {
        super();
    }

    public PageFaceRequest(int start, int length) {
        super();
        this.start = start;
        this.length = length;
    }

    public PageFaceRequest(String sessionId, String versn) {
        super(sessionId, versn);
    }

    public PageFaceRequest(int start, int length, String sessionId, String versn) {
        super(sessionId, versn);
        this.start = start;
        this.length = length;
    }

    public PageFaceRequest(String sessionId) {
        super(sessionId);
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getStart() {
        if (start <= 0) {
            if (pageNum > 0) {
                this.start = length * (pageNum - 1);
            }
        }
        return start <= 0 ? 0 : start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        if (length <= 0) {
            return 20;
        } else if (length > 500) {
            return 500;
        } else {
            return length;
        }
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isSearchCount() {
        return searchCount;
    }

    public void setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
    }

    public int getPageNum() {
        if (pageNum <= 0) {
            if (start <= 0) {
                this.pageNum = 1;
            } else {
                this.pageNum = start / length + 1;
            }
        }
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
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
