package org.highmed.dsf.bpe;

import static de.rwh.utils.jetty.JettyServer.forwardedSecureRequestCustomizer;
import static de.rwh.utils.jetty.JettyServer.httpConfiguration;
import static de.rwh.utils.jetty.JettyServer.httpConnector;
import static de.rwh.utils.jetty.JettyServer.start;
import static de.rwh.utils.jetty.JettyServer.statusCodeOnlyErrorHandler;
import static de.rwh.utils.jetty.JettyServer.webInfClassesDirs;
import static de.rwh.utils.jetty.JettyServer.webInfJars;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.glassfish.jersey.servlet.init.JerseyServletContainerInitializer;
import org.highmed.dsf.tools.db.DbMigrator;
import org.springframework.web.SpringServletContainerInitializer;

import de.rwh.utils.jetty.JettyServer;
import de.rwh.utils.jetty.Log4jInitializer;
import de.rwh.utils.jetty.PropertiesReader;

public class BpeJettyServer
{
	public static void main(String[] args)
	{
		Properties properties = PropertiesReader.read(Paths.get("conf/jetty.properties"), StandardCharsets.UTF_8);

		Log4jInitializer.initializeLog4j(properties);

		Properties dbProperties = PropertiesReader.read(Paths.get("conf/db.properties"), StandardCharsets.UTF_8);
		DbMigrator dbMigrator = new DbMigrator(dbProperties, "db.camunda_users_group", "db.camunda_user",
				"db.camunda_user_password");
		DbMigrator.retryOnConnectException(3, dbMigrator::migrate);

		HttpConfiguration httpConfiguration = httpConfiguration(forwardedSecureRequestCustomizer());
		Function<Server, ServerConnector> connector = httpConnector(httpConfiguration, properties);

		Properties initParameter = PropertiesReader.read(Paths.get("conf/config.properties"), StandardCharsets.UTF_8);

		Predicate<String> filter = s -> s.contains("bpe-server");
		Stream<String> webInfClassesDirs = webInfClassesDirs(filter);
		Stream<String> webInfJars = webInfJars(filter);

		List<Class<?>> initializers = Arrays.asList(SpringServletContainerInitializer.class,
				JerseyServletContainerInitializer.class);

		ErrorHandler errorHandler = statusCodeOnlyErrorHandler();

		JettyServer server = new JettyServer(connector, errorHandler, "/bpe", initializers, initParameter,
				webInfClassesDirs, webInfJars/* , AuthenticationFilter.class */);

		start(server);
	}
}
