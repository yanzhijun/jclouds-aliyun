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
package org.jclouds.s3.binders;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;

import org.jclouds.http.HttpRequest;
import org.jclouds.io.Payload;
import org.jclouds.io.Payloads;
import org.jclouds.rest.Binder;

@Singleton
public class BindPartIdsAndETagsToRequest implements Binder {

   @SuppressWarnings("unchecked")
   @Override
   public <R extends HttpRequest> R bindToRequest(R request, Object input) {
      checkArgument(checkNotNull(input, "input") instanceof Map, "this binder is only valid for Map!");
      checkNotNull(request, "request");

      Map<Integer, String> map = (Map<Integer, String>) input;
      checkArgument(!map.isEmpty(), "Please send parts");
      StringBuilder content = new StringBuilder();
      content.append("<CompleteMultipartUpload>");
      for (Entry<Integer, String> entry : map.entrySet()) {
         content.append("<Part>");
         content.append("<PartNumber>").append(entry.getKey()).append("</PartNumber>");
         content.append("<ETag>").append(entry.getValue()).append("</ETag>");
         content.append("</Part>");
      }
      content.append("</CompleteMultipartUpload>");
      Payload payload = Payloads.newStringPayload(content.toString());
      payload.getContentMetadata().setContentType(MediaType.TEXT_XML);
      request.setPayload(payload);
      return request;
   }
}
