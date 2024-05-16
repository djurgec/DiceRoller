package androidx.emoji2.text;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.SparseArray;
import androidx.core.os.TraceCompat;
import androidx.core.util.Preconditions;
import androidx.emoji2.text.flatbuffer.MetadataList;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public final class MetadataRepo {
  private static final int DEFAULT_ROOT_SIZE = 1024;
  
  private static final String S_TRACE_CREATE_REPO = "EmojiCompat.MetadataRepo.create";
  
  private final char[] mEmojiCharArray;
  
  private final MetadataList mMetadataList;
  
  private final Node mRootNode;
  
  private final Typeface mTypeface;
  
  private MetadataRepo(Typeface paramTypeface, MetadataList paramMetadataList) {
    this.mTypeface = paramTypeface;
    this.mMetadataList = paramMetadataList;
    this.mRootNode = new Node(1024);
    this.mEmojiCharArray = new char[paramMetadataList.listLength() * 2];
    constructIndex(paramMetadataList);
  }
  
  private void constructIndex(MetadataList paramMetadataList) {
    int i = paramMetadataList.listLength();
    for (byte b = 0; b < i; b++) {
      EmojiMetadata emojiMetadata = new EmojiMetadata(this, b);
      Character.toChars(emojiMetadata.getId(), this.mEmojiCharArray, b * 2);
      put(emojiMetadata);
    } 
  }
  
  public static MetadataRepo create(AssetManager paramAssetManager, String paramString) throws IOException {
    try {
      TraceCompat.beginSection("EmojiCompat.MetadataRepo.create");
      return new MetadataRepo(Typeface.createFromAsset(paramAssetManager, paramString), MetadataListReader.read(paramAssetManager, paramString));
    } finally {
      TraceCompat.endSection();
    } 
  }
  
  public static MetadataRepo create(Typeface paramTypeface) {
    try {
      TraceCompat.beginSection("EmojiCompat.MetadataRepo.create");
      MetadataList metadataList = new MetadataList();
      this();
      return new MetadataRepo(paramTypeface, metadataList);
    } finally {
      TraceCompat.endSection();
    } 
  }
  
  public static MetadataRepo create(Typeface paramTypeface, InputStream paramInputStream) throws IOException {
    try {
      TraceCompat.beginSection("EmojiCompat.MetadataRepo.create");
      return new MetadataRepo(paramTypeface, MetadataListReader.read(paramInputStream));
    } finally {
      TraceCompat.endSection();
    } 
  }
  
  public static MetadataRepo create(Typeface paramTypeface, ByteBuffer paramByteBuffer) throws IOException {
    try {
      TraceCompat.beginSection("EmojiCompat.MetadataRepo.create");
      return new MetadataRepo(paramTypeface, MetadataListReader.read(paramByteBuffer));
    } finally {
      TraceCompat.endSection();
    } 
  }
  
  public char[] getEmojiCharArray() {
    return this.mEmojiCharArray;
  }
  
  public MetadataList getMetadataList() {
    return this.mMetadataList;
  }
  
  int getMetadataVersion() {
    return this.mMetadataList.version();
  }
  
  Node getRootNode() {
    return this.mRootNode;
  }
  
  Typeface getTypeface() {
    return this.mTypeface;
  }
  
  void put(EmojiMetadata paramEmojiMetadata) {
    boolean bool;
    Preconditions.checkNotNull(paramEmojiMetadata, "emoji metadata cannot be null");
    if (paramEmojiMetadata.getCodepointsLength() > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    Preconditions.checkArgument(bool, "invalid metadata codepoint length");
    this.mRootNode.put(paramEmojiMetadata, 0, paramEmojiMetadata.getCodepointsLength() - 1);
  }
  
  static class Node {
    private final SparseArray<Node> mChildren;
    
    private EmojiMetadata mData;
    
    private Node() {
      this(1);
    }
    
    Node(int param1Int) {
      this.mChildren = new SparseArray(param1Int);
    }
    
    Node get(int param1Int) {
      Node node;
      SparseArray<Node> sparseArray = this.mChildren;
      if (sparseArray == null) {
        sparseArray = null;
      } else {
        node = (Node)sparseArray.get(param1Int);
      } 
      return node;
    }
    
    final EmojiMetadata getData() {
      return this.mData;
    }
    
    void put(EmojiMetadata param1EmojiMetadata, int param1Int1, int param1Int2) {
      Node node2 = get(param1EmojiMetadata.getCodepointAt(param1Int1));
      Node node1 = node2;
      if (node2 == null) {
        node1 = new Node();
        this.mChildren.put(param1EmojiMetadata.getCodepointAt(param1Int1), node1);
      } 
      if (param1Int2 > param1Int1) {
        node1.put(param1EmojiMetadata, param1Int1 + 1, param1Int2);
      } else {
        node1.mData = param1EmojiMetadata;
      } 
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\MetadataRepo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */