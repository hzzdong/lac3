package com.linkallcloud.core.face.message.request;

import com.linkallcloud.core.query.WebQuery;
import com.linkallcloud.core.query.rule.desc.StringRuleDescriptor;

public class ListFaceRequest extends LoginFaceRequest {
    private static final long serialVersionUID = -6826415228198457895L;

    // 查询条件
    private WebQuery query;

    public ListFaceRequest() {
        super();
    }

    public ListFaceRequest(String userType, String loginName, Long userId, Long companyId, String companyName) {
        super(userType, loginName, userId, companyId, companyName);
    }

    public ListFaceRequest(String userType, String loginName, Long userId) {
        super(userType, loginName, userId);
    }

    public ListFaceRequest(String token, String versn) {
        super(token, versn);
    }

    public ListFaceRequest(String token) {
        super(token);
    }

    public WebQuery getQuery() {
        return query;
    }

    public void setQuery(WebQuery query) {
        this.query = query;
    }

    public void addRule(StringRuleDescriptor rule) {
        if (rule != null) {
            if (this.query == null) {
                this.query = new WebQuery();
            }
            this.query.addRule(rule);
        }
    }

}
