//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.7 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2015.02.12 时间 05:35:32 PM CST 
//


package com.intel.bigdata.analysis.generated;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.intel.bigdata.analysis.generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Record_QNAME = new QName("", "record");
    private final static QName _InMemoryRecord_QNAME = new QName("", "inMemoryRecord");
    private final static QName _TxtRecord_QNAME = new QName("", "txtRecord");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.intel.bigdata.analysis.generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RecordSpec }
     * 
     */
    public RecordSpec createRecordSpec() {
        return new RecordSpec();
    }

    /**
     * Create an instance of {@link InMemoryRecordType }
     * 
     */
    public InMemoryRecordType createInMemoryRecordType() {
        return new InMemoryRecordType();
    }

    /**
     * Create an instance of {@link TxtRecordType }
     * 
     */
    public TxtRecordType createTxtRecordType() {
        return new TxtRecordType();
    }

    /**
     * Create an instance of {@link MultiQualifierType }
     * 
     */
    public MultiQualifierType createMultiQualifierType() {
        return new MultiQualifierType();
    }

    /**
     * Create an instance of {@link InMemoryColumnFamilyType }
     * 
     */
    public InMemoryColumnFamilyType createInMemoryColumnFamilyType() {
        return new InMemoryColumnFamilyType();
    }

    /**
     * Create an instance of {@link InMemoryRowKeyType }
     * 
     */
    public InMemoryRowKeyType createInMemoryRowKeyType() {
        return new InMemoryRowKeyType();
    }

    /**
     * Create an instance of {@link ColumnRef }
     * 
     */
    public ColumnRef createColumnRef() {
        return new ColumnRef();
    }

    /**
     * Create an instance of {@link MultiInMemoryQualifierType }
     * 
     */
    public MultiInMemoryQualifierType createMultiInMemoryQualifierType() {
        return new MultiInMemoryQualifierType();
    }

    /**
     * Create an instance of {@link QualifierType }
     * 
     */
    public QualifierType createQualifierType() {
        return new QualifierType();
    }

    /**
     * Create an instance of {@link ColumnSpec }
     * 
     */
    public ColumnSpec createColumnSpec() {
        return new ColumnSpec();
    }

    /**
     * Create an instance of {@link InMemoryQualifierType }
     * 
     */
    public InMemoryQualifierType createInMemoryQualifierType() {
        return new InMemoryQualifierType();
    }

    /**
     * Create an instance of {@link RowKeyFieldType }
     * 
     */
    public RowKeyFieldType createRowKeyFieldType() {
        return new RowKeyFieldType();
    }

    /**
     * Create an instance of {@link BaseColumnFamilyType }
     * 
     */
    public BaseColumnFamilyType createBaseColumnFamilyType() {
        return new BaseColumnFamilyType();
    }

    /**
     * Create an instance of {@link ColumnFamilyType }
     * 
     */
    public ColumnFamilyType createColumnFamilyType() {
        return new ColumnFamilyType();
    }

    /**
     * Create an instance of {@link RowKeyType }
     * 
     */
    public RowKeyType createRowKeyType() {
        return new RowKeyType();
    }

    /**
     * Create an instance of {@link BaseQualifierType }
     * 
     */
    public BaseQualifierType createBaseQualifierType() {
        return new BaseQualifierType();
    }

    /**
     * Create an instance of {@link InMemoryRowKeyFieldType }
     * 
     */
    public InMemoryRowKeyFieldType createInMemoryRowKeyFieldType() {
        return new InMemoryRowKeyFieldType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RecordSpec }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "record")
    public JAXBElement<RecordSpec> createRecord(RecordSpec value) {
        return new JAXBElement<RecordSpec>(_Record_QNAME, RecordSpec.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InMemoryRecordType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "inMemoryRecord")
    public JAXBElement<InMemoryRecordType> createInMemoryRecord(InMemoryRecordType value) {
        return new JAXBElement<InMemoryRecordType>(_InMemoryRecord_QNAME, InMemoryRecordType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TxtRecordType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "txtRecord")
    public JAXBElement<TxtRecordType> createTxtRecord(TxtRecordType value) {
        return new JAXBElement<TxtRecordType>(_TxtRecord_QNAME, TxtRecordType.class, null, value);
    }

}