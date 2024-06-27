package pl.bodzioch.damian.configuration.filter;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

class CachedContentServletInputStream extends ServletInputStream {

	private final InputStream inputStream;

	CachedContentServletInputStream(byte[] cachedContent) {
		this.inputStream = new ByteArrayInputStream(cachedContent);
	}

	@Override
	public boolean isFinished() {
		try {
			return inputStream.available() == 0;
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	@Override
	public boolean isReady() {
		return true;
	}

	@Override
	public void setReadListener(ReadListener readListener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int read() throws IOException {
		return this.inputStream.read();
	}
}
