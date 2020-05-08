package com.linkallcloud.web.face.base;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.domain.TreeDomain;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.BizException;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.face.message.request.IdFaceRequest;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.manager.ITreeManager;
import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.web.session.SessionUser;

public abstract class BaseTreeFace<T extends TreeDomain, S extends ITreeManager<T>> extends BaseFace<T, S> {

	public BaseTreeFace() {
		super();
	}

	@Face(simple = true)
	@RequestMapping(value = "/fetchParent", method = RequestMethod.POST)
	public @ResponseBody Object fetchParent(IdFaceRequest faceReq, Trace t, SessionUser su) {
		return doFetchParent(t, faceReq, su);
	}

	private Object doFetchParent(Trace t, IdFaceRequest faceReq, SessionUser su) {
		if (faceReq.getId() == null || Strings.isBlank(faceReq.getUuid())) {
			throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "参数错误");
		}
		T entity = doFetch(t, faceReq.getId(), faceReq.getUuid(), su);
		if (entity != null) {
			T parent = manager().fetchById(t, entity.getParentId());
			return convert(t, "fetch", faceReq, parent);
		}
		return null;
	}

	@Face(simple = true)
	@RequestMapping(value = "/fetchTreeNode", method = RequestMethod.POST)
	public @ResponseBody Object fetchTreeNode(IdFaceRequest faceReq, Trace t, SessionUser su) {
		return doFetchTreeNode(faceReq, t, su);
	}

	private Object doFetchTreeNode(IdFaceRequest faceReq, Trace t, SessionUser su) {
		if (faceReq.getId() == null || Strings.isBlank(faceReq.getUuid())) {
			throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "参数错误");
		}
		T entity = doFetch(t, faceReq.getId(), faceReq.getUuid(), su);
		return entity.toTreeNode();
	}

	@Face(simple = true)
	@RequestMapping(value = "/fetchParentTreeNode", method = RequestMethod.POST)
	public @ResponseBody Object fetchParentTreeNode(IdFaceRequest faceReq, Trace t, SessionUser su) {
		return doFetchParentTreeNode(faceReq, t, su);
	}

	private Object doFetchParentTreeNode(IdFaceRequest faceReq, Trace t, SessionUser su) {
		if (faceReq.getId() == null || Strings.isBlank(faceReq.getUuid())) {
			throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "参数错误");
		}
		T entity = doFetch(t, faceReq.getId(), faceReq.getUuid(), su);
		if (entity != null) {
			T parent = manager().fetchById(t, entity.getParentId());
			if (parent != null) {
				return parent.toTreeNode();
			}
		}
		return null;
	}

}
