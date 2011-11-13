package com.droidworks.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Handy little utility for serializing/deserializing serializable objects
 * for db insertion.
 *
 * @author Jason Hudgins <jason@droidworks.com>
 */
public class Freezer<T> {

    @SuppressWarnings("unchecked")
    public T thaw(byte[] data) throws IOException,
            ClassNotFoundException {

        ByteArrayInputStream bytes =
                new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bytes);
        return (T) ois.readObject();
    }

    public byte[] freeze(Serializable obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.flush();
        oos.close();
        return baos.toByteArray();
    }
}
