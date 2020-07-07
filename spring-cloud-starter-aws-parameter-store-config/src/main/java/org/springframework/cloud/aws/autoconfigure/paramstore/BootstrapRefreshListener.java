package org.springframework.cloud.aws.autoconfigure.paramstore;

import org.springframework.cloud.aws.paramstore.RefreshParamStore;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.ApplicationListener;

import java.util.List;

public class BootstrapRefreshListener implements ApplicationListener<RefreshScopeRefreshedEvent> {
	private final List<RefreshParamStore> refreshSchedulers;

	public BootstrapRefreshListener(List<RefreshParamStore> refreshSchedulers) {
		this.refreshSchedulers = refreshSchedulers;
	}

	@Override
	public void onApplicationEvent(RefreshScopeRefreshedEvent event) {
		refreshSchedulers.forEach(RefreshParamStore::refresh);
	}
}
