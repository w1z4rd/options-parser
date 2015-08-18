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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author costelradulescu
 *
 */
public final class JavaOptionsParser {

  private JavaOptionsParser() {

  };

  public static Map<String, String> parse(String input) {
    if (input == null || input.isEmpty()) {
      return new HashMap<String, String>();
    }
    StringTokenizer tokenizer = new StringTokenizer(input);
    List<String> tokens = new ArrayList<String>();
    while (tokenizer.hasMoreTokens()) {
      tokens.add(tokenizer.nextToken());
    }
    return parse(tokens);
  }

  public static Map<String, String> parse(List<String> input) {
    if (input == null || input.isEmpty()) {
      return new HashMap<String, String>();
    }
    Map<String, String> result = new HashMap<String, String>();
    for (String property : input) {
      PropertyParsingStrategy strategy = PropertyParsingStrategy.fromString(property);
      ParsingStrategy parsingStrategy = strategy.getParsingStrategy();
      MapEntry entry = parsingStrategy.parse(property);
      result.put(entry.getKey(), entry.getValue());
    }
    return result;
  }

}

interface ParsingStrategy {
  MapEntry parse(String property);
}

class XXParsingStrategy implements ParsingStrategy {

  @Override
  public MapEntry parse(String property) {
    MapEntry result = new MapEntry();
    StringTokenizer tokenizer = new StringTokenizer(property, "=");
    if (tokenizer.hasMoreTokens()) {
      result.setKey(tokenizer.nextToken());
    }
    StringBuilder sb = new StringBuilder();
    while (tokenizer.hasMoreTokens()) {
      sb.append(tokenizer.nextToken());
    }
    result.setValue(sb.length() == 0 ? null : sb.toString());
    return result;
  }
}

class XXTrueParsingStrategy implements ParsingStrategy {

  @Override
  public MapEntry parse(String property) {
    MapEntry result = new MapEntry();
    result.setKey(property.replace("-XX:+", "-XX:"));
    result.setValue("true");
    return result;
  }
}

class XXFalseParsingStrategy implements ParsingStrategy {

  @Override
  public MapEntry parse(String property) {
    MapEntry result = new MapEntry();
    result.setKey(property.replace("-XX:-", "-XX:"));
    result.setValue("false");
    return result;
  }
}

class XmxParsingStrategy implements ParsingStrategy {

  @Override
  public MapEntry parse(String property) {
    MapEntry result = new MapEntry();
    result.setKey(property.substring(0, 4));
    result.setValue(property.substring(4));
    return result;
  }
}

class DefaultParsingStrategy implements ParsingStrategy {
  private static final String EQUALS = "=";
  private static final String COLON = ":";

  @Override
  public MapEntry parse(String property) {
    MapEntry result = new MapEntry();
    StringTokenizer tokenizer = new StringTokenizer(property, getDelimiter(property));
    if (tokenizer.hasMoreTokens()) {
      result.setKey(tokenizer.nextToken());
    }
    StringBuilder sb = new StringBuilder();
    while (tokenizer.hasMoreTokens()) {
      sb.append(tokenizer.nextToken());
    }
    result.setValue(sb.length() == 0 ? null : sb.toString());
    return result;
  }

  private String getDelimiter(String input) {
    if (!input.contains(EQUALS))
      return COLON;
    if (!input.contains(COLON))
      return EQUALS;
    return input.indexOf(EQUALS) < input.indexOf(COLON) ? EQUALS : COLON;
  }
}

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

class MapEntry {
  private String key;
  private String value;

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
