package com.janwes.filter;

import com.alibaba.fastjson.JSON;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @author Janwes
 * @version 1.0
 * @package com.janwes.filter
 * @date 2021/7/11 14:40
 * @description 过滤器
 */
public class UserFilter implements Filter {

    public List<String> params = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 获取指定web.xml配置文件的过滤器init-param初始化参数
        String initParameter = filterConfig.getInitParameter("requestBody");
        if (initParameter != null) {
            if (!initParameter.contains("|") && !initParameter.contains(",")) {
                params.add(initParameter);
            }
            if (initParameter.contains("|")) {
                List<String> list = Arrays.asList(initParameter.split("\\|"));
                params.addAll(list);
            }
            if (initParameter.contains(",")) {
                List<String> list = Arrays.asList(initParameter.split(","));
                params.addAll(list);
            }
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 获取请求方法
        String method = request.getMethod();

        if ("POST".equals(method)) {
            String contentType = request.getContentType();
            if (contentType.equals("application/json")) {
                Map<String, String> data = parseJsonData(request);
                if (data != null) {
                    for (String key : data.keySet()) {
                        if (isExist(data.get(key))) {
                            // 如果存在 则返回403提示
                            response.sendError(403, "you request is forbidden");
                            return;
                        }
                    }
                }
            } else if (contentType.equals("application/x-www-form-urlencoded")) {
                List<String> paramList = parseRequestParam(request);
                if (isContains(paramList)) {
                    // 如果存在 则返回403提示
                    response.sendError(403, "you request is forbidden");
                    return;
                }
            } else if (contentType.contains("form-data")) {
                // 表单提交请求
            }
        }
        // 放行
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }

    /**
     * 判断post请求参数中是否存在过滤器初始化参数
     *
     * @param value
     * @return
     */
    private boolean isExist(String value) {
        for (String param : params) {
            if (value.contains(param)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断post请求参数中是否存在过滤器初始化参数
     *
     * @param paramList
     * @return
     */
    private boolean isContains(List<String> paramList) {
        if (paramList != null && paramList.size() > 0) {
            for (String s : paramList) {
                for (String param : params) {
                    if (s.contains(param)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取post请求参数
     *
     * @param request
     * @return
     */
    private List<String> parseRequestParam(HttpServletRequest request) {
        List<String> paramList = new ArrayList<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            paramList.add(request.getParameter(paramName));
        }
        return paramList;
    }

    /**
     * 获取post请求体的json数据
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    private Map<String, String> parseJsonData(HttpServletRequest request) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return JSON.parseObject(sb.toString(), Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
