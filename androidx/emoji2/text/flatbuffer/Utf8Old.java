package androidx.emoji2.text.flatbuffer;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.StandardCharsets;

public class Utf8Old extends Utf8 {
  private static final ThreadLocal<Cache> CACHE = ThreadLocal.withInitial(Utf8Old$$ExternalSyntheticLambda0.INSTANCE);
  
  public String decodeUtf8(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2) {
    CharsetDecoder charsetDecoder = ((Cache)CACHE.get()).decoder;
    charsetDecoder.reset();
    paramByteBuffer = paramByteBuffer.duplicate();
    paramByteBuffer.position(paramInt1);
    paramByteBuffer.limit(paramInt1 + paramInt2);
    try {
      return charsetDecoder.decode(paramByteBuffer).toString();
    } catch (CharacterCodingException characterCodingException) {
      throw new IllegalArgumentException("Bad encoding", characterCodingException);
    } 
  }
  
  public void encodeUtf8(CharSequence paramCharSequence, ByteBuffer paramByteBuffer) {
    Cache cache = CACHE.get();
    if (cache.lastInput != paramCharSequence)
      encodedLength(paramCharSequence); 
    paramByteBuffer.put(cache.lastOutput);
  }
  
  public int encodedLength(CharSequence paramCharSequence) {
    Cache cache = CACHE.get();
    int i = (int)(paramCharSequence.length() * cache.encoder.maxBytesPerChar());
    if (cache.lastOutput == null || cache.lastOutput.capacity() < i)
      cache.lastOutput = ByteBuffer.allocate(Math.max(128, i)); 
    cache.lastOutput.clear();
    cache.lastInput = paramCharSequence;
    if (paramCharSequence instanceof CharBuffer) {
      paramCharSequence = paramCharSequence;
    } else {
      paramCharSequence = CharBuffer.wrap(paramCharSequence);
    } 
    CoderResult coderResult = cache.encoder.encode((CharBuffer)paramCharSequence, cache.lastOutput, true);
    if (coderResult.isError())
      try {
        coderResult.throwException();
      } catch (CharacterCodingException characterCodingException) {
        throw new IllegalArgumentException("bad character encoding", characterCodingException);
      }  
    cache.lastOutput.flip();
    return cache.lastOutput.remaining();
  }
  
  private static class Cache {
    final CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
    
    final CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
    
    CharSequence lastInput = null;
    
    ByteBuffer lastOutput = null;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\flatbuffer\Utf8Old.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */