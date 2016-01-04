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
package org.jclouds.aliyun.features.test;

import static org.jclouds.util.Strings2.urlEncode;
import static org.testng.Assert.assertEquals;

import java.net.URI;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import org.jclouds.aliyun.AliyunContext;
import org.jclouds.aliyun.domain.AsyncCreateResponse;
import org.jclouds.aliyun.domain.Tag;
import org.jclouds.aliyun.features.TagApi;
import org.jclouds.aliyun.internal.test.BaseAliyunExpectTest;
import org.jclouds.aliyun.options.CreateTagsOptions;
import org.jclouds.aliyun.options.DeleteTagsOptions;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpResponse;
import org.testng.annotations.Test;

/**
 * Test the Aliyun TagApi
 */
@Test(groups = "unit", testName = "TagApiExpectTest")
public class TagApiExpectTest extends BaseAliyunExpectTest<TagApi> {


   public void testListTagsWhenResponseIs2xx() {
      TagApi client = requestSendsResponse(
            HttpRequest.builder().method("GET")
                  .endpoint("https://ecs.aliyuncs.com/")
                  .addQueryParam("response", "json")
                  .addQueryParam("command", "listTags")
                  .addQueryParam("listAll", "true")
                  .addQueryParam("apiKey", "identity")
                  .addQueryParam("signature", "amvtC2a0VHzzDF5SUAIOZpXHd0A%3D")
                  .addHeader("Accept", "application/json")
                  .build(),
            HttpResponse.builder()
                  .statusCode(200)
                  .payload(payloadFromResource("/listtagsresponse.json"))
                  .build()
      );

      assertEquals(client.listTags(),
            ImmutableSet.<Tag>of(
                  Tag.builder()
                        .account("admin")
                        .domain("ROOT")
                        .domainId("79dc06c4-4432-11e4-b70d-000c29e19aa0")
                        .key("test-tag")
                        .resourceId("54fe1d53-5d73-4184-8b62-948b9d8e08fb")
                        .resourceType(Tag.ResourceType.TEMPLATE)
                        .value("true").build()
            )
      );
   }

   public void testListTagsWhenResponseIs404() {
      TagApi client = requestSendsResponse(
         HttpRequest.builder()
            .method("GET")
            .endpoint(
                  URI.create("https://ecs.aliyuncs.com/?response=json&" +
                        "command=listTags&listAll=true&apiKey=identity&signature=amvtC2a0VHzzDF5SUAIOZpXHd0A%3D")
            )
            .addHeader("Accept", "application/json")
            .build(),
         HttpResponse.builder()
            .statusCode(404)
            .build());

      assertEquals(client.listTags(), ImmutableSet.of());
   }

   public void testCreateTagsWhenResponseIs2xx() {
      TagApi client = requestSendsResponse(
            HttpRequest.builder().method("GET")
                  .endpoint("https://ecs.aliyuncs.com/")
                  .addQueryParam("response", "json")
                  .addQueryParam("command", "createTags")
                  .addQueryParam("resourcetype", "Template")
                  .addQueryParam("resourceids", "52d89d5d-6070-4fd4-8131-c6c9ca4b062e")
                  .addQueryParam(urlEncode("tags[0].key"), "some-tag")
                  .addQueryParam(urlEncode("tags[0].value"), "some-value")
                  .addQueryParam("apiKey", "identity")
                  .addQueryParam("signature", "HDGTKGG9kONEwh5xlLe9R72z%2B9Q%3D")
                  .addHeader("Accept", "application/json")
                  .build(),
            HttpResponse.builder()
                  .statusCode(200)
                  .payload(payloadFromResource("/createtagsresponse.json"))
                  .build()
      );

      AsyncCreateResponse async = client.createTags(CreateTagsOptions.Builder.resourceType("Template")
            .resourceIds("52d89d5d-6070-4fd4-8131-c6c9ca4b062e")
            .tags(ImmutableMap.of("some-tag", "some-value")));

      assertEquals(
            async,
            AsyncCreateResponse.builder().jobId("32cfab73-f221-4b2b-a728-a73e924ac95d").build());
   }

   public void testDeleteTagsWhenResponseIs2xx() {
      TagApi client = requestSendsResponse(
            HttpRequest.builder().method("GET")
                  .endpoint("https://ecs.aliyuncs.com/")
                  .addQueryParam("response", "json")
                  .addQueryParam("command", "deleteTags")
                  .addQueryParam("resourcetype", "Template")
                  .addQueryParam("resourceids", "52d89d5d-6070-4fd4-8131-c6c9ca4b062e")
                  .addQueryParam(urlEncode("tags[0].key"), "some-tag")
                  .addQueryParam(urlEncode("tags[0].value"), "some-value")
                  .addQueryParam("apiKey", "identity")
                  .addQueryParam("signature", "inAqWH/GkkGipkZFG5Wfmxa8vOE%3D")
                  .addHeader("Accept", "application/json")
                  .build(),
            HttpResponse.builder()
                  .statusCode(200)
                  .payload(payloadFromResource("/deletetagsresponse.json"))
                  .build()
      );

      AsyncCreateResponse async = client.deleteTags(DeleteTagsOptions.Builder.resourceType("Template")
            .resourceIds("52d89d5d-6070-4fd4-8131-c6c9ca4b062e")
            .tags(ImmutableMap.of("some-tag", "some-value")));

      assertEquals(
            async,
            AsyncCreateResponse.builder().jobId("32cfab73-f221-4b2b-a728-a73e924ac95d").build());
   }

   @Override
   protected TagApi clientFrom(AliyunContext context) {
      return context.getApi().getTagApi();
   }
}
