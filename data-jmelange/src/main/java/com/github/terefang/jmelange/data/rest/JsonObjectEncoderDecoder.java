package com.github.terefang.jmelange.data.rest;

import com.github.terefang.jmelange.commons.http.RestClient;
import lombok.SneakyThrows;
import org.hjson.JsonValue;
import org.hjson.Stringify;

import java.io.*;

public class JsonObjectEncoderDecoder implements RestClient.EncoderDecoder<JsonValue, JsonValue> {
    @Override
    @SneakyThrows
    public JsonValue decode(byte[] buf, String _cs) {
        return JsonValue.readJSON(new InputStreamReader(new ByteArrayInputStream(buf), _cs));
    }

    @Override
    @SneakyThrows
    public byte[] encode(JsonValue obj, String _cs)
    {
        ByteArrayOutputStream _baos = new ByteArrayOutputStream();
        Writer _wr = new OutputStreamWriter(_baos, _cs);
        obj.writeTo(_wr, Stringify.FORMATTED);
        _wr.flush();
        return _baos.toByteArray();
    }

    @Override
    public String getContentType() {
        return "application/json";
    }

    @Override
    public String getAcceptType() {
        return "application/json";
    }
}
