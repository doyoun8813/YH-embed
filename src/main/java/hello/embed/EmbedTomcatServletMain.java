package hello.embed;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

import hello.servlet.HelloServlet;

public class EmbedTomcatServletMain {

	public static void main(String[] args) throws LifecycleException {
		System.out.println("EmbedTomcatServletMain.main");

		//톰캣 설정
		Tomcat tomcat = new Tomcat();
		Connector connector = new Connector();
		connector.setPort(8080);
		tomcat.setConnector(connector);

		//서블릿 등록 contextPath는 별도 지정 안하면 ROOT가 default
		Context context = tomcat.addContext("", "/");

		//--내장 톰캣에 appBase인 webapps 폴더가 생성이 안되는 오류문제로 인한 추가 코드
		File docBaseFile = new File(context.getDocBase());
		if(!docBaseFile.isAbsolute()) {
			docBaseFile = new File(((org.apache.catalina.Host) context.getParent()).getAppBaseFile(), docBaseFile.getPath());
		}

		docBaseFile.mkdirs();
		//--

		tomcat.addServlet("", "helloServlet", new HelloServlet());
		context.addServletMappingDecoded("/hello-servlet", "helloServlet");
		tomcat.start();
	}
}
