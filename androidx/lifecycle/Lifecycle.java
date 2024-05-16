package androidx.lifecycle;

import java.util.concurrent.atomic.AtomicReference;

public abstract class Lifecycle {
  AtomicReference<Object> mInternalScopeRef = new AtomicReference();
  
  public abstract void addObserver(LifecycleObserver paramLifecycleObserver);
  
  public abstract State getCurrentState();
  
  public abstract void removeObserver(LifecycleObserver paramLifecycleObserver);
  
  public enum Event {
    ON_ANY, ON_CREATE, ON_DESTROY, ON_PAUSE, ON_RESUME, ON_START, ON_STOP;
    
    private static final Event[] $VALUES;
    
    static {
      Event event3 = new Event("ON_CREATE", 0);
      ON_CREATE = event3;
      Event event6 = new Event("ON_START", 1);
      ON_START = event6;
      Event event1 = new Event("ON_RESUME", 2);
      ON_RESUME = event1;
      Event event4 = new Event("ON_PAUSE", 3);
      ON_PAUSE = event4;
      Event event7 = new Event("ON_STOP", 4);
      ON_STOP = event7;
      Event event5 = new Event("ON_DESTROY", 5);
      ON_DESTROY = event5;
      Event event2 = new Event("ON_ANY", 6);
      ON_ANY = event2;
      $VALUES = new Event[] { event3, event6, event1, event4, event7, event5, event2 };
    }
    
    public static Event downFrom(Lifecycle.State param1State) {
      switch (param1State) {
        default:
          return null;
        case null:
          return ON_PAUSE;
        case null:
          return ON_STOP;
        case null:
          break;
      } 
      return ON_DESTROY;
    }
    
    public static Event downTo(Lifecycle.State param1State) {
      switch (param1State) {
        default:
          return null;
        case null:
          return ON_DESTROY;
        case null:
          return ON_PAUSE;
        case null:
          break;
      } 
      return ON_STOP;
    }
    
    public static Event upFrom(Lifecycle.State param1State) {
      switch (param1State) {
        default:
          return null;
        case null:
          return ON_CREATE;
        case null:
          return ON_RESUME;
        case null:
          break;
      } 
      return ON_START;
    }
    
    public static Event upTo(Lifecycle.State param1State) {
      switch (param1State) {
        default:
          return null;
        case null:
          return ON_RESUME;
        case null:
          return ON_START;
        case null:
          break;
      } 
      return ON_CREATE;
    }
    
    public Lifecycle.State getTargetState() {
      switch (this) {
        default:
          throw new IllegalArgumentException(this + " has no target state");
        case null:
          return Lifecycle.State.DESTROYED;
        case null:
          return Lifecycle.State.RESUMED;
        case null:
        case null:
          return Lifecycle.State.STARTED;
        case null:
        case null:
          break;
      } 
      return Lifecycle.State.CREATED;
    }
  }
  
  public enum State {
    CREATED, DESTROYED, INITIALIZED, RESUMED, STARTED;
    
    private static final State[] $VALUES;
    
    static {
      State state3 = new State("DESTROYED", 0);
      DESTROYED = state3;
      State state2 = new State("INITIALIZED", 1);
      INITIALIZED = state2;
      State state4 = new State("CREATED", 2);
      CREATED = state4;
      State state1 = new State("STARTED", 3);
      STARTED = state1;
      State state5 = new State("RESUMED", 4);
      RESUMED = state5;
      $VALUES = new State[] { state3, state2, state4, state1, state5 };
    }
    
    public boolean isAtLeast(State param1State) {
      boolean bool;
      if (compareTo(param1State) >= 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\lifecycle\Lifecycle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */