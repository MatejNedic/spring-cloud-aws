/*
 * Copyright 2013-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.aws.paramstore;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersByPathRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersByPathResult;
import com.amazonaws.services.simplesystemsmanagement.model.Parameter;
import org.junit.Test;

import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test for {@link AwsParamStorePropertySourceLocator}.
 *
 * @author Matej Nedic
 */
public class AwsParamStorePropertySourceLocatorTest {

	private AWSSimpleSystemsManagement ssmClient = mock(AWSSimpleSystemsManagement.class);

	private MockEnvironment env = new MockEnvironment();

	@Test
	public void contextExpectedToHave2Elements() {
		AwsParamStoreProperties properties = new AwsParamStorePropertiesBuilder()
				.withDefaultContext("application").withName("application").build();
		int sizeOfContextList = 2;
		GetParametersByPathResult firstResult = getFirstResult();
		GetParametersByPathResult nextResult = getNextResult();
		when(ssmClient.getParametersByPath(any(GetParametersByPathRequest.class)))
				.thenReturn(firstResult, nextResult);
		AwsParamStorePropertySourceLocator locator = new AwsParamStorePropertySourceLocator(
				ssmClient, properties);
		env.setActiveProfiles("test");
		locator.locate(env);
		assertThat(locator.getContexts().size()).isEqualTo(sizeOfContextList);
	}

	@Test
	public void contextExpectedToHave4Elements() {
		AwsParamStoreProperties properties = new AwsParamStorePropertiesBuilder()
				.withDefaultContext("application").withName("messaging-service").build();
		int sizeOfContextList = 4;
		GetParametersByPathResult firstResult = getFirstResult();
		GetParametersByPathResult nextResult = getNextResult();
		when(ssmClient.getParametersByPath(any(GetParametersByPathRequest.class)))
				.thenReturn(firstResult, nextResult);
		AwsParamStorePropertySourceLocator locator = new AwsParamStorePropertySourceLocator(
				ssmClient, properties);
		env.setActiveProfiles("test");
		locator.locate(env);
		assertThat(locator.getContexts().size()).isEqualTo(sizeOfContextList);
	}

	private static GetParametersByPathResult getNextResult() {
		return new GetParametersByPathResult().withParameters(
				new Parameter().withName("/config/myservice/key3").withValue("value3"),
				new Parameter().withName("/config/myservice/key4").withValue("value4"));
	}

	private static GetParametersByPathResult getFirstResult() {
		return new GetParametersByPathResult().withParameters(
				new Parameter().withName("/config/myservice/key3").withValue("value3"),
				new Parameter().withName("/config/myservice/key4").withValue("value4"));
	}

	private static final class AwsParamStorePropertiesBuilder {

		private String prefix = "/config";

		private String defaultContext = "application";

		private String profileSeparator = "_";

		private boolean failFast = true;

		private String name;

		private boolean enabled = true;

		private AwsParamStorePropertiesBuilder() {
		}


		public AwsParamStorePropertiesBuilder withDefaultContext(String defaultContext) {
			this.defaultContext = defaultContext;
			return this;
		}


		public AwsParamStorePropertiesBuilder withName(String name) {
			this.name = name;
			return this;
		}

		public AwsParamStoreProperties build() {
			AwsParamStoreProperties awsParamStoreProperties = new AwsParamStoreProperties();
			awsParamStoreProperties.setPrefix(prefix);
			awsParamStoreProperties.setDefaultContext(defaultContext);
			awsParamStoreProperties.setProfileSeparator(profileSeparator);
			awsParamStoreProperties.setFailFast(failFast);
			awsParamStoreProperties.setName(name);
			awsParamStoreProperties.setEnabled(enabled);
			return awsParamStoreProperties;
		}

	}

}
