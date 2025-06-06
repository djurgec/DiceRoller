package androidx.appcompat.view;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.InflateException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import androidx.appcompat.R;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuItemWrapperICS;
import androidx.appcompat.widget.DrawableUtils;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.view.ActionProvider;
import androidx.core.view.MenuItemCompat;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class SupportMenuInflater extends MenuInflater {
  static final Class<?>[] ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE;
  
  static final Class<?>[] ACTION_VIEW_CONSTRUCTOR_SIGNATURE;
  
  static final String LOG_TAG = "SupportMenuInflater";
  
  static final int NO_ID = 0;
  
  private static final String XML_GROUP = "group";
  
  private static final String XML_ITEM = "item";
  
  private static final String XML_MENU = "menu";
  
  final Object[] mActionProviderConstructorArguments;
  
  final Object[] mActionViewConstructorArguments;
  
  Context mContext;
  
  private Object mRealOwner;
  
  static {
    Class[] arrayOfClass = new Class[1];
    arrayOfClass[0] = Context.class;
    ACTION_VIEW_CONSTRUCTOR_SIGNATURE = arrayOfClass;
    ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE = arrayOfClass;
  }
  
  public SupportMenuInflater(Context paramContext) {
    super(paramContext);
    this.mContext = paramContext;
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = paramContext;
    this.mActionViewConstructorArguments = arrayOfObject;
    this.mActionProviderConstructorArguments = arrayOfObject;
  }
  
  private Object findRealOwner(Object paramObject) {
    return (paramObject instanceof android.app.Activity) ? paramObject : ((paramObject instanceof ContextWrapper) ? findRealOwner(((ContextWrapper)paramObject).getBaseContext()) : paramObject);
  }
  
  private void parseMenu(XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Menu paramMenu) throws XmlPullParserException, IOException {
    MenuState menuState = new MenuState(paramMenu);
    int i = paramXmlPullParser.getEventType();
    int j = 0;
    Menu menu = null;
    while (true) {
      if (i == 2) {
        String str = paramXmlPullParser.getName();
        if (str.equals("menu")) {
          i = paramXmlPullParser.next();
          break;
        } 
        throw new RuntimeException("Expecting menu, got " + str);
      } 
      int m = paramXmlPullParser.next();
      i = m;
      if (m == 1) {
        i = m;
        break;
      } 
    } 
    boolean bool = false;
    int k = i;
    while (!bool) {
      String str1;
      Menu menu1;
      boolean bool1;
      String str2;
      switch (k) {
        default:
          i = j;
          paramMenu = menu;
          bool1 = bool;
          break;
        case 3:
          str2 = paramXmlPullParser.getName();
          if (j && str2.equals(menu)) {
            i = 0;
            paramMenu = null;
            bool1 = bool;
            break;
          } 
          if (str2.equals("group")) {
            menuState.resetGroup();
            i = j;
            paramMenu = menu;
            bool1 = bool;
            break;
          } 
          if (str2.equals("item")) {
            i = j;
            paramMenu = menu;
            bool1 = bool;
            if (!menuState.hasAddedItem()) {
              if (menuState.itemActionProvider != null && menuState.itemActionProvider.hasSubMenu()) {
                menuState.addSubMenuItem();
                i = j;
                paramMenu = menu;
                bool1 = bool;
                break;
              } 
              menuState.addItem();
              i = j;
              paramMenu = menu;
              bool1 = bool;
            } 
            break;
          } 
          i = j;
          paramMenu = menu;
          bool1 = bool;
          if (str2.equals("menu")) {
            bool1 = true;
            i = j;
            paramMenu = menu;
          } 
          break;
        case 2:
          if (j) {
            i = j;
            paramMenu = menu;
            bool1 = bool;
            break;
          } 
          str1 = paramXmlPullParser.getName();
          if (str1.equals("group")) {
            menuState.readGroup(paramAttributeSet);
            i = j;
            menu1 = menu;
            bool1 = bool;
            break;
          } 
          if (menu1.equals("item")) {
            menuState.readItem(paramAttributeSet);
            i = j;
            menu1 = menu;
            bool1 = bool;
            break;
          } 
          if (menu1.equals("menu")) {
            parseMenu(paramXmlPullParser, paramAttributeSet, (Menu)menuState.addSubMenuItem());
            i = j;
            menu1 = menu;
            bool1 = bool;
            break;
          } 
          i = 1;
          bool1 = bool;
          break;
        case 1:
          throw new RuntimeException("Unexpected end of document");
      } 
      k = paramXmlPullParser.next();
      j = i;
      menu = menu1;
      bool = bool1;
    } 
  }
  
  Object getRealOwner() {
    if (this.mRealOwner == null)
      this.mRealOwner = findRealOwner(this.mContext); 
    return this.mRealOwner;
  }
  
  public void inflate(int paramInt, Menu paramMenu) {
    XmlPullParserException xmlPullParserException1;
    if (!(paramMenu instanceof androidx.core.internal.view.SupportMenu)) {
      super.inflate(paramInt, paramMenu);
      return;
    } 
    XmlResourceParser xmlResourceParser2 = null;
    XmlResourceParser xmlResourceParser3 = null;
    XmlResourceParser xmlResourceParser1 = null;
    try {
      XmlResourceParser xmlResourceParser = this.mContext.getResources().getLayout(paramInt);
      xmlResourceParser1 = xmlResourceParser;
      xmlResourceParser2 = xmlResourceParser;
      xmlResourceParser3 = xmlResourceParser;
      parseMenu((XmlPullParser)xmlResourceParser, Xml.asAttributeSet((XmlPullParser)xmlResourceParser), paramMenu);
      if (xmlResourceParser != null)
        xmlResourceParser.close(); 
      return;
    } catch (XmlPullParserException xmlPullParserException2) {
      xmlResourceParser1 = xmlResourceParser3;
      InflateException inflateException = new InflateException();
      xmlResourceParser1 = xmlResourceParser3;
      this("Error inflating menu XML", (Throwable)xmlPullParserException2);
      xmlResourceParser1 = xmlResourceParser3;
      throw inflateException;
    } catch (IOException iOException) {
      xmlPullParserException1 = xmlPullParserException2;
      InflateException inflateException = new InflateException();
      xmlPullParserException1 = xmlPullParserException2;
      this("Error inflating menu XML", iOException);
      xmlPullParserException1 = xmlPullParserException2;
      throw inflateException;
    } finally {}
    if (xmlPullParserException1 != null)
      xmlPullParserException1.close(); 
    throw paramMenu;
  }
  
  private static class InflatedOnMenuItemClickListener implements MenuItem.OnMenuItemClickListener {
    private static final Class<?>[] PARAM_TYPES = new Class[] { MenuItem.class };
    
    private Method mMethod;
    
    private Object mRealOwner;
    
    public InflatedOnMenuItemClickListener(Object param1Object, String param1String) {
      this.mRealOwner = param1Object;
      Class<?> clazz = param1Object.getClass();
      try {
        this.mMethod = clazz.getMethod(param1String, PARAM_TYPES);
        return;
      } catch (Exception exception) {
        InflateException inflateException = new InflateException("Couldn't resolve menu item onClick handler " + param1String + " in class " + clazz.getName());
        inflateException.initCause(exception);
        throw inflateException;
      } 
    }
    
    public boolean onMenuItemClick(MenuItem param1MenuItem) {
      try {
        if (this.mMethod.getReturnType() == boolean.class)
          return ((Boolean)this.mMethod.invoke(this.mRealOwner, new Object[] { param1MenuItem })).booleanValue(); 
        this.mMethod.invoke(this.mRealOwner, new Object[] { param1MenuItem });
        return true;
      } catch (Exception exception) {
        throw new RuntimeException(exception);
      } 
    }
  }
  
  private class MenuState {
    private static final int defaultGroupId = 0;
    
    private static final int defaultItemCategory = 0;
    
    private static final int defaultItemCheckable = 0;
    
    private static final boolean defaultItemChecked = false;
    
    private static final boolean defaultItemEnabled = true;
    
    private static final int defaultItemId = 0;
    
    private static final int defaultItemOrder = 0;
    
    private static final boolean defaultItemVisible = true;
    
    private int groupCategory;
    
    private int groupCheckable;
    
    private boolean groupEnabled;
    
    private int groupId;
    
    private int groupOrder;
    
    private boolean groupVisible;
    
    ActionProvider itemActionProvider;
    
    private String itemActionProviderClassName;
    
    private String itemActionViewClassName;
    
    private int itemActionViewLayout;
    
    private boolean itemAdded;
    
    private int itemAlphabeticModifiers;
    
    private char itemAlphabeticShortcut;
    
    private int itemCategoryOrder;
    
    private int itemCheckable;
    
    private boolean itemChecked;
    
    private CharSequence itemContentDescription;
    
    private boolean itemEnabled;
    
    private int itemIconResId;
    
    private ColorStateList itemIconTintList = null;
    
    private PorterDuff.Mode itemIconTintMode = null;
    
    private int itemId;
    
    private String itemListenerMethodName;
    
    private int itemNumericModifiers;
    
    private char itemNumericShortcut;
    
    private int itemShowAsAction;
    
    private CharSequence itemTitle;
    
    private CharSequence itemTitleCondensed;
    
    private CharSequence itemTooltipText;
    
    private boolean itemVisible;
    
    private Menu menu;
    
    final SupportMenuInflater this$0;
    
    public MenuState(Menu param1Menu) {
      this.menu = param1Menu;
      resetGroup();
    }
    
    private char getShortcut(String param1String) {
      return (param1String == null) ? Character.MIN_VALUE : param1String.charAt(0);
    }
    
    private <T> T newInstance(String param1String, Class<?>[] param1ArrayOfClass, Object[] param1ArrayOfObject) {
      try {
        null = Class.forName(param1String, false, SupportMenuInflater.this.mContext.getClassLoader()).getConstructor(param1ArrayOfClass);
        null.setAccessible(true);
        return (T)null.newInstance(param1ArrayOfObject);
      } catch (Exception exception) {
        Log.w("SupportMenuInflater", "Cannot instantiate class: " + param1String, exception);
        return null;
      } 
    }
    
    private void setItem(MenuItem param1MenuItem) {
      boolean bool;
      MenuItem menuItem = param1MenuItem.setChecked(this.itemChecked).setVisible(this.itemVisible).setEnabled(this.itemEnabled);
      if (this.itemCheckable >= 1) {
        bool = true;
      } else {
        bool = false;
      } 
      menuItem.setCheckable(bool).setTitleCondensed(this.itemTitleCondensed).setIcon(this.itemIconResId);
      int i = this.itemShowAsAction;
      if (i >= 0)
        param1MenuItem.setShowAsAction(i); 
      if (this.itemListenerMethodName != null)
        if (!SupportMenuInflater.this.mContext.isRestricted()) {
          param1MenuItem.setOnMenuItemClickListener(new SupportMenuInflater.InflatedOnMenuItemClickListener(SupportMenuInflater.this.getRealOwner(), this.itemListenerMethodName));
        } else {
          throw new IllegalStateException("The android:onClick attribute cannot be used within a restricted context");
        }  
      if (this.itemCheckable >= 2)
        if (param1MenuItem instanceof MenuItemImpl) {
          ((MenuItemImpl)param1MenuItem).setExclusiveCheckable(true);
        } else if (param1MenuItem instanceof MenuItemWrapperICS) {
          ((MenuItemWrapperICS)param1MenuItem).setExclusiveCheckable(true);
        }  
      i = 0;
      String str = this.itemActionViewClassName;
      if (str != null) {
        param1MenuItem.setActionView(newInstance(str, SupportMenuInflater.ACTION_VIEW_CONSTRUCTOR_SIGNATURE, SupportMenuInflater.this.mActionViewConstructorArguments));
        i = 1;
      } 
      int j = this.itemActionViewLayout;
      if (j > 0)
        if (i == 0) {
          param1MenuItem.setActionView(j);
        } else {
          Log.w("SupportMenuInflater", "Ignoring attribute 'itemActionViewLayout'. Action view already specified.");
        }  
      ActionProvider actionProvider = this.itemActionProvider;
      if (actionProvider != null)
        MenuItemCompat.setActionProvider(param1MenuItem, actionProvider); 
      MenuItemCompat.setContentDescription(param1MenuItem, this.itemContentDescription);
      MenuItemCompat.setTooltipText(param1MenuItem, this.itemTooltipText);
      MenuItemCompat.setAlphabeticShortcut(param1MenuItem, this.itemAlphabeticShortcut, this.itemAlphabeticModifiers);
      MenuItemCompat.setNumericShortcut(param1MenuItem, this.itemNumericShortcut, this.itemNumericModifiers);
      PorterDuff.Mode mode = this.itemIconTintMode;
      if (mode != null)
        MenuItemCompat.setIconTintMode(param1MenuItem, mode); 
      ColorStateList colorStateList = this.itemIconTintList;
      if (colorStateList != null)
        MenuItemCompat.setIconTintList(param1MenuItem, colorStateList); 
    }
    
    public void addItem() {
      this.itemAdded = true;
      setItem(this.menu.add(this.groupId, this.itemId, this.itemCategoryOrder, this.itemTitle));
    }
    
    public SubMenu addSubMenuItem() {
      this.itemAdded = true;
      SubMenu subMenu = this.menu.addSubMenu(this.groupId, this.itemId, this.itemCategoryOrder, this.itemTitle);
      setItem(subMenu.getItem());
      return subMenu;
    }
    
    public boolean hasAddedItem() {
      return this.itemAdded;
    }
    
    public void readGroup(AttributeSet param1AttributeSet) {
      TypedArray typedArray = SupportMenuInflater.this.mContext.obtainStyledAttributes(param1AttributeSet, R.styleable.MenuGroup);
      this.groupId = typedArray.getResourceId(R.styleable.MenuGroup_android_id, 0);
      this.groupCategory = typedArray.getInt(R.styleable.MenuGroup_android_menuCategory, 0);
      this.groupOrder = typedArray.getInt(R.styleable.MenuGroup_android_orderInCategory, 0);
      this.groupCheckable = typedArray.getInt(R.styleable.MenuGroup_android_checkableBehavior, 0);
      this.groupVisible = typedArray.getBoolean(R.styleable.MenuGroup_android_visible, true);
      this.groupEnabled = typedArray.getBoolean(R.styleable.MenuGroup_android_enabled, true);
      typedArray.recycle();
    }
    
    public void readItem(AttributeSet param1AttributeSet) {
      boolean bool;
      TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(SupportMenuInflater.this.mContext, param1AttributeSet, R.styleable.MenuItem);
      this.itemId = tintTypedArray.getResourceId(R.styleable.MenuItem_android_id, 0);
      this.itemCategoryOrder = 0xFFFF0000 & tintTypedArray.getInt(R.styleable.MenuItem_android_menuCategory, this.groupCategory) | 0xFFFF & tintTypedArray.getInt(R.styleable.MenuItem_android_orderInCategory, this.groupOrder);
      this.itemTitle = tintTypedArray.getText(R.styleable.MenuItem_android_title);
      this.itemTitleCondensed = tintTypedArray.getText(R.styleable.MenuItem_android_titleCondensed);
      this.itemIconResId = tintTypedArray.getResourceId(R.styleable.MenuItem_android_icon, 0);
      this.itemAlphabeticShortcut = getShortcut(tintTypedArray.getString(R.styleable.MenuItem_android_alphabeticShortcut));
      this.itemAlphabeticModifiers = tintTypedArray.getInt(R.styleable.MenuItem_alphabeticModifiers, 4096);
      this.itemNumericShortcut = getShortcut(tintTypedArray.getString(R.styleable.MenuItem_android_numericShortcut));
      this.itemNumericModifiers = tintTypedArray.getInt(R.styleable.MenuItem_numericModifiers, 4096);
      if (tintTypedArray.hasValue(R.styleable.MenuItem_android_checkable)) {
        this.itemCheckable = tintTypedArray.getBoolean(R.styleable.MenuItem_android_checkable, false);
      } else {
        this.itemCheckable = this.groupCheckable;
      } 
      this.itemChecked = tintTypedArray.getBoolean(R.styleable.MenuItem_android_checked, false);
      this.itemVisible = tintTypedArray.getBoolean(R.styleable.MenuItem_android_visible, this.groupVisible);
      this.itemEnabled = tintTypedArray.getBoolean(R.styleable.MenuItem_android_enabled, this.groupEnabled);
      this.itemShowAsAction = tintTypedArray.getInt(R.styleable.MenuItem_showAsAction, -1);
      this.itemListenerMethodName = tintTypedArray.getString(R.styleable.MenuItem_android_onClick);
      this.itemActionViewLayout = tintTypedArray.getResourceId(R.styleable.MenuItem_actionLayout, 0);
      this.itemActionViewClassName = tintTypedArray.getString(R.styleable.MenuItem_actionViewClass);
      String str = tintTypedArray.getString(R.styleable.MenuItem_actionProviderClass);
      this.itemActionProviderClassName = str;
      if (str != null) {
        bool = true;
      } else {
        bool = false;
      } 
      if (bool && this.itemActionViewLayout == 0 && this.itemActionViewClassName == null) {
        this.itemActionProvider = newInstance(str, SupportMenuInflater.ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE, SupportMenuInflater.this.mActionProviderConstructorArguments);
      } else {
        if (bool)
          Log.w("SupportMenuInflater", "Ignoring attribute 'actionProviderClass'. Action view already specified."); 
        this.itemActionProvider = null;
      } 
      this.itemContentDescription = tintTypedArray.getText(R.styleable.MenuItem_contentDescription);
      this.itemTooltipText = tintTypedArray.getText(R.styleable.MenuItem_tooltipText);
      if (tintTypedArray.hasValue(R.styleable.MenuItem_iconTintMode)) {
        this.itemIconTintMode = DrawableUtils.parseTintMode(tintTypedArray.getInt(R.styleable.MenuItem_iconTintMode, -1), this.itemIconTintMode);
      } else {
        this.itemIconTintMode = null;
      } 
      if (tintTypedArray.hasValue(R.styleable.MenuItem_iconTint)) {
        this.itemIconTintList = tintTypedArray.getColorStateList(R.styleable.MenuItem_iconTint);
      } else {
        this.itemIconTintList = null;
      } 
      tintTypedArray.recycle();
      this.itemAdded = false;
    }
    
    public void resetGroup() {
      this.groupId = 0;
      this.groupCategory = 0;
      this.groupOrder = 0;
      this.groupCheckable = 0;
      this.groupVisible = true;
      this.groupEnabled = true;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\view\SupportMenuInflater.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */