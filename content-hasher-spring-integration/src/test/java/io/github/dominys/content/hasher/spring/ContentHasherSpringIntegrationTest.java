package io.github.dominys.content.hasher.spring;

import io.github.dominys.content.hasher.api.ContentHasher;
import io.github.dominys.content.hasher.spring.annotation.EnableContentHasher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@EnableContentHasher
class ContentHasherSpringIntegrationTest {

    @Autowired
    private ContentHasher contentHasher;

    @Test
    void hasherSpringStartupTest(){
        assertThat(contentHasher).isNotNull();
    }
}
