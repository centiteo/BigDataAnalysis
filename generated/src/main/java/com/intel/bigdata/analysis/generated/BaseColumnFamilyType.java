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
 * <p>BaseColumnFamilyType complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="BaseColumnFamilyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="familyName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="compressionType" type="{}CompressionType"/>
 *         &lt;element name="replication" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="maxVersion" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseColumnFamilyType", propOrder = {
    "familyName",
    "compressionType",
    "replication",
    "maxVersion"
})
@XmlSeeAlso({
    InMemoryColumnFamilyType.class,
    ColumnFamilyType.class
})
public class BaseColumnFamilyType {

    @XmlElement(required = true)
    protected String familyName;
    @XmlElement(required = true)
    protected CompressionType compressionType;
    protected short replication;
    protected int maxVersion;

    /**
     * 获取familyName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFamilyName() {
        return familyName;
    }

    /**
     * 设置familyName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFamilyName(String value) {
        this.familyName = value;
    }

    /**
     * 获取compressionType属性的值。
     * 
     * @return
     *     possible object is
     *     {@link CompressionType }
     *     
     */
    public CompressionType getCompressionType() {
        return compressionType;
    }

    /**
     * 设置compressionType属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link CompressionType }
     *     
     */
    public void setCompressionType(CompressionType value) {
        this.compressionType = value;
    }

    /**
     * 获取replication属性的值。
     * 
     */
    public short getReplication() {
        return replication;
    }

    /**
     * 设置replication属性的值。
     * 
     */
    public void setReplication(short value) {
        this.replication = value;
    }

    /**
     * 获取maxVersion属性的值。
     * 
     */
    public int getMaxVersion() {
        return maxVersion;
    }

    /**
     * 设置maxVersion属性的值。
     * 
     */
    public void setMaxVersion(int value) {
        this.maxVersion = value;
    }

}
