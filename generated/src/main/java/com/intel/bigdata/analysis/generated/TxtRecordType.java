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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>TxtRecordType complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="TxtRecordType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="useSeparater" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="inputSeparater" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="rowKeySpec" type="{}RowKeyType"/>
 *         &lt;element name="columnFamilySpec" type="{}ColumnFamilyType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TxtRecordType", propOrder = {
    "useSeparater",
    "inputSeparater",
    "rowKeySpec",
    "columnFamilySpec"
})
public class TxtRecordType {

    protected boolean useSeparater;
    @XmlElement(required = true, defaultValue = "|")
    protected String inputSeparater;
    @XmlElement(required = true)
    protected RowKeyType rowKeySpec;
    @XmlElement(required = true)
    protected List<ColumnFamilyType> columnFamilySpec;

    /**
     * 获取useSeparater属性的值。
     * 
     */
    public boolean isUseSeparater() {
        return useSeparater;
    }

    /**
     * 设置useSeparater属性的值。
     * 
     */
    public void setUseSeparater(boolean value) {
        this.useSeparater = value;
    }

    /**
     * 获取inputSeparater属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInputSeparater() {
        return inputSeparater;
    }

    /**
     * 设置inputSeparater属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInputSeparater(String value) {
        this.inputSeparater = value;
    }

    /**
     * 获取rowKeySpec属性的值。
     * 
     * @return
     *     possible object is
     *     {@link RowKeyType }
     *     
     */
    public RowKeyType getRowKeySpec() {
        return rowKeySpec;
    }

    /**
     * 设置rowKeySpec属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link RowKeyType }
     *     
     */
    public void setRowKeySpec(RowKeyType value) {
        this.rowKeySpec = value;
    }

    /**
     * Gets the value of the columnFamilySpec property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the columnFamilySpec property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getColumnFamilySpec().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ColumnFamilyType }
     * 
     * 
     */
    public List<ColumnFamilyType> getColumnFamilySpec() {
        if (columnFamilySpec == null) {
            columnFamilySpec = new ArrayList<ColumnFamilyType>();
        }
        return this.columnFamilySpec;
    }

}
