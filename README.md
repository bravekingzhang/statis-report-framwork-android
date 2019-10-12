# statis-report-framwork-android
性能数搜集及上报系统Demo

### 项目目的
实现搜集并且上报App性能数据

### 关于搜集系统一些想法
本来打算自己动手实现一个性能搜集的系统，当然主要是自己之前也研究过一些他人实现的性能搜集相关的开源库，比如说:

#### 关于内存泄露方面的

![leakcanary](https://square.github.io/leakcanary/images/screenshot-2.0.png) 
 
[https://square.github.io/leakcanary](https://square.github.io/leakcanary)

#### 关于卡顿方面的搜集

![](https://github.com/markzhai/AndroidPerformanceMonitor/blob/master/art/shot1.png)

[https://github.com/markzhai/AndroidPerformanceMonitor](https://github.com/markzhai/AndroidPerformanceMonitor) 

#### 关于方法耗时搜集

```java
@DebugLog
public String getName(String first, String last) {
  SystemClock.sleep(15); // Don't ever really do this!
  return first + " " + last;
}
```
```bash
V/Example: ⇢ getName(first="Jake", last="Wharton")
V/Example: ⇠ getName [16ms] = "Jake Wharton"
```
[https://github.com/JakeWharton/hugo](https://github.com/JakeWharton/hugo)

hugo可能还需要进行二次开发，且需要手动对方法加@DebugLog才可以实现对方法统计，可能还不具备自动收集的特性，当然也有一个思路，可以借助[asm](https://www.ibm.com/developerworks/cn/java/j-lo-asm30/index.html)【字节码插装】方式来自动加@DebugLog，然后收集耗时方法上报

#### 搜集页面启动耗时
其实结合使用[https://github.com/JakeWharton/hugo](https://github.com/JakeWharton/hugo)，可以轻松实现监控，就不在赘述。


**但是仔细想一想，还是不要轻易动手自己来实现，还是先看看业界有哪些库可以对这个做搜集在说**

这不，刚好瞌睡，就有人送枕头，稍微在github上逛一逛就发现了腾讯开发的[matrix](https://github.com/Tencent/matrix)符合我的性能数据搜集需求：

```html
Matrix-android 当前监控范围包括：应用安装包大小，帧率变化，启动耗时，卡顿，慢方法，SQLite 操作优化，文件读写，内存泄漏等等。

APK Checker: 针对 APK 安装包的分析检测工具，根据一系列设定好的规则，检测 APK 是否存在特定的问题，并输出较为详细的检测结果报告，用于分析排查问题以及版本追踪
Resource Canary: 基于 WeakReference 的特性和 Square Haha 库开发的 Activity 泄漏和 Bitmap 重复创建检测工具
Trace Canary: 监控界面流畅性、启动耗时、页面切换耗时、慢函数及卡顿等问题
SQLite Lint: 按官方最佳实践自动化检测 SQLite 语句的使用质量
IO Canary: 检测文件 IO 问题，包括：文件 IO 监控和 Closeable Leak 监控
```
而且比较愉快的一点是，[matrix](https://github.com/Tencent/matrix)是支持IOS的，这意味着我们双端不用对搜集上来的数据做过多的**归一化处理**。


### 关于上报系统一些想法
当然起初也是想着自己撸一个上报系统，主要要考虑的点有：

1. 可以支持不同的上报策略控制，比如多久上报一次，多少个性能数据上报一次。
2. 万一App crash ，可不能把我们好不容易的数据给丢是了，启动之后得继续上报，嗯，简单的来说就是容灾。

同样的道理，有了上面找收集系统的经验，不妨在github上在逛一逛看有没有符合需求的或基本符合需求的，一找还真找到一个(https://github.com/luojilab/DataReporter)[https://github.com/luojilab/DataReporter]，

```html
1. 跨平台，DataReporter是跨平台实现，适用于Android和iOS。
2. 准实时上报，网络状态好，直接上报，网络状态不好在网络转好后上报，同时上报顺序不乱序。
3. 不丢数据，采用mmap实现缓冲区。即使发生应用crash或者进程被杀，数据不会丢失，下次启动会再次上报。
4. 低功耗，采用c++实现，整个上报共用一个线程，多实例多业务场景共用同一线程，当数据全部上报完成后，线程休眠。
5. 高效率，可配置一次上报数据条数。增加上报效率。有效利用网络。减少链接次数。同时采用native方式实现，性能方面更优秀。
```
看起来也挺符合需求的，而且也是支持IOS的，这就happy了。

### 两个系统拼接一下

嗯，将两个融合在一起，就是这个demo了，demo实现了：

1. 启动就在开始了性能数据的搜集。
2. 一旦有数据收集到，就会使用DataReporter来进行上报，当然真是的上报需要开发者自己实现，比如你可以上报到你们后台。这里的DataReporter可以理解为一个上报中转仓库，起到一个很好的缓冲作用。

最后看看效果：
![](https://github.com/bravekingzhang/statis-report-framwork-android/blob/master/report-test.png)

上图可以看到由matrix搜集的性能数据，交给了DataReporter进行上报了。
