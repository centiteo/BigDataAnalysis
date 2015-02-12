//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.7 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2015.02.12 时间 05:35:32 PM CST 
//


package com.intel.bigdata.analysis.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>RowKeyType complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="RowKeyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="rowKeyFieldSpec" type="{}RowKeyFieldType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="rowKeySeparater" type="{http://www.w3.org/2001/XMLSchema}string" default="|" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RowKeyType", propOrder = {
    "rowKeyFieldSpec"
})
public class RowKeyType {

    @XmlElement(required = true)
    protected List<RowKeyFieldType> rowKeyFieldSpec;
    @XmlAttribute(name = "rowKeySeparater")
    protected String rowKeySeparater;

    /**
     * Gets the value of the rowKeyFieldSpec property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rowKeyFieldSpec property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRowKeyFieldSpec().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RowKeyFieldType }
     * 
     * 
     */
    public List<RowKeyFieldType> getRowKeyFieldSpec() {
        if (rowKeyFieldSpec == null) {
            rowKeyFieldSpec = new ArrayList<RowKeyFieldType>();
        }
        return this.rowKeyFieldSpec;
    }

    /**
     * 获取rowKeySeparater属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRowKeySeparater() {
        if (rowKeySeparater == null) {
            return "|";
        } else {
            return rowKeySeparater;
        }
    }

    /**
     * 设置rowKeySeparater属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRowKeySeparater(String value) {
        this.rowKeySeparater = value;
    }

}
