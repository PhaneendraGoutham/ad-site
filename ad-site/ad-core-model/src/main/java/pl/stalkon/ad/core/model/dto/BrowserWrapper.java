package pl.stalkon.ad.core.model.dto;

import java.io.IOException;
import java.util.List;

import org.springframework.core.GenericTypeResolver;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonSerialize(using = BrowserWrapper.BrowserSerializer.class)
public class BrowserWrapper<T > {

	protected final List<T> resultList;
	protected final Long total;

	public BrowserWrapper(List<T> resultList, Long total) {
		super();
		this.resultList = resultList;
		this.total = total;
	}

	public List<T> getResultList() {
		return resultList;
	}

	public Long getTotal() {
		return total;
	}

	@JsonIgnore
	private Class<?> getType() {
		return GenericTypeResolver.resolveTypeArgument(getClass(),
				BrowserWrapper.class);
	}

	private static class BrowserSerializer extends
			JsonSerializer<BrowserWrapper<?>> {

		@Override
		public void serialize(BrowserWrapper<?> value, JsonGenerator jgen,
				SerializerProvider sp) throws IOException,
				JsonProcessingException {
			String name = value.getType().getSimpleName() + "s";
			name = name.substring(0, 1).toLowerCase() + name.substring(1);
			jgen.writeStartObject();
			jgen.writeObjectField(name, value.getResultList());
			jgen.writeObjectField("total", value.getTotal());
			jgen.writeEndObject();
		}

	}

}
