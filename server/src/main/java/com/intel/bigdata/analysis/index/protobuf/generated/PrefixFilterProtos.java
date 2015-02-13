// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: PrefixFilter.proto

package com.intel.bigdata.analysis.index.protobuf.generated;

public final class PrefixFilterProtos {
  private PrefixFilterProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface PrefixFilterOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    // optional bytes prefix = 1;
    /**
     * <code>optional bytes prefix = 1;</code>
     */
    boolean hasPrefix();
    /**
     * <code>optional bytes prefix = 1;</code>
     */
    com.google.protobuf.ByteString getPrefix();
  }
  /**
   * Protobuf type {@code PrefixFilter}
   */
  public static final class PrefixFilter extends
      com.google.protobuf.GeneratedMessage
      implements PrefixFilterOrBuilder {
    // Use PrefixFilter.newBuilder() to construct.
    private PrefixFilter(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private PrefixFilter(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final PrefixFilter defaultInstance;
    public static PrefixFilter getDefaultInstance() {
      return defaultInstance;
    }

    public PrefixFilter getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private PrefixFilter(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              bitField0_ |= 0x00000001;
              prefix_ = input.readBytes();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.internal_static_PrefixFilter_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.internal_static_PrefixFilter_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.PrefixFilter.class, com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.PrefixFilter.Builder.class);
    }

    public static com.google.protobuf.Parser<PrefixFilter> PARSER =
        new com.google.protobuf.AbstractParser<PrefixFilter>() {
      public PrefixFilter parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new PrefixFilter(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<PrefixFilter> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    // optional bytes prefix = 1;
    public static final int PREFIX_FIELD_NUMBER = 1;
    private com.google.protobuf.ByteString prefix_;
    /**
     * <code>optional bytes prefix = 1;</code>
     */
    public boolean hasPrefix() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional bytes prefix = 1;</code>
     */
    public com.google.protobuf.ByteString getPrefix() {
      return prefix_;
    }

    private void initFields() {
      prefix_ = com.google.protobuf.ByteString.EMPTY;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, prefix_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, prefix_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.PrefixFilter)) {
        return super.equals(obj);
      }
      com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.PrefixFilter other = (com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.PrefixFilter) obj;

      boolean result = true;
      result = result && (hasPrefix() == other.hasPrefix());
      if (hasPrefix()) {
        result = result && getPrefix()
            .equals(other.getPrefix());
      }
      result = result &&
          getUnknownFields().equals(other.getUnknownFields());
      return result;
    }

    private int memoizedHashCode = 0;
    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptorForType().hashCode();
      if (hasPrefix()) {
        hash = (37 * hash) + PREFIX_FIELD_NUMBER;
        hash = (53 * hash) + getPrefix().hashCode();
      }
      hash = (29 * hash) + getUnknownFields().hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.PrefixFilter parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.PrefixFilter parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.PrefixFilter parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.PrefixFilter parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.PrefixFilter parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.PrefixFilter parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.PrefixFilter parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.PrefixFilter parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.PrefixFilter parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.PrefixFilter parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.PrefixFilter prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code PrefixFilter}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.PrefixFilterOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.internal_static_PrefixFilter_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.internal_static_PrefixFilter_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.PrefixFilter.class, com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.PrefixFilter.Builder.class);
      }

      // Construct using com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.PrefixFilter.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        prefix_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.internal_static_PrefixFilter_descriptor;
      }

      public com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.PrefixFilter getDefaultInstanceForType() {
        return com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.PrefixFilter.getDefaultInstance();
      }

      public com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.PrefixFilter build() {
        com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.PrefixFilter result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.PrefixFilter buildPartial() {
        com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.PrefixFilter result = new com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.PrefixFilter(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.prefix_ = prefix_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.PrefixFilter) {
          return mergeFrom((com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.PrefixFilter)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.PrefixFilter other) {
        if (other == com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.PrefixFilter.getDefaultInstance()) return this;
        if (other.hasPrefix()) {
          setPrefix(other.getPrefix());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.PrefixFilter parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.intel.bigdata.analysis.index.protobuf.generated.PrefixFilterProtos.PrefixFilter) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      // optional bytes prefix = 1;
      private com.google.protobuf.ByteString prefix_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <code>optional bytes prefix = 1;</code>
       */
      public boolean hasPrefix() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional bytes prefix = 1;</code>
       */
      public com.google.protobuf.ByteString getPrefix() {
        return prefix_;
      }
      /**
       * <code>optional bytes prefix = 1;</code>
       */
      public Builder setPrefix(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        prefix_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional bytes prefix = 1;</code>
       */
      public Builder clearPrefix() {
        bitField0_ = (bitField0_ & ~0x00000001);
        prefix_ = getDefaultInstance().getPrefix();
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:PrefixFilter)
    }

    static {
      defaultInstance = new PrefixFilter(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:PrefixFilter)
  }

  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_PrefixFilter_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_PrefixFilter_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\022PrefixFilter.proto\"\036\n\014PrefixFilter\022\016\n\006" +
      "prefix\030\001 \001(\014BQ\n3com.intel.bigdata.analys" +
      "is.index.protobuf.generatedB\022PrefixFilte" +
      "rProtosH\001\210\001\001\240\001\001"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_PrefixFilter_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_PrefixFilter_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_PrefixFilter_descriptor,
              new java.lang.String[] { "Prefix", });
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }

  // @@protoc_insertion_point(outer_class_scope)
}