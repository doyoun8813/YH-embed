package hello.boot;

import java.io.File;
import java.util.List;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class MySpringApplication {

	public static void run(Class configClass, String[] args) {
		System.out.println("MySpringApplication.run args= " + List.of(args));

		//톰캣 설정
		Tomcat tomcat = new Tomcat();
		Connector connector = new Connector();
		connector.setPort(8080);
		tomcat.setConnector(connector);

		//스프링 컨테이너 생성
		final AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();

		appContext.register(configClass);

		//스프링 MVC 디스패쳐 서블릿 생성, 스프링 컨테이너 연결
		final DispatcherServlet dispatcher = new DispatcherServlet(appContext);

		//톰캣 context 설정
		Context context = tomcat.addContext("", "/");

		//--내장 톰캣에 appBase인 webapps 폴더가 생성이 안되는 오류문제로 인한 추가 코드
		File docBaseFile = new File(context.getDocBase());
		if(!docBaseFile.isAbsolute()) {
			docBaseFile = new File(((org.apache.catalina.Host) context.getParent()).getAppBaseFile(), docBaseFile.getPath());
		}

		docBaseFile.mkdirs();
		//--

		//디스패쳐 서블릿 등록
		tomcat.addServlet("", "dispatcher", dispatcher);
		context.addServletMappingDecoded("/", "dispatcher");

		try {
			tomcat.start();
		} catch (LifecycleException e) {
			e.printStackTrace();
		}
	}
}
