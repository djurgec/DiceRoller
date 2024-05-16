package com.bumptech.glide.load.data;

import android.content.ContentResolver;
import android.content.UriMatcher;
import android.net.Uri;
import android.provider.ContactsContract;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class StreamLocalUriFetcher extends LocalUriFetcher<InputStream> {
  private static final int ID_CONTACTS_CONTACT = 3;
  
  private static final int ID_CONTACTS_LOOKUP = 1;
  
  private static final int ID_CONTACTS_PHOTO = 4;
  
  private static final int ID_CONTACTS_THUMBNAIL = 2;
  
  private static final int ID_LOOKUP_BY_PHONE = 5;
  
  private static final UriMatcher URI_MATCHER;
  
  static {
    UriMatcher uriMatcher = new UriMatcher(-1);
    URI_MATCHER = uriMatcher;
    uriMatcher.addURI("com.android.contacts", "contacts/lookup/*/#", 1);
    uriMatcher.addURI("com.android.contacts", "contacts/lookup/*", 1);
    uriMatcher.addURI("com.android.contacts", "contacts/#/photo", 2);
    uriMatcher.addURI("com.android.contacts", "contacts/#", 3);
    uriMatcher.addURI("com.android.contacts", "contacts/#/display_photo", 4);
    uriMatcher.addURI("com.android.contacts", "phone_lookup/*", 5);
  }
  
  public StreamLocalUriFetcher(ContentResolver paramContentResolver, Uri paramUri) {
    super(paramContentResolver, paramUri);
  }
  
  private InputStream loadResourceFromUri(Uri paramUri, ContentResolver paramContentResolver) throws FileNotFoundException {
    switch (URI_MATCHER.match(paramUri)) {
      default:
        return paramContentResolver.openInputStream(paramUri);
      case 3:
        return openContactPhotoInputStream(paramContentResolver, paramUri);
      case 1:
      case 5:
        break;
    } 
    paramUri = ContactsContract.Contacts.lookupContact(paramContentResolver, paramUri);
    if (paramUri != null)
      return openContactPhotoInputStream(paramContentResolver, paramUri); 
    throw new FileNotFoundException("Contact cannot be found");
  }
  
  private InputStream openContactPhotoInputStream(ContentResolver paramContentResolver, Uri paramUri) {
    return ContactsContract.Contacts.openContactPhotoInputStream(paramContentResolver, paramUri, true);
  }
  
  protected void close(InputStream paramInputStream) throws IOException {
    paramInputStream.close();
  }
  
  public Class<InputStream> getDataClass() {
    return InputStream.class;
  }
  
  protected InputStream loadResource(Uri paramUri, ContentResolver paramContentResolver) throws FileNotFoundException {
    InputStream inputStream = loadResourceFromUri(paramUri, paramContentResolver);
    if (inputStream != null)
      return inputStream; 
    throw new FileNotFoundException("InputStream is null for " + paramUri);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\data\StreamLocalUriFetcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */