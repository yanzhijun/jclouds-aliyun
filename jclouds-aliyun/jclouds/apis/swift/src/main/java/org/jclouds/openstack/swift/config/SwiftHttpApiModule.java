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
package org.jclouds.openstack.swift.config;

import static org.jclouds.util.Suppliers2.getLastValueInMap;
import static org.jclouds.util.Suppliers2.getValueInMapOrNull;

import java.net.URI;
import java.util.Map;

import javax.inject.Named;
import javax.inject.Singleton;

import org.jclouds.http.HttpErrorHandler;
import org.jclouds.http.annotation.ClientError;
import org.jclouds.http.annotation.Redirection;
import org.jclouds.http.annotation.ServerError;
import org.jclouds.json.config.GsonModule.DateAdapter;
import org.jclouds.json.config.GsonModule.Iso8601DateAdapter;
import org.jclouds.location.reference.LocationConstants;
import org.jclouds.location.suppliers.RegionIdToURISupplier;
import org.jclouds.openstack.config.OpenStackAuthenticationModule;
import org.jclouds.openstack.functions.URIFromAuthenticationResponseForService;
import org.jclouds.openstack.keystone.v2_0.config.KeystoneAuthenticationModule;
import org.jclouds.openstack.reference.AuthHeaders;
import org.jclouds.openstack.services.ServiceType;
import org.jclouds.openstack.swift.CommonSwiftClient;
import org.jclouds.openstack.swift.Storage;
import org.jclouds.openstack.swift.SwiftClient;
import org.jclouds.openstack.swift.handlers.ParseSwiftErrorFromHttpResponse;
import org.jclouds.rest.ConfiguresHttpApi;
import org.jclouds.rest.annotations.ApiVersion;
import org.jclouds.rest.config.HttpApiModule;

import com.google.common.base.Supplier;
import com.google.inject.Provides;
import com.google.inject.Scopes;

@ConfiguresHttpApi
public class SwiftHttpApiModule<S extends CommonSwiftClient> extends HttpApiModule<S> {

   @SuppressWarnings("unchecked")
   public SwiftHttpApiModule() {
      this(Class.class.cast(SwiftClient.class));
   }

   protected SwiftHttpApiModule(Class<S> syncClientType) {
      super(syncClientType);
   }

   public static class StorageEndpointModule extends OpenStackAuthenticationModule {
      @Provides
      @Singleton
      @Storage
      protected Supplier<URI> provideStorageUrl(URIFromAuthenticationResponseForService.Factory factory) {
         return factory.create(AuthHeaders.STORAGE_URL);
      }
   }

   public static class KeystoneStorageEndpointModule extends KeystoneAuthenticationModule {
      @Provides
      @Singleton
      @Storage
      protected Supplier<URI> provideStorageUrl(RegionIdToURISupplier.Factory factory,
            @ApiVersion String apiVersion,
            @Named(LocationConstants.PROPERTY_REGION) String region) {

         //Get the URI's keyed by their region name
         Supplier<Map<String, Supplier<URI>>> endpointsSupplier = factory.createForApiTypeAndVersion(ServiceType.OBJECT_STORE, apiVersion);

         //Pick the matching region name (if any) otherwise just return an arbitrary URL if no region name is set
         //NOTE: The region string should never be null (it can be empty) if this object was instantiated via guice
         //      as it pulls these named strings from a Properties object.
         if (region.isEmpty()) {
            return getLastValueInMap(endpointsSupplier);
         } else {
            return getValueInMapOrNull(endpointsSupplier, region);
         }
      }
   }

   @Override
   protected void configure() {
      install(new SwiftObjectModule());
      bind(DateAdapter.class).to(Iso8601DateAdapter.class);
      super.configure();
      bindResolvedClientsToCommonSwift();
   }

   protected void bindResolvedClientsToCommonSwift() {
      bind(CommonSwiftClient.class).to(SwiftClient.class).in(Scopes.SINGLETON);
   }

   @Override
   protected void bindErrorHandlers() {
      bind(HttpErrorHandler.class).annotatedWith(Redirection.class).to(ParseSwiftErrorFromHttpResponse.class);
      bind(HttpErrorHandler.class).annotatedWith(ClientError.class).to(ParseSwiftErrorFromHttpResponse.class);
      bind(HttpErrorHandler.class).annotatedWith(ServerError.class).to(ParseSwiftErrorFromHttpResponse.class);
   }

}
