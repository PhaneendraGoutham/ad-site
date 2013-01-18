package pl.styall.scylla.json.config;

import javax.annotation.PostConstruct;

import org.joda.time.LocalTime;
import org.springframework.stereotype.Component;

import pl.styall.librarycore.ext.json.serializer.CustomTimeSerializer;
import pl.styall.librarycore.ext.json.serializer.PointSerializer;



import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

@Component("jacksonObjectMapper")
public class CustomObjectMapper extends ObjectMapper {

	public CustomObjectMapper() {
		Hibernate4Module hm = new Hibernate4Module();
		registerModule(hm);
	}


	@PostConstruct
	public void afterPropertiesSet() throws Exception {
		SimpleModule simpleModule = new SimpleModule("MyModule", new Version(1,
				0, 0, "com.fasterxml.jackson.core", "jackson-core", "2.0.4"));
		simpleModule.addSerializer(LocalTime.class, new CustomTimeSerializer());
		registerModule(simpleModule);
	}
	
}
