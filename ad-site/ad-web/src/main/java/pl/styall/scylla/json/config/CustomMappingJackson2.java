package pl.styall.scylla.json.config;

import java.io.IOException;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.Assert;


import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomMappingJackson2 extends MappingJackson2HttpMessageConverter {
	
	private ObjectMapper objectMapper = new ObjectMapper();
	private boolean prefixJson;
	
	@Override
	protected void writeInternal(Object object, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {

		JsonEncoding encoding = getJsonEncoding(outputMessage.getHeaders().getContentType());
		JsonGenerator jsonGenerator =
				this.objectMapper.getJsonFactory().createJsonGenerator(outputMessage.getBody(), encoding);
		try {
			if (this.prefixJson) {
				jsonGenerator.writeRaw("{} && ");
			}
			if(object instanceof ResponseBodyWrapper){
				ResponseBodyWrapper responseBody = (ResponseBodyWrapper) object;
				this.objectMapper.writerWithView(responseBody.getView()).writeValue(jsonGenerator, responseBody.getObject());
			}else{
				this.objectMapper.writeValue(jsonGenerator, object);
			}
		}
		catch (IOException ex) {
			throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
		}
	}
	
	public void setObjectMapper(ObjectMapper objectMapper) {
		Assert.notNull(objectMapper, "ObjectMapper must not be null");
		this.objectMapper = objectMapper;
		super.setObjectMapper(objectMapper);
	}
	
	public ObjectMapper getObjectMapper() {
		return this.objectMapper;
	}
	
	public void setPrefixJson(boolean prefixJson) {
		this.prefixJson = prefixJson;
		super.setPrefixJson(prefixJson);
	}
}
