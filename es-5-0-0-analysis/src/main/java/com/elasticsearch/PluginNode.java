package com.elasticsearch;

import java.util.List;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.internal.InternalSettingsPreparer;
import org.elasticsearch.plugins.Plugin;

class PluginNode extends Node {
	public PluginNode(Settings preparedSettings,
			List<Class<? extends Plugin>> plugins) {
		super(InternalSettingsPreparer.prepareEnvironment(preparedSettings,
				null), plugins);
	}
}
