package com.linkallcloud.core.json;

import static com.linkallcloud.core.lang.Streams.buffr;
import static com.linkallcloud.core.lang.Streams.fileInr;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.linkallcloud.core.json.entity.JsonEntity;
import com.linkallcloud.core.json.impl.JsonCompileImplV2;
import com.linkallcloud.core.json.impl.JsonEntityFieldMakerImpl;
import com.linkallcloud.core.json.impl.JsonRenderImpl;
import com.linkallcloud.core.lang.Files;
import com.linkallcloud.core.lang.Lang;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.lang.Streams;
import com.linkallcloud.core.lang.util.PType;
import com.linkallcloud.core.lang.util.RcType;
import com.linkallcloud.core.mapl.Mapl;

public class Json {

    // =========================================================================
    // ============================Json.fromJson================================
    // =========================================================================
    /**
     * 从文本输入流中生成 JAVA 对象。
     * 
     * @param reader
     *            文本输入流
     * @return JAVA 对象
     * @throws JsonException
     */
    public static Object fromJson(Reader reader) throws JsonException {
        // return new com.linkallcloud.json.impl.JsonCompileImpl().parse(reader);
        return new JsonCompileImplV2().parse(reader);
    }

    /**
     * 根据指定的类型，从文本输入流中生成 JAVA 对象。 指定的类型可以是任意 JAVA 对象。
     * 
     * @param type
     *            对象类型
     * @param reader
     *            文本输入流
     * @return 指定类型的 JAVA 对象
     * @throws JsonException
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromJson(Class<T> type, Reader reader)
            throws JsonException {
        return (T) parse(type, reader);
    }

    /**
     * 根据指定的类型，从文本输入流中生成 JAVA 对象。 指定的类型可以是任意 JAVA 对象。
     * 
     * @param type
     *            对象类型，可以是范型
     * @param reader
     *            文本输入流
     * @return 指定类型的 JAVA 对象
     * @throws JsonException
     */
    public static Object fromJson(Type type, Reader reader)
            throws JsonException {
        return parse(type, reader);
    }

    private static Object parse(Type type, Reader reader) {
        Object obj = fromJson(reader);
        if (type != null)
            return Mapl.maplistToObj(obj, type);
        return obj;
    }

