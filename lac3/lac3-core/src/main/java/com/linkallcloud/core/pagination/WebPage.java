package com.linkallcloud.core.pagination;

import com.alibaba.fastjson.annotation.JSONField;
import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.dt.DtColumn;
import com.linkallcloud.core.dt.DtOrder;
import com.linkallcloud.core.dt.DtSearch;
import com.linkallcloud.core.query.Orderby;
import com.linkallcloud.core.query.WebQuery;

import java.util.ArrayList;
import java.util.List;

public class WebPage extends WebQuery {
    private static final long serialVersionUID = 2387079940133998932L;

    private int draw;
    // 当前记录位置
    private int start;
    // 每页记录数
    private int length;

    // 总记录数
    private long recordsTotal;
    private long recordsFiltered;
    // 查询结果
    private List<Object> data;

    /* 是否重新查询总记录数（默认 true） */
    @JSONField(serialize = false)
    private boolean searchCount = true;

    @JSONField(serialize = false)
    private DtSearch search;
    @JSONField(serialize = false)
    private List<DtOrder> order = new ArrayList<DtOrder>();
    @JSONField(serialize = false)
    private List<DtColumn> columns = new ArrayList<DtColumn>();

    public WebPage() {
        super();
    }

    public WebPage(int start, int length) {
        super();
        this.start = start < 0 ? 0 : start;
        this.length = length < 1 ? 1 : length;
    }

    public <E extends Domain> Page<E> toPage() {
        Page<E> page = new Page<E>();
        WebQuery.copyWebQueryFields2Query(this, page);
        WebPage.copyWebPageFields2Page(this, page);
        return page;
    }

    public <E> CPage<E> toCPage() {
        CPage<E> page = new CPage<E>();
        WebQuery.copyWebQueryFields2Query(this, page);
        WebPage.copyWebPageFields2CPage(this, page);
        return page;
    }

    private static <E> void copyWebPageFields2CPage(WebPage src, CPage<E> dest) {
        if (src == null || dest == null) {
            return;
        }
        dest.setDraw(src.getDraw());
        dest.setStart(src.getStart());
        dest.setLength(src.getLength());
        dest.setRecordsTotal(src.getRecordsTotal());
        dest.setRecordsFiltered(src.getRecordsFiltered());
        // dest.addDataAll(src.getData());

        dest.setSearchCount(src.isSearchCount());
        dest.setOrderby(src.getWebOrderby());
    }

    public static <E extends Domain> void copyWebPageFields2Page(WebPage src,
                                                                 Page<E> dest) {
        if (src == null || dest == null) {
            return;
        }
        dest.setDraw(src.getDraw());
        dest.setStart(src.getStart());
        dest.setLength(src.getLength());
        dest.setRecordsTotal(src.getRecordsTotal());
        dest.setRecordsFiltered(src.getRecordsFiltered());
        // dest.addDataAll(src.getData());

        dest.setSearchCount(src.isSearchCount());
        dest.setOrderby(src.getWebOrderby());
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

    public void addDataAll(List<?> d) {
        if (d == null || d.isEmpty()) {
            return;
        }
        if (this.data == null) {
            this.data = new ArrayList<Object>();
        }
        this.data.addAll(d);
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public boolean isSearchCount() {
        return searchCount;
    }

    public void setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
    }

    public DtSearch getSearch() {
        return search;
    }

    public void setSearch(DtSearch search) {
        this.search = search;
    }

    public List<DtOrder> getOrder() {
        return order;
    }

    public void setOrder(List<DtOrder> order) {
        this.order = order;
    }

    public List<DtColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<DtColumn> columns) {
        this.columns = columns;
    }

    public Orderby getWebOrderby() {
        if (getOrderby() != null && getOrderby().isOrderBySetted()) {
            return getOrderby();
        } else if (getOrder() != null && !getOrder().isEmpty()) {
            return getOrder().get(0).toOrderby(getColumns(), isMapUnderscoreToCamelCase());
        }
        return null;
    }

    @Override
    @JSONField(serialize = false)
    public String[] getOrdersSqlFrags() {
        if (getOrder() != null && !getOrder().isEmpty()) {
            List<String> orderSqlFrags = new ArrayList<String>();
            for (DtOrder order : getOrder()) {
                String orderSql = order.getOrderSql(getColumns(), isMapUnderscoreToCamelCase());
                orderSqlFrags.add(orderSql);
            }
            return orderSqlFrags.toArray(new String[orderSqlFrags.size()]);
        } else if (getOrderby() != null && getOrderby().isOrderBySetted()) {
            return getOrderby().getOrderSqls(isMapUnderscoreToCamelCase());
        }
        return null;
    }

}
