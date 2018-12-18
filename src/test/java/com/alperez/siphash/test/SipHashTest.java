package com.alperez.siphash.test;

import com.alperez.siphash.SipHash;
import com.alperez.siphash.SipHashKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class SipHashTest {
    private static final SipHashKey SPEC_KEY = SipHashKey.ofBytes(new byte[]{
            0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
            0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f
    });

    private static final byte[] SPEC_MSG = {
            0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
            0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e
    };

    public SipHashTest() {
    }

    @Test
    public void spec() {
        long digest = SipHash.calculateHash(SPEC_KEY, SPEC_MSG);
        assertEquals(0xa129ca6149be45e5L, digest);
    }

    @Test
    public void emptyString() throws Exception {
        long digest = SipHash.calculateHash(SPEC_KEY, "".getBytes("UTF8"));
        assertEquals(0x726fdb47dd0e0e31L, digest);
    }

    @Test
    public void oneByte() throws Exception {
        long digest = SipHash.calculateHash(SPEC_KEY, "a".getBytes("UTF8"));
        assertEquals(0x2ba3e8e9a71148caL, digest);
    }

    @Test
    public void sixBytes() throws Exception {
        long digest = SipHash.calculateHash(SPEC_KEY, "abcdef".getBytes("UTF8"));
        assertEquals(0x2a6e77e733c7c05dL, digest);
    }

    @Test
    public void sevenBytes() throws Exception {
        long digest = SipHash.calculateHash(SPEC_KEY, "SipHash".getBytes("UTF8"));
        assertEquals(0x8325093242a96f60L, digest);
    }

    @Test
    public void eightBytes() throws Exception {
        long digest = SipHash.calculateHash(SPEC_KEY, "12345678".getBytes("UTF8"));
        assertEquals(0x2130609caea37ebL, digest);
    }

    @Test
    public void oneMillionZeroBytes() throws Exception {
        long digest = SipHash.calculateHash(SPEC_KEY, new byte[1000000]);
        assertEquals(0x28205108397aa742L, digest);
    }



    // Test hashing data with negative byte values
    // The original implementation produced identical hash values for these two
    // messages.
    // The test vectors do not contain negative byte values, this should be fixed.
    @Test
    public void testHashCollision() throws Exception {
        SipHashKey z_key = SipHashKey.ofBytes(new byte[16]);
        byte[] msg1 = { 109, -45, -99, -85, -72, 37, -51, 120, -56, -10, -17, -53, -83, 84, -127, 67 };
        byte[] msg2 = { 109, -45, -99, -85, -72, 37, -51, 120, -56, 80,  111, 67,  -59, 92, 100,  2 };
        assertTrue(SipHash.calculateHash(z_key, msg1) == SipHash.calculateHash(z_key, msg2));
    }



    private long[] EXPECTED = new long[] {
            0x726fdb47dd0e0e31L,
            0x74f839c593dc67fdL,
            0x0d6c8009d9a94f5aL,
            0x85676696d7fb7e2dL,
            0xcf2794e0277187b7L,
            0x18765564cd99a68dL,
            0xcbc9466e58fee3ceL,
            0xab0200f58b01d137L,
            0x93f5f5799a932462L,
            0x9e0082df0ba9e4b0L,
            0x7a5dbbc594ddb9f3L,
            0xf4b32f46226bada7L,
            0x751e8fbc860ee5fbL,
            0x14ea5627c0843d90L,
            0xf723ca908e7af2eeL,
            0xa129ca6149be45e5L,
            0x3f2acc7f57c29bdbL,
            0x699ae9f52cbe4794L,
            0x4bc1b3f0968dd39cL,
            0xbb6dc91da77961bdL,
            0xbed65cf21aa2ee98L,
            0xd0f2cbb02e3b67c7L,
            0x93536795e3a33e88L,
            0xa80c038ccd5ccec8L,
            0xb8ad50c6f649af94L,
            0xbce192de8a85b8eaL,
            0x17d835b85bbb15f3L,
            0x2f2e6163076bcfadL,
            0xde4daaaca71dc9a5L,
            0xa6a2506687956571L,
            0xad87a3535c49ef28L,
            0x32d892fad841c342L,
            0x7127512f72f27cceL,
            0xa7f32346f95978e3L,
            0x12e0b01abb051238L,
            0x15e034d40fa197aeL,
            0x314dffbe0815a3b4L,
            0x027990f029623981L,
            0xcadcd4e59ef40c4dL,
            0x9abfd8766a33735cL,
            0x0e3ea96b5304a7d0L,
            0xad0c42d6fc585992L,
            0x187306c89bc215a9L,
            0xd4a60abcf3792b95L,
            0xf935451de4f21df2L,
            0xa9538f0419755787L,
            0xdb9acddff56ca510L,
            0xd06c98cd5c0975ebL,
            0xe612a3cb9ecba951L,
            0xc766e62cfcadaf96L,
            0xee64435a9752fe72L,
            0xa192d576b245165aL,
            0x0a8787bf8ecb74b2L,
            0x81b3e73d20b49b6fL,
            0x7fa8220ba3b2eceaL,
            0x245731c13ca42499L,
            0xb78dbfaf3a8d83bdL,
            0xea1ad565322a1a0bL,
            0x60e61c23a3795013L,
            0x6606d7e446282b93L,
            0x6ca4ecb15c5f91e1L,
            0x9f626da15c9625f3L,
            0xe51b38608ef25f57L,
            0x958a324ceb064572L
    };

    // Ported from test vectors in siphash24.c at https://www.131002.net/siphash/siphash24.c
    @Test
    public void testMultipleVectors() throws Exception {
        SipHashKey key = SipHashKey.ofNumbers(0x0706050403020100L, 0x0f0e0d0c0b0a0908L);
        for (int i = 0; i < EXPECTED.length; ++i) {
            byte[] msg = new byte[i];
            for (int j = 0; j < i; ++j) {
                msg[j] = (byte) j;
            }
            long hash = SipHash.calculateHash(key, msg);
            assertEquals(EXPECTED[i], hash/*SipHashInline.hash24(k0, k1, msg)*/);
        }
    }
}
