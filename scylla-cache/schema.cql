CREATE KEYSPACE tushare WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};
USE tushare;

// generic key value cache
CREATE TABLE IF NOT EXISTS cache (
                                key text,
                                value text,
                                PRIMARY KEY (key)
) WITH gc_grace_seconds = 86400;