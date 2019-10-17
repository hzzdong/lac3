package com.linkallcloud.core.dt;

public class DtColumn {

    private String data;// columns[1][data]=phone
    private String name;// columns[0][name]=
    private boolean searchable;// columns[0][searchable]=true
    private boolean orderable;// columns[0][orderable]=true
    private DtSearch search;// columns[0][search][value]=//columns[0][search][regex]=false

    private String alias;// sql表别名，用户自动生成order by

    public DtColumn() {
        super();
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSearchable() {
        return searchable;
    }

    public void setSearchable(boolean searchable) {
        this.searchable = searchable;
    }

    public boolean isOrderable() {
        return orderable;
    }

    public void setOrderable(boolean orderable) {
        this.orderable = orderable;
    }

    public DtSearch getSearch() {
        return search;
    }

    public void setSearch(DtSearch search) {
        this.search = search;
    }

}
