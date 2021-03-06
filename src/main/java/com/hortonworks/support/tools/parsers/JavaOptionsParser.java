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

import com.hortonworks.support.tools.utils.KeyValuePair;

/**
 * @author costelradulescu
 *
 */
public final class JavaOptionsParser {

  private JavaOptionsParser() {

  }

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
      KeyValuePair<String, String> entry = parsingStrategy.parse(property);
      result.put(entry.getKey(), entry.getValue());
    }
    return result;
  }

}
