# Sqlbrite Assist

[![Build Status](https://travis-ci.org/znyang/sqlbrite-assist.svg?branch=master)](https://travis-ci.org/znyang/sqlbrite-assist)
[![](https://jitpack.io/v/znyang/sqlbrite-assist.svg)](https://jitpack.io/#znyang/sqlbrite-assist)

* sqlbrite-wrapper
* sqlbrite-dbflow


## Usage

### 依赖配置

```gradle
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }

    dependencies {
        compile 'com.github.znyang.sqlbrite-assist:sqlbrite-dbflow:0.1'
        apt "com.github.Raizlabs.DBFlow:dbflow-processor:3.1.1"
    }
```

### 一个例子

- Weather是一个使用Dbflow的BaseModel，这部分省略，参考[dbflow文档](https://github.com/Raizlabs/DBFlow/blob/master/usage2/Intro.md)

- 订阅Weather这个表的变化：

```java
    DbflowBrite.Query.from(Weather.class)
            .queryModels()
            .subscribe(weathers -> {
                // handle weathers
            });
```

还可以，通过dbflow生成sql作为查询条件：

```java
    String sql = SQLite.select().from(Weather.class)
            .where(Weather_Table.temperature.lessThan(30))
            .toString();

    DbflowBrite.Query.from(Weather.class)
            .sql(sql)
            .queryModels()
            .subscribe(weathers -> {
                // handle weathers
            });
```

- 使用封装的cud方法：

```java
    List<Weather> weathers;
    // ...
    DbflowBrite.save(Weather.class, weathers);

    Weather weather;
    // update weather;
    DbflowBrite.update(weather);

    DbflowBrite.delete(weather);

    Weather cityA;
    Weather cityB;
    DbflowBrite.insert(cityA, cityB);
```

提供各种简单的API，每次变更后，都会通知到原来的订阅者，按需处理业务逻辑（比如更新视图）。

这样，你可以将视图和数据通过订阅来绑定，而无需了解它在什么时候变化(比ContentProvider+Loader的方式更简洁)。
