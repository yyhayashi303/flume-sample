package local.hys.flume.sink;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.flume.Channel;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.Transaction;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;

public class SampleSink extends AbstractSink implements Configurable {

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private final LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();

	@Override
	public synchronized void start() {
		System.out.println("start()");
		super.start();
		scheduler.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				SampleSink.this.run();
			}
		}, 0, 10, TimeUnit.SECONDS);
	}

	@Override
	public synchronized void stop() {
		System.out.println("stop()");
//		this.run();
		super.stop();
	}

	private void run() {
		final List<String> list = new ArrayList<String>();
		System.out.println("start drainTo");
		queue.drainTo(list);
		System.out.println("end drainTo");
		for (final String data : list) {
			System.out.println("run executor[" + Thread.currentThread().getId() + "] =" + data);
		}
	}

	@Override
	public void configure(Context context) {
		System.out.println("configure");
	}

	@Override
	public Status process() throws EventDeliveryException {
		final Channel channel = getChannel();
		final Transaction transaction = channel.getTransaction();
		transaction.begin();;
		try {
			final Event event = channel.take();
			System.out.println("call process[" + Thread.currentThread().getId() + "] =" + new String(event.getBody()));
			queue.add(new String(event.getBody()));
			System.out.println(queue);
			if (false) {
				throw new RuntimeException("throw exception");
			}
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			return Status.BACKOFF;
		} finally {
			transaction.close();
		}
		return Status.READY;
	}
}
