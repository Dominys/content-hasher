package io.github.dominys.content.hasher.spring;

import io.github.dominys.content.hasher.api.ContentHasher;
import io.github.dominys.content.hasher.impl.ObjectContentHasher;
import org.springframework.beans.factory.FactoryBean;

public class ContentHasherFactoryBean implements FactoryBean<ContentHasher> {

	@Override
	public ContentHasher getObject() {
		return new ObjectContentHasher();
	}

	@Override
	public Class<?> getObjectType() {
		return ObjectContentHasher.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}