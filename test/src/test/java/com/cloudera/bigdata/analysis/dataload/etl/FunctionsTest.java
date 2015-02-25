package com.cloudera.bigdata.analysis.dataload.etl;

import org.apache.commons.lang.CharSetUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.cloudera.bigdata.analysis.dataload.util.CommonUtils;

public class FunctionsTest {

  public static void main(String[] args) {

    String constExpressions = "'abc'";

    if (CommonUtils.isSingleQuotationMarksWrapedExpression(constExpressions)) {
      String stripStr = StringUtils.strip(constExpressions, "'");
      System.out.println(stripStr);
      System.out.println("Const Value:"
          + CommonUtils
              .getSingleQuotationMarksWrapedExpressionValue(constExpressions));
    }

    System.out.println(CharSetUtils.delete("abcadaeaaf", "abcde"));

    System.out.println(CharSetUtils.keep("abcadaeaaf", "abcde"));

    System.out.println(CharSetUtils.squeeze("a    bbbbbb c dd", "b d"));

    String[] header = new String[3];

    header[0] = StringUtils.repeat("*", 50);

    header[1] = StringUtils.center("  StringUtilsDemo  ", 50, "^O^");

    header[2] = header[0];

    String head = StringUtils.join(header, "\n");

    System.out.println(head);

    
    System.out.println(StringUtils.abbreviate(

    "The quick brown fox jumps over the lazy dog.", 10));

    System.out.println(StringUtils.abbreviate(

    "The quick brown fox jumps over the lazy dog.", 17, 10));

    System.out.println(StringUtils.strip("fsfsdf", "f"));

    System.out.println(StringUtils.stripStart("ddsuuu", "d"));

    System.out.println(StringUtils.stripEnd("dabads", "das"));

    
    System.out.println(StringUtils.isAlpha("ab12"));

    
    System.out.println(StringUtils.isAlphanumeric("12as"));

    System.out.println(StringUtils.isBlank(null));

    System.out.println(StringUtils.isNumeric("123"));

    
    System.out.println(StringUtils.indexOfDifference("aaabc", "aaacc"));

    
    System.out.println(StringUtils.difference("aaabcde", "aaaccde"));

    
    System.out.println(StringUtils.chomp("aaabcde"));

    
    System.out.println(StringUtils.containsOnly("aad", "aadd"));

   
    System.out.println(StringUtils.containsNone("defg", "aadd"));

   
    System.out.println(StringUtils.contains("defg", "ef"));

    System.out.println(StringUtils.containsOnly("ef", "defg"));

    
    System.out.println(StringUtils.defaultString("aaaa"));

    System.out.println("?" + StringUtils.defaultString(null) + "!");

   
    System.out.println(StringUtils.deleteWhitespace("aa  bb  cc"));

    System.out.println(ObjectUtils.defaultIfNull(null, "a"));

    System.out.println(WordUtils.wrap("aa a", 2));

    System.out.println(NumberUtils.isDigits("123456a"));

    
    System.out.println(NumberUtils.isDigits("123.1"));

    
    System.out.println(NumberUtils.isNumber("0123*1"));

    System.out.println(StringUtils.defaultIfEmpty(null, "aa"));

    System.out.println(StringUtils.chomp("aaabb", "bb"));

    if (StringUtils.isBlank(null))
      System.out.println("Null");

  }

}
