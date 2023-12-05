package io.github.dominys.content.hasher.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import io.github.dominys.content.hasher.annotation.IgnoreHashing;
import io.github.dominys.content.hasher.annotation.IgnoreHashings;
import io.github.dominys.content.hasher.api.config.HashConfiguration;
import io.github.dominys.content.hasher.api.exceptions.HasherCreationException;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_METHOD;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@TestInstance(PER_METHOD)
class ObjectContentHasherTest {

    private final ObjectContentHasher contentHasher = new ObjectContentHasher(true);

    private static final String HASH_TAG_1 = "tag1";
    private static final String HASH_TAG_2 = "tag2";

    @Test
    void test() {
        TestObjectOne testObject = Mockito.mock(TestObjectOne.class);
        contentHasher.hash(testObject, HashConfiguration.builder()
                .sourceType(TestObjectOne.class)
                .build());

        verify(testObject).getStringField();
        verify(testObject).isBooleanField();
        verify(testObject).getStringList();
        verify(testObject).getObjectTwoList();
        verify(testObject).getMultiDimensionList();
        verify(testObject, never()).getSkippedField();
    }

    @Test
    void testCircularReference() {
        TestObjectThree testObject = new TestObjectThree();
        var config = HashConfiguration.builder()
                .build();
        assertThrows(HasherCreationException.class, () -> contentHasher.hash(testObject, config));
    }

    @Test
    void testCollectionsOrder() {
        TestObjectTwo o1 = new TestObjectTwo();
        o1.setStringField("field1");
        TestObjectTwo o2 = new TestObjectTwo();
        o2.setStringField("field2");

        String h1 = contentHasher.hash(Arrays.asList(o1, o2), HashConfiguration.builder()
                .sourceType(new TypeReference<List<TestObjectTwo>>() {
                }.getType())
                .build());
        String h2 = contentHasher.hash(Arrays.asList(o2, o1), HashConfiguration.builder()
                .sourceType(new TypeReference<List<TestObjectTwo>>() {
                }.getType())
                .build());

        assertThat(h1).isEqualTo(h2);
    }

    @Test
    void testEmptyAndNullCollectionAreTheSame() {
        TestObjectOne o1 = new TestObjectOne();
        o1.setStringList(Collections.emptyList());
        TestObjectOne o2 = new TestObjectOne();

        String h1 = contentHasher.hash(o1, HashConfiguration.builder()
                .sourceType(TestObjectOne.class)
                .build());
        String h2 = contentHasher.hash(o2, HashConfiguration.builder()
                .sourceType(TestObjectOne.class)
                .build());

        assertThat(h1).isEqualTo(h2);
    }

    @Test
    void testHashForClassWithInheritance() {
        TestObjectFive o1 = new TestObjectFive();
        o1.setStringField("field1");
        TestObjectFive o2 = new TestObjectFive();
        o2.setStringField("field2");

        String h1 = contentHasher.hash(o1, HashConfiguration.builder()
                .sourceType(TestObjectFive.class)
                .build());
        String h2 = contentHasher.hash(o2, HashConfiguration.builder()
                .sourceType(TestObjectFive.class)
                .build());

        assertThat(h1).isNotEqualTo(h2);
    }

    @Test
    void testHashForMap() {
        TestObjectFive o1 = new TestObjectFive();
        o1.setStringField("field1");

        String hash = contentHasher.hash(ImmutableMap.of("key", o1), HashConfiguration.builder()
                .sourceType(new TypeReference<Map<String, TestObjectFive>>() {
                }.getType())
                .build());

        assertThat(hash).isNotNull();
    }

    @Test
    void testWildcard() {
        TestObjectSix o1 = new TestObjectSix();
        o1.setObjectMap(ImmutableMap.of("field1", 123L));

        String hash = contentHasher.hash(o1, HashConfiguration.builder()
                .sourceType(TestObjectSix.class)
                .build());

        assertThat(hash).isNotNull();
    }

    @Test
    void testEmptyAndNullMapAreTheSame() {
        TestObjectSix o1 = new TestObjectSix();
        o1.setObjectMap(ImmutableMap.of());
        TestObjectSix o2 = new TestObjectSix();

        String h1 = contentHasher.hash(o1, HashConfiguration.builder()
                .sourceType(TestObjectSix.class)
                .build());
        String h2 = contentHasher.hash(o2, HashConfiguration.builder()
                .sourceType(TestObjectSix.class)
                .build());

        assertThat(h1).isEqualTo(h2);
    }

    @Test
    void testHashUnchangedForNewNullField() {
        TestObjectOne o1 = new TestObjectOne();
        o1.setStringField("field1");
        TestObjectSeven o2 = new TestObjectSeven();
        o2.setStringField("field1");

        String hash1 = contentHasher.hash(o1, HashConfiguration.builder()
                .sourceType(TestObjectOne.class)
                .build());
        String hash2 = contentHasher.hash(o2, HashConfiguration.builder()
                .sourceType(TestObjectSeven.class)
                .build());
        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    void testHashWithDefaultIgnoreTag() {
        TestObjectEight testObject = Mockito.mock(TestObjectEight.class);
        contentHasher.hash(testObject, HashConfiguration.builder()
                .sourceType(TestObjectEight.class)
                .build());

        verify(testObject).getTestField();
        verify(testObject, never()).getSkippedFieldByDefault();
        verify(testObject).getSkippedFieldOnTag1();
        verify(testObject).getSkippedFieldOnTag2();
        verify(testObject).getSkippedFieldOnTag12();
        verify(testObject, never()).getSkippedFieldOnAllTags();
    }

    @Test
    void testHashWithOneCustomIgnoreTag() {
        TestObjectEight testObject = Mockito.mock(TestObjectEight.class);
        contentHasher.hash(testObject, HashConfiguration.builder()
                .sourceType(TestObjectEight.class)
                .tags(List.of(HASH_TAG_1))
                .excludeDefault(true)
                .build());

        verify(testObject).getTestField();
        verify(testObject).getSkippedFieldByDefault();
        verify(testObject, never()).getSkippedFieldOnTag1();
        verify(testObject).getSkippedFieldOnTag2();
        verify(testObject, never()).getSkippedFieldOnTag12();
        verify(testObject, never()).getSkippedFieldOnAllTags();
    }

    @Test
    void testHashWithFewCustomIgnoreTags() {
        TestObjectEight testObject = Mockito.mock(TestObjectEight.class);
        contentHasher.hash(testObject, HashConfiguration.builder()
                .sourceType(TestObjectEight.class)
                .tags(List.of(HASH_TAG_1, HASH_TAG_2))
                .excludeDefault(true)
                .build());

        verify(testObject).getTestField();
        verify(testObject).getSkippedFieldByDefault();
        verify(testObject, never()).getSkippedFieldOnTag1();
        verify(testObject, never()).getSkippedFieldOnTag2();
        verify(testObject, never()).getSkippedFieldOnTag12();
        verify(testObject, never()).getSkippedFieldOnAllTags();
    }

    @Test
    void testHashWithAllIgnoreTags() {
        TestObjectEight testObject = Mockito.mock(TestObjectEight.class);
        contentHasher.hash(testObject, HashConfiguration.builder()
                .sourceType(TestObjectEight.class)
                .tags(List.of(HASH_TAG_1, HASH_TAG_2))
                .build());

        verify(testObject).getTestField();
        verify(testObject, never()).getSkippedFieldByDefault();
        verify(testObject, never()).getSkippedFieldOnTag1();
        verify(testObject, never()).getSkippedFieldOnTag2();
        verify(testObject, never()).getSkippedFieldOnTag12();
        verify(testObject, never()).getSkippedFieldOnAllTags();
    }

    @Test
    void testHashWithEmptyIgnoreTags() {
        TestObjectEight testObject = Mockito.mock(TestObjectEight.class);
        contentHasher.hash(testObject, HashConfiguration.builder()
                .sourceType(TestObjectEight.class)
                .excludeDefault(true)
                .build());

        verify(testObject).getTestField();
        verify(testObject).getSkippedFieldByDefault();
        verify(testObject).getSkippedFieldOnTag1();
        verify(testObject).getSkippedFieldOnTag2();
        verify(testObject).getSkippedFieldOnTag12();
        verify(testObject).getSkippedFieldOnAllTags();
    }

    @Test
    void testExcludeDefaultConfig() {
        TestObjectEight testObject = new TestObjectEight();
        testObject.setSkippedFieldByDefault("123");

        String hash1 = contentHasher.hash(testObject, HashConfiguration.builder()
                .sourceType(TestObjectEight.class)
                .excludeDefault(true)
                .build());

        String hash2 = contentHasher.hash(testObject, HashConfiguration.builder()
                .sourceType(TestObjectEight.class)
                .excludeDefault(false)
                .build());

        assertThat(hash1).isNotEqualTo(hash2);
    }

    @Data
    public class TestObjectOne {
        private String stringField;
        private boolean booleanField;
        private List<String> stringList;
        private List<TestObjectTwo> objectTwoList;
        @Getter(onMethod_ = {@IgnoreHashing})
        private String skippedField;
        private List<List<String>> multiDimensionList;
    }

    @Data
    public class TestObjectTwo {
        private String stringField;
    }

    @Getter
    @Setter
    public class TestObjectThree {

        private TestObjectFour testObjectFour;
    }

    @Getter
    @Setter
    public class TestObjectFour {
        private TestObjectThree testObjectThree;
    }

    public class TestObjectFive extends TestObjectOne {

    }

    @Getter
    @Setter
    public class TestObjectSix {
        Map<String, Object> objectMap;
    }

    @Data
    public class TestObjectSeven extends TestObjectOne {

        private String testField;
        private BigDecimal bigDecimalField;
    }

    @Data
    public class TestObjectEight {

        private String testField;
        @Getter(onMethod_ = {@IgnoreHashing})
        private String skippedFieldByDefault;
        @Getter(onMethod_ = {@IgnoreHashing(HASH_TAG_1)})
        private String skippedFieldOnTag1;
        @Getter(onMethod_ = {@IgnoreHashing(HASH_TAG_2)})
        private String skippedFieldOnTag2;
        @Getter(onMethod_ = {@IgnoreHashing(HASH_TAG_1), @IgnoreHashing(HASH_TAG_2)})
        private String skippedFieldOnTag12;
        @Getter(onMethod_ = {@IgnoreHashings({@IgnoreHashing({HASH_TAG_1, HASH_TAG_2}), @IgnoreHashing})})
        private String skippedFieldOnAllTags;
    }
}
