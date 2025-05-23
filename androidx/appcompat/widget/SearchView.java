package androidx.appcompat.widget;

import android.app.PendingIntent;
import android.app.SearchableInfo;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.appcompat.view.CollapsibleActionView;
import androidx.core.view.ViewCompat;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.customview.view.AbsSavedState;
import java.lang.reflect.Method;
import java.util.WeakHashMap;

public class SearchView extends LinearLayoutCompat implements CollapsibleActionView {
  static final boolean DBG = false;
  
  private static final String IME_OPTION_NO_MICROPHONE = "nm";
  
  static final String LOG_TAG = "SearchView";
  
  static final PreQAutoCompleteTextViewReflector PRE_API_29_HIDDEN_METHOD_INVOKER;
  
  private Bundle mAppSearchData;
  
  private boolean mClearingFocus;
  
  final ImageView mCloseButton;
  
  private final ImageView mCollapsedIcon;
  
  private int mCollapsedImeOptions;
  
  private final CharSequence mDefaultQueryHint;
  
  private final View mDropDownAnchor;
  
  private boolean mExpandedInActionView;
  
  final ImageView mGoButton;
  
  private boolean mIconified;
  
  private boolean mIconifiedByDefault;
  
  private int mMaxWidth;
  
  private CharSequence mOldQueryText;
  
  private final View.OnClickListener mOnClickListener;
  
  private OnCloseListener mOnCloseListener;
  
  private final TextView.OnEditorActionListener mOnEditorActionListener;
  
  private final AdapterView.OnItemClickListener mOnItemClickListener;
  
  private final AdapterView.OnItemSelectedListener mOnItemSelectedListener;
  
  private OnQueryTextListener mOnQueryChangeListener;
  
  View.OnFocusChangeListener mOnQueryTextFocusChangeListener;
  
  private View.OnClickListener mOnSearchClickListener;
  
  private OnSuggestionListener mOnSuggestionListener;
  
  private final WeakHashMap<String, Drawable.ConstantState> mOutsideDrawablesCache = new WeakHashMap<>();
  
  private CharSequence mQueryHint;
  
  private boolean mQueryRefinement;
  
  private Runnable mReleaseCursorRunnable = new Runnable() {
      final SearchView this$0;
      
      public void run() {
        if (SearchView.this.mSuggestionsAdapter instanceof SuggestionsAdapter)
          SearchView.this.mSuggestionsAdapter.changeCursor(null); 
      }
    };
  
  final ImageView mSearchButton;
  
  private final View mSearchEditFrame;
  
  private final Drawable mSearchHintIcon;
  
  private final View mSearchPlate;
  
  final SearchAutoComplete mSearchSrcTextView;
  
  private Rect mSearchSrcTextViewBounds = new Rect();
  
  private Rect mSearchSrtTextViewBoundsExpanded = new Rect();
  
  SearchableInfo mSearchable;
  
  private final View mSubmitArea;
  
  private boolean mSubmitButtonEnabled;
  
  private final int mSuggestionCommitIconResId;
  
  private final int mSuggestionRowLayout;
  
  CursorAdapter mSuggestionsAdapter;
  
  private int[] mTemp = new int[2];
  
  private int[] mTemp2 = new int[2];
  
  View.OnKeyListener mTextKeyListener;
  
  private TextWatcher mTextWatcher;
  
  private UpdatableTouchDelegate mTouchDelegate;
  
  private final Runnable mUpdateDrawableStateRunnable = new Runnable() {
      final SearchView this$0;
      
      public void run() {
        SearchView.this.updateFocusedState();
      }
    };
  
  private CharSequence mUserQuery;
  
  private final Intent mVoiceAppSearchIntent;
  
  final ImageView mVoiceButton;
  
  private boolean mVoiceButtonEnabled;
  
  private final Intent mVoiceWebSearchIntent;
  
  static {
    PreQAutoCompleteTextViewReflector preQAutoCompleteTextViewReflector;
    if (Build.VERSION.SDK_INT < 29) {
      preQAutoCompleteTextViewReflector = new PreQAutoCompleteTextViewReflector();
    } else {
      preQAutoCompleteTextViewReflector = null;
    } 
    PRE_API_29_HIDDEN_METHOD_INVOKER = preQAutoCompleteTextViewReflector;
  }
  
