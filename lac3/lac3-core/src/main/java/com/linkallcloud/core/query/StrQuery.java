package com.linkallcloud.core.query;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import com.linkallcloud.core.query.Query.GroupOperator;
import com.linkallcloud.core.query.rule.QueryRule;
import com.linkallcloud.core.query.rule.QueryRuleFactory;
import com.linkallcloud.core.query.rule.desc.StringRuleDescriptor;

public class StrQuery implements Serializable {
	private static final long serialVersionUID = 8326903501443055444L;

    private List<StringRuleDescriptor> rules;
    private List<StrQuery> groups;
    private GroupOperator groupOp;
    
    
	public List<StringRuleDescriptor> getRules() {
		return rules;
	}
	public void setRules(List<StringRuleDescriptor> rules) {
		this.rules = rules;
	}
	public List<StrQuery> getGroups() {
		return groups;
	}
	public void setGroups(List<StrQuery> groups) {
		this.groups = groups;
	}
	public GroupOperator getGroupOp() {
		return groupOp;
	}
	public void setGroupOp(GroupOperator groupOp) {
		this.groupOp = groupOp;
	}
	
	public Query toQuery() {
		Query query = new Query();
		
		query.setGroupOp(this.groupOp);
		
		if(this.rules!=null && !rules.isEmpty()) {
			for(StringRuleDescriptor ruleDesc: rules) {
				QueryRule[] qrs = QueryRuleFactory.create(ruleDesc);
	            if (qrs != null && qrs.length > 0) {
	                for (QueryRule qr : qrs) {
	                	query.addRule(qr);
	                }
	            }
			}
		}
		
		if(this.groups!=null && !this.groups.isEmpty()) {
			for(StrQuery g: groups) {
				Query group = g.toQuery();
				query.addGroup(group);
			}
		}
		return query;
	}
	
    /**
     * 是否有查询条件
     *
     * @return boolean
     */
    public boolean hasQueryConditons() {
        if (this.getRules() != null && this.getRules().size() > 0) {
            return true;
        } else {
            if (this.getGroups() != null && this.getGroups().size() > 0) {
                for (StrQuery g : this.getGroups()) {
                    if (g.hasQueryConditons()) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
    
    /**
     * 是否存在名称是fieldName的rule
     *
     * @param fieldName
     * @return boolean
     */
    public boolean hasRule4Field(String fieldName) {
        if (getRules() != null && !getRules().isEmpty()) {
            for (StringRuleDescriptor rd : getRules()) {
                if (rd.getField().equals(fieldName)) {
                    return true;
                }
            }
        }
        if (getGroups() != null && !getGroups().isEmpty()) {
            for (StrQuery group : getGroups()) {
                if (group.hasRule4Field(fieldName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 得到名称是fieldName的rule
     *
     * @param fieldName
     * @return boolean
     */
    public StringRuleDescriptor getRule4Field(String fieldName) {
        if (getRules() != null && !getRules().isEmpty()) {
            for (StringRuleDescriptor rd : getRules()) {
                if (rd.getField().equals(fieldName)) {
                    return rd;
                }
            }
        }
        if (getGroups() != null && !getGroups().isEmpty()) {
            for (StrQuery group : getGroups()) {
            	StringRuleDescriptor r = group.getRule4Field(fieldName);
                if (r != null) {
                    return r;
                }
            }
        }
        return null;
    }

    /**
     * 重新设置名称是fieldName的rule的值
     *
     * @param fieldName
     * @param value
     */
    public void resetRule4Field(String fieldName, Object value) {
        if (getRules() != null && !getRules().isEmpty()) {
            for (StringRuleDescriptor rd : getRules()) {
                if (rd.getField().equals(fieldName)) {
                	rd.setValue(value);
                    return;
                }
            }
        }
        if (getGroups() != null && !getGroups().isEmpty()) {
            for (StrQuery group : getGroups()) {
                group.resetRule4Field(fieldName, value);
            }
        }
    }

    /**
     * 删除名称是fieldName的rule
     * 
     * @param fieldName
     */
    public void delRule4Field(String fieldName) {
        if (getRules() != null && !getRules().isEmpty()) {
            Iterator<StringRuleDescriptor> it = getRules().iterator();
            while (it.hasNext()) {
            	StringRuleDescriptor rd = it.next();
                if (rd.getField().equals(fieldName)) {
                    it.remove();
                    return;
                }
            }
        }
        if (getGroups() != null && !getGroups().isEmpty()) {
            for (StrQuery group : getGroups()) {
                group.delRule4Field(fieldName);
            }
        }
    }

}
