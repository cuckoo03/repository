package org.elasticsearch.rest.action.analysis;

import org.apache.lucene.analysis.ko.morph.MorphException;
import org.apache.lucene.analysis.ko.utils.DictionaryUtil;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.RestChannel;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestRequest;

public class ArirangAnalysisRestAction extends BaseRestHandler {
	protected ArirangAnalysisRestAction(Settings settings, RestController controller, Client client) {
		super(settings, controller, client);
		
		controller.registerHandler(RestRequest.Method.GET, "/arirang_dictionary_reload", this);
	}

	
	@Override
	protected void handleRequest(RestRequest request, RestChannel channel, Client client) throws Exception {
		try {
			DictionaryUtil.loadDictionary();
		} catch (MorphException me) {
			System.err.println("Dictionary load failed.");
		}
	}
}
