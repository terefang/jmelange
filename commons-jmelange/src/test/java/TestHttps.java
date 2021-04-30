import com.github.terefang.jmelange.commons.http.HttpClientResponse;
import com.github.terefang.jmelange.commons.http.RestClient;
import lombok.SneakyThrows;

public class TestHttps
{
    @SneakyThrows
    public static void main(String[] args) {
        RestClient _rc = new RestClient();

        _rc.setEncoderDecoder(new RestClient.EncoderDecoder() {
            @Override
            public Object decode(byte[] buf) {
                return new String(buf);
            }

            @Override
            public byte[] encode(Object obj) {
                return obj.toString().getBytes();
            }
        });

        _rc.setServiceUrl("https://www.google.com");

        HttpClientResponse<String> _resp = _rc.executeRestRequest("/", "GET",null);

        System.err.println(_resp.getStatus());
        System.err.println(_resp.getMessage());
        _resp.getCookieJar().forEach((x) -> {
            System.err.println(x);
        });
        _resp.getHeader().entrySet().forEach((x) -> {
            System.err.println(x);
        });

    }
}
