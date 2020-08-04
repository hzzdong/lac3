package com.linkallcloud.core.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.domain.TreeDomain;
import com.linkallcloud.core.dto.born.DefaultDtoBorning;
import com.linkallcloud.core.dto.born.DtoBorning;
import com.linkallcloud.core.dto.born.DtoBorns;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.vo.Vo;

public class Domains {
	private static Log log = Logs.get();

	public static final int USER_NORMAL = 0;// 普通用户
	public static final int USER_ADMIN = 9;// 管理员

	public static final int ROLE_NORMAL = 0;// 公司角色（局部，公司内部有效）
	public static final int ROLE_SYSTEM = 9;// 系统角色（全局）

	public static final String ROOT_YW_COMPANY_GOVCODE = "0";
	public static final String SYS_ADMIN_ROLE_STUFF = "_sys_admin";

	/*-
	 * 根据父domain，生成自己的code
	 *
	 * id,Party,code
	 * 1,公司,1#
	 * 2,公司,1#2#
	 * 3,部门,1#3-
	 * 4,人员,1#4@
	 * 5,部门,1#3-5-
	 * 6,人员,1#3-5-6@
	 *
	 * @param parent
	 * @return
	 */
	public static String genDomainCode(TreeDomain parent, TreeDomain node) {
		if (parent != null) {
			return parent.getCode() + node.getId() + node.codeTag();
		} else {
			return node.getId() + node.codeTag();
		}
	}

	public static Long parseMyRootDepartmentId(String code) {// 1#6#68-24-100-200-39@
		if (code != null) {
			int idx = code.indexOf("-");
			int lidx = code.lastIndexOf("#");
			if (idx != -1 && lidx != -1) {
				return Long.valueOf(code.substring(lidx + 1, idx));// 68
			}
		}
		return null;
	}

	/**
	 * 根据自己code得到公司的id
	 *
	 * @param code
	 * @return
	 */
	public static Long parseMyRootCompanyId(String code) {// ?#?#?#?#
		if (code != null) {
			int idx = code.indexOf("#");
			if (idx != -1) {
				return Long.valueOf(code.substring(0, idx));
			}
		}
		return null;
	}

	/**
	 * 根据自己code得到公司的id
	 *
	 * @param code
	 * @return
	 */
	public static Long parseMyCompanyId(String code) {
		String companyCode = Domains.parseMyCompanyCode(code);// ?#?#?#?#
		if (companyCode != null) {
			int idx = companyCode.indexOf("#");
			if (idx != -1) {
				if (idx == companyCode.length() - 1) {// ?#
					return Long.valueOf(companyCode.substring(0, idx));
				} else {// ?#?#?#?#
					String temp = companyCode.substring(0, companyCode.length() - 1);// ?#?#?#?
					int lidx = temp.lastIndexOf("#");
					if (lidx != -1) {
						return Long.valueOf(temp.substring(lidx + 1));
					}
				}
			}
		}
		return null;
	}

	/**
	 * 根据自己code得到公司的code
	 *
	 * @param myCode
	 * @return ?#?#
	 */
	public static String parseMyCompanyCode(String myCode) {
		if (!Strings.isBlank(myCode)) {
			int myIdx = myCode.lastIndexOf("#");
			if (myIdx != -1) {
				return myCode.substring(0, myIdx + 1);
			}
		}
		return null;
	}

	/**
	 * @param uuidIds
	 * @return
	 */
	public static <PK extends Serializable> List<PK> parseIds(Map<String, PK> uuidIds) {
		if (uuidIds != null && !uuidIds.isEmpty()) {
			List<PK> ids = new ArrayList<PK>();
			for (String uuid : uuidIds.keySet()) {
				ids.add(uuidIds.get(uuid));
			}
			return ids;
		}
		return null;
	}

	/**
	 * 判断doms中是否存在id==existId的domain
	 *
	 * @param existId
	 * @param doms
	 * @return
	 */
	public static boolean exist(Long existId, List<? extends Domain> doms) {
		if (existId != null && doms != null && !doms.isEmpty()) {
			for (Domain d : doms) {
				if (d.getId().equals(existId)) {
					return true;
				}
			}
		}
		return false;
	}

	public static <T extends Domain> List<Long> getIds(List<T> doms) {
		List<Long> ids = new ArrayList<Long>();
		if (doms != null && !doms.isEmpty()) {
			for (T dom : doms) {
				ids.add(dom.getId());
			}
		}
		return ids;
	}

	public static <T extends Domain> List<String> getUuids(List<T> doms) {
		List<String> ids = new ArrayList<String>();
		if (doms != null && !doms.isEmpty()) {
			for (T dom : doms) {
				ids.add(dom.getUuid());
			}
		}
		return ids;
	}

