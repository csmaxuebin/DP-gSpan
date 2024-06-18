# This code is the source code implementation for the paper "DP-gSpan: A Pattern Growth-based Differentially Private Frequent Subgraph Mining Algorithm."



# Abstract
Frequent subgraph mining has been one of the researches focuses in the field of data mining, which plays an important role in understanding social interaction mechanisms, urban planning, and studying the spread of diseases in social networks. Frequent subgraph mining provides a lot of valuable information. However, mining and publishing frequent subgraphs brings more and more risks of privacy leakage. In order to solve the problem of privacy leakage, the combination of frequent subgraph mining based on Apriori and differential privacy has become the mainstream method. Nevertheless, most of the existing research studies suffer from too large candidate subgraph sets, low accuracy, and low efficiency. Therefore, we propose a more secure and effective depth-first search algorithm for frequent subgraph mining under differential privacy, which is referred to as DP-gSpan. We design a heuristic truncation strategy and a new privacy budget allocation strategy to realize the reduction of the candidate set size and the rational allocation of the privacy budget. Through privacy analysis, we prove that DP-gSpan satisfies ε-differential privacy. Experimental results over a large number of real-world datasets prove that the performance of the proposed mechanism is better.


# Experimental Environment

```
Operating Environment:Java with Intel Core i5 CPU 1.38 GHz and 16 GB RAM, running Windows.
```

# Datasets

- **Cancer Dataset**: Includes structures of human tumor cell lines.
- **HIV Dataset**: Contains molecules tested against the HIV virus.

# Experimental Setup

### Experimental Purpose and Methods
This paper compares the DP-gSpan algorithm with two other differential privacy-based frequent subgraph mining algorithms proposed in recent years:
1. **DFS algorithm (DFG)**: Proposed by Cheng et al., this algorithm also consists of two phases.
2. **Algorithm based on Markov Chain Monte Carlo (MCMC) sampling (DFPM)**: Proposed by Shen et al., this is used to realize frequent graph pattern mining under differential privacy.

### Experimental Environment
- All algorithms were implemented in Java.
- Hardware configuration: Intel Core i5 CPU at 1.38 GHz and 16 GB RAM.
- Operating system: Windows 10.

### Experimental Parameter Settings
- **Fixed Privacy Budget**: The privacy budget, ε, was fixed at 0.2 for all experiments.
- Specific allocations of the privacy budget were as follows: ε1 = 0.01ε, ε2 = 0.5ε, and ε3 = 0.49ε.
- To address the randomness inherent in differential privacy protection algorithms, each experiment was performed 10 times, with the average of all results taken as the final outcome.

# Java Files

1. **Main.java**:
   - This file serves as the main entry point of a Java application. It initializes and coordinates the execution of different modules or functionalities provided in the rest of the application.

2. **AlgorithmZ.java**:
   - Another algorithm implementation, distinct from AlgorithmD. This file contains methods and logic for a different set of operations or calculations, tailored to particular requirements or data types.

3. **BottomKSketch.java**:
   - Contains an implementation of a sketching algorithm that approximates the bottom 'k' elements in a dataset. It is typically used in scenarios where processing large datasets efficiently is required, focusing on elements with minimal values.

4. **EdgeHandler.java**:
   - Manages edge operations in graph data structures, including the addition and deletion of edges. This file is particularly useful in applications involving dynamic graph algorithms or real-time processing of graph-based data.

5. **ReservoirSampling.java**:
   - Implements the Reservoir Sampling algorithm, which is used to randomly select a sample of 'k' items from a stream of data of unknown size. It's useful in data streaming scenarios where memory efficiency is critical.


6. **UniformHasher.java**:
   - Implements hashing functionalities, utilizing a uniform hashing strategy to distribute input values evenly. This hasher could be employed in hash tables, load balancing algorithms, or for ensuring data distribution uniformity.


#  Experimental Results

Fig.4 and Fig.5 show the performance of DP-gSpan, DFG and DFPM on Cancer HIV under different thresholds.
The performance of the three algorithms for mining top 50 frequent subgraphs under varying privacy budgets on datasets Cancer and HIV is shown in Fig.6 and Fig.7, and the results of privacy budgets between 0.1 and 0.5.
Fig.8 and Fig.9 show the performance of DP-gSpan, DFG, and DFPM in the Top-k frequent subgraph mining experiment with different k values.
Fig.10 shows the performance of DP-gSpan and gSpan in the Top-k frequent subgraph mining experiment with different k values.

![输入图片说明](https://github.com/csmaxuebin/DP-gSpan/blob/main/pic/fig4-6.jpg)
![输入图片说明](https://github.com/csmaxuebin/DP-gSpan/blob/main/pic/fig7-8.jpg)
![输入图片说明](https://github.com/csmaxuebin/DP-gSpan/blob/main/pic/fig9-10.jpg)








## Update log

```
- {24.06.15} Uploaded overall framework code and readme file
```

