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
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>BaseQualifierType complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="BaseQualifierType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="qualifierName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="buildIndex" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseQualifierType", propOrder = {
    "qualifierName",
    "buildIndex"
})
@XmlSeeAlso({
    MultiQualifierType.class,
    QualifierType.class,
    InMemoryQualifierType.class
})
public class BaseQualifierType {

    @XmlElement(required = true)
    protected String qualifierName;
    @XmlElement(defaultValue = "false")
    protected boolean buildIndex;

    /**
     * 获取qualifierName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQualifierName() {
        return qualifierName;
    }

    /**
     * 设置qualifierName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQualifierName(String value) {
        this.qualifierName = value;
    }

    /**
     * 获取buildIndex属性的值。
     * 
     */
    public boolean isBuildIndex() {
        return buildIndex;
    }

    /**
     * 设置buildIndex属性的值。
     * 
     */
    public void setBuildIndex(boolean value) {
        this.buildIndex = value;
    }

}
