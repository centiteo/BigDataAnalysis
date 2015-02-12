//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.7 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2015.02.12 时间 05:35:32 PM CST 
//


package com.intel.bigdata.analysis.generated;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>RandomType的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * <p>
 * <pre>
 * &lt;simpleType name="RandomType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="LONG"/>
 *     &lt;enumeration value="BOOLEAN"/>
 *     &lt;enumeration value="PHONE"/>
 *     &lt;enumeration value="CORRELATE"/>
 *     &lt;enumeration value="UDF"/>
 *     &lt;enumeration value="DATETIME"/>
 *     &lt;enumeration value="CONSTANT"/>
 *     &lt;enumeration value="STRING"/>
 *     &lt;enumeration value="URL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "RandomType")
@XmlEnum
public enum RandomType {

    LONG,
    BOOLEAN,
    PHONE,
    CORRELATE,
    UDF,
    DATETIME,
    CONSTANT,
    STRING,
    URL;

    public String value() {
        return name();
    }

    public static RandomType fromValue(String v) {
        return valueOf(v);
    }

}