	public static <T extends Domain> Map<Long, T> getIdMap(List<T> entitys) {
		return entitys.stream().collect(Collectors.toMap(T::getId, Function.identity(), (key1, key2) -> key2));
	}

	// public static <T extends Domain<Long>> Map<Long, T> getLongIdMap(List<T>
	// entitys) {
	// return entitys.stream().collect(Collectors.toMap(T::getId,
	// Function.identity(), (key1, key2) -> key2));
	// }

	/**
	 * Domain转换成Vo,自动查找合适的构造器或者合适的静态构造方法
	 *
	 * @param domain
	 * @param classV
	 * @return
	 */
	public static <E extends Domain, V extends Vo> V castDomain2Vo(E domain, Class<V> classV) {
		return castDomain2Vo(domain, classV, new Object[] { domain });
	}

	/**
	 * vo转换成DTO,根据提供的构造参数，自动查找合适的构造器或者合适的静态构造方法
	 *
	 * @param domain
	 * @param classV
	 * @param createArgs createArgs[0]预留给domain。即构造参数数组的第一个参数约定为domain
	 * @return
	 */
	public static <E extends Domain, V extends Vo> V castDomain2Vo(E domain, Class<V> classV, Object[] createArgs) {
		return castDomain2Vo(domain, classV, null, createArgs);
	}

	/**
	 * vo转换成DTO,根据提供的构造参数，自动查找合适的构造器或者合适的静态构造方法
	 *
	 * @param domain
	 * @param classV
	 * @param methodName
	 * @param createArgs createArgs[0]预留给domain。即构造参数数组的第一个参数约定为domain
	 * @return
	 */
	public static <E extends Domain, V extends Vo> V castDomain2Vo(E domain, Class<V> classV, String methodName,
			Object[] createArgs) {
		return Domain.domain2Vo(domain, classV, methodName, createArgs);
	}

	/**
	 * Vo转换成Domain,根据提供的构造参数，自动查找合适的构造器或者合适的静态构造方法
	 *
	 * @param vo
	 * @param classe
	 * @return
	 */
	public static <E extends Domain, V extends Vo> E castVo2Domain(V vo, Class<E> classe) {
		return castVo2Domain(vo, classe, new Object[] { vo });
	}

	/**
	 * Vo转换成Domain,根据提供的构造参数，自动查找合适的构造器或者合适的静态构造方法
	 *
	 * @param vo
	 * @param classe
	 * @param createArgs createArgs[0]预留给vo。即构造参数数组的第一个参数约定为vo
	 * @return
	 */
	public static <E extends Domain, V extends Vo> E castVo2Domain(V vo, Class<E> classe, Object[] createArgs) {
		return castVo2Domain(vo, classe, null, createArgs);
	}

	/**
	 * Vo转换成Domain,根据提供的构造参数，自动查找合适的构造器或者合适的静态构造方法
	 *
	 * @param vo
	 * @param classe
	 * @param methodName
	 * @param createArgs createArgs[0]预留给vo。即构造参数数组的第一个参数约定为vo
	 * @return
	 */
	public static <E extends Domain, V extends Vo> E castVo2Domain(V vo, Class<E> classe, String methodName,
			Object[] createArgs) {
		return Domain.vo2Domain(vo, classe, methodName, createArgs);
	}

	/**
	 * domains转换成Vo,根据提供的构造参数，自动查找合适的构造器或者合适的静态构造方法
	 *
	 * @param <E>
	 * @param <V>
	 * @param domains
	 * @param classOfDTO
	 * @return DTO列表
	 */
	public static <E extends Domain, V extends Vo> List<V> castDomains2Vos(List<E> domains, Class<V> classOfDTO) {
		return castDomains2Vos(domains, classOfDTO, new Object[1]);
	}

	/**
	 * domains转换成Vo,根据提供的构造参数，自动查找合适的构造器或者合适的静态构造方法
	 *
	 * @param domains
	 * @param classOfDTO
	 * @param createArgs createArgs[0]预留给domain。即构造参数数组的第一个参数约定为domain
	 * @return
	 */
	public static <E extends Domain, V extends Vo> List<V> castDomains2Vos(List<E> domains, Class<V> classOfDTO,
			Object[] createArgs) {
		return castDomains2DTOs(domains, classOfDTO, null, createArgs);
	}

