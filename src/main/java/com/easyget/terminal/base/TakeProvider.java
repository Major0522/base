package com.easyget.terminal.base;

import org.kohsuke.MetaInfServices;

import com.easyget.terminal.base.provider.embed.EmbedPluginProvider;
import com.easyget.terminal.base.provider.embed.EmbedProvider;
import com.easyget.terminal.base.provider.embed.EmbedProviderFactory;
import com.easyget.terminal.base.util.Const;

/**
 * <p>
 * 取样品供应者实现类
 * </p>
 */
@MetaInfServices(EmbedPluginProvider.class)
public class TakeProvider implements EmbedPluginProvider {

	@Override
	public EmbedProvider getProvider() throws Exception {
		return EmbedProviderFactory.newEmbedProvider(Const.FXML_TAKE, Const.JSON_TAKE);
	}

}