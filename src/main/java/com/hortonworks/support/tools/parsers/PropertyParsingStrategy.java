/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hortonworks.support.tools.parsers;

/**
 * @author costelradulescu
 *
 */
enum PropertyParsingStrategy {
  XX_TRUE("-XX:+", new XXTrueParsingStrategy()),
  XX_FALSE("-XX:-", new XXFalseParsingStrategy()),
  XX("-XX:", new XXParsingStrategy()),
  XMX("-Xmx", new XmxParsingStrategy()),
  XMS("-Xms", new XmxParsingStrategy()),
  DEFAULT("default", new DefaultParsingStrategy());

  private String value;
  private ParsingStrategy parsingStrategy;

  public String getValue() {
    return value;
  }

  public ParsingStrategy getParsingStrategy() {
    return parsingStrategy;
  }

  private PropertyParsingStrategy(String value, ParsingStrategy parsingStrategy) {
    this.value = value;
    this.parsingStrategy = parsingStrategy;
  }

  public static PropertyParsingStrategy fromString(String property) {
    for (PropertyParsingStrategy strategy : values()) {
      if (property != null && property.startsWith(strategy.value)) {
        return strategy;
      }
    }
    return DEFAULT;
  }
}
