package com.linkallcloud.core.lang.tmpl;

import com.linkallcloud.core.lang.util.RcBean;

interface TmplEle {

    void join(StringBuilder sb, RcBean context, boolean showKey);

}
