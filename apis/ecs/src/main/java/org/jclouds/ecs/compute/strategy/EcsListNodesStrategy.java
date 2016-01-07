/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.ecs.compute.strategy;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jclouds.Constants;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.reference.ComputeServiceConstants;
import org.jclouds.compute.strategy.ListNodesStrategy;
import org.jclouds.logging.Logger;

import com.google.common.base.Predicate;
import com.google.inject.Inject;

@Singleton
public class EcsListNodesStrategy implements ListNodesStrategy {

	@Resource
	@Named(ComputeServiceConstants.COMPUTE_LOGGER)
	protected Logger logger = Logger.NULL;

	@Inject(optional = true)
	@Named(Constants.PROPERTY_REQUEST_TIMEOUT)
	protected static Long maxTime;

	@Override
	public Iterable<? extends NodeMetadata> listDetailsOnNodesMatching(
			Predicate<ComputeMetadata> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<? extends ComputeMetadata> listNodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<? extends NodeMetadata> listNodesByIds(Iterable<String> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}