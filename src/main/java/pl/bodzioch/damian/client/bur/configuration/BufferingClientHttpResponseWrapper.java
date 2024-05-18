package pl.bodzioch.damian.client.bur.configuration;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@AllArgsConstructor
public class BufferingClientHttpResponseWrapper implements ClientHttpResponse {

    private final ClientHttpResponse response;
    private byte[] body;

    @Override
    @NonNull
    public HttpStatusCode getStatusCode() throws IOException {
        return this.response.getStatusCode();
    }

    @Override
    @NonNull
    public String getStatusText() throws IOException {
        return this.response.getStatusText();
    }

    @Override
    @NonNull
    public HttpHeaders getHeaders() {
        return this.response.getHeaders();
    }

    @Override
    @NonNull
    public InputStream getBody() throws IOException {
        if (this.body == null) {
            this.body = StreamUtils.copyToByteArray(this.response.getBody());
        }
        return new ByteArrayInputStream(this.body);
    }

    @Override
    public void close() {
        this.response.close();
    }
}
