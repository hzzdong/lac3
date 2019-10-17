package com.linkallcloud.core.json;

import java.util.List;

import com.linkallcloud.core.json.entity.JsonEntityField;
import com.linkallcloud.core.lang.Mirror;

/**
 * JsonEntityFieldMaker
 * 通过定制JsonEntityField的生成过程来影响toJson的行为
 *
 */
public interface JsonEntityFieldMaker {
    List<JsonEntityField> make(Mirror<?> mirror);
}