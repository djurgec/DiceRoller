package com.bumptech.glide.load;

public enum DataSource {
  DATA_DISK_CACHE, LOCAL, MEMORY_CACHE, REMOTE, RESOURCE_DISK_CACHE;
  
  private static final DataSource[] $VALUES;
  
  static {
    DataSource dataSource4 = new DataSource("LOCAL", 0);
    LOCAL = dataSource4;
    DataSource dataSource5 = new DataSource("REMOTE", 1);
    REMOTE = dataSource5;
    DataSource dataSource3 = new DataSource("DATA_DISK_CACHE", 2);
    DATA_DISK_CACHE = dataSource3;
    DataSource dataSource1 = new DataSource("RESOURCE_DISK_CACHE", 3);
    RESOURCE_DISK_CACHE = dataSource1;
    DataSource dataSource2 = new DataSource("MEMORY_CACHE", 4);
    MEMORY_CACHE = dataSource2;
    $VALUES = new DataSource[] { dataSource4, dataSource5, dataSource3, dataSource1, dataSource2 };
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\DataSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */