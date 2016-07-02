# PersistentChangingAnim
一个可连续重复变化的动画，并且在重复时也能遵循自定义曲线保持速度的连续。
Android中的动画速度的变化，都是有Interpolator来控制的，有加速、减速、加速后减速、匀速等等。但是这只能控制一个周期内速度的变化，若是想做一个循环的、速度持续变化的插值器，系统这个Interpolator并不支持。

这就是这个项目的由来，它能够让一个重复动画的速度持续变化。可以自定义的时间与完成进度的函数，函数曲线的斜率即为瞬时速度。

工程中提供了3个样例：持续加速，持续加速到某一速度后保持不变，持续加速后减速。最后还有一个功能，就是它可以与其他Interpolator结合，一个整体速度变化的控制器和一个单个周期的速度变化器的结合(下面视频中的第二个)。

demo：

<p><a href="https://youtu.be/ZEaH-7IyIs8">https://youtu.be/ZEaH-7IyIs8</a> </p>
<p><a href="https://youtu.be/tIOA6GeFokc">https://youtu.be/tIOA6GeFokc</a> </p>



![image](https://github.com/chopiter/PersistentChangingAnim/blob/master/demo.gif?raw=true)