package org.openkoala.koala.web.jaxrs.handler;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.cxf.jaxrs.ext.RequestHandler;
import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.openkoala.auth.application.domain.IpBlackWhiteListApplication;
import org.openkoala.auth.application.domain.SecuritySettingsApplication;
import org.openkoala.auth.application.vo.IpBlackWhiteListVO;

/**
 * IP黑白名单处理器
 * @author zyb
 * @since 2013-6-27 下午2:08:10
 */
public class IpRequestHandler implements RequestHandler {
	
	@Inject
	private SecuritySettingsApplication securitySettingsApplication;
	
	@Inject
	private IpBlackWhiteListApplication ipBlackWhiteListApplication;

	@Override
	public Response handleRequest(Message message, ClassResourceInfo resourceClass) {
		String ip = getIp(message);
		
		if (!isIpFilterEnabled()) {
			return null;
		}
		
		if (!isIpFilterEnabled()) {
			return null;
		}
		
		if (isInSingleWhiteList(ip)) {
			return null;
		}
		
		if (isInWhiteListSection(ip)) {
			return null;
		}
		
		if (isInSingleBlackList(ip)) {
			return buildResponse(Status.BAD_REQUEST, "This ip [" + ip + "] is denied to access.");
		}
		
		if (isInBlackListSection(ip)) {
			return buildResponse(Status.BAD_REQUEST, "This ip [" + ip + "] is denied to access.");
		}
		
		return null;
	}

	private Response buildResponse(Status status, String message) {
		return Response.status(status).entity(new KoalaJAXRSFault("", message))
				.type(MediaType.APPLICATION_XML).build();
	}

	/**
	 * 是否在单个白名单中
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
	 * @return
	 */
	private boolean isIpFilterEnabled() {
		return !(securitySettingsApplication.findAll().isEmpty() || 
				!securitySettingsApplication.findAll().get(0).isIpFilterEnabled());
	}

	/**
	 * 获取IP
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
