package com.linkallcloud.core.util;

public interface IConstants {

    static final String TRUE = "true";
    static final String FALSE = "false";

    static final String BLANK = "";

    static final String FORMAT_PATTEN_DATE = "yyyy-MM-dd";
    static final String FORMAT_PATTEN_DATETIME = "yyyy-MM-dd HH:mm:ss";
    static final String FORMAT_PATTEN_TIME = "HH:mm:ss";

    static final String APPLICATION = "application";
    static final String SESSION = "session";

    static final String ID = "id";
    static final String IDS = "ids";

    static final String OBJECT = "object";
    static final String ENTITY = "entity";
    static final String ENTITIES = "entities";
    static final String ENTITY_ID = "entity.id";
    static final String DTO = "dto";
    static final String DTOS = "dtos";

    static final int DEFAULT_PAGE_SIZE = 20;
    static final String PAGE = "page";

    static final char EQUAL = '=';
    static final char MIDDLE_BAR = '-';
    static final char BOTTOM_BAR = '_';
    static final char LEFT_PARENTHESIS = '(';
    static final char RIGHT_PARENTHESIS = ')';
    static final char QUOTATION_MARK = '\'';
    static final char WHITE_SPACE = ' ';
    static final char LIKE_TAG = '%';
    static final char COLON = ':';
    static final char COMMA = ',';
    static final char FULL_STOP = '.';
    static final char POINT = '.';
    static final char QUESTION_MARK = '?';
    static final char ASTERISK = '*';
    static final char POUND = '#';
    static final char AND = '&';
    static final char LEFT_SLASH = '/';
    static final char RIGHT_SLASH = '\\';
    static final String LOWER = "LOWER";
    static final String LIKE = "LIKE";
    static final String NOT = "NOT";

    static final String NUMBERS = "1234567890";
    static final String CAPITAL_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final String SMALL_LETTERS = "abcdefghijklmnopqrstuvwxyz";

    /*-
     * NUMBERS
     */
    static final int TEN = 10;
    static final int HUNDRED = 100;
    static final int THOUSAND = 1000;

    /*-
     * EnCoding
     */
    static final String ENCODING_GBK = "gbk";
    static final String ENCODING_UTF8 = "utf-8";

    /*-
     * Log
     */
    static final String TRACE = "TRACE";
    static final String DEBUG = "DEBUG";
    static final String INFO = "INFO";
    static final String WARN = "WARN";
    static final String ERROR = "ERROR";
    static final String FATAL = "FATAL";

    /*-
     * protocol
     */
    static final String HTTP = "http";
    static final String HTTP_REQUEST_FREFIX = "http://";
    static final String HTTPS = "https";
    static final String HTTPS_REQUEST_FREFIX = "https://";

    static final String HTTP_HEADER_EXPIRES = "Expires";
    static final String HTTP_HEADER_PRAGMA = "Pragma";
    static final String HTTP_HEADER_CACHE_CONTROL = "Cache-Control";
    static final String HTTP_HEADER_LAST_MODIFIED = "Last-Modified";
    static final String HTTP_HEADER_ETAG = "ETag";
    static final String HTTP_HEADER_IF_MODIFIED_SINCE = "If-Modified-Since";

    static final String DEFAULT_HTTP_CONTENT_TYPE = "text/html";
    static final String JSON_HTTP_CONTENT_TYPE = "application/json";

    /*-
     * SQL
     */
    static final String HQL = "hql";

    static final String SQL = "sql";
    static final String SELECT = "select";
    static final String UPDATE = "update";
    static final String DELETE = "delete";
    static final String FROM = "from";
    static final String WHERE = "where";
    static final String ORDER_BY = "order by";

    /*-
     * static server
     */
    static final String STATIC_IMAGE_SERVER = "static_image_server";
    static final String STATIC_PAGE_SERVER = "static_page_server";
    static final String STATIC_CUSTOM_CSS = "static_custom_css";

    /*
     * nonce time out
     */
    static final int NONCE_TIMEOUT = 1 * 60 * 60 * 1000;
}
