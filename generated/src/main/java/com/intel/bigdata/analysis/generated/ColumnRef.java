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
 * <p>ColumnRef complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ColumnRef">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="refColumn" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="refStart" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="refEnd" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="refOp" type="{}OpType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ColumnRef", propOrder = {
    "refColumn",
    "refStart",
    "refEnd",
    "refOp"
})
public class ColumnRef {

    @XmlElement(required = true)
    protected String refColumn;
    protected int refStart;
    protected int refEnd;
    @XmlElement(required = true)
    protected OpType refOp;

    /**
     * 获取refColumn属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefColumn() {
        return refColumn;
    }

    /**
     * 设置refColumn属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefColumn(String value) {
        this.refColumn = value;
    }

    /**
     * 获取refStart属性的值。
     * 
     */
    public int getRefStart() {
        return refStart;
    }

    /**
     * 设置refStart属性的值。
     * 
     */
    public void setRefStart(int value) {
        this.refStart = value;
    }

    /**
     * 获取refEnd属性的值。
     * 
     */
    public int getRefEnd() {
        return refEnd;
    }

    /**
     * 设置refEnd属性的值。
     * 
     */
    public void setRefEnd(int value) {
        this.refEnd = value;
    }

    /**
     * 获取refOp属性的值。
     * 
     * @return
     *     possible object is
     *     {@link OpType }
     *     
     */
    public OpType getRefOp() {
        return refOp;
    }

    /**
     * 设置refOp属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link OpType }
     *     
     */
    public void setRefOp(OpType value) {
        this.refOp = value;
    }

}
