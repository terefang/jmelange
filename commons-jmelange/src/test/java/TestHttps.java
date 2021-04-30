import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.http.HttpClientResponse;
import com.github.terefang.jmelange.commons.http.RestClient;
import lombok.SneakyThrows;


public class TestHttps
{
    @SneakyThrows
    public static void main(String[] args) {
        RestClient _rc = new RestClient();

        _rc.setEncoderDecoder(new RestClient.EncoderDecoder<String,String>() {
            @Override
            @SneakyThrows
            public String decode(byte[] buf, String _cs) {
                return new String(buf, _cs);
            }

            @Override
            @SneakyThrows
            public byte[] encode(String obj, String _cs) {
                return obj.getBytes(_cs);
            }

            @Override
            public String getContentType() {
                return "text/plain";
            }

            @Override
            public String getAcceptType() {
                return "*/*";
            }
        });

        _rc.setServiceUrl("https://www.google.com");

        HttpClientResponse<String> _resp = _rc.executeRestRequest("/", "GET",null);

        System.err.println("S: "+_resp.getStatus());
        System.err.println("M: "+_resp.getMessage());
        _resp.getCookieJar().forEach((x) -> {
            System.err.println("C: "+x);
        });
        _resp.getHeader().entrySet().forEach((x) -> {
            System.err.println("H: "+x);
        });
        System.err.println("E: "+_resp.getContentEncoding());
        System.err.println("X: "+_resp.getContentCharset());
        System.err.println("T: "+_resp.getContentType());
    }
}
