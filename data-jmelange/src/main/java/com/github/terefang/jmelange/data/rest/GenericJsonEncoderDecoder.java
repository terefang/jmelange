package com.github.terefang.jmelange.data.rest;

import com.github.terefang.jmelange.commons.http.RestClient;
import com.github.terefang.jmelange.data.util.HsonUtil;
import lombok.SneakyThrows;

import java.io.*;
import java.util.Map;

public class GenericJsonEncoderDecoder implements RestClient.EncoderDecoder<Map<String,Object>, Map<String,Object>> {
    @Override
    @SneakyThrows
    public Map<String, Object> decode(byte[] buf, String _cs) {
        return HsonUtil.loadContextFromHjson(new InputStreamReader(new ByteArrayInputStream(buf), _cs));
    }

    @Override
    @SneakyThrows
    public byte[] encode(Map<String, Object> obj, String _cs)
    {
        ByteArrayOutputStream _baos = new ByteArrayOutputStream();
        OutputStreamWriter _wr = new OutputStreamWriter(_baos, _cs);
        HsonUtil.writeAsHson(true, _wr, obj);
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
