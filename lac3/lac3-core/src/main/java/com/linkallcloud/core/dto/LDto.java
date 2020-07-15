package com.linkallcloud.core.dto;

import com.linkallcloud.core.domain.Domain;

public class LDto<E extends Domain> extends Dto<E> {
    private static final long serialVersionUID = 2037328396312304824L;

    public LDto() {
        super();
    }

    public LDto(E entity) {
        super(entity);
    }

}
