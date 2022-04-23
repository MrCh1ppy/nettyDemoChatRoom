package pack.protocol.serilizer;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.jackson.Jacksonized;
import pack.meaage.Message;

import java.io.*;
import java.nio.charset.StandardCharsets;

public interface Serializer {

	<T> T deserialize(Class<T> clazz,byte[] bytes);

	<T> byte[] serialize(T object) throws RuntimeException;

	enum Algorithm implements Serializer{
		JDK{
			@Override
			public <T> T deserialize(Class<T> clazz, byte[] bytes) {
				try {
					var stream = new ObjectInputStream(new ByteArrayInputStream(bytes));
					return (T) stream.readObject();
				}catch (IOException|ClassNotFoundException e){
					e.printStackTrace();
					throw new RuntimeException("反序列化失败");
				}
			}

			@Override
			public <T> byte[] serialize(T object) {
				var byteArrayOutputStream = new ByteArrayOutputStream();
				try {
					var stream = new ObjectOutputStream(byteArrayOutputStream);
					stream.writeObject(object);
					return byteArrayOutputStream.toByteArray();
				}catch (IOException e){
					e.printStackTrace();
					throw new RuntimeException("序列化失败");
				}
			}
		},
		JSON{
			@Override
			public <T> T deserialize(Class<T> clazz, byte[] bytes) {
				try {
					return MAPPER.readValue(bytes, clazz);
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}

			@Override
			public <T> byte[] serialize(T object) throws RuntimeException {
				try {
					return MAPPER.writeValueAsString(object).getBytes(StandardCharsets.UTF_8);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		};
		public static final ObjectMapper MAPPER = new ObjectMapper();
	}
}