    /**
     * 根据指定的类型，从字符串中生成 JAVA 对象。 指定的类型可以是任意 JAVA 对象。
     * 
     * @param type
     *            对象类型，可以是范型
     * @param cs
     *            JSON 字符串
     * @return 指定类型的 JAVA 对象
     * @throws JsonException
     */
    public static Object fromJson(Type type, CharSequence cs)
            throws JsonException {
        return fromJson(type, Lang.inr(cs));
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T fromJson(PType<T> type, Reader reader)
            throws JsonException {
        return (T) fromJson((Type)type, reader);
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromJson(PType<T> type, CharSequence cs)
            throws JsonException {
        return (T)fromJson((Type)type, cs);
    }

    /**
     * 根据指定的类型，读取指定的 JSON 文件生成 JAVA 对象。 指定的类型可以是任意 JAVA 对象。
     * 
     * @param type
     *            对象类型
     * @param f
     *            文件对象
     * @return 指定类型的 JAVA 对象
     * @throws JsonException
     */
    public static <T> T fromJsonFile(Class<T> type, File f) {
        BufferedReader br = null;
        try {
            br = buffr(fileInr(f));
            return Json.fromJson(type, br);
        }
        finally {
            Streams.safeClose(br);
        }
    }

    /**
     * 从 JSON 字符串中，获取 JAVA 对象。 实际上，它就是用一个 Read 包裹了的字符串。
     * <p>
     * 请参看函数 ‘Object fromJson(Reader reader)’ 的描述
     * 
     * @param cs
     *            JSON 字符串
     * @return JAVA 对象
     * @throws JsonException
     * 
     * @see #fromJson(Reader reader)
     */
    public static Object fromJson(CharSequence cs) throws JsonException {
        return fromJson(Lang.inr(cs));
    }

    /**
     * 根据指定的类型，从字符串中生成 JAVA 对象。 指定的类型可以是任意 JAVA 对象。
     * <p>
     * 请参看函数 ‘<T> T fromJson(Class<T> type, Reader reader)’ 的描述
     * 
     * @param type
     *            对象类型
     * @param cs
     *            JSON 字符串
     * @return 特定类型的 JAVA 对象
     * @throws JsonException
     * 
     * @see #fromJson(Class type, Reader reader)
     */
    public static <T> T fromJson(Class<T> type, CharSequence cs)
            throws JsonException {
        return fromJson(type, Lang.inr(cs));
    }

    // =========================================================================
    // ============================Json.toJson==================================
    // =========================================================================
    private static Class<? extends JsonRender> jsonRenderCls;

    public static Class<? extends JsonRender> getJsonRenderCls() {
        return jsonRenderCls;
    }

    public static void setJsonRenderCls(Class<? extends JsonRender> cls) {
        jsonRenderCls = cls;
    }

    /**
     * 将一个 JAVA 对象转换成 JSON 字符串
     * 
     * @param obj
     *            JAVA 对象
     * @return JSON 字符串
     */
    public static String toJson(Object obj) {
        return toJson(obj, null);
    }

    /**
     * 将一个 JAVA 对象转换成 JSON 字符串，并且可以设定 JSON 字符串的格式化方式
     * 
     * @param obj
     *            JAVA 对象
     * @param format
     *            JSON 字符串格式化方式 ，若 format 为 null ，则以 JsonFormat.nice() 格式输出
     * @return JSON 字符串
     */
    public static String toJson(Object obj, JsonFormat format) {
        StringBuilder sb = new StringBuilder();
        toJson(Lang.opw(sb), obj, format);
        return sb.toString();
    }

    /**
     * 将一个 JAVA 对象以 JSON 的形式写到一个文本输出流里
     * 
     * @param writer
     *            文本输出流
     * @param obj
     *            JAVA 对象
     */
    public static void toJson(Writer writer, Object obj) {
        toJson(writer, obj, null);
    }

    /**
     * 将一个 JAVA 对象以 JSON 的形式写到一个文本输出流里，并且可以设定 JSON 字符串的格式化方式
     * 
     * @param writer
     *            文本输出流
     * @param obj
     *            JAVA 对象
     * @param format
     *            JSON 字符串格式化方式 ，若 format 为 null ，则以 JsonFormat.nice() 格式输出
     */
    public static void toJson(Writer writer, Object obj, JsonFormat format) {
        try {
            if (format == null)
                format = deft.clone();
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
        }
        catch (IOException e) {
            throw Lang.wrapThrow(e, JsonException.class);
        }
    }

    /**
     * 将一个 JAVA 对象以 JSON 的形式写到一个文件里
     * 
     * @param f
     *            文本文件
     * @param obj
     *            JAVA 对象
     */
    public static void toJsonFile(File f, Object obj) {
        toJsonFile(f, obj, null);
    }

    /**
     * 将一个 JAVA 对象以 JSON 的形式写到一个文件里，并且可以设定 JSON 字符串的格式化方式
     * 
     * @param f
     *            文本文件
     * @param obj
     *            JAVA 对象
     * @param format
     *            JSON 字符串格式化方式 ，若 format 为 null ，则以 JsonFormat.nice() 格式输出
     */
    public static void toJsonFile(File f, Object obj, JsonFormat format) {
        Writer writer = null;
        try {
            Files.createFileIfNoExists(f);
            writer = Streams.fileOutw(f);
            Json.toJson(writer, obj, format);
            writer.append('\n');
        }
        catch (IOException e) {
            throw Lang.wrapThrow(e);
        }
        finally {
            Streams.safeClose(writer);
        }
    }

    /**
     * 清除 Json 解析器对实体解析的缓存
     */
    public static void clearEntityCache() {
        entities.clear();
    }

    /**
     * 保存所有的 Json 实体
     */
    private static final ConcurrentHashMap<String, JsonEntity> entities = new ConcurrentHashMap<String, JsonEntity>();

    /**
     * 获取一个 Json 实体
     */
    public static JsonEntity getEntity(Mirror<?> mirror) {
        JsonEntity je = entities.get(mirror.getTypeId());
        if (null == je) {
            je = new JsonEntity(mirror);
            entities.put(mirror.getTypeId(), je);
        }
        return je;
    }

    // ==================================================================================
    // ====================帮助函数======================================================

    /**
     * 从 JSON 字符串中，根据获取某种指定类型的 List 对象。
     * <p>
     * 请参看函数 ‘Object fromJson(Type type, CharSequence cs)’ 的描述
     * 
     * @param eleType
     *            对象类型
     * @param cs
     *            JSON 字符串
     * @return 特定类型的 JAVA 对象
     * @throws JsonException
     * 
     * @see #fromJson(Type type, CharSequence cs)
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> fromJsonAsList(Class<T> eleType, CharSequence cs) {
        return (List<T>) fromJson(RcType.list(eleType), cs);
    }

    /**
     * 从 JSON 输入流中，根据获取某种指定类型的 List 对象。
     * <p>
     * 请参看函数 ‘Object fromJson(Type type, Reader reader)’ 的描述
     * 
     * @param eleType
     *            对象类型
     * @param reader
     *            JSON 输入流
     * @return 特定类型的 JAVA 对象
     * @throws JsonException
     * 
     * @see #fromJson(Type type, Reader reader)
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> fromJsonAsList(Class<T> eleType, Reader reader) {
        return (List<T>) fromJson(RcType.list(eleType), reader);
    }

    /**
     * 从 JSON 字符串中，根据获取某种指定类型的 数组 对象。
     * <p>
     * 请参看函数 ‘Object fromJson(Type type, CharSequence cs)’ 的描述
     * 
     * @param eleType
     *            对象类型
     * @param cs
     *            JSON 字符串
     * @return 特定类型的 JAVA 对象
     * @throws JsonException
     * 
     * @see #fromJson(Type type, CharSequence cs)
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] fromJsonAsArray(Class<T> eleType, CharSequence cs) {
        return (T[]) fromJson(RcType.array(eleType), cs);
    }

    /**
     * 从 JSON 输入流中，根据获取某种指定类型的 数组 对象。
     * <p>
     * 请参看函数 ‘Object fromJson(Type type, Reader reader)’ 的描述
     * 
     * @param eleType
     *            对象类型
     * @param reader
     *            JSON 输入流
     * @return 特定类型的 JAVA 对象
     * @throws JsonException
     * 
     * @see #fromJson(Class type, Reader reader)
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] fromJsonAsArray(Class<T> eleType, Reader reader) {
        return (T[]) fromJson(RcType.array(eleType), reader);
    }

    /**
     * 从 JSON 字符串中，根据获取某种指定类型的 Map 对象。
     * <p>
     * 请参看函数 ‘Object fromJson(Type type, CharSequence cs)’ 的描述
     * 
     * @param eleType
     *            对象类型
     * @param cs
     *            JSON 字符串
     * @return 特定类型的 JAVA 对象
     * @throws JsonException
     * 
     * @see #fromJson(Type type, CharSequence cs)
     */
    @SuppressWarnings("unchecked")
    public static <T> Map<String, T> fromJsonAsMap(Class<T> eleType,
                                                   CharSequence cs) {
        return (Map<String, T>) fromJson(RcType.mapStr(eleType), cs);
    }

    /**
     * 从 JSON 输入流中，根据获取某种指定类型的 Map 对象。
     * <p>
     * 请参看函数 ‘Object fromJson(Type type, Reader reader)’ 的描述
     * 
     * @param eleType
     *            对象类型
     * @param reader
     *            JSON 输入流
     * @return 特定类型的 JAVA 对象
     * @throws JsonException
     * 
     * @see #fromJson(Type type, Reader reader)
     */
    @SuppressWarnings("unchecked")
    public static <T> Map<String, T> fromJsonAsMap(Class<T> eleType,
                                                   Reader reader) {
        return (Map<String, T>) fromJson(RcType.mapStr(eleType), reader);
    }

    protected static JsonFormat deft = JsonFormat.nice();
    public static void setDefaultJsonformat(JsonFormat defaultJf) {
        if (defaultJf == null)
            defaultJf = JsonFormat.nice();
        Json.deft = defaultJf;
    }

    private static JsonEntityFieldMaker deftMaker = new JsonEntityFieldMakerImpl();
    public static void setDefaultFieldMaker(JsonEntityFieldMaker fieldMaker) {
        if (fieldMaker != null)
            Json.deftMaker = fieldMaker;
    }
    public static JsonEntityFieldMaker getDefaultFieldMaker() {
        return deftMaker;
    }
}