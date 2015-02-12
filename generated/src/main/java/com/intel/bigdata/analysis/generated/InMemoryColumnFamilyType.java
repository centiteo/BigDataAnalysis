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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>InMemoryColumnFamilyType complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="InMemoryColumnFamilyType">
 *   &lt;complexContent>
 *     &lt;extension base="{}BaseColumnFamilyType">
 *       &lt;sequence>
 *         &lt;element name="qualifierSpec" type="{}InMemoryQualifierType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="multiQualifierSpec" type="{}MultiInMemoryQualifierType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InMemoryColumnFamilyType", propOrder = {
    "qualifierSpec",
    "multiQualifierSpec"
})
public class InMemoryColumnFamilyType
    extends BaseColumnFamilyType
{

    protected List<InMemoryQualifierType> qualifierSpec;
    protected List<MultiInMemoryQualifierType> multiQualifierSpec;

    /**
     * Gets the value of the qualifierSpec property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the qualifierSpec property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getQualifierSpec().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InMemoryQualifierType }
     * 
     * 
     */
    public List<InMemoryQualifierType> getQualifierSpec() {
        if (qualifierSpec == null) {
            qualifierSpec = new ArrayList<InMemoryQualifierType>();
        }
        return this.qualifierSpec;
    }

    /**
     * Gets the value of the multiQualifierSpec property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the multiQualifierSpec property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMultiQualifierSpec().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MultiInMemoryQualifierType }
     * 
     * 
     */
    public List<MultiInMemoryQualifierType> getMultiQualifierSpec() {
        if (multiQualifierSpec == null) {
            multiQualifierSpec = new ArrayList<MultiInMemoryQualifierType>();
        }
        return this.multiQualifierSpec;
    }

}
