package cn.hippo4j.springboot.starter.adapter.web;

import cn.hippo4j.adapter.web.JettyWebThreadPoolHandler;
import cn.hippo4j.adapter.web.TomcatWebThreadPoolHandler;
import cn.hippo4j.adapter.web.UndertowWebThreadPoolHandler;
import cn.hippo4j.adapter.web.WebThreadPoolRunStateHandler;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.UpgradeProtocol;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.Loader;
import org.eclipse.jetty.webapp.WebAppContext;
import io.undertow.Undertow;
import org.xnio.SslClientAuthMode;

import javax.servlet.Servlet;

/**
 * Web Thread Pool Handler Configuration
 **/
@Configuration(proxyBeanMethods = false)
public class WebThreadPoolHandlerConfiguration {

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({Servlet.class, Tomcat.class, UpgradeProtocol.class})
    @ConditionalOnBean(value = ServletWebServerFactory.class, search = SearchStrategy.CURRENT)
    static class EmbeddedTomcat {

        /**
         * Refer to the Tomcat loading source code .
         * This load is performed if the {@link Tomcat} class exists and
         * the Web embedded server loads the {@link ServletWebServerFactory} top-level interface type at the same time
         */
        @Bean
        public TomcatWebThreadPoolHandler tomcatWebThreadPoolHandler(WebThreadPoolRunStateHandler webThreadPoolRunStateHandler) {
            return new TomcatWebThreadPoolHandler(webThreadPoolRunStateHandler);
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({ Servlet.class, Server.class, Loader.class, WebAppContext.class })
    @ConditionalOnBean(value = ServletWebServerFactory.class, search = SearchStrategy.CURRENT)
    static class EmbeddedJetty {

        /**
         * Refer to the Server loading source code .
         * This load is performed if the {@link Server} class exists and
         * the Web embedded server loads the {@link ServletWebServerFactory} top-level interface type at the same time
         */
        @Bean
        public JettyWebThreadPoolHandler jettyWebThreadPoolHandler() {
            return new JettyWebThreadPoolHandler();
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({ Servlet.class, Undertow.class, SslClientAuthMode.class })
    @ConditionalOnBean(value = ServletWebServerFactory.class, search = SearchStrategy.CURRENT)
    static class EmbeddedUndertow {

        /**
         * Refer to the Undertow loading source code .
         * This load is performed if the {@link Undertow} class exists and
         * the Web embedded server loads the {@link ServletWebServerFactory} top-level interface type at the same time
         */
        @Bean
        public UndertowWebThreadPoolHandler undertowWebThreadPoolHandler() {
            return new UndertowWebThreadPoolHandler();
        }
    }
}
