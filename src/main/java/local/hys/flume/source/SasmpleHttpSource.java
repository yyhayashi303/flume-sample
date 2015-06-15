package local.hys.flume.source;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.source.http.HTTPBadRequestException;
import org.apache.flume.source.http.HTTPSourceHandler;

/**
 */
public class SasmpleHttpSource implements HTTPSourceHandler {

	@Override
	public List<Event> getEvents(HttpServletRequest httpServletRequest) throws HTTPBadRequestException, Exception {
		return null;
	}

	@Override
	public void configure(Context context) {

	}
}
