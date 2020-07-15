
> # 本项目于5.19日实验课完成
> # `（该更新的）更新完成`


![HIT](https://img-blog.csdnimg.cn/20200308214236949.png#pic_center)

# 2020春计算机学院《软件构造》课程Lab4实验报告

> - Software Construction 2020 Spring
> - Lab-4 Debugging, Exception Handling, and Defensive Programming
> - 版权声明：`1183710109 郭茁宁`

@[TOC]

# 1 实验目标概述
略

# 2 实验环境配置
略

# 3 实验过程
## 3.1 Error and Exception Handling
在data/Exceptions/中构造了错误数据，并在`ExceptionTest.java`中测试了这些错误。
![](https://img-blog.csdnimg.cn/20200519165022805.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2d6bjAwNDE3,size_16,color_FFFFFF,t_70#pic_center)

### 3.1.1 处理输入文本中的三类错误（代码节选）
第1-8个为不符合语法规则错误，第9个为元素相同错误，第10-13个为依赖关系不正确错误。
处理方法为：

```java
try {
	……
throw new ……Exception();
} catch (……Exception e1) {
logger.log(Level.WARNING, e1.getMessage(), e1);
……
}
```

#### 3.1.1.1 DataPatternException
原因：由于数据的常量错误而没有匹配到单个元素。
抛出异常方法：在正则表达式匹配时，若没有匹配到则抛出该错误。

```java
if (!matcher.find()) {
throw new DataPatternException("Data: " + stringInfo + " mismatch Pattern.");
}
```

#### 3.1.1.2 EntryNumberFormatException
原因：计划项编号不符合规则。
抛出异常方法：检查是否符合“前两个字符为大写字母，后2-4个字符为数字”。

```java
/**
 * check entry number
 * @param planningEntryNumber
 * @throws EntryNumberFormatException
 */
public static void checkEntryNumber(String planningEntryNumber) throws EntryNumberFormatException {
    if (Character.isUpperCase(planningEntryNumber.charAt(0))
            && Character.isUpperCase(planningEntryNumber.charAt(1))) {
        for (int i = 2; i < planningEntryNumber.length(); i++) {
            if (!Character.isDigit(planningEntryNumber.charAt(i)))
                throw new EntryNumberFormatException(planningEntryNumber + " has incorrect format.");
        }
    } else
        throw new EntryNumberFormatException(planningEntryNumber + " has incorrect format.");
}
```

#### 3.1.1.3 SameAirportException
原因：起飞和到达机场相同引起的错误。
抛出异常方法：对比两个机场字符串是否相等。


#### 3.1.1.4 TimeOrderException
原因：起飞时间应该在到达时间之前（不能相等）。
抛出异常方法：首先try时间能否被parse，若不行则抛出DateTimeParseException；否则在finally中使用LocalDateTime.isBefore()方法比较时间先后。

```java
/**
 * check time format and departure is before arrival
 * @param departureTime
 * @param arrivalTime
 * @throws TimeOrderException
 * @throws DateTimeParseException
 */
public static void checkTime(String departureTime, String arrivalTime)
        throws TimeOrderException, DateTimeParseException {
    LocalDateTime dt = null, at = null;
    try {
        dt = LocalDateTime.parse(departureTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        at = LocalDateTime.parse(arrivalTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    } catch (Exception e) {
        throw new DateTimeParseException("The date time is not matched.", departureTime + arrivalTime, 0);
    } finally {
        if (dt != null && at != null) {
            if (!dt.isBefore(at))
                throw new TimeOrderException(
                        "Departure time " + departureTime + " is not before arrival time " + arrivalTime + " .");
        }
    }
}
```

#### 3.1.1.5 PlaneNumberFormatException
原因：飞机编号不符合格式。
抛出异常方法：检查字符串长度以及首字母、后4位数字。


#### 3.1.1.6 PlaneTypeException
原因：飞机类型不符合格式。
抛出异常方法：检查是否由字母和数字构成。


#### 3.1.1.7 PlaneSeatRangeException
原因：飞机座位数范围错误。
抛出异常方法：转换为整数比较范围。


#### 3.1.1.8 PlaneAgeFormatException
原因：飞机年龄非一位小数或整数，且介于0-30之间
抛出异常方法：查找小数点的位置，与字符串长度比较，得出几位小数，并查找区间。


#### 3.1.1.9 SameEntryException
原因：存在两个航班，飞机和航班号都相等。
抛出异常方法：遍历所有计划项，两两比较是否存在上述条件。


#### 3.1.1.10 HugeTimeGapException
原因：起飞时间和到达时间超过一天。
抛出异常方法：判断每个计划项的起飞时间晚1d是否比到达时间晚。

```java
/**
 * check gap between leaving and arrival
 * @throws HugeTimeGapException
 */
public void checkTimeGap() throws HugeTimeGapException {
    List<PlanningEntry<Resource>> entries = this.getAllPlanningEntries();
    int n = entries.size();
    for (int i = 0; i < n - 1; i++) {
        FlightSchedule<Resource> e = (FlightSchedule<Resource>) entries.get(i);
        LocalDateTime t1 = e.getTimeLeaving(), t2 = e.getTimeArrival();
        if (t1.plusDays(1).isBefore(t2))
            throw new HugeTimeGapException(t1.toString() + " is to early than " + t2.toString());
    }
}
```

#### 3.1.1.11 EntryInconsistentInfoException
原因：相同航班号的航班信息（起降地点/时间）不一致。
抛出异常方法：检查每一对计划项，得到其时间和地点对象。


#### 3.1.1.12 PlaneInconsistentInfoException
原因：不同的航班中出现相同的飞机。
抛出异常方法：遍历每一对飞机，若飞机号相同，但内容不相同，则出现不一致信息。

```java
/**
 * check plane information consistent
 * @throws PlaneInconsistentInfoException
 */
public void checkPlaneConsistentInfo() throws PlaneInconsistentInfoException {
    Set<Resource> planes = this.getAllResource();
    for (Resource r1 : planes) {
        for (Resource r2 : planes) {
            if (r1 != r2) {
                Plane p1 = (Plane) r1, p2 = (Plane) r2;
                if (p1.getNumber().equals(p2.getNumber()) && !p1.equals(p2))
                    throw new PlaneInconsistentInfoException(p1.getNumber() + " has inconsistent information.");
            }
        }
    }
}
```

#### 3.1.1.13 SameEntrySameDayException
原因：相同航班号的航班在同一天。
抛出异常方法：遍历比较


### 3.1.2 处理客户端操作时产生的异常
在App中遇到客户端操作异常时，抛出异常后使用Logger记录，并取消该操作。
#### 3.1.2.1 DeleteAllocatedResourceException
原因：在删除某资源的时候，如果有尚未结束的计划项正在占用该资源。
抛出异常方法：遍历计划项，对于多个使用该资源的计划项，均检查计划项状态。捕获到异常后将“允许删除标签”设为false，最后显示弹窗声明删除失败。

```java
Resource deletingResource = allResourceList.get(num);
boolean flag = true;
try {
	checkResourceAllocated(flightScheduleCollection, deletingResource);
} catch (DeleteAllocatedResourceException e1) {
	logger.log(Level.WARNING, e1.getMessage(), e1);
	flag = false;
}
flag &= flightScheduleCollection.deleteResource(deletingResource);
JOptionPane.showMessageDialog(resourceFrame, flag ? "Successful" : "Failed", "Deleting Resource", JOptionPane.PLAIN_MESSAGE);
```

#### 3.1.2.2 DeleteOccupiedLocationException
原因：在删除某位置的时候，如果有尚未结束的计划项正在该位置执行。
抛出异常方法：遍历计划项，对于多个使用该位置的计划项，均检查计划项状态。
（与上`DeleteAllocatedResourceException`同理）
Check方法Spec如下：

```java
/**
 * check location occupied
 * @param flightScheduleCollection0
 * @param location
 * @throws DeleteOccupiedLocationException
 */
public static void checkLocationOccupied(FlightScheduleCollection flightScheduleCollection0, String location)
        throws DeleteOccupiedLocationException {
    List<PlanningEntry<Resource>> planningEntries = flightScheduleCollection0.getAllPlanningEntries();
    for (PlanningEntry<Resource> planningEntry : planningEntries) {
        FlightSchedule<Resource> flightSchedule = (FlightSchedule<Resource>) planningEntry;
        if (flightSchedule.getLocationOrigin().equals(location)
                || flightSchedule.getLocationTerminal().equals(location))
            if (planningEntry.getState().getState().equals(EntryStateEnum.ALLOCATED)
                    || planningEntry.getState().getState().equals(EntryStateEnum.BLOCKED)
                    || planningEntry.getState().getState().equals(EntryStateEnum.RUNNING))
                throw new DeleteOccupiedLocationException(location + " is occupied");
    }
}
```

#### 3.1.2.3 UnableCancelException
原因：在取消某计划项的时候，如果该计划项的当前状态不允许取消。
抛出异常方法：通过`cancelPlanningEntry()`-->`setNewState()`返回的Boolean来判断是否可取消。


#### 3.1.2.4 ResourceSharedException
原因：在为某计划项分配某资源的时候，如果分配后会导致与已有的其他计划项产生“资源独占冲突”。
抛出异常方法：与`DeleteAllocatedResourceException`类似，与其不同的是在分配资源是遍历查找。

#### 3.1.2.5 LocationSharedException
原因：在为某计划项变更位置的时候，如果变更后会导致与已有的其他计划项产生“位置独占冲突”。
抛出异常方法：与`ResourceSharedException`同理。该功能在`Activity Calendar App`中。


# 3.2 Assertion and Defensive Programming
### 3.2.1 checkRep()检查rep invariants
#### 3.2.1.1 TimeSlot
`TimeSlot`的AF、RI如下：

```java
/*
 * AF:
 * arrival[i] represent the time it arrives locations[i]
 * leaving[i] represent the time it leaves locations[i]
 * 
 * when Flight Schedule:
 * length == 2, arrival[0] == leaving[0], arrival[1] == leaving[1]
 * 
 * when Activity Schedule:
 * length == 1, arrival[0] is ending time, leaving[0] is beginning time
 * 
 * RI:
 * the length of arrival and leaving should be equal
 * leaving[i] should be later than arrival[i]
 * when i<length arrival[i] and leaving[i] should be non-null
 * 
 * Safety:
 * do not provide mutator
 */
```

由此可以设计`checkRep()`方法：

```java
/**
 * check Rep
 */
private void checkRep() {
    assert (arrival.size() == leaving.size());
    for (int i = 0; i < arrival.size(); i++) {
        assert (arrival.get(i) != null);
        assert (leaving.get(i) != null);
    }
}
```

#### 3.2.1.2 Location
`Location`的AF、RI如下：

```java
/*
 * AF:
 * locations represent the locations in the plan
 * 
 * RI:
 * locations should be as long as arrival and leaving in class TimeSlot
 * 
 * Safety:
 * do not provide mutator
 */
```

Location的Representation可以保证包括航班和高铁在内的“任意两个站不相同”。该`checkRep()`如下：

```java
/**
 * check Rep
 */
private void checkRep() {
    for (String strLocation1 : locations) {
        assert (strLocation1.length() > 0);
        for (String strLocation2 : locations) {
            if (strLocation1 != strLocation2)
                assert (!strLocation1.equals(strLocation2));
        }
    }
}
```

#### 3.2.1.3 EntryState
`EntryState`的AF、RI如下：

```java
/*
 * AF:
 * the state enum's name represents the state 
 * RI:
 * state must be in enums
 * Safety:
 * it's a mutable object, but do not let the outside modify state directly
 */
```

该`checkRep()`非常容易，略。
#### 3.2.1.4 Resource
`Resource`的3个实现类均是immutable类型ADT，存储一定信息，因此其checkRep就是保证信息存储的变量符合格式，检查方法与抛出异常方法类似，因此对抛出异常的方法进行复用。以Plane为例：

```java
/**
 * check Rep
 */
private void checkRep() {
    try {
        FlightScheduleCollection.checkPlaneNumber(number);
    } catch (PlaneNumberFormatException e) {
        assert false;
    }
    try {
        FlightScheduleCollection.checkPlaneType(strType);
    } catch (PlaneTypeException e) {
        assert false;
    }
    try {
        FlightScheduleCollection.checkPlaneSeat(String.valueOf(intSeats));
    } catch (PlaneSeatRangeException e) {
        assert false;
    }
    try {
        FlightScheduleCollection.checkPlaneAge(Double.toString(age));
    } catch (PlaneAgeFormatException e) {
        assert false;
    }
}
```

#### 3.2.1.5 PlanningEntry
在新建计划项时，资源、位置、时间、状态均被检查过，因此只要检查4者不为空，且标签正确即可。

```java
private void checkRep() {
    assert (strPlanningEntryType.equals("FlightSchedule"));
    assert (location != null);
    assert (timeSlot != null);
    assert (state != null);
    assert (resource != null);
}
```

### 3.2.2 Assertion/异常机制来保障pre-/post-condition
Assertion主要针对mutable对象的mutator。
#### 3.2.2.1 EntryState
在修改状态时，前置条件和后置条件均为：当前状态合法。除了类型为高铁，否则不能为blocked。因此判断两次状态的合法性。

```java
/**
 * set the new state
 * @param strPlanningEntryType in {"FlightSchedule", "TrainSchedule", "ActivityCalendar"}
 * @param strNewState
 * @return true if the setting is successful, false if not
 */
public Boolean setNewState(String strPlanningEntryType, String strNewState) {
    assert (strPlanningEntryType.toLowerCase().contains("train")
            || !this.getStrState().toLowerCase().equals("blocked"));
    if (this.setAvailability(strPlanningEntryType, strNewState.toUpperCase())){
        this.state = EntryStateEnum.valueOf(strNewState.toUpperCase());
        assert (strPlanningEntryType.toLowerCase().contains("train")
                || !this.getStrState().toLowerCase().equals("blocked"));
        return true;
    }
    return false;
}
```

#### 3.2.2.2 PlanningEntry
计划项的mutator在于分配资源和更改状态。
分配资源时，前置条件为：被分配的资源不能为空。以ActivityCalendar为例：

```java
/**
 * allocate the resource to the flight schedule
 * set the state as ALLOCATED
 * @param resource
 * @param intResourceNumber
 * @return true if the resource is set and state is ALLOCATED
 */
public Boolean allocateResource(R resource, int intResourceNumber) {
    assert (resource != null && intResourceNumber > 0);
    super.resource = resource;
    this.intResourceNumber = intResourceNumber;
    return this.state.setNewState(strPlanningEntryType, "Allocated");
}
```

更改状态时，后置条件为：更改后的状态不能为空且为某一合法状态。以`CommonPlanningEntry.start()`为例：

```java
@Override
public Boolean start() {
    boolean flag = this.state.setNewState(strPlanningEntryType, "Running");
    assert (this.state != null && this.state.getState() != null);
    return flag;
}
```

其中，`PlanningEntry`中的`TrainSchedule`有操作“取第i个车厢”，对于该i的前置条件为：不能查询第1个站的到达时间且不能查询最后一个站的出发时间。以查询出发时间为例：

```java
/**
 * get the LocalDateTime of leaving time of No.indexLocation Location
 * @param indexLocation
 * @return the LocalDateTime of leaving time of No.indexLocation Location
 */
public LocalDateTime getLeavingTimeOfIndex(int indexLocation) {
    assert (indexLocation != TERMINAL);
    return super.getTimeSlot().getLeaving().get(indexLocation);
}
```

#### 3.2.2.3 PlanningEntryCollection
在计划项集合类中，有许多关联到计划项编号的操作，前置条件要求计划项编号参数不能为blank。同理，所有有关查询操作的参数均不能为空白。

```java
/**
 * search for a planning entry whose number matches the given
 * @param planningEntryNumber
 * @return the planning entry
 */
public PlanningEntry<Resource> getPlanningEntryByStrNumber(String planningEntryNumber) {
    assert (!planningEntryNumber.isBlank());
    for (PlanningEntry<Resource> planningEntry : planningEntries)
        if (planningEntry.getPlanningEntryNumber().equals(planningEntryNumber))
            return planningEntry;
    return null;
}
```

### 3.2.3 你的代码的防御式策略概述
代码的“错误传递”发生在客户端到API、API到ADT之间，因此在这两种传递过程的起始和完成阶段，都应该进行防御。
![](https://img-blog.csdnimg.cn/20200519170235962.png#pic_center)

#### 3.2.3.1 Client-->API前置条件防御
客户端和API之间，需要基于用户输入参数进行功能控制，因此用户输入的内容正确性决定了API功能实现的正确性。客户端的输入方法或API的方法起始阶段需要对用户输入进行检查。
例如FlightScheduleCollection.addPlanningEntry()中需要读入一段数据，在方法中进行了对各项参数的检查，错误则抛出包括EntryNumberFormatException在内的相应异常；而在查询指定计划项信息时，则是在FlightScheduleApp中先对该编号正确性进行检查（该操作委派给了FlightScheduleCollection）然后才获取指定信息。
#### 3.2.3.2 Client-->API后置条件防御
在API操作完成之后，在客户端或API中需要对结果进行正确性的大致检查，避免一下明显错误情况；若API操作不当，可能在程序中引入隐式错误。
例如在启动计划项时，FlightScheduleApp在完成操作后弹窗显示操作结果；在暂停计划项之后，会检查该计划项类型是否为可暂停的计划项对象类型。
#### 3.2.3.3 API-->ADT前置条件防御
在API的操作会对ADT进行影响，若ADT为可变的，则要求Setter()参数正确。检查参数正确可以在API的方法中，也可以在ADT的方法中。
例如API在获得某计划项的资源时，会判断该ADT的资源是否为空；在API需要获得高铁的第i站的到达时间，在ADT的方法中会对i的取值进行断言（不能为0）。
#### 3.2.3.4 API-->ADT后置条件防御
在修改ADT的内容之后，需要确认修改后的ADT符合RI。此时，可以调用ADT私有方法checkRep()进行校验。在各个ADT中均有checkRep()，出现在构造器（immutable对象），也会出现在mutator（mutable对象）。

# 3.3 Logging
本实验中，日志功能的实现调用了 Java 的库 java.util.logging。并且日志功能仅支持在应用中实现，不支持在测试时使用。

```java
private final static Logger logger = Logger.getLogger("Flight Schedule Log");
```

在后续的日志创建中，如果 Logger.getLogger(String name)的 name 参数和上述三个之一相同，那么日志变量将指向同一块内存区域，即共享同一 logger。然后对日志配置。
首先配置日志的输出语言为英文。

```java
Locale.setDefault(new Locale("en", "EN"));
```

然后设置日志显示信息的最低级别（即包括了 INFO, WARNING 和 SEVERE）。

```java
logger.setLevel(Level.INFO);
```

接着对三个 logger 配置加入相应的文件管理，使得日志可以写入到文件中。

```java
FileHandler fileHandler = new FileHandler("log/FlightScheduleLog.txt", true);
```

对文件管理handler配置好文件写入的格式，采用SimpleFormatter格式文件，可读性高，但在检索时可能比较麻烦。

```java
fileHandler.setFormatter(new SimpleFormatter());
logger.addHandler(fileHandler);
```

这样日志的配置就做好了。在应用类中，只需要加一个全局静态变量，调用已经创建的相关的 logger，再设置无需从控制台输出日志内容即可。除了应用类，凡是应用类调用的函数如果有异常被 catch 的话，其所在的类中，也应该有这个 logger 管理变量。
文件整体效果：
![](https://img-blog.csdnimg.cn/20200521152223628.png#pic_center)

### 3.3.1 异常处理的日志功能
#### 3.3.1.1 需要终止当前操作的异常
如果遇到需要终止当前操作的异常，在 catch 结束前，应该记录 SEVERE级别的日志信息，如：

```java
try {
flightSchedule = flightScheduleCollection.addPlanningEntry(stringInfo.toString());
} catch (DataPatternException e) {
logger.log(Level.SEVERE, e.getMessage(), e);
break;
}
```

#### 3.3.1.2 不需要终止当前操作的异常
如果遇到不需要终止当前操作的异常，在 catch 结束前，应该记录WARNING 级别的日志信息，如：

```java
try {
checkResourceShared(flightScheduleCollection,  flightScheduleCollection.getPlaneOfNumber(strResourceNumber));
} catch (ResourceSharedException e1) {
logger.log(Level.WARNING, e1.getMessage(), e1);
flag = false;
}
```

#### 3.3.1.3 Assertion error
对于应用中遇到的 Assertion error，应该记录下 SEVERE 级别的信息。
### 3.3.2 应用层操作的日志功能
应用中使用功能在应用中使用的任何功能，都应该在调用之后马上生成 INFO 调用信息，在功能成功结束后，生成 INFO 成功信息。如：

```java
switch (strOperation) {
case "Start":
logger.log(Level.INFO, "Start Planning Entry");
operationFlag = flightScheduleCollection.startPlanningEntry(planningEntryNumber);
break;
```

### 3.3.3 日志查询功能
首先处理字符串，并能够将所有的日志显示出来。通过委派复用Board功能，可视化JTable。以下，将增加方法参数（时间、事件类型和App类型），对日志项进行筛选，以下图形式进行显示。

![](https://img-blog.csdnimg.cn/20200521152240176.png#pic_center)

通过正则表达式读入日志。

```java
Pattern pattern1 = Pattern.compile("(.*?) apps\\.(.*?)App (.*?)\\.");
Pattern pattern2 = Pattern.compile("([A-Z]*?): (.*?)\\.+");
```

匹配后得出所需信息。

```java
String time = matcher1.group(1);
String logType1 = matcher2.group(1);
String appType1 = matcher1.group(2);
String action = matcher1.group(3);
String message = matcher2.group(2);
```

转换时间后，检测时间是否匹配（设定：在`WITHIN_MINUTE`分钟以内）：

```java
if (!askedTime.isBlank()) {
askingTime = LocalDateTime.parse(askedTime, DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
}
LocalDateTime timeFormatted = LocalDateTime.parse(timeString,
DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss a"));
if (askingTime == null || 
(askingTime.plusMinutes(WITHIN_MINUTE).isAfter(timeFormatted) 
&& askingTime.minusMinutes(WITHIN_MINUTE).isBefore(timeFormatted))) {
……
}
```

同理，检测事件类型和App类型：

```java
if (logType.isBlank() || logType.equals(logType1)) {
if (appType.isBlank() || appType.equals(appType1)) {
		……
}
}
```

方法Spec：

```java
/**
 * show logs
 * @param askedTime
 * @param logType
 * @param appType
 * @throws IOException
 */
public void showLog(String askedTime, String logType, String appType) throws IOException {}
```

最后通过GUI显示出选择日志，方法的按钮在visualization内的“Show Logs”。
![](https://img-blog.csdnimg.cn/20200521152252668.png#pic_center)

## 3.4 Testing for Robustness and Correctness
### 3.4.1 Testing strategy
使用等价类和边界值的测试思想，为各 ADT 添加 testing strategy。以下以PlanningEntryAPITest为例：

```java
/*
 * Test strategy
 * 
 * Location Conflict:
 * Add entries from without conflict to with conflict.
 * call method when they're added.
 * 
 * Resource conflict:
 * Add entries from without conflict to with conflict.
 * call method when they're added.
 * 
 * Find Pre Entry:
 * Add entries from non-pre-entry to has it, and to new pre-entry.
 * call method when they're added, and update the new entry closer to the asking.
 */
```

### 3.4.2 测试用例设计
为每种Exception设计测试用例，保存在data/Exceptions/中。
![](https://img-blog.csdnimg.cn/20200521152307327.png#pic_center)

主要有两种数据：最少需要一组数据的、最少需要两组数据的。当测试计划项数据文本内容各种Exception、以及资源相关Exception最少只需要一组数据。以PlaneSeatRangeException为例：
![](https://img-blog.csdnimg.cn/20200521152316784.png#pic_center)

测试用例代码：

```java
@Test
public void testPlaneSeatRangeException() throws Exception {
    exception.expect(PlaneSeatRangeException.class);
    exception.expectMessage(1000 + " is not in [50, 600].");
    String data = getOneData("data/Exceptions/PlaneSeatRangeException.txt");
    FlightScheduleCollection flightScheduleCollection = new FlightScheduleCollection();
    FlightSchedule<Resource> flightSchedule = flightScheduleCollection.addPlanningEntry(data);
    flightScheduleCollection.allocatePlanningEntry(flightSchedule.getPlanningEntryNumber(), data);
}
```

如此设计多种非法文件，促使程序出错，提高健壮性和正确性。
### 3.4.3 测试运行结果与EclEmma覆盖度报告
ExceptionTest测试运行结果：
![](https://img-blog.csdnimg.cn/20200519165022805.png#pic_center)

所有测试运行结果：
![](https://img-blog.csdnimg.cn/20200521152347159.png#pic_center)

EclEmma覆盖度测试：
语句覆盖度：
![](https://img-blog.csdnimg.cn/20200521152353926.png#pic_center)

分支覆盖度：
![](https://img-blog.csdnimg.cn/20200521152400665.png#pic_center)

复杂度覆盖度：
![](https://img-blog.csdnimg.cn/20200521152406737.png#pic_center)

3.5 SpotBugs tool
没有Spot到Bugs。
之前有用到过，检测出实际返回类型和方法返回类型不一致的错误，还有可能为Null Pointer的错误。
![](https://img-blog.csdnimg.cn/20200521152414250.png#pic_center)
![](https://img-blog.csdnimg.cn/20200521152420495.png#pic_center)


## 3.6 Debugging（恕不给出详细错误）
### 3.6.1 EventManager程序
- 理解待调试程序的代码思想：
	该程序要求若干时间区间交集数量的最大值。算法是标记+搜索。将区间的每一个整数点进行标记（用Map），查询是查找Map.values()最大值。

- 发现错误并修改：
略

- 修复之后的测试结果：
![](https://img-blog.csdnimg.cn/2020052115253730.png#pic_center)

3.6.2 LowestPrice程序
- 理解待调试程序的代码思想：
该程序想要求在有special offer的情况下最优价格。算法思想是贪心。首先假设最低代价为所有均用零售，然后每次将一个special offer加入“购物车”，更新需求，再用新需求迭代求解。

- 发现错误并修改：
略

- 修复之后的测试结果：
![](https://img-blog.csdnimg.cn/20200521152558341.png#pic_center)

### 3.6.3 FlightClient/Flight/Plane程序
- 理解待调试程序的代码思想：
	该程序通过枚举每个航班，尝试安排飞机，确保没有与其他已经分配的航班冲突，最后确认是否能所有同时分配成功。

- 发现并修正错误：
略

修复之后的测试结果：
![](https://img-blog.csdnimg.cn/20200521152610695.png#pic_center)





