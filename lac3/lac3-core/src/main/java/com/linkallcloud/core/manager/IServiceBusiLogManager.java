package com.linkallcloud.core.manager;

import java.io.Serializable;

import com.linkallcloud.core.laclog.ServiceBusiLog;

public interface IServiceBusiLogManager<PK extends Serializable, T extends ServiceBusiLog<PK>> extends IManager<PK, T> {

}
