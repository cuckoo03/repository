package storm.starter.tools;

import backtype.storm.Config;
import backtype.storm.topology.OutputFieldsDeclarer;
import java.util.Map;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import java.util.HashMap;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestingWordSpout extends BaseRichSpout {
	private static final long serialVersionUID = 1L;
	public static Logger LOG = LoggerFactory.getLogger(TestingWordSpout.class);
	boolean _isDistributed;
	SpoutOutputCollector _collector;

	public TestingWordSpout() {
		this(true);
	}

	public TestingWordSpout(boolean isDistributed) {
		_isDistributed = isDistributed;
	}

	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		_collector = collector;
	}

	public void close() {

	}
	int i = 0;
	boolean first = false;
	public void nextTuple() {
		Utils.sleep(1000);
		final String[] words = new String[] { "A", "B", "C", "D", "E" };
		final Random rand = new Random();
//		final String word = words[rand.nextInt(words.length)];
		final String word = words[i++];
		if (i > 4) {
			i = 0;
		}
//		System.out.println("spout:" + word);
		_collector.emit(new Values(word));
	}

	public void ack(Object msgId) {

	}

	public void fail(Object msgId) {

	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		if (!_isDistributed) {
			Map<String, Object> ret = new HashMap<String, Object>();
			ret.put(Config.TOPOLOGY_MAX_TASK_PARALLELISM, 1);
			return ret;
		} else {
			return null;
		}
	}
}