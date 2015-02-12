//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.7 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2015.02.12 时间 05:35:32 PM CST 
//


package com.intel.bigdata.analysis.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>QualifierType complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="QualifierType">
 *   &lt;complexContent>
 *     &lt;extension base="{}BaseQualifierType">
 *       &lt;sequence>
 *         &lt;element name="startPos" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="length" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="fieldIndex" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QualifierType", propOrder = {
    "startPos",
    "length",
    "fieldIndex"
})
public class QualifierType
    extends BaseQualifierType
{

    protected int startPos;
    protected int length;
    protected int fieldIndex;

    /**
     * 获取startPos属性的值。
     * 
     */
    public int getStartPos() {
        return startPos;
    }

    /**
     * 设置startPos属性的值。
     * 
     */
    public void setStartPos(int value) {
        this.startPos = value;
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
     * 获取fieldIndex属性的值。
     * 
     */
    public int getFieldIndex() {
        return fieldIndex;
    }

    /**
     * 设置fieldIndex属性的值。
     * 
     */
    public void setFieldIndex(int value) {
        this.fieldIndex = value;
    }

}
