package io.github.dominys.content.hasher.impl;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HasherFactory {

    private static final HashFunction HASH_FUNCTION = Hashing.murmur3_128();

    public static Hasher hasher() {
        return HASH_FUNCTION.newHasher();
    }

}
