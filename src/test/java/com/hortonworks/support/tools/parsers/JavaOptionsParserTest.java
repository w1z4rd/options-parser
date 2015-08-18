package com.hortonworks.support.tools.parsers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class JavaOptionsParserTest {

  @Test
  public void testParseStringNullInput() {
    Map<String, String> result = JavaOptionsParser.parse((String) null);
    assertNotNull(result);
  }

  @Test
  public void testParseListNullInput() {
    Map<String, String> result = JavaOptionsParser.parse((List<String>) null);
    assertNotNull(result);
  }

  @Test
  public void testParseStringEmptyInput() {
    Map<String, String> result = JavaOptionsParser.parse("");
    assertNotNull(result);
  }

  @Test
  public void testParseListEmptyInput() {
    Map<String, String> result = JavaOptionsParser.parse(Collections.<String> emptyList());
    assertNotNull(result);
  }

  @Test
  public void testNoDelimiterInput() {
    String input = "-DrandomProp1 -DRandomProp2";
    Map<String, String> expected = new HashMap<String, String>();
    expected.put("-DrandomProp1", null);
    expected.put("-DRandomProp2", null);
    Map<String, String> actual = JavaOptionsParser.parse(input);
    assertEquals(expected, actual);
  }

  @Test
  public void testOnlyEqualsDelimiterInput() {
    String input = "-Dhdp.version=2.2.4.0-2633 -Djava.net.preferIPv4Stack=true";
    Map<String, String> expected = new HashMap<String, String>();
    expected.put("-Dhdp.version", "2.2.4.0-2633");
    expected.put("-Djava.net.preferIPv4Stack", "true");
    Map<String, String> actual = JavaOptionsParser.parse(input);
    assertEquals(expected, actual);
  }

  @Test
  public void testOnlyColonDelimiterInput() {
    String input = "-agentlib:jdwp -verbose:gc";
    Map<String, String> expected = new HashMap<String, String>();
    expected.put("-verbose", "gc");
    expected.put("-agentlib", "jdwp");
    Map<String, String> actual = JavaOptionsParser.parse(input);
    assertEquals(expected, actual);
  }

  @Test
  public void testXXTrueCaseInput() {
    String input = "-XX:+PrintGCDetails -XX:+PrintGCDateStamps";
    Map<String, String> expected = new HashMap<String, String>();
    expected.put("-XX:PrintGCDetails", "true");
    expected.put("-XX:PrintGCDateStamps", "true");
    Map<String, String> actual = JavaOptionsParser.parse(input);
    assertEquals(expected, actual);
  }

  @Test
  public void testXXFalseCaseInput() {
    String input = "-XX:-PrintGCTimeStamps -XX:-PrintGCDateStamps";
    Map<String, String> expected = new HashMap<String, String>();
    expected.put("-XX:PrintGCTimeStamps", "false");
    expected.put("-XX:PrintGCDateStamps", "false");
    Map<String, String> actual = JavaOptionsParser.parse(input);
    assertEquals(expected, actual);
  }

  @Test
  public void testXXCaseInput() {
    String input = "-XX:PrintGCTimeStamps=256m -XX:PrintGCDateStamps";
    Map<String, String> expected = new HashMap<String, String>();
    expected.put("-XX:PrintGCTimeStamps", "256m");
    expected.put("-XX:PrintGCDateStamps", null);
    Map<String, String> actual = JavaOptionsParser.parse(input);
    assertEquals(expected, actual);
  }

  @Test
  public void testSpecialXmxCaseInput() {
    String input = "-Xmx1024m";
    Map<String, String> expected = new HashMap<String, String>();
    expected.put("-Xmx", "1024m");
    Map<String, String> actual = JavaOptionsParser.parse(input);
    assertEquals(expected, actual);
  }

  @Test
  public void testSpecialXmsCaseInput() {
    String input = "-Xms1024m";
    Map<String, String> expected = new HashMap<String, String>();
    expected.put("-Xms", "1024m");
    Map<String, String> actual = JavaOptionsParser.parse(input);
    assertEquals(expected, actual);
  }

  @Test
  public void testSpecialXmsXmsNoValueInput() {
    String input = "-Xms -Xmx";
    Map<String, String> expected = new HashMap<String, String>();
    expected.put("-Xms", "");
    expected.put("-Xmx", "");
    Map<String, String> actual = JavaOptionsParser.parse(input);
    assertEquals(expected, actual);
  }

  @Test
  public void testEqualsBeforeColonDelimiterInput() {
    String input = "-Djava.library.path=:/usr/hdp/2.2.4.0-2633/hadoop/lib/native/Linux-amd64-64:/usr/hdp/2.2.4.0-2633/hadoop/lib/native address=localhost:52102";
    Map<String, String> expected = new HashMap<String, String>();
    expected.put("-Djava.library.path",
        ":/usr/hdp/2.2.4.0-2633/hadoop/lib/native/Linux-amd64-64:/usr/hdp/2.2.4.0-2633/hadoop/lib/native");
    expected.put("address", "localhost:52102");
    Map<String, String> actual = JavaOptionsParser.parse(input);
    assertEquals(expected, actual);
  }

  @Test
  public void testColonBeforeEqualsDelimiterInput() {
    String input = "-agentlib:jdwp=transport=dt_socket";
    Map<String, String> expected = new HashMap<String, String>();
    expected.put("-agentlib", "jdwp=transport=dt_socket");
    Map<String, String> actual = JavaOptionsParser.parse(input);
    assertEquals(expected, actual);
  }

  @Test
  public void testOverridingValues() {
    String input = "-Dhdp.version=2.2.4.0-2633 -Dhdp.version= -XX:prop=2m -XX:prop=3m -XX:+True -XX:-True -Xms256m -Xmx256m -Xms16m -Xmx16m";
    Map<String, String> expected = new HashMap<String, String>();
    expected.put("-Dhdp.version", null);
    expected.put("-XX:prop", "3m");
    expected.put("-XX:True", "false");
    expected.put("-Xms", "16m");
    expected.put("-Xmx", "16m");
    Map<String, String> actual = JavaOptionsParser.parse(input);
    assertEquals(expected, actual);
  }
}
