# pi

Idea of this project was to visualize pi by means of a hilbert polygon

## Results

[![demo1-thumb](src/main/resources/demo1-thumb.png)](src/main/resources/demo1.png)
[![demo1-thumb](src/main/resources/inc-color-hue-long-XL-thumb.png)](src/main/resources/inc-color-hue-long-XL.png)

# Length of a hilbert polygon

```
+---------+---------------------+
| depth   | lengh               |
+---------+---------------------+
|       1 |                    3|
|       2 |                   15|
|       3 |                   63|
|       4 |                  255|
|       5 |                1.023|
|       6 |                4.095|
|       7 |               16.383|
|       8 |               65.535|
|       9 |              262.143|
|      10 |            1.048.575|
|      11 |            4.194.303|
|      12 |           16.777.215|
|      13 |           67.108.863|
|      14 |          268.435.455|
|      15 |        1.073.741.823|
|      16 |        4.294.967.295|
|      17 |       17.179.869.183|
|      18 |       68.719.476.735|
|      19 |      274.877.906.943|
|      20 |    1.099.511.627.775|
|      21 |    4.398.046.511.103|
|      22 |   17.592.186.044.415|
|      23 |   70.368.744.177.663|
+---------+---------------------+
```
# Resources for math constants

https://archive.org/details/Math_Constants

## Performance of reading digits from text files.

```
  100,000,000 - 12s
1,000,000,000 - 92s
```
# Minimum resolution to show a hilbert curve.

Minimum size of the canvas for a polygon of depth n is

len = pow(2, n + 1) + 1
