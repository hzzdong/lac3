package com.linkallcloud.core.manager;

import java.io.Serializable;

import com.linkallcloud.core.laclog.WebBusiLog;

public interface IWebBusiLogManager<PK extends Serializable, T extends WebBusiLog<PK>> extends IManager<PK, T> {

}
