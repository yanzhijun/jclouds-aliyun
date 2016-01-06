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
package org.jclouds.aliyun.features;

import java.util.Set;

import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;

import org.jclouds.Fallbacks.EmptySetOnNotFoundOr404;
import org.jclouds.Fallbacks.NullOnNotFoundOr404;
import org.jclouds.aliyun.domain.Account;
import org.jclouds.aliyun.filters.AuthenticationFilter;
import org.jclouds.aliyun.options.ListAccountsOptions;
import org.jclouds.rest.annotations.Fallback;
import org.jclouds.rest.annotations.OnlyElement;
import org.jclouds.rest.annotations.QueryParams;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.annotations.SelectJson;

/**
 * Provides synchronous access to aliyun via their REST API.
 * <p/>
 * 
 * @see <a href="http://download.cloud.com/releases/2.2.0/api_2.2.12/TOC_User.html" />
 */
@RequestFilters(AuthenticationFilter.class)
@QueryParams(keys = { "Format", "Version" }, values = { "json", "2014-05-26" })
public interface AccountApi{
   /**
    * Lists Accounts
    * 
    * @param options
    *           if present, how to constrain the list.
    * @return Accounts matching query, or empty set, if no Accounts are found
    */
   @Named("listAccounts")
   @GET
   @QueryParams(keys = { "DescribeInstances","RegionId"}, values = { "DescribeRegions","region1"})
   @SelectJson("account")
   @Fallback(EmptySetOnNotFoundOr404.class)
   Set<Account> listAccounts(ListAccountsOptions... options);

   /**
    * get a specific Account by id
    * 
    * @param id
    *           Account to get
    * @return Account or null if not found
    */
   @Named("listAccounts")
   @GET
   @QueryParams(keys = { "Action", "listAll" }, values = { "DescribeRegions", "true" })
   @SelectJson("account")
   @OnlyElement
   @Fallback(NullOnNotFoundOr404.class)
   Account getAccount(@QueryParam("id") String id);

}
