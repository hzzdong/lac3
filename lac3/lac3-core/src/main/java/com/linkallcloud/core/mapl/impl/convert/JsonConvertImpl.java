package com.linkallcloud.core.mapl.impl.convert;

import java.io.IOException;
import java.io.Writer;

import com.linkallcloud.core.json.JsonException;
import com.linkallcloud.core.json.JsonFormat;
import com.linkallcloud.core.json.JsonRender;
import com.linkallcloud.core.json.impl.JsonRenderImpl;
import com.linkallcloud.core.lang.Lang;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.lang.stream.StringWriter;
import com.linkallcloud.core.mapl.MaplConvert;

/**
 * 将MapList转换成Json
 * 
 */
public class JsonConvertImpl implements MaplConvert {
    private static Class<? extends JsonRender> jsonRenderCls;

    public static Class<? extends JsonRender> getJsonRenderCls() {
        return jsonRenderCls;
    }

    public static void setJsonRenderCls(Class<? extends JsonRender> cls) {
        jsonRenderCls = cls;
    }

    private JsonFormat format = null;

    public JsonConvertImpl() {
        format = new JsonFormat();
    }

    public JsonConvertImpl(JsonFormat format) {
        this.format = format;
    }

    public Object convert(Object obj) {
        StringBuilder sb = new StringBuilder();
        Writer writer = new StringWriter(sb);
        try {
            JsonRender jr;
            Class<? extends JsonRender> jrCls = getJsonRenderCls();
            if (jrCls == null)
                jr = new JsonRenderImpl();
            else
                jr = Mirror.me(jrCls).born();
            jr.setWriter(writer);
            jr.setFormat(format);
            jr.render(obj);

            writer.flush();
            return sb.toString();
        }
        catch (IOException e) {
            throw Lang.wrapThrow(e, JsonException.class);
        }
    }
}
