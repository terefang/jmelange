package com.github.terefang.jmelange.commons.xid;

//  This is free and unencumbered software released into the public domain.
//
//  Anyone is free to copy, modify, publish, use, compile, sell, or
//  distribute this software, either in source code form or as a compiled
//  binary, for any purpose, commercial or non-commercial, and by any
//  means.
//
//  In jurisdictions that recognize copyright laws, the author or authors
//  of this software dedicate any and all copyright interest in the
//  software to the public domain. We make this dedication for the benefit
//  of the public at large and to the detriment of our heirs and
//  successors. We intend this dedication to be an overt act of
//  relinquishment in perpetuity of all present and future rights to this
//  software under copyright law.
//
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
//  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
//  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
//  IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
//  OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
//  ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
//  OTHER DEALINGS IN THE SOFTWARE.
//
//  For more information, please refer to <https://unlicense.org>

import lombok.SneakyThrows;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.UUID;

public class UUIDv7 {
    private static final SecureRandom random = new SecureRandom();
    
    public static UUID randomUUID() {
        byte[] value = randomBytes();
        ByteBuffer buf = ByteBuffer.wrap(value);
        long high = buf.getLong();
        long low = buf.getLong();
        return new UUID(high, low);
    }
    
    public static byte[] randomBytes() {
        // random bytes
        byte[] value = new byte[16];
        random.nextBytes(value);
        
        // current timestamp in ms
        ByteBuffer timestamp = ByteBuffer.allocate(Long.BYTES);
        timestamp.putLong(System.currentTimeMillis());
        
        // timestamp
        System.arraycopy(timestamp.array(), 2, value, 0, 6);
        
        // version and variant
        value[6] = (byte) ((value[6] & 0x0F) | 0x70);
        value[8] = (byte) ((value[8] & 0x3F) | 0x80);
        
        return value;
    }
    
    @SneakyThrows
    public static UUID fromBytes(byte[] value)
    {
        if(value==null || value.length!=16) throw new IllegalArgumentException();
        
        // version and variant
        value[6] = (byte) ((value[6] & 0x0F) | 0x70);
        value[8] = (byte) ((value[8] & 0x3F) | 0x80);

        ByteBuffer buf = ByteBuffer.wrap(value);
        long high = buf.getLong();
        long low = buf.getLong();
        return new UUID(high, low);
    }
    
    public static void main(String[] args) {
        var uuid = UUIDv7.randomUUID();
        System.out.println(uuid);
    }
}