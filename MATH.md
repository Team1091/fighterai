# Math
While we will do our best to use libraries to hide the uglier side of the math, you will
still need to have a good concept of what vectors are and how to manipulate them.

## Tensors
Tensors are a multi-dimentional array or grid of numbers.

### Scalers
A scaler is just a number.  1, 2, 3/2, 232.123, -2, and PI are all scalers.
We have a few different types on the JVM, but for this project most of what you will see are Floats.
```kotlin
   val speed = 45f
   val distance:Float
```


### Cartesian Coordinate Plane
You probably should have some experience dealing with a 2d coordinate plane.  The aircraft will be in a 3d coordinates, 
which works very similar to the 2d ones, you just also have a height.


### Vectors
Your and your opponents positions are represented as vectors.

You should try to get a visual representation of what we mean by adding, subtracting, and scaling vectors.

[Khan Vectors](https://www.khanacademy.org/science/high-school-physics/one-dimensional-motion-2/x2a2d643227022488:physics-foundations/v/introduction-to-vectors-and-scalars?utm_campaign=embed)

[3Blue1Brown Vectors](https://www.youtube.com/watch?v=fNk_zzaMoSs)

**Add**

**Subtract**

**Magnitude** 

The length of a vector. Can be calculated with .len()
```kotlin
    val position = Vector3(2f, 1f, 3f)
    val distanceFromOrigin = position.len()
```

**Distance** 

The distance between two vectors.
```kotlin
    val distanceToEnemy = us.position.dst(enemy.position)
```

**Scale** 

Multiplies the vector by a number, making it bigger or smaller.
```kotlin
    // get a position behind by 30 units
    val behind = backwards.cpy().scl(30f)
```

**Normalize**

Scale the vector to length 1.  This can be used to create a unit sphere.
```kotlin
    val vector = Vector3(2f, 2f, 0f)
    vector.nor() 
    // 0.7071f, 0.7071f, 0f
```

**Cross Product**

Cross product can be used to get a right angle to two existing vectors.
[Cross Product](https://www.mathsisfun.com/algebra/vectors-cross-product.html)
```kotlin
    val a = Vector3(1f, 0f, 0f)
    val b = Vector3(0f, 1f, 0f)
    val c = a.cpy().crs(b)     
    println(c) // (0.0, 0.0, 1.0)
```

**Dot Product**

[Dot Product](https://www.mathsisfun.com/algebra/vectors-dot-product.html)


### Quaternions
To represent rotations in 3 dimensions, we use a special type of 4 dimensional representation called a quaternion.  

[Overview of Quaternions](https://www.youtube.com/watch?v=d4EgbgTm0Bg)

If you don't understand this, and feel like your brain is melting, that is ok.  It's an advanced topic that is generally 
only covered in computer graphics and robotics.  We will be using a library to handle the math for it.
