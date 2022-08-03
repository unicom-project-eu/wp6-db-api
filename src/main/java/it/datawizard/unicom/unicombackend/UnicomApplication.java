package it.datawizard.unicom.unicombackend;

import it.datawizard.unicom.unicombackend.dataimport.DataImport;
import it.datawizard.unicom.unicombackend.fhir.UnicomFHIRServlet;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication
public class UnicomApplication implements CommandLineRunner {
	private static Logger LOG = LoggerFactory.getLogger(UnicomApplication.class);

	@Bean
	public ServletRegistrationBean unicomServletBean() {
		ServletRegistrationBean bean = new ServletRegistrationBean(new UnicomFHIRServlet(), "/fhir/*");
		bean.setLoadOnStartup(1);
		return bean;
	}

	public static void main(String[] args) {
		SpringApplication.run(UnicomApplication.class, args);
	}

	@Override
	public void run(String... args) {
		Options options = new Options();
		Option importOption = new Option("i", "import", true, "imports data for country");
		importOption.setRequired(false);
		importOption.setType(String.class);
		importOption.setArgName("country");
		options.addOption(importOption);

		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd = null;

		try {
			cmd = parser.parse(options, args);

			ArrayList<Thread> threads = new ArrayList<>();
			if (cmd.hasOption(importOption)) {
				Thread thread = new Thread(new DataImport(cmd.getOptionValue(importOption)));
				threads.add(thread);
				thread.run();
			}

			for (Thread thread: threads) {
				try {
					thread.join();
				} catch (InterruptedException e) {
					continue;
				}
			}
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp("utility-name", options);

			System.exit(1);
		}
	}
}