package com.joolun.common.sensitive;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.joolun.common.utils.SensitiveUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Objects;

/**
 * @author JooLun
 * @Author: https://www.cnblogs.com/xiluonanfeng/p/10183926.html
 * 脱敏序列化
 */
@NoArgsConstructor
@AllArgsConstructor
public class SensitiveSerialize extends JsonSerializer<String> implements ContextualSerializer {

	private SensitiveTypeEnum type;

	@Override
	public void serialize(final String originStr, final JsonGenerator jsonGenerator,
                          final SerializerProvider serializerProvider) throws IOException {
		switch (type) {
		case CHINESE_NAME:
			jsonGenerator.writeString(SensitiveUtils.chineseName(originStr));
			break;
		case MOBILE_PHONE:
			jsonGenerator.writeString(SensitiveUtils.mobilePhone(originStr));
			break;
		case EMAIL:
			jsonGenerator.writeString(SensitiveUtils.email(originStr));
			break;
		case PASSWORD:
			jsonGenerator.writeString(SensitiveUtils.password(originStr));
			break;
		case KEY:
			jsonGenerator.writeString(SensitiveUtils.key(originStr));
			break;
		default:
			throw new IllegalArgumentException("未定义的敏感信息枚举类" + type);
		}
	}

	@Override
	public JsonSerializer<?> createContextual(final SerializerProvider serializerProvider, final BeanProperty beanProperty) throws JsonMappingException {
		if (beanProperty != null) {
			if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
				Sensitive sensitive = beanProperty.getAnnotation(Sensitive.class);
				if (sensitive == null) {
					sensitive = beanProperty.getContextAnnotation(Sensitive.class);
				}
				if (sensitive != null) {
					return new SensitiveSerialize(sensitive.type());
				}
			}
			return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
		}
		return serializerProvider.findNullValueSerializer(null);
	}

}
