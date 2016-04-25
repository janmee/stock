package com.janmee.stock.web.resolver;

import com.janmee.stock.base.StatusCode;
import com.seewo.core.base.Constants;
import com.seewo.core.util.HttpServletUtils;
import com.seewo.core.util.json.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理controller层异常
 *
 * @author chenyonghui
 * @version 1.0
 * @since 1.0
 */
public class JsonMappingExceptionResolver extends SimpleMappingExceptionResolver {

	private static Logger logger = LoggerFactory.getLogger(JsonMappingExceptionResolver.class);

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response,
			Object handler, Exception ex) {

//		logger.error("RequestURI:{}, Exception:{}", request.getRequestURI(), StackTraceUtils.getStackTrace(ex));

		String viewName = determineViewName(ex, request);
		if (viewName != null) {
			Integer statusCode = determineStatusCode(request, viewName);
			applyStatusCodeIfPossible(request, response, statusCode);
			Map<String, Object> resultMap = new HashMap<String, Object>(2);
			resultMap.put(Constants.STATUS_CODE, statusCode);
			resultMap.put(Constants.MESSAGE, StatusCode.getMessage(statusCode));
			String json = JsonUtils.mapToJson(resultMap);
			response.setStatus(statusCode / 100);
			HttpServletUtils.render(response, "application/json", json);
			return null;
		} else {
			return super.doResolveException(request, response, handler, ex);
		}
	}
}