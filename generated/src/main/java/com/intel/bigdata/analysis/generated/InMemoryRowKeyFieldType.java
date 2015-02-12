//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.7 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2015.02.12 时间 05:35:32 PM CST 
//


package com.intel.bigdata.analysis.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>InMemoryRowKeyFieldType complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="InMemoryRowKeyFieldType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="fieldType" type="{}FieldType"/>
 *         &lt;element name="fixedLength" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="length" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="fieldPattern" type="{}FieldPattern"/>
 *         &lt;element name="randomAlgorithm" type="{}RandomAlgorithm"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InMemoryRowKeyFieldType", propOrder = {
    "fieldType",
    "fixedLength",
    "length",
    "fieldPattern",
    "randomAlgorithm"
})
public class InMemoryRowKeyFieldType {

    @XmlElement(required = true)
    protected FieldType fieldType;
    protected boolean fixedLength;
    protected int length;
    @XmlElement(required = true)
    protected FieldPattern fieldPattern;
    @XmlElement(required = true)
    protected RandomAlgorithm randomAlgorithm;

    /**
     * 获取fieldType属性的值。
     * 
     * @return
     *     possible object is
     *     {@link FieldType }
     *     
     */
    public FieldType getFieldType() {
        return fieldType;
    }

    /**
     * 设置fieldType属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link FieldType }
     *     
     */
    public void setFieldType(FieldType value) {
        this.fieldType = value;
    }

    /**
     * 获取fixedLength属性的值。
     * 
     */
    public boolean isFixedLength() {
        return fixedLength;
    }

    /**
     * 设置fixedLength属性的值。
     * 
     */
    public void setFixedLength(boolean value) {
        this.fixedLength = value;
    }

    /**
     * 获取length属性的值。
     * 
     */
    public int getLength() {
        return length;
    }

    /**
     * 设置length属性的值。
     * 
     */
    public void setLength(int value) {
        this.length = value;
    }

    /**
     * 获取fieldPattern属性的值。
     * 
     * @return
     *     possible object is
     *     {@link FieldPattern }
     *     
     */
    public FieldPattern getFieldPattern() {
        return fieldPattern;
    }

    /**
     * 设置fieldPattern属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link FieldPattern }
     *     
     */
    public void setFieldPattern(FieldPattern value) {
        this.fieldPattern = value;
    }

    /**
     * 获取randomAlgorithm属性的值。
     * 
     * @return
     *     possible object is
     *     {@link RandomAlgorithm }
     *     
     */
    public RandomAlgorithm getRandomAlgorithm() {
        return randomAlgorithm;
    }

    /**
     * 设置randomAlgorithm属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link RandomAlgorithm }
     *     
     */
    public void setRandomAlgorithm(RandomAlgorithm value) {
        this.randomAlgorithm = value;
    }

}
