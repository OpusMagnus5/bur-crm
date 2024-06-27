package pl.bodzioch.damian.configuration.filter;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class CustomCachingRequestWrapper extends ContentCachingRequestWrapper {
	private final byte[] content;
	private  BufferedReader reader;

	CustomCachingRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		this.content = IOUtils.toByteArray(super.getInputStream());
	}

	@Override
	@NonNull
	public ServletInputStream getInputStream() {
		return new CachedContentServletInputStream(this.content);
	}

	@Override
	@NonNull
	public BufferedReader getReader() throws IOException {
		if (this.reader == null) {
			this.reader = new BufferedReader(new InputStreamReader(getInputStream(), getCharacterEncoding()));
		}
		return this.reader;
	}
}
