package androidx.recyclerview.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewPropertyAnimator;
import androidx.core.view.ViewCompat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefaultItemAnimator extends SimpleItemAnimator {
  private static final boolean DEBUG = false;
  
  private static TimeInterpolator sDefaultInterpolator;
  
  ArrayList<RecyclerView.ViewHolder> mAddAnimations = new ArrayList<>();
  
  ArrayList<ArrayList<RecyclerView.ViewHolder>> mAdditionsList = new ArrayList<>();
  
  ArrayList<RecyclerView.ViewHolder> mChangeAnimations = new ArrayList<>();
  
  ArrayList<ArrayList<ChangeInfo>> mChangesList = new ArrayList<>();
  
  ArrayList<RecyclerView.ViewHolder> mMoveAnimations = new ArrayList<>();
  
  ArrayList<ArrayList<MoveInfo>> mMovesList = new ArrayList<>();
  
  private ArrayList<RecyclerView.ViewHolder> mPendingAdditions = new ArrayList<>();
  
  private ArrayList<ChangeInfo> mPendingChanges = new ArrayList<>();
  
  private ArrayList<MoveInfo> mPendingMoves = new ArrayList<>();
  
  private ArrayList<RecyclerView.ViewHolder> mPendingRemovals = new ArrayList<>();
  
  ArrayList<RecyclerView.ViewHolder> mRemoveAnimations = new ArrayList<>();
  
  private void animateRemoveImpl(final RecyclerView.ViewHolder holder) {
    final View view = holder.itemView;
    final ViewPropertyAnimator animation = view.animate();
    this.mRemoveAnimations.add(holder);
    viewPropertyAnimator.setDuration(getRemoveDuration()).alpha(0.0F).setListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
          final DefaultItemAnimator this$0;
          
          final ViewPropertyAnimator val$animation;
          
          final RecyclerView.ViewHolder val$holder;
          
          final View val$view;
          
          public void onAnimationEnd(Animator param1Animator) {
            animation.setListener(null);
            view.setAlpha(1.0F);
            DefaultItemAnimator.this.dispatchRemoveFinished(holder);
            DefaultItemAnimator.this.mRemoveAnimations.remove(holder);
            DefaultItemAnimator.this.dispatchFinishedWhenDone();
          }
          
          public void onAnimationStart(Animator param1Animator) {
            DefaultItemAnimator.this.dispatchRemoveStarting(holder);
          }
        }).start();
  }
  
  private void endChangeAnimation(List<ChangeInfo> paramList, RecyclerView.ViewHolder paramViewHolder) {
    for (int i = paramList.size() - 1; i >= 0; i--) {
      ChangeInfo changeInfo = paramList.get(i);
      if (endChangeAnimationIfNecessary(changeInfo, paramViewHolder) && changeInfo.oldHolder == null && changeInfo.newHolder == null)
        paramList.remove(changeInfo); 
    } 
  }
  
  private void endChangeAnimationIfNecessary(ChangeInfo paramChangeInfo) {
    if (paramChangeInfo.oldHolder != null)
      endChangeAnimationIfNecessary(paramChangeInfo, paramChangeInfo.oldHolder); 
    if (paramChangeInfo.newHolder != null)
      endChangeAnimationIfNecessary(paramChangeInfo, paramChangeInfo.newHolder); 
  }
  
  private boolean endChangeAnimationIfNecessary(ChangeInfo paramChangeInfo, RecyclerView.ViewHolder paramViewHolder) {
    boolean bool = false;
    if (paramChangeInfo.newHolder == paramViewHolder) {
      paramChangeInfo.newHolder = null;
    } else {
      if (paramChangeInfo.oldHolder == paramViewHolder) {
        paramChangeInfo.oldHolder = null;
        bool = true;
        paramViewHolder.itemView.setAlpha(1.0F);
        paramViewHolder.itemView.setTranslationX(0.0F);
        paramViewHolder.itemView.setTranslationY(0.0F);
        dispatchChangeFinished(paramViewHolder, bool);
        return true;
      } 
      return false;
    } 
    paramViewHolder.itemView.setAlpha(1.0F);
    paramViewHolder.itemView.setTranslationX(0.0F);
    paramViewHolder.itemView.setTranslationY(0.0F);
    dispatchChangeFinished(paramViewHolder, bool);
    return true;
  }
  
  private void resetAnimation(RecyclerView.ViewHolder paramViewHolder) {
    if (sDefaultInterpolator == null)
      sDefaultInterpolator = (new ValueAnimator()).getInterpolator(); 
    paramViewHolder.itemView.animate().setInterpolator(sDefaultInterpolator);
    endAnimation(paramViewHolder);
  }
  
  public boolean animateAdd(RecyclerView.ViewHolder paramViewHolder) {
    resetAnimation(paramViewHolder);
    paramViewHolder.itemView.setAlpha(0.0F);
    this.mPendingAdditions.add(paramViewHolder);
    return true;
  }
  
  void animateAddImpl(final RecyclerView.ViewHolder holder) {
    final View view = holder.itemView;
    final ViewPropertyAnimator animation = view.animate();
    this.mAddAnimations.add(holder);
    viewPropertyAnimator.alpha(1.0F).setDuration(getAddDuration()).setListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
          final DefaultItemAnimator this$0;
          
          final ViewPropertyAnimator val$animation;
          
          final RecyclerView.ViewHolder val$holder;
          
          final View val$view;
          
          public void onAnimationCancel(Animator param1Animator) {
            view.setAlpha(1.0F);
          }
          
          public void onAnimationEnd(Animator param1Animator) {
            animation.setListener(null);
            DefaultItemAnimator.this.dispatchAddFinished(holder);
            DefaultItemAnimator.this.mAddAnimations.remove(holder);
            DefaultItemAnimator.this.dispatchFinishedWhenDone();
          }
          
          public void onAnimationStart(Animator param1Animator) {
            DefaultItemAnimator.this.dispatchAddStarting(holder);
          }
        }).start();
  }
  
  public boolean animateChange(RecyclerView.ViewHolder paramViewHolder1, RecyclerView.ViewHolder paramViewHolder2, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (paramViewHolder1 == paramViewHolder2)
      return animateMove(paramViewHolder1, paramInt1, paramInt2, paramInt3, paramInt4); 
    float f3 = paramViewHolder1.itemView.getTranslationX();
    float f1 = paramViewHolder1.itemView.getTranslationY();
    float f2 = paramViewHolder1.itemView.getAlpha();
    resetAnimation(paramViewHolder1);
    int i = (int)((paramInt3 - paramInt1) - f3);
    int j = (int)((paramInt4 - paramInt2) - f1);
    paramViewHolder1.itemView.setTranslationX(f3);
    paramViewHolder1.itemView.setTranslationY(f1);
    paramViewHolder1.itemView.setAlpha(f2);
    if (paramViewHolder2 != null) {
      resetAnimation(paramViewHolder2);
      paramViewHolder2.itemView.setTranslationX(-i);
      paramViewHolder2.itemView.setTranslationY(-j);
      paramViewHolder2.itemView.setAlpha(0.0F);
    } 
    this.mPendingChanges.add(new ChangeInfo(paramViewHolder1, paramViewHolder2, paramInt1, paramInt2, paramInt3, paramInt4));
    return true;
  }
  
  void animateChangeImpl(final ChangeInfo changeInfo) {
    final View view;
    RecyclerView.ViewHolder viewHolder1 = changeInfo.oldHolder;
    final View newView = null;
    if (viewHolder1 == null) {
      viewHolder1 = null;
    } else {
      view1 = viewHolder1.itemView;
    } 
    RecyclerView.ViewHolder viewHolder2 = changeInfo.newHolder;
    if (viewHolder2 != null)
      view2 = viewHolder2.itemView; 
    if (view1 != null) {
      final ViewPropertyAnimator newViewAnimation = view1.animate().setDuration(getChangeDuration());
      this.mChangeAnimations.add(changeInfo.oldHolder);
      viewPropertyAnimator.translationX((changeInfo.toX - changeInfo.fromX));
      viewPropertyAnimator.translationY((changeInfo.toY - changeInfo.fromY));
      viewPropertyAnimator.alpha(0.0F).setListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
            final DefaultItemAnimator this$0;
            
            final DefaultItemAnimator.ChangeInfo val$changeInfo;
            
            final ViewPropertyAnimator val$oldViewAnim;
            
            final View val$view;
            
            public void onAnimationEnd(Animator param1Animator) {
              oldViewAnim.setListener(null);
              view.setAlpha(1.0F);
              view.setTranslationX(0.0F);
              view.setTranslationY(0.0F);
              DefaultItemAnimator.this.dispatchChangeFinished(changeInfo.oldHolder, true);
              DefaultItemAnimator.this.mChangeAnimations.remove(changeInfo.oldHolder);
              DefaultItemAnimator.this.dispatchFinishedWhenDone();
            }
            
            public void onAnimationStart(Animator param1Animator) {
              DefaultItemAnimator.this.dispatchChangeStarting(changeInfo.oldHolder, true);
            }
          }).start();
    } 
    if (view2 != null) {
      final ViewPropertyAnimator newViewAnimation = view2.animate();
      this.mChangeAnimations.add(changeInfo.newHolder);
      viewPropertyAnimator.translationX(0.0F).translationY(0.0F).setDuration(getChangeDuration()).alpha(1.0F).setListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
            final DefaultItemAnimator this$0;
            
            final DefaultItemAnimator.ChangeInfo val$changeInfo;
            
            final View val$newView;
            
            final ViewPropertyAnimator val$newViewAnimation;
            
            public void onAnimationEnd(Animator param1Animator) {
              newViewAnimation.setListener(null);
              newView.setAlpha(1.0F);
              newView.setTranslationX(0.0F);
              newView.setTranslationY(0.0F);
              DefaultItemAnimator.this.dispatchChangeFinished(changeInfo.newHolder, false);
              DefaultItemAnimator.this.mChangeAnimations.remove(changeInfo.newHolder);
              DefaultItemAnimator.this.dispatchFinishedWhenDone();
            }
            
            public void onAnimationStart(Animator param1Animator) {
              DefaultItemAnimator.this.dispatchChangeStarting(changeInfo.newHolder, false);
            }
          }).start();
    } 
  }
  
  public boolean animateMove(RecyclerView.ViewHolder paramViewHolder, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    View view = paramViewHolder.itemView;
    paramInt1 += (int)paramViewHolder.itemView.getTranslationX();
    int i = paramInt2 + (int)paramViewHolder.itemView.getTranslationY();
    resetAnimation(paramViewHolder);
    paramInt2 = paramInt3 - paramInt1;
    int j = paramInt4 - i;
    if (paramInt2 == 0 && j == 0) {
      dispatchMoveFinished(paramViewHolder);
      return false;
    } 
    if (paramInt2 != 0)
      view.setTranslationX(-paramInt2); 
    if (j != 0)
      view.setTranslationY(-j); 
    this.mPendingMoves.add(new MoveInfo(paramViewHolder, paramInt1, i, paramInt3, paramInt4));
    return true;
  }
  
  void animateMoveImpl(final RecyclerView.ViewHolder holder, final int deltaX, final int deltaY, int paramInt3, int paramInt4) {
    final View view = holder.itemView;
    deltaX = paramInt3 - deltaX;
    deltaY = paramInt4 - deltaY;
    if (deltaX != 0)
      view.animate().translationX(0.0F); 
    if (deltaY != 0)
      view.animate().translationY(0.0F); 
    final ViewPropertyAnimator animation = view.animate();
    this.mMoveAnimations.add(holder);
    viewPropertyAnimator.setDuration(getMoveDuration()).setListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
          final DefaultItemAnimator this$0;
          
          final ViewPropertyAnimator val$animation;
          
          final int val$deltaX;
          
          final int val$deltaY;
          
          final RecyclerView.ViewHolder val$holder;
          
          final View val$view;
          
          public void onAnimationCancel(Animator param1Animator) {
            if (deltaX != 0)
              view.setTranslationX(0.0F); 
            if (deltaY != 0)
              view.setTranslationY(0.0F); 
          }
          
          public void onAnimationEnd(Animator param1Animator) {
            animation.setListener(null);
            DefaultItemAnimator.this.dispatchMoveFinished(holder);
            DefaultItemAnimator.this.mMoveAnimations.remove(holder);
            DefaultItemAnimator.this.dispatchFinishedWhenDone();
          }
          
          public void onAnimationStart(Animator param1Animator) {
            DefaultItemAnimator.this.dispatchMoveStarting(holder);
          }
        }).start();
  }
  
  public boolean animateRemove(RecyclerView.ViewHolder paramViewHolder) {
    resetAnimation(paramViewHolder);
    this.mPendingRemovals.add(paramViewHolder);
    return true;
  }
  
  public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder paramViewHolder, List<Object> paramList) {
    return (!paramList.isEmpty() || super.canReuseUpdatedViewHolder(paramViewHolder, paramList));
  }
  
  void cancelAll(List<RecyclerView.ViewHolder> paramList) {
    for (int i = paramList.size() - 1; i >= 0; i--)
      ((RecyclerView.ViewHolder)paramList.get(i)).itemView.animate().cancel(); 
  }
  
  void dispatchFinishedWhenDone() {
    if (!isRunning())
      dispatchAnimationsFinished(); 
  }
  
  public void endAnimation(RecyclerView.ViewHolder paramViewHolder) {
    View view = paramViewHolder.itemView;
    view.animate().cancel();
    int i;
    for (i = this.mPendingMoves.size() - 1; i >= 0; i--) {
      if (((MoveInfo)this.mPendingMoves.get(i)).holder == paramViewHolder) {
        view.setTranslationY(0.0F);
        view.setTranslationX(0.0F);
        dispatchMoveFinished(paramViewHolder);
        this.mPendingMoves.remove(i);
      } 
    } 
    endChangeAnimation(this.mPendingChanges, paramViewHolder);
    if (this.mPendingRemovals.remove(paramViewHolder)) {
      view.setAlpha(1.0F);
      dispatchRemoveFinished(paramViewHolder);
    } 
    if (this.mPendingAdditions.remove(paramViewHolder)) {
      view.setAlpha(1.0F);
      dispatchAddFinished(paramViewHolder);
    } 
    for (i = this.mChangesList.size() - 1; i >= 0; i--) {
      ArrayList<ChangeInfo> arrayList = this.mChangesList.get(i);
      endChangeAnimation(arrayList, paramViewHolder);
      if (arrayList.isEmpty())
        this.mChangesList.remove(i); 
    } 
    for (i = this.mMovesList.size() - 1; i >= 0; i--) {
      ArrayList arrayList = this.mMovesList.get(i);
      for (int j = arrayList.size() - 1; j >= 0; j--) {
        if (((MoveInfo)arrayList.get(j)).holder == paramViewHolder) {
          view.setTranslationY(0.0F);
          view.setTranslationX(0.0F);
          dispatchMoveFinished(paramViewHolder);
          arrayList.remove(j);
          if (arrayList.isEmpty())
            this.mMovesList.remove(i); 
          break;
        } 
      } 
    } 
    for (i = this.mAdditionsList.size() - 1; i >= 0; i--) {
      ArrayList arrayList = this.mAdditionsList.get(i);
      if (arrayList.remove(paramViewHolder)) {
        view.setAlpha(1.0F);
        dispatchAddFinished(paramViewHolder);
        if (arrayList.isEmpty())
          this.mAdditionsList.remove(i); 
      } 
    } 
    this.mRemoveAnimations.remove(paramViewHolder);
    this.mAddAnimations.remove(paramViewHolder);
    this.mChangeAnimations.remove(paramViewHolder);
    this.mMoveAnimations.remove(paramViewHolder);
    dispatchFinishedWhenDone();
  }
  
  public void endAnimations() {
    int i;
    for (i = this.mPendingMoves.size() - 1; i >= 0; i--) {
      MoveInfo moveInfo = this.mPendingMoves.get(i);
      View view = moveInfo.holder.itemView;
      view.setTranslationY(0.0F);
      view.setTranslationX(0.0F);
      dispatchMoveFinished(moveInfo.holder);
      this.mPendingMoves.remove(i);
    } 
    for (i = this.mPendingRemovals.size() - 1; i >= 0; i--) {
      dispatchRemoveFinished(this.mPendingRemovals.get(i));
      this.mPendingRemovals.remove(i);
    } 
    for (i = this.mPendingAdditions.size() - 1; i >= 0; i--) {
      RecyclerView.ViewHolder viewHolder = this.mPendingAdditions.get(i);
      viewHolder.itemView.setAlpha(1.0F);
      dispatchAddFinished(viewHolder);
      this.mPendingAdditions.remove(i);
    } 
    for (i = this.mPendingChanges.size() - 1; i >= 0; i--)
      endChangeAnimationIfNecessary(this.mPendingChanges.get(i)); 
    this.mPendingChanges.clear();
    if (!isRunning())
      return; 
    for (i = this.mMovesList.size() - 1; i >= 0; i--) {
      ArrayList<MoveInfo> arrayList = this.mMovesList.get(i);
      for (int j = arrayList.size() - 1; j >= 0; j--) {
        MoveInfo moveInfo = arrayList.get(j);
        View view = moveInfo.holder.itemView;
        view.setTranslationY(0.0F);
        view.setTranslationX(0.0F);
        dispatchMoveFinished(moveInfo.holder);
        arrayList.remove(j);
        if (arrayList.isEmpty())
          this.mMovesList.remove(arrayList); 
      } 
    } 
    for (i = this.mAdditionsList.size() - 1; i >= 0; i--) {
      ArrayList<RecyclerView.ViewHolder> arrayList = this.mAdditionsList.get(i);
      for (int j = arrayList.size() - 1; j >= 0; j--) {
        RecyclerView.ViewHolder viewHolder = arrayList.get(j);
        viewHolder.itemView.setAlpha(1.0F);
        dispatchAddFinished(viewHolder);
        arrayList.remove(j);
        if (arrayList.isEmpty())
          this.mAdditionsList.remove(arrayList); 
      } 
    } 
    for (i = this.mChangesList.size() - 1; i >= 0; i--) {
      ArrayList<ChangeInfo> arrayList = this.mChangesList.get(i);
      for (int j = arrayList.size() - 1; j >= 0; j--) {
        endChangeAnimationIfNecessary(arrayList.get(j));
        if (arrayList.isEmpty())
          this.mChangesList.remove(arrayList); 
      } 
    } 
    cancelAll(this.mRemoveAnimations);
    cancelAll(this.mMoveAnimations);
    cancelAll(this.mAddAnimations);
    cancelAll(this.mChangeAnimations);
    dispatchAnimationsFinished();
  }
  
  public boolean isRunning() {
    return (!this.mPendingAdditions.isEmpty() || !this.mPendingChanges.isEmpty() || !this.mPendingMoves.isEmpty() || !this.mPendingRemovals.isEmpty() || !this.mMoveAnimations.isEmpty() || !this.mRemoveAnimations.isEmpty() || !this.mAddAnimations.isEmpty() || !this.mChangeAnimations.isEmpty() || !this.mMovesList.isEmpty() || !this.mAdditionsList.isEmpty() || !this.mChangesList.isEmpty());
  }
  
  public void runPendingAnimations() {
    int m = this.mPendingRemovals.isEmpty() ^ true;
    int j = this.mPendingMoves.isEmpty() ^ true;
    int i = this.mPendingChanges.isEmpty() ^ true;
    int k = this.mPendingAdditions.isEmpty() ^ true;
    if (m == 0 && j == 0 && k == 0 && i == 0)
      return; 
    Iterator<RecyclerView.ViewHolder> iterator = this.mPendingRemovals.iterator();
    while (iterator.hasNext())
      animateRemoveImpl(iterator.next()); 
    this.mPendingRemovals.clear();
    if (j != 0) {
      final ArrayList<MoveInfo> additions = new ArrayList();
      arrayList.addAll(this.mPendingMoves);
      this.mMovesList.add(arrayList);
      this.mPendingMoves.clear();
      Runnable runnable = new Runnable() {
          final DefaultItemAnimator this$0;
          
          final ArrayList val$moves;
          
          public void run() {
            for (DefaultItemAnimator.MoveInfo moveInfo : moves)
              DefaultItemAnimator.this.animateMoveImpl(moveInfo.holder, moveInfo.fromX, moveInfo.fromY, moveInfo.toX, moveInfo.toY); 
            moves.clear();
            DefaultItemAnimator.this.mMovesList.remove(moves);
          }
        };
      if (m != 0) {
        ViewCompat.postOnAnimationDelayed(((MoveInfo)arrayList.get(0)).holder.itemView, runnable, getRemoveDuration());
      } else {
        runnable.run();
      } 
    } 
    if (i != 0) {
      final ArrayList<ChangeInfo> additions = new ArrayList();
      arrayList.addAll(this.mPendingChanges);
      this.mChangesList.add(arrayList);
      this.mPendingChanges.clear();
      Runnable runnable = new Runnable() {
          final DefaultItemAnimator this$0;
          
          final ArrayList val$changes;
          
          public void run() {
            for (DefaultItemAnimator.ChangeInfo changeInfo : changes)
              DefaultItemAnimator.this.animateChangeImpl(changeInfo); 
            changes.clear();
            DefaultItemAnimator.this.mChangesList.remove(changes);
          }
        };
      if (m != 0) {
        ViewCompat.postOnAnimationDelayed(((ChangeInfo)arrayList.get(0)).oldHolder.itemView, runnable, getRemoveDuration());
      } else {
        runnable.run();
      } 
    } 
    if (k != 0) {
      final ArrayList<RecyclerView.ViewHolder> additions = new ArrayList();
      arrayList.addAll(this.mPendingAdditions);
      this.mAdditionsList.add(arrayList);
      this.mPendingAdditions.clear();
      Runnable runnable = new Runnable() {
          final DefaultItemAnimator this$0;
          
          final ArrayList val$additions;
          
          public void run() {
            for (RecyclerView.ViewHolder viewHolder : additions)
              DefaultItemAnimator.this.animateAddImpl(viewHolder); 
            additions.clear();
            DefaultItemAnimator.this.mAdditionsList.remove(additions);
          }
        };
      if (m != 0 || j != 0 || i != 0) {
        long l1;
        long l3 = 0L;
        if (m != 0) {
          l1 = getRemoveDuration();
        } else {
          l1 = 0L;
        } 
        if (j != 0) {
          l2 = getMoveDuration();
        } else {
          l2 = 0L;
        } 
        if (i != 0)
          l3 = getChangeDuration(); 
        long l2 = Math.max(l2, l3);
        ViewCompat.postOnAnimationDelayed(((RecyclerView.ViewHolder)arrayList.get(0)).itemView, runnable, l2 + l1);
        return;
      } 
      runnable.run();
    } 
  }
  
  private static class ChangeInfo {
    public int fromX;
    
    public int fromY;
    
    public RecyclerView.ViewHolder newHolder;
    
    public RecyclerView.ViewHolder oldHolder;
    
    public int toX;
    
    public int toY;
    
    private ChangeInfo(RecyclerView.ViewHolder param1ViewHolder1, RecyclerView.ViewHolder param1ViewHolder2) {
      this.oldHolder = param1ViewHolder1;
      this.newHolder = param1ViewHolder2;
    }
    
    ChangeInfo(RecyclerView.ViewHolder param1ViewHolder1, RecyclerView.ViewHolder param1ViewHolder2, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      this(param1ViewHolder1, param1ViewHolder2);
      this.fromX = param1Int1;
      this.fromY = param1Int2;
      this.toX = param1Int3;
      this.toY = param1Int4;
    }
    
    public String toString() {
      return "ChangeInfo{oldHolder=" + this.oldHolder + ", newHolder=" + this.newHolder + ", fromX=" + this.fromX + ", fromY=" + this.fromY + ", toX=" + this.toX + ", toY=" + this.toY + '}';
    }
  }
  
  private static class MoveInfo {
    public int fromX;
    
    public int fromY;
    
    public RecyclerView.ViewHolder holder;
    
    public int toX;
    
    public int toY;
    
    MoveInfo(RecyclerView.ViewHolder param1ViewHolder, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      this.holder = param1ViewHolder;
      this.fromX = param1Int1;
      this.fromY = param1Int2;
      this.toX = param1Int3;
      this.toY = param1Int4;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\recyclerview\widget\DefaultItemAnimator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */