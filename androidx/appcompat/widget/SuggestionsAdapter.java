package androidx.appcompat.widget;

import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.core.content.ContextCompat;
import androidx.cursoradapter.widget.ResourceCursorAdapter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.WeakHashMap;

class SuggestionsAdapter extends ResourceCursorAdapter implements View.OnClickListener {
  private static final boolean DBG = false;
  
  static final int INVALID_INDEX = -1;
  
  private static final String LOG_TAG = "SuggestionsAdapter";
  
  private static final int QUERY_LIMIT = 50;
  
  static final int REFINE_ALL = 2;
  
  static final int REFINE_BY_ENTRY = 1;
  
  static final int REFINE_NONE = 0;
  
  private boolean mClosed = false;
  
  private final int mCommitIconResId;
  
  private int mFlagsCol = -1;
  
  private int mIconName1Col = -1;
  
  private int mIconName2Col = -1;
  
  private final WeakHashMap<String, Drawable.ConstantState> mOutsideDrawablesCache;
  
  private final Context mProviderContext;
  
  private int mQueryRefinement = 1;
  
  private final SearchView mSearchView;
  
  private final SearchableInfo mSearchable;
  
  private int mText1Col = -1;
  
  private int mText2Col = -1;
  
  private int mText2UrlCol = -1;
  
  private ColorStateList mUrlColor;
  
  public SuggestionsAdapter(Context paramContext, SearchView paramSearchView, SearchableInfo paramSearchableInfo, WeakHashMap<String, Drawable.ConstantState> paramWeakHashMap) {
    super(paramContext, paramSearchView.getSuggestionRowLayout(), null, true);
    this.mSearchView = paramSearchView;
    this.mSearchable = paramSearchableInfo;
    this.mCommitIconResId = paramSearchView.getSuggestionCommitIconResId();
    this.mProviderContext = paramContext;
    this.mOutsideDrawablesCache = paramWeakHashMap;
  }
  
  private Drawable checkIconCache(String paramString) {
    Drawable.ConstantState constantState = this.mOutsideDrawablesCache.get(paramString);
    return (constantState == null) ? null : constantState.newDrawable();
  }
  
  private CharSequence formatUrl(CharSequence paramCharSequence) {
    if (this.mUrlColor == null) {
      TypedValue typedValue = new TypedValue();
      this.mProviderContext.getTheme().resolveAttribute(R.attr.textColorSearchUrl, typedValue, true);
      this.mUrlColor = this.mProviderContext.getResources().getColorStateList(typedValue.resourceId);
    } 
    SpannableString spannableString = new SpannableString(paramCharSequence);
    spannableString.setSpan(new TextAppearanceSpan(null, 0, 0, this.mUrlColor, null), 0, paramCharSequence.length(), 33);
    return (CharSequence)spannableString;
  }
  
  private Drawable getActivityIcon(ComponentName paramComponentName) {
    PackageManager packageManager = this.mProviderContext.getPackageManager();
    try {
      ActivityInfo activityInfo = packageManager.getActivityInfo(paramComponentName, 128);
      int i = activityInfo.getIconResource();
      if (i == 0)
        return null; 
      Drawable drawable = packageManager.getDrawable(paramComponentName.getPackageName(), i, activityInfo.applicationInfo);
      if (drawable == null) {
        Log.w("SuggestionsAdapter", "Invalid icon resource " + i + " for " + paramComponentName.flattenToShortString());
        return null;
      } 
      return drawable;
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      Log.w("SuggestionsAdapter", nameNotFoundException.toString());
      return null;
    } 
  }
  
  private Drawable getActivityIconWithCache(ComponentName paramComponentName) {
    Drawable drawable1;
    Drawable.ConstantState constantState1;
    String str = paramComponentName.flattenToShortString();
    boolean bool = this.mOutsideDrawablesCache.containsKey(str);
    Drawable drawable2 = null;
    Drawable.ConstantState constantState2 = null;
    if (bool) {
      constantState1 = this.mOutsideDrawablesCache.get(str);
      if (constantState1 == null) {
        constantState1 = constantState2;
      } else {
        drawable1 = constantState1.newDrawable(this.mProviderContext.getResources());
      } 
      return drawable1;
    } 
    Drawable drawable3 = getActivityIcon((ComponentName)drawable1);
    if (drawable3 == null) {
      drawable1 = drawable2;
    } else {
      constantState1 = drawable3.getConstantState();
    } 
    this.mOutsideDrawablesCache.put(str, constantState1);
    return drawable3;
  }
  
  public static String getColumnString(Cursor paramCursor, String paramString) {
    return getStringOrNull(paramCursor, paramCursor.getColumnIndex(paramString));
  }
  
  private Drawable getDefaultIcon1() {
    Drawable drawable = getActivityIconWithCache(this.mSearchable.getSearchActivity());
    return (drawable != null) ? drawable : this.mProviderContext.getPackageManager().getDefaultActivityIcon();
  }
  
  private Drawable getDrawable(Uri paramUri) {
    try {
      boolean bool = "android.resource".equals(paramUri.getScheme());
      if (bool)
        try {
          return getDrawableFromResourceUri(paramUri);
        } catch (android.content.res.Resources.NotFoundException notFoundException) {
          FileNotFoundException fileNotFoundException1 = new FileNotFoundException();
          StringBuilder stringBuilder1 = new StringBuilder();
          this();
          this(stringBuilder1.append("Resource does not exist: ").append(paramUri).toString());
          throw fileNotFoundException1;
        }  
      InputStream inputStream = this.mProviderContext.getContentResolver().openInputStream(paramUri);
      if (inputStream != null) {
        StringBuilder stringBuilder1;
        try {
          return Drawable.createFromStream(inputStream, null);
        } finally {
          try {
            stringBuilder1.close();
          } catch (IOException iOException) {
            StringBuilder stringBuilder2 = new StringBuilder();
            this();
            Log.e("SuggestionsAdapter", stringBuilder2.append("Error closing icon stream for ").append(paramUri).toString(), iOException);
          } 
        } 
      } 
      FileNotFoundException fileNotFoundException = new FileNotFoundException();
      StringBuilder stringBuilder = new StringBuilder();
      this();
      this(stringBuilder.append("Failed to open ").append(paramUri).toString());
      throw fileNotFoundException;
    } catch (FileNotFoundException fileNotFoundException) {
      Log.w("SuggestionsAdapter", "Icon not found: " + paramUri + ", " + fileNotFoundException.getMessage());
      return null;
    } 
  }
  
  private Drawable getDrawableFromResourceValue(String paramString) {
    if (paramString == null || paramString.isEmpty() || "0".equals(paramString))
      return null; 
    try {
      int i = Integer.parseInt(paramString);
      StringBuilder stringBuilder = new StringBuilder();
      this();
      String str = stringBuilder.append("android.resource://").append(this.mProviderContext.getPackageName()).append("/").append(i).toString();
      Drawable drawable = checkIconCache(str);
      if (drawable != null)
        return drawable; 
      drawable = ContextCompat.getDrawable(this.mProviderContext, i);
      storeInIconCache(str, drawable);
      return drawable;
    } catch (NumberFormatException numberFormatException) {
      Drawable drawable = checkIconCache(paramString);
      if (drawable != null)
        return drawable; 
      drawable = getDrawable(Uri.parse(paramString));
      storeInIconCache(paramString, drawable);
      return drawable;
    } catch (android.content.res.Resources.NotFoundException notFoundException) {
      Log.w("SuggestionsAdapter", "Icon resource not found: " + paramString);
      return null;
    } 
  }
  
  private Drawable getIcon1(Cursor paramCursor) {
    int i = this.mIconName1Col;
    if (i == -1)
      return null; 
    Drawable drawable = getDrawableFromResourceValue(paramCursor.getString(i));
    return (drawable != null) ? drawable : getDefaultIcon1();
  }
  
  private Drawable getIcon2(Cursor paramCursor) {
    int i = this.mIconName2Col;
    return (i == -1) ? null : getDrawableFromResourceValue(paramCursor.getString(i));
  }
  
  private static String getStringOrNull(Cursor paramCursor, int paramInt) {
    if (paramInt == -1)
      return null; 
    try {
      return paramCursor.getString(paramInt);
    } catch (Exception exception) {
      Log.e("SuggestionsAdapter", "unexpected error retrieving valid column from cursor, did the remote process die?", exception);
      return null;
    } 
  }
  
  private void setViewDrawable(ImageView paramImageView, Drawable paramDrawable, int paramInt) {
    paramImageView.setImageDrawable(paramDrawable);
    if (paramDrawable == null) {
      paramImageView.setVisibility(paramInt);
    } else {
      paramImageView.setVisibility(0);
      paramDrawable.setVisible(false, false);
      paramDrawable.setVisible(true, false);
    } 
  }
  
  private void setViewText(TextView paramTextView, CharSequence paramCharSequence) {
    paramTextView.setText(paramCharSequence);
    if (TextUtils.isEmpty(paramCharSequence)) {
      paramTextView.setVisibility(8);
    } else {
      paramTextView.setVisibility(0);
    } 
  }
  
  private void storeInIconCache(String paramString, Drawable paramDrawable) {
    if (paramDrawable != null)
      this.mOutsideDrawablesCache.put(paramString, paramDrawable.getConstantState()); 
  }
  
  private void updateSpinnerState(Cursor paramCursor) {
    if (paramCursor != null) {
      Bundle bundle = paramCursor.getExtras();
    } else {
      paramCursor = null;
    } 
    if (paramCursor != null) {
      paramCursor.getBoolean("in_progress");
      return;
    } 
  }
  
  public void bindView(View paramView, Context paramContext, Cursor paramCursor) {
    ChildViewCache childViewCache = (ChildViewCache)paramView.getTag();
    int i = 0;
    int j = this.mFlagsCol;
    if (j != -1)
      i = paramCursor.getInt(j); 
    if (childViewCache.mText1 != null) {
      String str = getStringOrNull(paramCursor, this.mText1Col);
      setViewText(childViewCache.mText1, str);
    } 
    if (childViewCache.mText2 != null) {
      String str = getStringOrNull(paramCursor, this.mText2UrlCol);
      if (str != null) {
        CharSequence charSequence = formatUrl(str);
      } else {
        str = getStringOrNull(paramCursor, this.mText2Col);
      } 
      if (TextUtils.isEmpty(str)) {
        if (childViewCache.mText1 != null) {
          childViewCache.mText1.setSingleLine(false);
          childViewCache.mText1.setMaxLines(2);
        } 
      } else if (childViewCache.mText1 != null) {
        childViewCache.mText1.setSingleLine(true);
        childViewCache.mText1.setMaxLines(1);
      } 
      setViewText(childViewCache.mText2, str);
    } 
    if (childViewCache.mIcon1 != null)
      setViewDrawable(childViewCache.mIcon1, getIcon1(paramCursor), 4); 
    if (childViewCache.mIcon2 != null)
      setViewDrawable(childViewCache.mIcon2, getIcon2(paramCursor), 8); 
    j = this.mQueryRefinement;
    if (j == 2 || (j == 1 && (i & 0x1) != 0)) {
      childViewCache.mIconRefine.setVisibility(0);
      childViewCache.mIconRefine.setTag(childViewCache.mText1.getText());
      childViewCache.mIconRefine.setOnClickListener(this);
      return;
    } 
    childViewCache.mIconRefine.setVisibility(8);
  }
  
  public void changeCursor(Cursor paramCursor) {
    if (this.mClosed) {
      Log.w("SuggestionsAdapter", "Tried to change cursor after adapter was closed.");
      if (paramCursor != null)
        paramCursor.close(); 
      return;
    } 
    try {
      super.changeCursor(paramCursor);
      if (paramCursor != null) {
        this.mText1Col = paramCursor.getColumnIndex("suggest_text_1");
        this.mText2Col = paramCursor.getColumnIndex("suggest_text_2");
        this.mText2UrlCol = paramCursor.getColumnIndex("suggest_text_2_url");
        this.mIconName1Col = paramCursor.getColumnIndex("suggest_icon_1");
        this.mIconName2Col = paramCursor.getColumnIndex("suggest_icon_2");
        this.mFlagsCol = paramCursor.getColumnIndex("suggest_flags");
      } 
    } catch (Exception exception) {
      Log.e("SuggestionsAdapter", "error changing cursor and caching columns", exception);
    } 
  }
  
  public void close() {
    changeCursor((Cursor)null);
    this.mClosed = true;
  }
  
  public CharSequence convertToString(Cursor paramCursor) {
    if (paramCursor == null)
      return null; 
    String str = getColumnString(paramCursor, "suggest_intent_query");
    if (str != null)
      return str; 
    if (this.mSearchable.shouldRewriteQueryFromData()) {
      str = getColumnString(paramCursor, "suggest_intent_data");
      if (str != null)
        return str; 
    } 
    if (this.mSearchable.shouldRewriteQueryFromText()) {
      String str1 = getColumnString(paramCursor, "suggest_text_1");
      if (str1 != null)
        return str1; 
    } 
    return null;
  }
  
  Drawable getDrawableFromResourceUri(Uri paramUri) throws FileNotFoundException {
    String str = paramUri.getAuthority();
    if (!TextUtils.isEmpty(str))
      try {
        Resources resources = this.mProviderContext.getPackageManager().getResourcesForApplication(str);
        List<String> list = paramUri.getPathSegments();
        if (list != null) {
          int i = list.size();
          if (i == 1) {
            try {
              i = Integer.parseInt(list.get(0));
            } catch (NumberFormatException numberFormatException) {
              throw new FileNotFoundException("Single path segment is not a resource ID: " + paramUri);
            } 
          } else if (i == 2) {
            i = resources.getIdentifier(list.get(1), list.get(0), (String)numberFormatException);
          } else {
            throw new FileNotFoundException("More than two path segments: " + paramUri);
          } 
          if (i != 0)
            return resources.getDrawable(i); 
          throw new FileNotFoundException("No resource found for: " + paramUri);
        } 
        throw new FileNotFoundException("No path: " + paramUri);
      } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
        throw new FileNotFoundException("No package found for authority: " + paramUri);
      }  
    throw new FileNotFoundException("No authority: " + paramUri);
  }
  
  public View getDropDownView(int paramInt, View paramView, ViewGroup paramViewGroup) {
    try {
      return super.getDropDownView(paramInt, paramView, paramViewGroup);
    } catch (RuntimeException runtimeException) {
      Log.w("SuggestionsAdapter", "Search suggestions cursor threw exception.", runtimeException);
      View view = newDropDownView(this.mProviderContext, getCursor(), paramViewGroup);
      if (view != null)
        ((ChildViewCache)view.getTag()).mText1.setText(runtimeException.toString()); 
      return view;
    } 
  }
  
  public int getQueryRefinement() {
    return this.mQueryRefinement;
  }
  
  Cursor getSearchManagerSuggestions(SearchableInfo paramSearchableInfo, String paramString, int paramInt) {
    if (paramSearchableInfo == null)
      return null; 
    String str1 = paramSearchableInfo.getSuggestAuthority();
    if (str1 == null)
      return null; 
    Uri.Builder builder = (new Uri.Builder()).scheme("content").authority(str1).query("").fragment("");
    String str2 = paramSearchableInfo.getSuggestPath();
    if (str2 != null)
      builder.appendEncodedPath(str2); 
    builder.appendPath("search_suggest_query");
    str2 = paramSearchableInfo.getSuggestSelection();
    if (str2 != null) {
      String[] arrayOfString = { paramString };
    } else {
      builder.appendPath(paramString);
      paramSearchableInfo = null;
    } 
    if (paramInt > 0)
      builder.appendQueryParameter("limit", String.valueOf(paramInt)); 
    Uri uri = builder.build();
    return this.mProviderContext.getContentResolver().query(uri, null, str2, (String[])paramSearchableInfo, null);
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
    try {
      return super.getView(paramInt, paramView, paramViewGroup);
    } catch (RuntimeException runtimeException) {
      Log.w("SuggestionsAdapter", "Search suggestions cursor threw exception.", runtimeException);
      View view = newView(this.mProviderContext, getCursor(), paramViewGroup);
      if (view != null)
        ((ChildViewCache)view.getTag()).mText1.setText(runtimeException.toString()); 
      return view;
    } 
  }
  
  public boolean hasStableIds() {
    return false;
  }
  
  public View newView(Context paramContext, Cursor paramCursor, ViewGroup paramViewGroup) {
    View view = super.newView(paramContext, paramCursor, paramViewGroup);
    view.setTag(new ChildViewCache(view));
    ((ImageView)view.findViewById(R.id.edit_query)).setImageResource(this.mCommitIconResId);
    return view;
  }
  
  public void notifyDataSetChanged() {
    super.notifyDataSetChanged();
    updateSpinnerState(getCursor());
  }
  
  public void notifyDataSetInvalidated() {
    super.notifyDataSetInvalidated();
    updateSpinnerState(getCursor());
  }
  
  public void onClick(View paramView) {
    Object object = paramView.getTag();
    if (object instanceof CharSequence)
      this.mSearchView.onQueryRefine((CharSequence)object); 
  }
  
  public Cursor runQueryOnBackgroundThread(CharSequence paramCharSequence) {
    if (paramCharSequence == null) {
      paramCharSequence = "";
    } else {
      paramCharSequence = paramCharSequence.toString();
    } 
    if (this.mSearchView.getVisibility() != 0 || this.mSearchView.getWindowVisibility() != 0)
      return null; 
    try {
      Cursor cursor = getSearchManagerSuggestions(this.mSearchable, (String)paramCharSequence, 50);
      if (cursor != null) {
        cursor.getCount();
        return cursor;
      } 
    } catch (RuntimeException runtimeException) {
      Log.w("SuggestionsAdapter", "Search suggestions query threw an exception.", runtimeException);
    } 
    return null;
  }
  
  public void setQueryRefinement(int paramInt) {
    this.mQueryRefinement = paramInt;
  }
  
  private static final class ChildViewCache {
    public final ImageView mIcon1;
    
    public final ImageView mIcon2;
    
    public final ImageView mIconRefine;
    
    public final TextView mText1;
    
    public final TextView mText2;
    
    public ChildViewCache(View param1View) {
      this.mText1 = (TextView)param1View.findViewById(16908308);
      this.mText2 = (TextView)param1View.findViewById(16908309);
      this.mIcon1 = (ImageView)param1View.findViewById(16908295);
      this.mIcon2 = (ImageView)param1View.findViewById(16908296);
      this.mIconRefine = (ImageView)param1View.findViewById(R.id.edit_query);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\SuggestionsAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */