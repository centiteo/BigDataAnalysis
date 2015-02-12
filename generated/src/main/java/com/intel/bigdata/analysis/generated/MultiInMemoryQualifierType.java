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
 * <p>MultiInMemoryQualifierType complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="MultiInMemoryQualifierType">
 *   &lt;complexContent>
 *     &lt;extension base="{}InMemoryQualifierType">
 *       &lt;sequence>
 *         &lt;element name="qualifierNum" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="qualifierPrefix" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="useConstant" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="constantValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MultiInMemoryQualifierType", propOrder = {
    "qualifierNum",
    "qualifierPrefix",
    "useConstant",
    "constantValue"
})
public class MultiInMemoryQualifierType
    extends InMemoryQualifierType
{

    protected int qualifierNum;
    @XmlElement(required = true)
    protected String qualifierPrefix;
    @XmlElement(defaultValue = "false")
    protected boolean useConstant;
    @XmlElement(required = true)
    protected String constantValue;

    /**
     * 获取qualifierNum属性的值。
     * 
     */
    public int getQualifierNum() {
        return qualifierNum;
    }

    /**
     * 设置qualifierNum属性的值。
     * 
     */
    public void setQualifierNum(int value) {
        this.qualifierNum = value;
    }

    /**
     * 获取qualifierPrefix属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQualifierPrefix() {
        return qualifierPrefix;
    }

    /**
     * 设置qualifierPrefix属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQualifierPrefix(String value) {
        this.qualifierPrefix = value;
    }

    /**
     * 获取useConstant属性的值。
     * 
     */
    public boolean isUseConstant() {
        return useConstant;
    }

    /**
     * 设置useConstant属性的值。
     * 
     */
    public void setUseConstant(boolean value) {
        this.useConstant = value;
    }

    /**
     * 获取constantValue属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConstantValue() {
        return constantValue;
    }

    /**
     * 设置constantValue属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConstantValue(String value) {
        this.constantValue = value;
    }

}
