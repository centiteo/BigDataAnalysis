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
 * <p>ColumnSpec complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ColumnSpec">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="start" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="end" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="length" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="pattern" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="udfName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ref" type="{}ColumnRef"/>
 *         &lt;element name="randomPattern" type="{}RandomPattern"/>
 *         &lt;element name="randomType" type="{}RandomType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ColumnSpec", propOrder = {
    "name",
    "start",
    "end",
    "value",
    "length",
    "pattern",
    "udfName",
    "ref",
    "randomPattern",
    "randomType"
})
public class ColumnSpec {

    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected String start;
    @XmlElement(required = true)
    protected String end;
    @XmlElement(required = true)
    protected String value;
    protected long length;
    @XmlElement(required = true)
    protected String pattern;
    @XmlElement(required = true)
    protected String udfName;
    @XmlElement(required = true)
    protected ColumnRef ref;
    @XmlElement(required = true)
    protected RandomPattern randomPattern;
    @XmlElement(required = true)
    protected RandomType randomType;

    /**
     * 获取name属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * 设置name属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * 获取start属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStart() {
        return start;
    }

    /**
     * 设置start属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStart(String value) {
        this.start = value;
    }

    /**
     * 获取end属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnd() {
        return end;
    }

    /**
     * 设置end属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnd(String value) {
        this.end = value;
    }

    /**
     * 获取value属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置value属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 获取length属性的值。
     * 
     */
    public long getLength() {
        return length;
    }

    /**
     * 设置length属性的值。
     * 
     */
    public void setLength(long value) {
        this.length = value;
    }

    /**
     * 获取pattern属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * 设置pattern属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPattern(String value) {
        this.pattern = value;
    }

    /**
     * 获取udfName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUdfName() {
        return udfName;
    }

    /**
     * 设置udfName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUdfName(String value) {
        this.udfName = value;
    }

    /**
     * 获取ref属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ColumnRef }
     *     
     */
    public ColumnRef getRef() {
        return ref;
    }

    /**
     * 设置ref属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ColumnRef }
     *     
     */
    public void setRef(ColumnRef value) {
        this.ref = value;
    }

    /**
     * 获取randomPattern属性的值。
     * 
     * @return
     *     possible object is
     *     {@link RandomPattern }
     *     
     */
    public RandomPattern getRandomPattern() {
        return randomPattern;
    }

    /**
     * 设置randomPattern属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link RandomPattern }
     *     
     */
    public void setRandomPattern(RandomPattern value) {
        this.randomPattern = value;
    }

    /**
     * 获取randomType属性的值。
     * 
     * @return
     *     possible object is
     *     {@link RandomType }
     *     
     */
    public RandomType getRandomType() {
        return randomType;
    }

    /**
     * 设置randomType属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link RandomType }
     *     
     */
    public void setRandomType(RandomType value) {
        this.randomType = value;
    }

}