  public SearchView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public SearchView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.searchViewStyle);
  }
  
  public SearchView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    View.OnClickListener onClickListener = new View.OnClickListener() {
        final SearchView this$0;
        
        public void onClick(View param1View) {
          if (param1View == SearchView.this.mSearchButton) {
            SearchView.this.onSearchClicked();
          } else if (param1View == SearchView.this.mCloseButton) {
            SearchView.this.onCloseClicked();
          } else if (param1View == SearchView.this.mGoButton) {
            SearchView.this.onSubmitQuery();
          } else if (param1View == SearchView.this.mVoiceButton) {
            SearchView.this.onVoiceClicked();
          } else if (param1View == SearchView.this.mSearchSrcTextView) {
            SearchView.this.forceSuggestionQuery();
          } 
        }
      };
    this.mOnClickListener = onClickListener;
    this.mTextKeyListener = new View.OnKeyListener() {
        final SearchView this$0;
        
        public boolean onKey(View param1View, int param1Int, KeyEvent param1KeyEvent) {
          if (SearchView.this.mSearchable == null)
            return false; 
          if (SearchView.this.mSearchSrcTextView.isPopupShowing() && SearchView.this.mSearchSrcTextView.getListSelection() != -1)
            return SearchView.this.onSuggestionsKey(param1View, param1Int, param1KeyEvent); 
          if (!SearchView.this.mSearchSrcTextView.isEmpty() && param1KeyEvent.hasNoModifiers() && param1KeyEvent.getAction() == 1 && param1Int == 66) {
            param1View.cancelLongPress();
            SearchView searchView = SearchView.this;
            searchView.launchQuerySearch(0, (String)null, searchView.mSearchSrcTextView.getText().toString());
            return true;
          } 
          return false;
        }
      };
    TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        final SearchView this$0;
        
        public boolean onEditorAction(TextView param1TextView, int param1Int, KeyEvent param1KeyEvent) {
          SearchView.this.onSubmitQuery();
          return true;
        }
      };
    this.mOnEditorActionListener = onEditorActionListener;
    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        final SearchView this$0;
        
        public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
          SearchView.this.onItemClicked(param1Int, 0, (String)null);
        }
      };
    this.mOnItemClickListener = onItemClickListener;
    AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        final SearchView this$0;
        
        public void onItemSelected(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
          SearchView.this.onItemSelected(param1Int);
        }
        
        public void onNothingSelected(AdapterView<?> param1AdapterView) {}
      };
    this.mOnItemSelectedListener = onItemSelectedListener;
    this.mTextWatcher = new TextWatcher() {
        final SearchView this$0;
        
        public void afterTextChanged(Editable param1Editable) {}
        
        public void beforeTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
        
        public void onTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {
          SearchView.this.onTextChanged(param1CharSequence);
        }
      };
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.SearchView, paramInt, 0);
    ViewCompat.saveAttributeDataForStyleable((View)this, paramContext, R.styleable.SearchView, paramAttributeSet, tintTypedArray.getWrappedTypeArray(), paramInt, 0);
    LayoutInflater.from(paramContext).inflate(tintTypedArray.getResourceId(R.styleable.SearchView_layout, R.layout.abc_search_view), this, true);
    SearchAutoComplete searchAutoComplete = (SearchAutoComplete)findViewById(R.id.search_src_text);
    this.mSearchSrcTextView = searchAutoComplete;
    searchAutoComplete.setSearchView(this);
    this.mSearchEditFrame = findViewById(R.id.search_edit_frame);
    View view2 = findViewById(R.id.search_plate);
    this.mSearchPlate = view2;
    View view3 = findViewById(R.id.submit_area);
    this.mSubmitArea = view3;
    ImageView imageView1 = (ImageView)findViewById(R.id.search_button);
    this.mSearchButton = imageView1;
    ImageView imageView3 = (ImageView)findViewById(R.id.search_go_btn);
    this.mGoButton = imageView3;
    ImageView imageView2 = (ImageView)findViewById(R.id.search_close_btn);
    this.mCloseButton = imageView2;
    ImageView imageView4 = (ImageView)findViewById(R.id.search_voice_btn);
    this.mVoiceButton = imageView4;
    ImageView imageView5 = (ImageView)findViewById(R.id.search_mag_icon);
    this.mCollapsedIcon = imageView5;
    ViewCompat.setBackground(view2, tintTypedArray.getDrawable(R.styleable.SearchView_queryBackground));
    ViewCompat.setBackground(view3, tintTypedArray.getDrawable(R.styleable.SearchView_submitBackground));
    imageView1.setImageDrawable(tintTypedArray.getDrawable(R.styleable.SearchView_searchIcon));
    imageView3.setImageDrawable(tintTypedArray.getDrawable(R.styleable.SearchView_goIcon));
    imageView2.setImageDrawable(tintTypedArray.getDrawable(R.styleable.SearchView_closeIcon));
    imageView4.setImageDrawable(tintTypedArray.getDrawable(R.styleable.SearchView_voiceIcon));
    imageView5.setImageDrawable(tintTypedArray.getDrawable(R.styleable.SearchView_searchIcon));
    this.mSearchHintIcon = tintTypedArray.getDrawable(R.styleable.SearchView_searchHintIcon);
    TooltipCompat.setTooltipText((View)imageView1, getResources().getString(R.string.abc_searchview_description_search));
    this.mSuggestionRowLayout = tintTypedArray.getResourceId(R.styleable.SearchView_suggestionRowLayout, R.layout.abc_search_dropdown_item_icons_2line);
    this.mSuggestionCommitIconResId = tintTypedArray.getResourceId(R.styleable.SearchView_commitIcon, 0);
    imageView1.setOnClickListener(onClickListener);
    imageView2.setOnClickListener(onClickListener);
    imageView3.setOnClickListener(onClickListener);
    imageView4.setOnClickListener(onClickListener);
    searchAutoComplete.setOnClickListener(onClickListener);
    searchAutoComplete.addTextChangedListener(this.mTextWatcher);
    searchAutoComplete.setOnEditorActionListener(onEditorActionListener);
    searchAutoComplete.setOnItemClickListener(onItemClickListener);
    searchAutoComplete.setOnItemSelectedListener(onItemSelectedListener);
    searchAutoComplete.setOnKeyListener(this.mTextKeyListener);
    searchAutoComplete.setOnFocusChangeListener(new View.OnFocusChangeListener() {
          final SearchView this$0;
          
          public void onFocusChange(View param1View, boolean param1Boolean) {
            if (SearchView.this.mOnQueryTextFocusChangeListener != null)
              SearchView.this.mOnQueryTextFocusChangeListener.onFocusChange((View)SearchView.this, param1Boolean); 
          }
        });
    setIconifiedByDefault(tintTypedArray.getBoolean(R.styleable.SearchView_iconifiedByDefault, true));
    paramInt = tintTypedArray.getDimensionPixelSize(R.styleable.SearchView_android_maxWidth, -1);
    if (paramInt != -1)
      setMaxWidth(paramInt); 
    this.mDefaultQueryHint = tintTypedArray.getText(R.styleable.SearchView_defaultQueryHint);
    this.mQueryHint = tintTypedArray.getText(R.styleable.SearchView_queryHint);
    paramInt = tintTypedArray.getInt(R.styleable.SearchView_android_imeOptions, -1);
    if (paramInt != -1)
      setImeOptions(paramInt); 
    paramInt = tintTypedArray.getInt(R.styleable.SearchView_android_inputType, -1);
    if (paramInt != -1)
      setInputType(paramInt); 
    setFocusable(tintTypedArray.getBoolean(R.styleable.SearchView_android_focusable, true));
    tintTypedArray.recycle();
    Intent intent = new Intent("android.speech.action.WEB_SEARCH");
    this.mVoiceWebSearchIntent = intent;
    intent.addFlags(268435456);
    intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "web_search");
    intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
    this.mVoiceAppSearchIntent = intent;
    intent.addFlags(268435456);
    View view1 = findViewById(searchAutoComplete.getDropDownAnchor());
    this.mDropDownAnchor = view1;
    if (view1 != null)
      view1.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            final SearchView this$0;
            
            public void onLayoutChange(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6, int param1Int7, int param1Int8) {
              SearchView.this.adjustDropDownSizeAndPosition();
            }
          }); 
    updateViewsVisibility(this.mIconifiedByDefault);
    updateQueryHint();
  }
  
  private Intent createIntent(String paramString1, Uri paramUri, String paramString2, String paramString3, int paramInt, String paramString4) {
    Intent intent = new Intent(paramString1);
    intent.addFlags(268435456);
    if (paramUri != null)
      intent.setData(paramUri); 
    intent.putExtra("user_query", this.mUserQuery);
    if (paramString3 != null)
      intent.putExtra("query", paramString3); 
    if (paramString2 != null)
      intent.putExtra("intent_extra_data_key", paramString2); 
    Bundle bundle = this.mAppSearchData;
    if (bundle != null)
      intent.putExtra("app_data", bundle); 
    if (paramInt != 0) {
      intent.putExtra("action_key", paramInt);
      intent.putExtra("action_msg", paramString4);
    } 
    intent.setComponent(this.mSearchable.getSearchActivity());
    return intent;
  }
  
  private Intent createIntentFromSuggestion(Cursor paramCursor, int paramInt, String paramString) {
    try {
      Uri uri;
      String str2 = SuggestionsAdapter.getColumnString(paramCursor, "suggest_intent_action");
      String str1 = str2;
      if (str2 == null)
        str1 = this.mSearchable.getSuggestIntentAction(); 
      str2 = str1;
      if (str1 == null)
        str2 = "android.intent.action.SEARCH"; 
      String str3 = SuggestionsAdapter.getColumnString(paramCursor, "suggest_intent_data");
      str1 = str3;
      if (str3 == null)
        str1 = this.mSearchable.getSuggestIntentData(); 
      if (str1 != null) {
        String str = SuggestionsAdapter.getColumnString(paramCursor, "suggest_intent_data_id");
        if (str != null) {
          StringBuilder stringBuilder = new StringBuilder();
          this();
          str1 = stringBuilder.append(str1).append("/").append(Uri.encode(str)).toString();
        } 
      } 
      if (str1 == null) {
        str1 = null;
      } else {
        uri = Uri.parse(str1);
      } 
      str3 = SuggestionsAdapter.getColumnString(paramCursor, "suggest_intent_query");
      return createIntent(str2, uri, SuggestionsAdapter.getColumnString(paramCursor, "suggest_intent_extra_data"), str3, paramInt, paramString);
    } catch (RuntimeException runtimeException) {
      try {
        paramInt = paramCursor.getPosition();
      } catch (RuntimeException runtimeException1) {
        paramInt = -1;
      } 
      Log.w("SearchView", "Search suggestions cursor at row " + paramInt + " returned exception.", runtimeException);
      return null;
    } 
  }
  
  private Intent createVoiceAppSearchIntent(Intent paramIntent, SearchableInfo paramSearchableInfo) {
    String str2;
    ComponentName componentName = paramSearchableInfo.getSearchActivity();
    Intent intent1 = new Intent("android.intent.action.SEARCH");
    intent1.setComponent(componentName);
    PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent1, 1107296256);
    Bundle bundle2 = new Bundle();
    Bundle bundle1 = this.mAppSearchData;
    if (bundle1 != null)
      bundle2.putParcelable("app_data", (Parcelable)bundle1); 
    Intent intent2 = new Intent(paramIntent);
    String str1 = "free_form";
    bundle1 = null;
    String str3 = null;
    int i = 1;
    Resources resources = getResources();
    if (paramSearchableInfo.getVoiceLanguageModeId() != 0)
      str1 = resources.getString(paramSearchableInfo.getVoiceLanguageModeId()); 
    if (paramSearchableInfo.getVoicePromptTextId() != 0)
      str2 = resources.getString(paramSearchableInfo.getVoicePromptTextId()); 
    if (paramSearchableInfo.getVoiceLanguageId() != 0)
      str3 = resources.getString(paramSearchableInfo.getVoiceLanguageId()); 
    if (paramSearchableInfo.getVoiceMaxResults() != 0)
      i = paramSearchableInfo.getVoiceMaxResults(); 
    intent2.putExtra("android.speech.extra.LANGUAGE_MODEL", str1);
    intent2.putExtra("android.speech.extra.PROMPT", str2);
    intent2.putExtra("android.speech.extra.LANGUAGE", str3);
    intent2.putExtra("android.speech.extra.MAX_RESULTS", i);
    if (componentName == null) {
      str1 = null;
    } else {
      str1 = componentName.flattenToShortString();
    } 
    intent2.putExtra("calling_package", str1);
    intent2.putExtra("android.speech.extra.RESULTS_PENDINGINTENT", (Parcelable)pendingIntent);
    intent2.putExtra("android.speech.extra.RESULTS_PENDINGINTENT_BUNDLE", bundle2);
    return intent2;
  }
  
  private Intent createVoiceWebSearchIntent(Intent paramIntent, SearchableInfo paramSearchableInfo) {
    String str;
    Intent intent = new Intent(paramIntent);
    ComponentName componentName = paramSearchableInfo.getSearchActivity();
    if (componentName == null) {
      componentName = null;
    } else {
      str = componentName.flattenToShortString();
    } 
    intent.putExtra("calling_package", str);
    return intent;
  }
  
  private void dismissSuggestions() {
    this.mSearchSrcTextView.dismissDropDown();
  }
  
  private void getChildBoundsWithinSearchView(View paramView, Rect paramRect) {
    paramView.getLocationInWindow(this.mTemp);
    getLocationInWindow(this.mTemp2);
    int[] arrayOfInt1 = this.mTemp;
    int i = arrayOfInt1[1];
    int[] arrayOfInt2 = this.mTemp2;
    i -= arrayOfInt2[1];
    int j = arrayOfInt1[0] - arrayOfInt2[0];
    paramRect.set(j, i, paramView.getWidth() + j, paramView.getHeight() + i);
  }
  
  private CharSequence getDecoratedHint(CharSequence paramCharSequence) {
    if (!this.mIconifiedByDefault || this.mSearchHintIcon == null)
      return paramCharSequence; 
    int i = (int)(this.mSearchSrcTextView.getTextSize() * 1.25D);
    this.mSearchHintIcon.setBounds(0, 0, i, i);
    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("   ");
    spannableStringBuilder.setSpan(new ImageSpan(this.mSearchHintIcon), 1, 2, 33);
    spannableStringBuilder.append(paramCharSequence);
    return (CharSequence)spannableStringBuilder;
  }
  
  private int getPreferredHeight() {
    return getContext().getResources().getDimensionPixelSize(R.dimen.abc_search_view_preferred_height);
  }
  
  private int getPreferredWidth() {
    return getContext().getResources().getDimensionPixelSize(R.dimen.abc_search_view_preferred_width);
  }
  
  private boolean hasVoiceSearch() {
    SearchableInfo searchableInfo = this.mSearchable;
    boolean bool = false;
    if (searchableInfo != null && searchableInfo.getVoiceSearchEnabled()) {
      Intent intent;
      searchableInfo = null;
      if (this.mSearchable.getVoiceSearchLaunchWebSearch()) {
        intent = this.mVoiceWebSearchIntent;
      } else if (this.mSearchable.getVoiceSearchLaunchRecognizer()) {
        intent = this.mVoiceAppSearchIntent;
      } 
      if (intent != null) {
        if (getContext().getPackageManager().resolveActivity(intent, 65536) != null)
          bool = true; 
        return bool;
      } 
    } 
    return false;
  }
  
  static boolean isLandscapeMode(Context paramContext) {
    boolean bool;
    if ((paramContext.getResources().getConfiguration()).orientation == 2) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private boolean isSubmitAreaEnabled() {
    boolean bool;
    if ((this.mSubmitButtonEnabled || this.mVoiceButtonEnabled) && !isIconified()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void launchIntent(Intent paramIntent) {
    if (paramIntent == null)
      return; 
    try {
      getContext().startActivity(paramIntent);
    } catch (RuntimeException runtimeException) {
      Log.e("SearchView", "Failed launch activity: " + paramIntent, runtimeException);
    } 
  }
  
  private boolean launchSuggestion(int paramInt1, int paramInt2, String paramString) {
    Cursor cursor = this.mSuggestionsAdapter.getCursor();
    if (cursor != null && cursor.moveToPosition(paramInt1)) {
      launchIntent(createIntentFromSuggestion(cursor, paramInt2, paramString));
      return true;
    } 
    return false;
  }
  
  private void postUpdateFocusedState() {
    post(this.mUpdateDrawableStateRunnable);
  }
  
  private void rewriteQueryFromSuggestion(int paramInt) {
    Editable editable = this.mSearchSrcTextView.getText();
    Cursor cursor = this.mSuggestionsAdapter.getCursor();
    if (cursor == null)
      return; 
    if (cursor.moveToPosition(paramInt)) {
      CharSequence charSequence = this.mSuggestionsAdapter.convertToString(cursor);
      if (charSequence != null) {
        setQuery(charSequence);
      } else {
        setQuery((CharSequence)editable);
      } 
    } else {
      setQuery((CharSequence)editable);
    } 
  }
  
  private void setQuery(CharSequence paramCharSequence) {
    int i;
    this.mSearchSrcTextView.setText(paramCharSequence);
    SearchAutoComplete searchAutoComplete = this.mSearchSrcTextView;
    if (TextUtils.isEmpty(paramCharSequence)) {
      i = 0;
    } else {
      i = paramCharSequence.length();
    } 
    searchAutoComplete.setSelection(i);
  }
  
  private void updateCloseButton() {
    boolean bool = TextUtils.isEmpty((CharSequence)this.mSearchSrcTextView.getText());
    byte b3 = 1;
    int i = bool ^ true;
    byte b2 = 0;
    byte b1 = b3;
    if (i == 0)
      if (this.mIconifiedByDefault && !this.mExpandedInActionView) {
        b1 = b3;
      } else {
        b1 = 0;
      }  
    ImageView imageView = this.mCloseButton;
    if (b1) {
      b1 = b2;
    } else {
      b1 = 8;
    } 
    imageView.setVisibility(b1);
    Drawable drawable = this.mCloseButton.getDrawable();
    if (drawable != null) {
      int[] arrayOfInt;
      if (i != 0) {
        arrayOfInt = ENABLED_STATE_SET;
      } else {
        arrayOfInt = EMPTY_STATE_SET;
      } 
      drawable.setState(arrayOfInt);
    } 
  }
  
  private void updateQueryHint() {
    CharSequence charSequence = getQueryHint();
    SearchAutoComplete searchAutoComplete = this.mSearchSrcTextView;
    if (charSequence == null)
      charSequence = ""; 
    searchAutoComplete.setHint(getDecoratedHint(charSequence));
  }
  
  private void updateSearchAutoComplete() {
    this.mSearchSrcTextView.setThreshold(this.mSearchable.getSuggestThreshold());
    this.mSearchSrcTextView.setImeOptions(this.mSearchable.getImeOptions());
    int j = this.mSearchable.getInputType();
    boolean bool = true;
    int i = j;
    if ((j & 0xF) == 1) {
      j &= 0xFFFEFFFF;
      i = j;
      if (this.mSearchable.getSuggestAuthority() != null)
        i = j | 0x10000 | 0x80000; 
    } 
    this.mSearchSrcTextView.setInputType(i);
    CursorAdapter cursorAdapter = this.mSuggestionsAdapter;
    if (cursorAdapter != null)
      cursorAdapter.changeCursor(null); 
    if (this.mSearchable.getSuggestAuthority() != null) {
      SuggestionsAdapter suggestionsAdapter = new SuggestionsAdapter(getContext(), this, this.mSearchable, this.mOutsideDrawablesCache);
      this.mSuggestionsAdapter = (CursorAdapter)suggestionsAdapter;
      this.mSearchSrcTextView.setAdapter((ListAdapter)suggestionsAdapter);
      suggestionsAdapter = (SuggestionsAdapter)this.mSuggestionsAdapter;
      if (this.mQueryRefinement) {
        i = 2;
      } else {
        i = bool;
      } 
      suggestionsAdapter.setQueryRefinement(i);
    } 
  }
  
  private void updateSubmitArea() {
    // Byte code:
    //   0: bipush #8
    //   2: istore_2
    //   3: iload_2
    //   4: istore_1
    //   5: aload_0
    //   6: invokespecial isSubmitAreaEnabled : ()Z
    //   9: ifeq -> 36
    //   12: aload_0
    //   13: getfield mGoButton : Landroid/widget/ImageView;
    //   16: invokevirtual getVisibility : ()I
    //   19: ifeq -> 34
    //   22: iload_2
    //   23: istore_1
    //   24: aload_0
    //   25: getfield mVoiceButton : Landroid/widget/ImageView;
    //   28: invokevirtual getVisibility : ()I
    //   31: ifne -> 36
    //   34: iconst_0
    //   35: istore_1
    //   36: aload_0
    //   37: getfield mSubmitArea : Landroid/view/View;
    //   40: iload_1
    //   41: invokevirtual setVisibility : (I)V
    //   44: return
  }
  
  private void updateSubmitButton(boolean paramBoolean) {
    // Byte code:
    //   0: bipush #8
    //   2: istore_3
    //   3: iload_3
    //   4: istore_2
    //   5: aload_0
    //   6: getfield mSubmitButtonEnabled : Z
    //   9: ifeq -> 45
    //   12: iload_3
    //   13: istore_2
    //   14: aload_0
    //   15: invokespecial isSubmitAreaEnabled : ()Z
    //   18: ifeq -> 45
    //   21: iload_3
    //   22: istore_2
    //   23: aload_0
    //   24: invokevirtual hasFocus : ()Z
    //   27: ifeq -> 45
    //   30: iload_1
    //   31: ifne -> 43
    //   34: iload_3
    //   35: istore_2
    //   36: aload_0
    //   37: getfield mVoiceButtonEnabled : Z
    //   40: ifne -> 45
    //   43: iconst_0
    //   44: istore_2
    //   45: aload_0
    //   46: getfield mGoButton : Landroid/widget/ImageView;
    //   49: iload_2
    //   50: invokevirtual setVisibility : (I)V
    //   53: return
  }
  
  private void updateViewsVisibility(boolean paramBoolean) {
    byte b1;
    this.mIconified = paramBoolean;
    byte b2 = 8;
    boolean bool = false;
    if (paramBoolean) {
      b1 = 0;
    } else {
      b1 = 8;
    } 
    int i = TextUtils.isEmpty((CharSequence)this.mSearchSrcTextView.getText()) ^ true;
    this.mSearchButton.setVisibility(b1);
    updateSubmitButton(i);
    View view = this.mSearchEditFrame;
    if (paramBoolean) {
      b1 = b2;
    } else {
      b1 = 0;
    } 
    view.setVisibility(b1);
    if (this.mCollapsedIcon.getDrawable() == null || this.mIconifiedByDefault) {
      b1 = 8;
    } else {
      b1 = 0;
    } 
    this.mCollapsedIcon.setVisibility(b1);
    updateCloseButton();
    paramBoolean = bool;
    if (i == 0)
      paramBoolean = true; 
    updateVoiceButton(paramBoolean);
    updateSubmitArea();
  }
  
  private void updateVoiceButton(boolean paramBoolean) {
    byte b2 = 8;
    byte b1 = b2;
    if (this.mVoiceButtonEnabled) {
      b1 = b2;
      if (!isIconified()) {
        b1 = b2;
        if (paramBoolean) {
          b1 = 0;
          this.mGoButton.setVisibility(8);
        } 
      } 
    } 
    this.mVoiceButton.setVisibility(b1);
  }
  
  void adjustDropDownSizeAndPosition() {
    if (this.mDropDownAnchor.getWidth() > 1) {
      byte b;
      Resources resources = getContext().getResources();
      int j = this.mSearchPlate.getPaddingLeft();
      Rect rect = new Rect();
      boolean bool = ViewUtils.isLayoutRtl((View)this);
      if (this.mIconifiedByDefault) {
        b = resources.getDimensionPixelSize(R.dimen.abc_dropdownitem_icon_width) + resources.getDimensionPixelSize(R.dimen.abc_dropdownitem_text_padding_left);
      } else {
        b = 0;
      } 
      this.mSearchSrcTextView.getDropDownBackground().getPadding(rect);
      if (bool) {
        i = -rect.left;
      } else {
        i = j - rect.left + b;
      } 
      this.mSearchSrcTextView.setDropDownHorizontalOffset(i);
      int k = this.mDropDownAnchor.getWidth();
      int m = rect.left;
      int i = rect.right;
      this.mSearchSrcTextView.setDropDownWidth(k + m + i + b - j);
    } 
  }
  
  public void clearFocus() {
    this.mClearingFocus = true;
    super.clearFocus();
    this.mSearchSrcTextView.clearFocus();
    this.mSearchSrcTextView.setImeVisibility(false);
    this.mClearingFocus = false;
  }
  
  void forceSuggestionQuery() {
    if (Build.VERSION.SDK_INT >= 29) {
      this.mSearchSrcTextView.refreshAutoCompleteResults();
    } else {
      PreQAutoCompleteTextViewReflector preQAutoCompleteTextViewReflector = PRE_API_29_HIDDEN_METHOD_INVOKER;
      preQAutoCompleteTextViewReflector.doBeforeTextChanged(this.mSearchSrcTextView);
      preQAutoCompleteTextViewReflector.doAfterTextChanged(this.mSearchSrcTextView);
    } 
  }
  
  public int getImeOptions() {
    return this.mSearchSrcTextView.getImeOptions();
  }
  
  public int getInputType() {
    return this.mSearchSrcTextView.getInputType();
  }
  
  public int getMaxWidth() {
    return this.mMaxWidth;
  }
  
  public CharSequence getQuery() {
    return (CharSequence)this.mSearchSrcTextView.getText();
  }
  
  public CharSequence getQueryHint() {
    CharSequence charSequence;
    if (this.mQueryHint != null) {
      charSequence = this.mQueryHint;
    } else {
      SearchableInfo searchableInfo = this.mSearchable;
      if (searchableInfo != null && searchableInfo.getHintId() != 0) {
        charSequence = getContext().getText(this.mSearchable.getHintId());
      } else {
        charSequence = this.mDefaultQueryHint;
      } 
    } 
    return charSequence;
  }
  
  int getSuggestionCommitIconResId() {
    return this.mSuggestionCommitIconResId;
  }
  
  int getSuggestionRowLayout() {
    return this.mSuggestionRowLayout;
  }
  
  public CursorAdapter getSuggestionsAdapter() {
    return this.mSuggestionsAdapter;
  }
  
  public boolean isIconfiedByDefault() {
    return this.mIconifiedByDefault;
  }
  
  public boolean isIconified() {
    return this.mIconified;
  }
  
  public boolean isQueryRefinementEnabled() {
    return this.mQueryRefinement;
  }
  
  public boolean isSubmitButtonEnabled() {
    return this.mSubmitButtonEnabled;
  }
  
  void launchQuerySearch(int paramInt, String paramString1, String paramString2) {
    Intent intent = createIntent("android.intent.action.SEARCH", (Uri)null, (String)null, paramString2, paramInt, paramString1);
    getContext().startActivity(intent);
  }
  
  public void onActionViewCollapsed() {
    setQuery("", false);
    clearFocus();
    updateViewsVisibility(true);
    this.mSearchSrcTextView.setImeOptions(this.mCollapsedImeOptions);
    this.mExpandedInActionView = false;
  }
  
  public void onActionViewExpanded() {
    if (this.mExpandedInActionView)
      return; 
    this.mExpandedInActionView = true;
    int i = this.mSearchSrcTextView.getImeOptions();
    this.mCollapsedImeOptions = i;
    this.mSearchSrcTextView.setImeOptions(i | 0x2000000);
    this.mSearchSrcTextView.setText("");
    setIconified(false);
  }
  
  void onCloseClicked() {
    if (TextUtils.isEmpty((CharSequence)this.mSearchSrcTextView.getText())) {
      if (this.mIconifiedByDefault) {
        OnCloseListener onCloseListener = this.mOnCloseListener;
        if (onCloseListener == null || !onCloseListener.onClose()) {
          clearFocus();
          updateViewsVisibility(true);
        } 
      } 
    } else {
      this.mSearchSrcTextView.setText("");
      this.mSearchSrcTextView.requestFocus();
      this.mSearchSrcTextView.setImeVisibility(true);
    } 
  }
  
  protected void onDetachedFromWindow() {
    removeCallbacks(this.mUpdateDrawableStateRunnable);
    post(this.mReleaseCursorRunnable);
    super.onDetachedFromWindow();
  }
  
  boolean onItemClicked(int paramInt1, int paramInt2, String paramString) {
    OnSuggestionListener onSuggestionListener = this.mOnSuggestionListener;
    if (onSuggestionListener == null || !onSuggestionListener.onSuggestionClick(paramInt1)) {
      launchSuggestion(paramInt1, 0, (String)null);
      this.mSearchSrcTextView.setImeVisibility(false);
      dismissSuggestions();
      return true;
    } 
    return false;
  }
  
  boolean onItemSelected(int paramInt) {
    OnSuggestionListener onSuggestionListener = this.mOnSuggestionListener;
    if (onSuggestionListener == null || !onSuggestionListener.onSuggestionSelect(paramInt)) {
      rewriteQueryFromSuggestion(paramInt);
      return true;
    } 
    return false;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    if (paramBoolean) {
      getChildBoundsWithinSearchView((View)this.mSearchSrcTextView, this.mSearchSrcTextViewBounds);
      this.mSearchSrtTextViewBoundsExpanded.set(this.mSearchSrcTextViewBounds.left, 0, this.mSearchSrcTextViewBounds.right, paramInt4 - paramInt2);
      UpdatableTouchDelegate updatableTouchDelegate = this.mTouchDelegate;
      if (updatableTouchDelegate == null) {
        updatableTouchDelegate = new UpdatableTouchDelegate(this.mSearchSrtTextViewBoundsExpanded, this.mSearchSrcTextViewBounds, (View)this.mSearchSrcTextView);
        this.mTouchDelegate = updatableTouchDelegate;
        setTouchDelegate(updatableTouchDelegate);
      } else {
        updatableTouchDelegate.setBounds(this.mSearchSrtTextViewBoundsExpanded, this.mSearchSrcTextViewBounds);
      } 
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    if (isIconified()) {
      super.onMeasure(paramInt1, paramInt2);
      return;
    } 
    int j = View.MeasureSpec.getMode(paramInt1);
    int i = View.MeasureSpec.getSize(paramInt1);
    switch (j) {
      default:
        paramInt1 = i;
        break;
      case 1073741824:
        j = this.mMaxWidth;
        paramInt1 = i;
        if (j > 0)
          paramInt1 = Math.min(j, i); 
        break;
      case 0:
        paramInt1 = this.mMaxWidth;
        if (paramInt1 > 0)
          break; 
        paramInt1 = getPreferredWidth();
        break;
      case -2147483648:
        paramInt1 = this.mMaxWidth;
        if (paramInt1 > 0) {
          paramInt1 = Math.min(paramInt1, i);
          break;
        } 
        paramInt1 = Math.min(getPreferredWidth(), i);
        break;
    } 
    i = View.MeasureSpec.getMode(paramInt2);
    paramInt2 = View.MeasureSpec.getSize(paramInt2);
    switch (i) {
      case 0:
        paramInt2 = getPreferredHeight();
        break;
      case -2147483648:
        paramInt2 = Math.min(getPreferredHeight(), paramInt2);
        break;
    } 
    super.onMeasure(View.MeasureSpec.makeMeasureSpec(paramInt1, 1073741824), View.MeasureSpec.makeMeasureSpec(paramInt2, 1073741824));
  }
  
  void onQueryRefine(CharSequence paramCharSequence) {
    setQuery(paramCharSequence);
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    updateViewsVisibility(savedState.isIconified);
    requestLayout();
  }
  
  protected Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    savedState.isIconified = isIconified();
    return (Parcelable)savedState;
  }
  
  void onSearchClicked() {
    updateViewsVisibility(false);
    this.mSearchSrcTextView.requestFocus();
    this.mSearchSrcTextView.setImeVisibility(true);
    View.OnClickListener onClickListener = this.mOnSearchClickListener;
    if (onClickListener != null)
      onClickListener.onClick((View)this); 
  }
  
  void onSubmitQuery() {
    Editable editable = this.mSearchSrcTextView.getText();
    if (editable != null && TextUtils.getTrimmedLength((CharSequence)editable) > 0) {
      OnQueryTextListener onQueryTextListener = this.mOnQueryChangeListener;
      if (onQueryTextListener == null || !onQueryTextListener.onQueryTextSubmit(editable.toString())) {
        if (this.mSearchable != null)
          launchQuerySearch(0, (String)null, editable.toString()); 
        this.mSearchSrcTextView.setImeVisibility(false);
        dismissSuggestions();
      } 
    } 
  }
  
  boolean onSuggestionsKey(View paramView, int paramInt, KeyEvent paramKeyEvent) {
    if (this.mSearchable == null)
      return false; 
    if (this.mSuggestionsAdapter == null)
      return false; 
    if (paramKeyEvent.getAction() == 0 && paramKeyEvent.hasNoModifiers()) {
      if (paramInt == 66 || paramInt == 84 || paramInt == 61)
        return onItemClicked(this.mSearchSrcTextView.getListSelection(), 0, (String)null); 
      if (paramInt == 21 || paramInt == 22) {
        if (paramInt == 21) {
          paramInt = 0;
        } else {
          paramInt = this.mSearchSrcTextView.length();
        } 
        this.mSearchSrcTextView.setSelection(paramInt);
        this.mSearchSrcTextView.setListSelection(0);
        this.mSearchSrcTextView.clearListSelection();
        this.mSearchSrcTextView.ensureImeVisible();
        return true;
      } 
      if (paramInt == 19) {
        this.mSearchSrcTextView.getListSelection();
        return false;
      } 
    } 
    return false;
  }
  
  void onTextChanged(CharSequence paramCharSequence) {
    Editable editable = this.mSearchSrcTextView.getText();
    this.mUserQuery = (CharSequence)editable;
    boolean bool1 = TextUtils.isEmpty((CharSequence)editable);
    boolean bool = true;
    int i = bool1 ^ true;
    updateSubmitButton(i);
    if (i != 0)
      bool = false; 
    updateVoiceButton(bool);
    updateCloseButton();
    updateSubmitArea();
    if (this.mOnQueryChangeListener != null && !TextUtils.equals(paramCharSequence, this.mOldQueryText))
      this.mOnQueryChangeListener.onQueryTextChange(paramCharSequence.toString()); 
    this.mOldQueryText = paramCharSequence.toString();
  }
  
  void onTextFocusChanged() {
    updateViewsVisibility(isIconified());
    postUpdateFocusedState();
    if (this.mSearchSrcTextView.hasFocus())
      forceSuggestionQuery(); 
  }
  
  void onVoiceClicked() {
    if (this.mSearchable == null)
      return; 
    SearchableInfo searchableInfo = this.mSearchable;
    try {
      Intent intent;
      if (searchableInfo.getVoiceSearchLaunchWebSearch()) {
        intent = createVoiceWebSearchIntent(this.mVoiceWebSearchIntent, searchableInfo);
        getContext().startActivity(intent);
      } else if (intent.getVoiceSearchLaunchRecognizer()) {
        intent = createVoiceAppSearchIntent(this.mVoiceAppSearchIntent, (SearchableInfo)intent);
        getContext().startActivity(intent);
      } 
    } catch (ActivityNotFoundException activityNotFoundException) {
      Log.w("SearchView", "Could not find voice search activity");
    } 
  }
  
  public void onWindowFocusChanged(boolean paramBoolean) {
    super.onWindowFocusChanged(paramBoolean);
    postUpdateFocusedState();
  }
  
  public boolean requestFocus(int paramInt, Rect paramRect) {
    if (this.mClearingFocus)
      return false; 
    if (!isFocusable())
      return false; 
    if (!isIconified()) {
      boolean bool = this.mSearchSrcTextView.requestFocus(paramInt, paramRect);
      if (bool)
        updateViewsVisibility(false); 
      return bool;
    } 
    return super.requestFocus(paramInt, paramRect);
  }
  
  public void setAppSearchData(Bundle paramBundle) {
    this.mAppSearchData = paramBundle;
  }
  
  public void setIconified(boolean paramBoolean) {
    if (paramBoolean) {
      onCloseClicked();
    } else {
      onSearchClicked();
    } 
  }
  
  public void setIconifiedByDefault(boolean paramBoolean) {
    if (this.mIconifiedByDefault == paramBoolean)
      return; 
    this.mIconifiedByDefault = paramBoolean;
    updateViewsVisibility(paramBoolean);
    updateQueryHint();
  }
  
  public void setImeOptions(int paramInt) {
    this.mSearchSrcTextView.setImeOptions(paramInt);
  }
  
  public void setInputType(int paramInt) {
    this.mSearchSrcTextView.setInputType(paramInt);
  }
  
  public void setMaxWidth(int paramInt) {
    this.mMaxWidth = paramInt;
    requestLayout();
  }
  
  public void setOnCloseListener(OnCloseListener paramOnCloseListener) {
    this.mOnCloseListener = paramOnCloseListener;
  }
  
  public void setOnQueryTextFocusChangeListener(View.OnFocusChangeListener paramOnFocusChangeListener) {
    this.mOnQueryTextFocusChangeListener = paramOnFocusChangeListener;
  }
  
  public void setOnQueryTextListener(OnQueryTextListener paramOnQueryTextListener) {
    this.mOnQueryChangeListener = paramOnQueryTextListener;
  }
  
  public void setOnSearchClickListener(View.OnClickListener paramOnClickListener) {
    this.mOnSearchClickListener = paramOnClickListener;
  }
  
  public void setOnSuggestionListener(OnSuggestionListener paramOnSuggestionListener) {
    this.mOnSuggestionListener = paramOnSuggestionListener;
  }
  
  public void setQuery(CharSequence paramCharSequence, boolean paramBoolean) {
    this.mSearchSrcTextView.setText(paramCharSequence);
    if (paramCharSequence != null) {
      SearchAutoComplete searchAutoComplete = this.mSearchSrcTextView;
      searchAutoComplete.setSelection(searchAutoComplete.length());
      this.mUserQuery = paramCharSequence;
    } 
    if (paramBoolean && !TextUtils.isEmpty(paramCharSequence))
      onSubmitQuery(); 
  }
  
  public void setQueryHint(CharSequence paramCharSequence) {
    this.mQueryHint = paramCharSequence;
    updateQueryHint();
  }
  
  public void setQueryRefinementEnabled(boolean paramBoolean) {
    this.mQueryRefinement = paramBoolean;
    CursorAdapter cursorAdapter = this.mSuggestionsAdapter;
    if (cursorAdapter instanceof SuggestionsAdapter) {
      boolean bool;
      SuggestionsAdapter suggestionsAdapter = (SuggestionsAdapter)cursorAdapter;
      if (paramBoolean) {
        bool = true;
      } else {
        bool = true;
      } 
      suggestionsAdapter.setQueryRefinement(bool);
    } 
  }
  
  public void setSearchableInfo(SearchableInfo paramSearchableInfo) {
    this.mSearchable = paramSearchableInfo;
    if (paramSearchableInfo != null) {
      updateSearchAutoComplete();
      updateQueryHint();
    } 
    boolean bool = hasVoiceSearch();
    this.mVoiceButtonEnabled = bool;
    if (bool)
      this.mSearchSrcTextView.setPrivateImeOptions("nm"); 
    updateViewsVisibility(isIconified());
  }
  
  public void setSubmitButtonEnabled(boolean paramBoolean) {
    this.mSubmitButtonEnabled = paramBoolean;
    updateViewsVisibility(isIconified());
  }
  
  public void setSuggestionsAdapter(CursorAdapter paramCursorAdapter) {
    this.mSuggestionsAdapter = paramCursorAdapter;
    this.mSearchSrcTextView.setAdapter((ListAdapter)paramCursorAdapter);
  }
  
  void updateFocusedState() {
    int[] arrayOfInt;
    if (this.mSearchSrcTextView.hasFocus()) {
      arrayOfInt = FOCUSED_STATE_SET;
    } else {
      arrayOfInt = EMPTY_STATE_SET;
    } 
    Drawable drawable = this.mSearchPlate.getBackground();
    if (drawable != null)
      drawable.setState(arrayOfInt); 
    drawable = this.mSubmitArea.getBackground();
    if (drawable != null)
      drawable.setState(arrayOfInt); 
    invalidate();
  }
  
  public static interface OnCloseListener {
    boolean onClose();
  }
  
  public static interface OnQueryTextListener {
    boolean onQueryTextChange(String param1String);
    
    boolean onQueryTextSubmit(String param1String);
  }
  
  public static interface OnSuggestionListener {
    boolean onSuggestionClick(int param1Int);
    
    boolean onSuggestionSelect(int param1Int);
  }
  
  private static class PreQAutoCompleteTextViewReflector {
    private Method mDoAfterTextChanged = null;
    
    private Method mDoBeforeTextChanged = null;
    
    private Method mEnsureImeVisible = null;
    
    PreQAutoCompleteTextViewReflector() {
      preApi29Check();
      try {
        Method method = AutoCompleteTextView.class.getDeclaredMethod("doBeforeTextChanged", new Class[0]);
        this.mDoBeforeTextChanged = method;
        method.setAccessible(true);
      } catch (NoSuchMethodException noSuchMethodException) {}
      try {
        Method method = AutoCompleteTextView.class.getDeclaredMethod("doAfterTextChanged", new Class[0]);
        this.mDoAfterTextChanged = method;
        method.setAccessible(true);
      } catch (NoSuchMethodException noSuchMethodException) {}
      try {
        Method method = AutoCompleteTextView.class.getMethod("ensureImeVisible", new Class[] { boolean.class });
        this.mEnsureImeVisible = method;
        method.setAccessible(true);
      } catch (NoSuchMethodException noSuchMethodException) {}
    }
    
    private static void preApi29Check() {
      if (Build.VERSION.SDK_INT < 29)
        return; 
      throw new UnsupportedClassVersionError("This function can only be used for API Level < 29.");
    }
    
    void doAfterTextChanged(AutoCompleteTextView param1AutoCompleteTextView) {
      preApi29Check();
      Method method = this.mDoAfterTextChanged;
      if (method != null)
        try {
          method.invoke(param1AutoCompleteTextView, new Object[0]);
        } catch (Exception exception) {} 
    }
    
    void doBeforeTextChanged(AutoCompleteTextView param1AutoCompleteTextView) {
      preApi29Check();
      Method method = this.mDoBeforeTextChanged;
      if (method != null)
        try {
          method.invoke(param1AutoCompleteTextView, new Object[0]);
        } catch (Exception exception) {} 
    }
    
    void ensureImeVisible(AutoCompleteTextView param1AutoCompleteTextView) {
      preApi29Check();
      Method method = this.mEnsureImeVisible;
      if (method != null)
        try {
          method.invoke(param1AutoCompleteTextView, new Object[] { Boolean.valueOf(true) });
        } catch (Exception exception) {} 
    }
  }
  
  static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = (Parcelable.Creator<SavedState>)new Parcelable.ClassLoaderCreator<SavedState>() {
        public SearchView.SavedState createFromParcel(Parcel param2Parcel) {
          return new SearchView.SavedState(param2Parcel, null);
        }
        
        public SearchView.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
          return new SearchView.SavedState(param2Parcel, param2ClassLoader);
        }
        
        public SearchView.SavedState[] newArray(int param2Int) {
          return new SearchView.SavedState[param2Int];
        }
      };
    
    boolean isIconified;
    
    public SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      this.isIconified = ((Boolean)param1Parcel.readValue(null)).booleanValue();
    }
    
    SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public String toString() {
      return "SearchView.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " isIconified=" + this.isIconified + "}";
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeValue(Boolean.valueOf(this.isIconified));
    }
  }
  
  class null implements Parcelable.ClassLoaderCreator<SavedState> {
    public SearchView.SavedState createFromParcel(Parcel param1Parcel) {
      return new SearchView.SavedState(param1Parcel, null);
    }
    
    public SearchView.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new SearchView.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public SearchView.SavedState[] newArray(int param1Int) {
      return new SearchView.SavedState[param1Int];
    }
  }
  
  public static class SearchAutoComplete extends AppCompatAutoCompleteTextView {
    private boolean mHasPendingShowSoftInputRequest;
    
    final Runnable mRunShowSoftInputIfNecessary = new Runnable() {
        final SearchView.SearchAutoComplete this$0;
        
        public void run() {
          SearchView.SearchAutoComplete.this.showSoftInputIfNecessary();
        }
      };
    
    private SearchView mSearchView;
    
    private int mThreshold = getThreshold();
    
    public SearchAutoComplete(Context param1Context) {
      this(param1Context, (AttributeSet)null);
    }
    
    public SearchAutoComplete(Context param1Context, AttributeSet param1AttributeSet) {
      this(param1Context, param1AttributeSet, R.attr.autoCompleteTextViewStyle);
    }
    
    public SearchAutoComplete(Context param1Context, AttributeSet param1AttributeSet, int param1Int) {
      super(param1Context, param1AttributeSet, param1Int);
    }
    
    private int getSearchViewTextMinWidthDp() {
      Configuration configuration = getResources().getConfiguration();
      int j = configuration.screenWidthDp;
      int i = configuration.screenHeightDp;
      return (j >= 960 && i >= 720 && configuration.orientation == 2) ? 256 : ((j >= 600 || (j >= 640 && i >= 480)) ? 192 : 160);
    }
    
    public boolean enoughToFilter() {
      return (this.mThreshold <= 0 || super.enoughToFilter());
    }
    
    void ensureImeVisible() {
      if (Build.VERSION.SDK_INT >= 29) {
        setInputMethodMode(1);
        if (enoughToFilter())
          showDropDown(); 
      } else {
        SearchView.PRE_API_29_HIDDEN_METHOD_INVOKER.ensureImeVisible(this);
      } 
    }
    
    boolean isEmpty() {
      boolean bool;
      if (TextUtils.getTrimmedLength((CharSequence)getText()) == 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public InputConnection onCreateInputConnection(EditorInfo param1EditorInfo) {
      InputConnection inputConnection = super.onCreateInputConnection(param1EditorInfo);
      if (this.mHasPendingShowSoftInputRequest) {
        removeCallbacks(this.mRunShowSoftInputIfNecessary);
        post(this.mRunShowSoftInputIfNecessary);
      } 
      return inputConnection;
    }
    
    protected void onFinishInflate() {
      super.onFinishInflate();
      DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
      setMinWidth((int)TypedValue.applyDimension(1, getSearchViewTextMinWidthDp(), displayMetrics));
    }
    
    protected void onFocusChanged(boolean param1Boolean, int param1Int, Rect param1Rect) {
      super.onFocusChanged(param1Boolean, param1Int, param1Rect);
      this.mSearchView.onTextFocusChanged();
    }
    
    public boolean onKeyPreIme(int param1Int, KeyEvent param1KeyEvent) {
      if (param1Int == 4) {
        if (param1KeyEvent.getAction() == 0 && param1KeyEvent.getRepeatCount() == 0) {
          KeyEvent.DispatcherState dispatcherState = getKeyDispatcherState();
          if (dispatcherState != null)
            dispatcherState.startTracking(param1KeyEvent, this); 
          return true;
        } 
        if (param1KeyEvent.getAction() == 1) {
          KeyEvent.DispatcherState dispatcherState = getKeyDispatcherState();
          if (dispatcherState != null)
            dispatcherState.handleUpEvent(param1KeyEvent); 
          if (param1KeyEvent.isTracking() && !param1KeyEvent.isCanceled()) {
            this.mSearchView.clearFocus();
            setImeVisibility(false);
            return true;
          } 
        } 
      } 
      return super.onKeyPreIme(param1Int, param1KeyEvent);
    }
    
    public void onWindowFocusChanged(boolean param1Boolean) {
      super.onWindowFocusChanged(param1Boolean);
      if (param1Boolean && this.mSearchView.hasFocus() && getVisibility() == 0) {
        this.mHasPendingShowSoftInputRequest = true;
        if (SearchView.isLandscapeMode(getContext()))
          ensureImeVisible(); 
      } 
    }
    
    public void performCompletion() {}
    
    protected void replaceText(CharSequence param1CharSequence) {}
    
    void setImeVisibility(boolean param1Boolean) {
      InputMethodManager inputMethodManager = (InputMethodManager)getContext().getSystemService("input_method");
      if (!param1Boolean) {
        this.mHasPendingShowSoftInputRequest = false;
        removeCallbacks(this.mRunShowSoftInputIfNecessary);
        inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
        return;
      } 
      if (inputMethodManager.isActive((View)this)) {
        this.mHasPendingShowSoftInputRequest = false;
        removeCallbacks(this.mRunShowSoftInputIfNecessary);
        inputMethodManager.showSoftInput((View)this, 0);
        return;
      } 
      this.mHasPendingShowSoftInputRequest = true;
    }
    
    void setSearchView(SearchView param1SearchView) {
      this.mSearchView = param1SearchView;
    }
    
    public void setThreshold(int param1Int) {
      super.setThreshold(param1Int);
      this.mThreshold = param1Int;
    }
    
    void showSoftInputIfNecessary() {
      if (this.mHasPendingShowSoftInputRequest) {
        ((InputMethodManager)getContext().getSystemService("input_method")).showSoftInput((View)this, 0);
        this.mHasPendingShowSoftInputRequest = false;
      } 
    }
  }
  
  class null implements Runnable {
    final SearchView.SearchAutoComplete this$0;
    
    public void run() {
      this.this$0.showSoftInputIfNecessary();
    }
  }
  
  private static class UpdatableTouchDelegate extends TouchDelegate {
    private final Rect mActualBounds;
    
    private boolean mDelegateTargeted;
    
    private final View mDelegateView;
    
    private final int mSlop;
    
    private final Rect mSlopBounds;
    
    private final Rect mTargetBounds;
    
    public UpdatableTouchDelegate(Rect param1Rect1, Rect param1Rect2, View param1View) {
      super(param1Rect1, param1View);
      this.mSlop = ViewConfiguration.get(param1View.getContext()).getScaledTouchSlop();
      this.mTargetBounds = new Rect();
      this.mSlopBounds = new Rect();
      this.mActualBounds = new Rect();
      setBounds(param1Rect1, param1Rect2);
      this.mDelegateView = param1View;
    }
    
    public boolean onTouchEvent(MotionEvent param1MotionEvent) {
      boolean bool1;
      boolean bool5;
      int i = (int)param1MotionEvent.getX();
      int j = (int)param1MotionEvent.getY();
      boolean bool3 = false;
      boolean bool2 = true;
      boolean bool4 = false;
      switch (param1MotionEvent.getAction()) {
        default:
          bool1 = bool2;
          break;
        case 3:
          bool3 = this.mDelegateTargeted;
          this.mDelegateTargeted = false;
          bool1 = bool2;
          break;
        case 1:
        case 2:
          bool5 = this.mDelegateTargeted;
          bool3 = bool5;
          bool1 = bool2;
          if (bool5) {
            bool3 = bool5;
            bool1 = bool2;
            if (!this.mSlopBounds.contains(i, j)) {
              bool1 = false;
              bool3 = bool5;
            } 
          } 
          break;
        case 0:
          bool1 = bool2;
          if (this.mTargetBounds.contains(i, j)) {
            this.mDelegateTargeted = true;
            bool3 = true;
            bool1 = bool2;
          } 
          break;
      } 
      if (bool3) {
        if (bool1 && !this.mActualBounds.contains(i, j)) {
          param1MotionEvent.setLocation((this.mDelegateView.getWidth() / 2), (this.mDelegateView.getHeight() / 2));
        } else {
          param1MotionEvent.setLocation((i - this.mActualBounds.left), (j - this.mActualBounds.top));
        } 
        bool4 = this.mDelegateView.dispatchTouchEvent(param1MotionEvent);
      } 
      return bool4;
    }
    
    public void setBounds(Rect param1Rect1, Rect param1Rect2) {
      this.mTargetBounds.set(param1Rect1);
      this.mSlopBounds.set(param1Rect1);
      param1Rect1 = this.mSlopBounds;
      int i = this.mSlop;
      param1Rect1.inset(-i, -i);
      this.mActualBounds.set(param1Rect2);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\SearchView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */