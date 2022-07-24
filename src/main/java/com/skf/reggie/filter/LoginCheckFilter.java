package com.skf.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.skf.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * desc:
 *
 * @author: skf
 * @date: 2022/07/24
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    // 路径匹配器
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 1. 获取uri
        String requestURI = request.getRequestURI();
        log.info("拦截到请求 {}", requestURI);
        // 定义不需要处理的请求路径
        String[] urls = {"/employee/login", "/employee/logout", "/backend/**", "/front/**"};
        // 2. 判断本次请求是否需要处理
        boolean flag = checkUri(urls, requestURI);
        // 3. 如果不需要处理，直接放行
        if (flag) {
            log.info("本次请求{}不需要处理", requestURI);
            filterChain.doFilter(request, response);
            return;
        }
        // 4. 判定登录状态，如果已登录，则放行
        if (Objects.nonNull(request.getSession().getAttribute("employee"))) {
            log.info("用户已登录, 用户id为:{}", request.getSession().getAttribute("employee"));
            filterChain.doFilter(request, response);
            return;
        }
        // 5. 配合前端响应拦截器, 写回数据, 提示未登录
        log.info("用户未登录");
        //ServletOutputStream outputStream = response.getOutputStream();
        R r = R.error("NOTLOGIN");
        //outputStream.print(JSONObject.toJSONString(r));
        response.getWriter().write(JSON.toJSONString(r));
    }

    private boolean checkUri(String[] urls, String requestURI) {
        for (String url : urls) {
            if (PATH_MATCHER.match(url, requestURI)) {
                return true;
            }
        }
        return false;
    }
}
