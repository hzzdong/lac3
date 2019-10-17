package com.linkallcloud.web.face;

import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashMap;

import javax.validation.UnexpectedTypeException;

import org.aspectj.lang.ProceedingJoinPoint;

import com.linkallcloud.core.aop.LacAspect;
import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.web.face.annotation.FdMode;
import com.linkallcloud.web.face.processor.IRequestProcessor;
import com.linkallcloud.web.face.processor.OriginalProcessor;
import com.linkallcloud.web.face.processor.PackageJsonProcessor;
import com.linkallcloud.web.face.processor.PackageXmlProcessor;
import com.linkallcloud.web.face.processor.SimpleJsonProcessor;
import com.linkallcloud.web.face.processor.SimpleXmlProcessor;
import com.linkallcloud.core.exception.BaseException;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.core.face.message.response.ErrorFaceResponse;

public abstract class FaceAspect extends LacAspect {

	private static HashMap<String, IRequestProcessor> processors = new HashMap<String, IRequestProcessor>() {
		private static final long serialVersionUID = -6721742187412510857L;
		{
			put(OriginalProcessor.class.getName(), new OriginalProcessor());
			put(SimpleJsonProcessor.class.getName(), new SimpleJsonProcessor());
			put(SimpleXmlProcessor.class.getName(), new SimpleXmlProcessor());
		}
	};

	// private PackageJsonProcessor packageJsonProcessor;
	// private PackageXmlProcessor packageXmlProcessor;

	public FaceAspect() {
		super();
	}

	// public void setPackageJsonProcessor(PackageJsonProcessor
	// packageJsonProcessor) {
	// this.packageJsonProcessor = packageJsonProcessor;
	// }
	//
	// public void setPackageXmlProcessor(PackageXmlProcessor packageXmlProcessor) {
	// this.packageXmlProcessor = packageXmlProcessor;
	// }

	protected abstract PackageJsonProcessor getPackageJsonProcessor();

	protected abstract PackageXmlProcessor getPackageXmlProcessor();

	protected String getVersion() {
		return "1.0";
	}

	public Object checkFace(ProceedingJoinPoint joinPoint) throws Throwable {
		Method method = getMethod(joinPoint);
		Face faceAnno = method.getAnnotation(Face.class);
		if (faceAnno == null) {
			return joinPoint.proceed();
		} else {
			Object result = null;
			IRequestProcessor processor = getRequestProcessor(faceAnno.mode(), faceAnno.simple());
			try {
				result = processor.handle(joinPoint, method, faceAnno);
			} catch (BaseException e) {
				log.error(e.getCode() + ":" + e.getMessage());
				result = new ErrorFaceResponse(e.getCode(), e.getMessage(), getVersion());
			} catch (BaseRuntimeException e) {
				log.error(e.getCode() + ":" + e.getMessage());
				result = new ErrorFaceResponse(e.getCode(), e.getMessage(), getVersion());
			} catch (UnexpectedTypeException e) {
				log.error(e.getMessage(), e);
				result = new ErrorFaceResponse("10000001", "请检测验证注解是否设置正确。", getVersion());
			} catch (UndeclaredThrowableException e) {
				log.error(e.getMessage(), e);
				Throwable be = e.getUndeclaredThrowable();
				if (be instanceof BaseException) {
					BaseException lace = (BaseException) be;
					result = new ErrorFaceResponse(lace.getCode(), lace.getMessage(), getVersion());
				} else {
					result = new ErrorFaceResponse("10000001", "未知错误,请联系管理员。", getVersion());
				}
			} catch (Throwable e) {
				log.error(e.getMessage(), e);
				result = new ErrorFaceResponse("10000001", "未知错误,请联系管理员。", getVersion());
			}
			return processor.packageResult(result, faceAnno);
		}
	}

	private IRequestProcessor getRequestProcessor(FdMode mode, boolean simple) {
		if (FdMode.ORIGINAL.equals(mode)) {
			return getRequestProcessorByName(OriginalProcessor.class.getName());
		} else if (FdMode.JSON.equals(mode)) {
			if (simple == true) {
				return getRequestProcessorByName(SimpleJsonProcessor.class.getName());
			} else {
				return getRequestProcessorByName(PackageJsonProcessor.class.getName());
			}
		} else if (FdMode.XML.equals(mode)) {
			if (simple == true) {
				return getRequestProcessorByName(SimpleXmlProcessor.class.getName());
			} else {
				return getRequestProcessorByName(PackageXmlProcessor.class.getName());
			}
		}
		return null;
	}

	private IRequestProcessor getRequestProcessorByName(String processorClassName) {
		if (processors.containsKey(processorClassName)) {
			return processors.get(processorClassName);
		} else {
			return syncProcessor(processorClassName);
		}
	}

	private synchronized IRequestProcessor syncProcessor(String processorClassName) {
		if (processors.containsKey(processorClassName)) {
			return processors.get(processorClassName);
		} else {
			if (PackageJsonProcessor.class.getName().equals(processorClassName)) {
				PackageJsonProcessor pjp = this.getPackageJsonProcessor();
				if (pjp != null) {
					processors.put(processorClassName, pjp);
					return pjp;
				}
				throw new BaseRuntimeException("10000001", "请配置PackageJsonProcessor进行处理");
			} else if (PackageXmlProcessor.class.getName().equals(processorClassName)) {
				PackageXmlProcessor pxp = this.getPackageXmlProcessor();
				if (pxp != null) {
					processors.put(processorClassName, pxp);
					return pxp;
				}
				throw new BaseRuntimeException("10000001", "请配置PackageXmlProcessor进行处理");
			} else if (SimpleJsonProcessor.class.getName().equals(processorClassName)) {
				SimpleJsonProcessor processor = new SimpleJsonProcessor();
				processors.put(processorClassName, processor);
				return processor;
			} else if (SimpleXmlProcessor.class.getName().equals(processorClassName)) {
				SimpleXmlProcessor processor = new SimpleXmlProcessor();
				processors.put(processorClassName, processor);
				return processor;
			} else if (OriginalProcessor.class.getName().equals(processorClassName)) {
				OriginalProcessor processor = new OriginalProcessor();
				processors.put(processorClassName, processor);
				return processor;
			} else {
				throw new BaseRuntimeException("10000001", "名称为：" + processorClassName + " 的Processor不存在。");
			}
		}
	}

}
