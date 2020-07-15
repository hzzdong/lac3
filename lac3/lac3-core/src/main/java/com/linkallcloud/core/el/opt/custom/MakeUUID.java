package com.linkallcloud.core.el.opt.custom;

import java.util.List;
import java.util.UUID;

import com.linkallcloud.core.el.opt.RunMethod;
import com.linkallcloud.core.lang.random.R;
import com.linkallcloud.core.plugin.Plugin;

/**
 * EL内置的方法之一, 基本用法 uuid()   uuid(32)  uuid(64), 分别生成16进制,32进制,64进制的UUID字符串
 *
 */
public class MakeUUID implements RunMethod, Plugin {

	public boolean canWork() {
		return true;
	}

	public Object run(List<Object> fetchParam) {
	    if (fetchParam.isEmpty() || !(fetchParam.get(0) instanceof Number))
	        return UUID.randomUUID().toString().replace("-", "");
	    int type = ((Number)fetchParam.get(0)).intValue();
	    switch (type) {
        case 32:
            return R.UU32();
        case 64:
            return R.UU64();
        default:
            return UUID.randomUUID().toString().replace("-", "");
        }
	}

	public String fetchSelf() {
		return "uuid";
	}

}
