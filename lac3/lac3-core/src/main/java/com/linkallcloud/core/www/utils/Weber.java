package com.linkallcloud.core.www.utils;

import com.linkallcloud.core.lang.Strings;

public class Weber {

    private String server;
    private int port;
    private String addr;

    public Weber() {
        super();
    }

    public Weber(String server, int port, String addr) {
        super();
        this.server = server;
        this.port = port;
        this.addr = Strings.isBlank(addr) ? "/" : addr;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

}
