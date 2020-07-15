package com.linkallcloud.fs.proto;

import com.linkallcloud.fs.conn.Connection;

/**
 * Fdfs交易命令抽象
 * 
 */
public interface FdfsCommand<T> {

    /** 执行交易 */
    public T execute(Connection conn);

}
