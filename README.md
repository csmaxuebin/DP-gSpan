# DP-gSpan

算法1

本代码是论文<DP-gSpan: A Pattern Growth-based Differentially Private Frequent Subgraph Mining Algorithm>的源码实现。

本代码将频繁子图挖掘算法与差分隐私结合。（数据挖掘源代码来源于2018 Tony Zhu）

差分隐私主要添加到该算法的挖掘频繁子图的过程中及输出相应的噪声支持度过程中，可以依据需要分配不同的隐私预算。

运行环境：Java with Intel Core i5 CPU 1.38 GHz and 16 GB RAM, running Windows 1。

gSpan函数是主函数调用的函数，主要功能是隐私挖掘频繁子图。

为了测量误差及准确率增加了对RE及F-score指标的计算函数。

DFSCode函数是对图的五元组DFS编码。

Graph函数是读取已经处理的输入的图数据。

Misc函数是输入数据挖掘频繁子图函数中需要调用的探索新边的函数。

代码运行过程:

![image](https://user-images.githubusercontent.com/104848157/170817725-94c6012a-f6ba-44c9-b031-6304e5a40c0c.png)

test是需要输入用于测试的样本

minimun support是需要输入的最小支持度

test_result是输出测试结果，结果将输出到test_result当中

F-SCORE表示测试的准确度
