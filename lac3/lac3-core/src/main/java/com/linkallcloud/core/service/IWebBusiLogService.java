package com.linkallcloud.core.service;

import java.io.Serializable;

import com.linkallcloud.core.laclog.WebBusiLog;

public interface IWebBusiLogService<PK extends Serializable, T extends WebBusiLog<PK>> extends IService<PK, T> {

}
