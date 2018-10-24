package com.sa.sbsimplewebapp.audit;

import java.io.Serializable;

import org.hibernate.Session;
import org.hibernate.envers.configuration.internal.AuditEntitiesConfiguration;
import org.hibernate.envers.strategy.ValidityAuditStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomValidityAuditStrategy extends ValidityAuditStrategy {
	private static final Logger logger = LoggerFactory.getLogger(CustomValidityAuditStrategy.class);

	@Override
	public void perform(final Session session, final String entityName, final AuditEntitiesConfiguration audEntitiesCfg,
			final Serializable id, final Object data, final Object revision) {
		try {
			super.perform(session, entityName, audEntitiesCfg, id, data, revision);
		} catch (RuntimeException e) {
			logger.error(
					"Exception at CustomValidityAuditStrategy.perform(). Ignoring exception. WARNING! You may not have complete Audit Table!",
					e);
		}
	}
}
