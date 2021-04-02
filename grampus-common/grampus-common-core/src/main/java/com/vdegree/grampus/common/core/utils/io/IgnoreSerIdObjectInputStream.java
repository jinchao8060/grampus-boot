package com.vdegree.grampus.common.core.utils.io;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * 忽略序列化 id 的 jdk 对象序列化
 *
 * @author Beck
 */
@Slf4j
public class IgnoreSerIdObjectInputStream extends ObjectInputStream {

	public IgnoreSerIdObjectInputStream(byte[] bytes) throws IOException {
		this(new ByteArrayInputStream(bytes));
	}

	public IgnoreSerIdObjectInputStream(InputStream in) throws IOException {
		super(in);
	}

	@Override
	protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
		// initially streams descriptor
		ObjectStreamClass resultClassDescriptor = super.readClassDescriptor();
		// the class in the local JVM that this descriptor represents.
		Class<?> localClass;
		try {
			localClass = Class.forName(resultClassDescriptor.getName());
		} catch (ClassNotFoundException e) {
			log.warn("No local class for " + resultClassDescriptor.getName());
			return resultClassDescriptor;
		}
		ObjectStreamClass localClassDescriptor = ObjectStreamClass.lookup(localClass);
		// only if class implements serializable
		if (localClassDescriptor != null) {
			long localSerId = localClassDescriptor.getSerialVersionUID();
			long streamSerId = resultClassDescriptor.getSerialVersionUID();
			// check for serialVersionUID mismatch.
			if (streamSerId != localSerId) {
				log.warn("Overriding serialized class {} version mismatch: local serialVersionUID = {} stream serialVersionUID = {}", localClass, localSerId, streamSerId);
				// Use local class descriptor for deserialization
				resultClassDescriptor = localClassDescriptor;
			}
		}
		return resultClassDescriptor;
	}

}
