package com.linkallcloud.core.activity;

import java.util.Date;
import java.util.List;

import com.github.pagehelper.PageHelper;
import com.linkallcloud.core.dao.ILacLogDao;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.BizException;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.laclog.BusiLog;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.query.Query;

public abstract class LacLogActivity<T extends BusiLog, D extends ILacLogDao<T>> implements ILacLogActivity<T> {
	protected Log log = Logs.get();
	
	public abstract D dao();
	
	protected void before(Trace t, boolean isNew, T entity) {
        if (Strings.isBlank(entity.getUuid())) {
            entity.setUuid(entity.generateUuid());
        }
        if (isNew) {
            entity.setCreateTime(new Date());
        } else {
            entity.setUpdateTime(new Date());
        }
    }

    protected void after(Trace t, boolean isNew, T entity) {
        //
    }
    
    /**
     * 判断数据库操作是否成功
     *
     * @param result 数据库操作返回影响条数
     * @return boolean
     */
    protected boolean retBool(int result) {
        return result >= 1;
    }

	@Override
	public Long insert(Trace t, T entity) {
		if (entity == null) {
            log.error("T entity 不能为null。");
            throw new BizException(Exceptions.CODE_ERROR_ADD, "T entity 不能为null。");
        }

        before(t, true, entity);
        int rows = dao().insert(t, entity);
        after(t, true, entity);

        if (rows > 0) {
            log.debugf("日志 insert 成功，tid：%s, id:%s", t.getTid(), entity.getId());
        } else {
            log.debugf("日志 insert 失败，tid：%s, id:%s", t.getTid(), entity.getId());
        }
        return entity.getId();
	}

	@Override
	public boolean delete(Trace t, Long id, String uuid) {
		int rows = dao().delete(t, id, uuid);
        if (rows > 0) {
            log.debugf("日志 delete 成功，tid：%s, id:%s", t.getTid(), id);
        } else {
            log.debugf("日志 delete 失败，tid：%s, id:%s",  t.getTid(), id);
        }
        return retBool(rows);
	}

	@Override
	public T fetchById(Trace t, Long id) {
		return dao().fetchById(t, id);
	}

	@Override
	public List<T> find(Trace t, Query query) {
		return dao().find(t, query);
	}

	@Override
	public Page<T> findPage(Trace t, Page<T> page) {
		page.checkPageParameters();
        try {
            PageHelper.startPage(page.getPageNum(), page.getLength());
            List<T> list = dao().findPage(t, page);
            if (list instanceof com.github.pagehelper.Page) {
                page.setRecordsTotal(((com.github.pagehelper.Page<T>) list).getTotal());
                page.checkPageParameters();
                page.setRecordsFiltered(page.getRecordsTotal());
                page.addDataAll(list);
            }
            return page;
        } finally {
            PageHelper.clearPage();
        }
	}

}
