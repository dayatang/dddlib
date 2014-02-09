package org.openkoala.koala.web.cxf.interceptor;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.openkoala.auth.application.domain.IpBlackWhiteListApplication;
import org.openkoala.auth.application.domain.SecuritySettingsApplication;
import org.openkoala.auth.application.vo.IpBlackWhiteListVO;

/**
 * IP黑白名单拦截器
 * 
 * @author zyb
 * @since 2013-6-26 下午3:10:57
 */
public class IpInterceptor extends AbstractPhaseInterceptor<Message> {

	@Inject
	private SecuritySettingsApplication securitySettingsApplication;

	@Inject
	private IpBlackWhiteListApplication ipBlackWhiteListApplication;

	public IpInterceptor() {
		super(Phase.PRE_INVOKE);
	}

	public void handleMessage(Message message) throws Fault {
		String ip = getIp(message);

		if (!isIpFilterEnabled()) {
			message.getInterceptorChain().doIntercept(message);
		}

		if (!isIpFilterEnabled()) {
			message.getInterceptorChain().doIntercept(message);
		}

		if (isInSingleWhiteList(ip)) {
			message.getInterceptorChain().doIntercept(message);
		}

		if (isInWhiteListSection(ip)) {
			message.getInterceptorChain().doIntercept(message);
		}

		if (isInSingleBlackList(ip)) {
			 throw new RuntimeException("This ip [" + ip + "] is denied to access.");
		}

		if (isInBlackListSection(ip)) {
			throw new RuntimeException("This ip [" + ip + "] is denied to access.");
		}

	}

	/**
	 * 是否在单个白名单中
	 * 
	 * @param ip
	 * @return
	 */
	private boolean isInSingleWhiteList(String ip) {
		for (IpBlackWhiteListVO ipBlackWhiteListVO : ipBlackWhiteListApplication.getWhiteListSingle()) {
			if (ip.equals(ipBlackWhiteListVO.getStart())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否在单个黑名单中
	 * 
	 * @param ip
	 * @return
	 */
	private boolean isInSingleBlackList(String ip) {
		for (IpBlackWhiteListVO ipBlackWhiteListVO : ipBlackWhiteListApplication.getBlackListSingle()) {
			if (ip.equals(ipBlackWhiteListVO.getStart())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否在白名单IP段中
	 * 
	 * @param ip
	 * @return
	 */
	private boolean isInWhiteListSection(String ip) {
		for (IpBlackWhiteListVO ipBlackWhiteListVO : ipBlackWhiteListApplication.getWhiteListSection()) {
			if (isIpValid(ipBlackWhiteListVO.getStart() + "-" + ipBlackWhiteListVO.getEnd(), ip)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否在黑名单IP段中
	 * 
	 * @param ip
	 * @return
	 */
	private boolean isInBlackListSection(String ip) {
		for (IpBlackWhiteListVO ipBlackWhiteListVO : ipBlackWhiteListApplication.getBlackListSection()) {
			if (isIpValid(ipBlackWhiteListVO.getStart() + "-" + ipBlackWhiteListVO.getEnd(), ip)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否启用IP过滤
	 * 
	 * @return
	 */
	private boolean isIpFilterEnabled() {
		return !(securitySettingsApplication.findAll().isEmpty() || !securitySettingsApplication.findAll().get(0)
				.isIpFilterEnabled());
	}

	/**
	 * 获取IP
	 * 
	 * @param message
	 * @return
	 */
	private String getIp(Message message) {
		return ((HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST)).getRemoteAddr();
	}

	/**
	 * 判断某个IP是否在一个IP网段内
	 * 
	 * @param ipSection
	 * @param ip
	 * @return
	 */
	private boolean isIpValid(String ipSection, String ip) {
		ipSection = ipSection.trim();
		ip = ip.trim();
		final String REGX_IP = "((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)";
		final String REGX_IPB = REGX_IP + "\\-" + REGX_IP;
		if (!ipSection.matches(REGX_IPB) || !ip.matches(REGX_IP)) {
			return false;
		}
		int idx = ipSection.indexOf('-');
		String[] sips = ipSection.substring(0, idx).split("\\.");
		String[] sipe = ipSection.substring(idx + 1).split("\\.");
		String[] sipt = ip.split("\\.");
		long ips = 0L, ipe = 0L, ipt = 0L;
		for (int i = 0; i < 4; i++) {
			ips = ips << 8 | Integer.parseInt(sips[i]);
			ipe = ipe << 8 | Integer.parseInt(sipe[i]);
			ipt = ipt << 8 | Integer.parseInt(sipt[i]);
		}
		if (ips > ipe) {
			long t = ips;
			ips = ipe;
			ipe = t;
		}
		return ips <= ipt && ipt <= ipe;
	}

}
