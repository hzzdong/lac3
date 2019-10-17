package com.linkallcloud.core.service;

import java.io.Serializable;

import com.linkallcloud.core.laclog.ServiceBusiLog;

public interface IServiceBusiLogService<PK extends Serializable, T extends ServiceBusiLog<PK>> extends IService<PK, T> {

}
