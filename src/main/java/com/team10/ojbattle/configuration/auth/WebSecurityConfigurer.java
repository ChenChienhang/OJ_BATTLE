package com.team10.ojbattle.configuration.auth;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

/**
 * @author: 陈健航
 * @description: Security授权配置主文件
 * @since: 2020/4/4 23:21
 * @version: 1.0
 */
@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("sysUserService")
    private UserDetailsService userDetailsService;

    @Autowired
    private MyOncePerRequestFilter myOncePerRequestFilter;

    @Autowired
    private MyAuthenticationEntryPointBase myAuthenticationEntryPoint;

    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;

    /**
     * 登录成功处理器
     */
    @Autowired
    private MyAuthenticationSuccessHandlerBase myAuthenticationSuccessHandler;

    /**
     * 登录失败处理器
     */
    @Autowired
    private MyAuthenticationFailureHandlerBase myAuthenticationFailureHandler;

    /**
     * 退出登录处理器
     */
    @Autowired
    private MyLogoutHandler myLogoutHandler;

    /**
     * 退出登录成功处理器
     */
    @Autowired
    private MyLogoutSuccessHandler myLogoutSuccessHandler;

    /**
     * 加密器
     */
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    /**
     * 从容器中取出 AuthenticationManagerBuilder，执行方法里面的逻辑之后，放回容器
     *
     * @param authenticationManagerBuilder authenticationManagerBuilder
     * @throws Exception
     */
    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //解决跨域问题。cors 预检请求放行,让Spring security 放行所有preflight request（cors 预检请求）
        http.authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll();

        //让Security永远不会创建HttpSession，它不会使用HttpSession来获取SecurityContext
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().headers().cacheControl();


        //请求权限配置
        //放行注册API请求，其它任何请求都必须经过身份验证.
        http.authorizeRequests()
                //所有注册和登录的请求都可以通过
                .antMatchers(HttpMethod.POST, "/user/register").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.GET, "/user/reg_email").permitAll()
                .antMatchers(HttpMethod.PUT, "/user/reset").permitAll()
                .antMatchers(HttpMethod.GET, "/user/top_list").permitAll()
                .antMatchers(HttpMethod.GET, "/user/find_email").permitAll()
                .antMatchers(HttpMethod.GET, "/battle/match").permitAll()
                .antMatchers(HttpMethod.GET, "/battle/on").permitAll()
                .anyRequest().access("@dynamicPermission.checkPermission(request,authentication)");

        //拦截账号、密码。覆盖 UsernamePasswordAuthenticationFilter过滤器
        http.addFilterAt(myUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        //拦截token，并检测。在 UsernamePasswordAuthenticationFilter 之前添加 JwtAuthenticationTokenFilter
        http.addFilterBefore(myOncePerRequestFilter, UsernamePasswordAuthenticationFilter.class);

        //处理异常情况：认证失败和权限不足
        http.exceptionHandling().authenticationEntryPoint(myAuthenticationEntryPoint).accessDeniedHandler(myAccessDeniedHandler);

        //登录,因为使用前端发送JSON方式进行登录，所以登录模式不设置也是可以的。
        http.formLogin();

        //退出
        http.logout().addLogoutHandler(myLogoutHandler).logoutSuccessHandler(myLogoutSuccessHandler);
    }

    /**
     * 手动注册账号、密码拦截器
     *
     * @return
     * @throws Exception
     */
    @Bean
    MyUsernamePasswordAuthenticationFilter myUsernamePasswordAuthenticationFilter() throws Exception {
        MyUsernamePasswordAuthenticationFilter filter = new MyUsernamePasswordAuthenticationFilter();
        //成功后处理
        filter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        //失败后处理
        filter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);

        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }
}