	/**
	 * domains转换成Vo,根据提供的构造参数，自动查找合适的构造器或者合适的静态构造方法
	 *
	 * @param domains
	 * @param classV
	 * @param methodName
	 * @param createArgs createArgs[0]预留给domain。即构造参数数组的第一个参数约定为domain
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <E extends Domain, V extends Vo> List<V> castDomains2DTOs(List<E> domains, Class<V> classV,
			String methodName, Object[] createArgs) {
		List<V> vos = null;
		if (null != domains && domains.size() > 0) {
			vos = new ArrayList<V>();
			createArgs[0] = domains.get(0);
			Class<?>[] createArgTypes = Mirror.evalToTypes(createArgs);
			DtoBorning borning = DtoBorns.evalBorning(classV, domains.get(0), methodName, createArgTypes);
			if (null != borning) {
				if (borning instanceof DefaultDtoBorning) {
					try {
						for (E domain : domains) {
							V vo = (V) domain.toVo(classV);
							vos.add(vo);
						}
					} catch (Throwable e1) {
						log.error(e1.getMessage(), e1);
					}
				} else {
					try {
						for (E domain : domains) {
							createArgs[0] = domain;
							V vo = (V) borning.born(createArgs);
							vos.add(vo);
						}
					} catch (Throwable e) {
						log.error(e.getMessage(), e);
					}
				}
			} else {
				Mirror<V> mirror = Mirror.me(classV);
				try {
					for (E domain : domains) {
						V vo = mirror.born();
						BeanUtils.copyProperties(domain, vo);
						vos.add(vo);
					}
				} catch (Throwable e) {
				}
			}
		}
		return vos;
	}

	/**
	 * Vos转换成domains,根据提供的构造参数，自动查找合适的构造器或者合适的静态构造方法
	 *
	 * @param vos
	 * @param classE
	 * @return
	 */
	public static <E extends Domain, V extends Vo> List<E> castVos2Domains(List<V> vos, Class<E> classE) {
		return castVos2Domains(vos, classE, new Object[1]);
	}

	/**
	 * Vos转换成domains,根据提供的构造参数，自动查找合适的构造器或者合适的静态构造方法
	 *
	 * @param vos
	 * @param classE
	 * @param createArgs
	 * @return
	 */
	public static <E extends Domain, V extends Vo> List<E> castVos2Domains(List<V> vos, Class<E> classE,
			Object[] createArgs) {
		return castVos2Domains(vos, classE, null, createArgs);
	}

	/**
	 * Vos转换成domains,根据提供的构造参数，自动查找合适的构造器或者合适的静态构造方法
	 *
	 * @param vos
	 * @param classE
	 * @param methodName
	 * @param createArgs
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <E extends Domain, V extends Vo> List<E> castVos2Domains(List<V> vos, Class<E> classE,
			String methodName, Object[] createArgs) {
		List<E> domains = null;
		if (null != vos && vos.size() > 0) {
			domains = new ArrayList<E>();
			createArgs[0] = vos.get(0);
			Class<?>[] createArgTypes = Mirror.evalToTypes(createArgs);
			DtoBorning borning = DtoBorns.evalBorning(classE, vos.get(0), methodName, createArgTypes);
			if (null != borning) {
				try {
					for (V vo : vos) {
						createArgs[0] = vo;
						E domain = (E) borning.born(createArgs);
						domains.add(domain);
					}
				} catch (Throwable e) {
					log.error(e.getMessage(), e);
				}
			} else {
				Mirror<E> mirror = Mirror.me(classE);
				try {
					for (V vo : vos) {
						E domain = mirror.born();
						BeanUtils.copyProperties(vo, domain);
						domains.add(domain);
					}
				} catch (Throwable e) {
				}
			}
		}
		return domains;
	}

	public static String convertCompanyType(String companyType) {
		if ("Yw".equals(companyType)) {
			return "YwCompany";
		} else if ("Kh".equals(companyType)) {
			return "KhCompany";
		}
		return companyType;
	}

	public static boolean compareCompanyType(String type1, String type2) {
		if (!Strings.isBlank(type1) && !Strings.isBlank(type2)) {
			if (type1.equals(type2)) {
				return true;
			} else {
				if (("Yw".equals(type1) || "YwCompany".equals(type1))
						&& ("Yw".equals(type2) || "YwCompany".equals(type2))) {
					return true;
				} else if (("Kh".equals(type1) || "KhCompany".equals(type1))
						&& ("Kh".equals(type2) || "KhCompany".equals(type2))) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean comparePartyType(String type1, String type2) {
		if (!Strings.isBlank(type1) && !Strings.isBlank(type2)) {
			if (type1.equals(type2)) {
				return true;
			} else {
				if (type1.startsWith("Yw") && type2.startsWith("Yw")) {
					return true;
				} else if (type1.startsWith("Kh") && type2.startsWith("Kh")) {
					return true;
				}
			}
		}
		return false;
	}

}
