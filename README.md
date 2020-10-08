# Combat Flight Simulator
This is a project to practice ai and driving a vehicle.

## How to get set up

### Install Intellij
Intellij is a Integrated Development Environment (IDE).  Much like a word processor will point out issues with spelling
and common grammatical mistakes, an IDE will tell you about the problems with your code.  You can do this whole project
in any text editor, but I highly recommend getting an IDE to catch these mistakes early.

You can grab the community edition [here](https://www.jetbrains.com/idea/download/#section=windows).
The ultimate edition contains more web frameworks, which we won't be using now, so just get the community.

### Install Git
https://git-scm.com/downloads

Choose the default options for the most part.  Once its running, you should make a directory for code and checkout
 the project there.
 
You will need a github account to get access to this repo.  Contact the mentors and we can get you signed up for that.

Next we need to checkout the project.  This will make a folder called code in your home directory, and download the code there.
```bash
cd ~
mkdir code
cd code
git clone https://github.com/Team1091/fighterai.git
cd fighterai
```
Next, you should start Intellij and file->open->(choose the build.gradle file inside the project)


### Install JVM
I think Intellij comes with a version of java, so you probably can skip this step for now.
https://jdk.java.net/15/


## Programming Languages
We are using Kotlin and Java for the game engine.  I would prefer if you used one of those for this.

https://www.codecademy.com/learn/learn-java

https://kotlinlang.org/docs/tutorials/


## Math Prerequisites
While we will do our best to use libraries to hide the uglier side of the math, you will
still need to have a good concept of what vectors are and how to manipulate them.

### Coordinate Plane
You probably should have some experience dealing with a 2d coordinate plane.  The aircraft will be in a 3d coordinates, 
which works very similar to the 2d ones.

### Vectors
Your and your opponents positions are represented as vectors.  You should try to get a visual representation of what we mean
by adding, subtracting, and scaling vectors.

[Khan Vectors](https://www.khanacademy.org/science/high-school-physics/one-dimensional-motion-2/x2a2d643227022488:physics-foundations/v/introduction-to-vectors-and-scalars?utm_campaign=embed)

[3Blue1Brown Vectors](https://www.youtube.com/watch?v=fNk_zzaMoSs)



### Quaternions
To represent rotations in 3 dimensions, we use a special type of 4 dimensional representation called a quaternion.  

[Overview of Quaternions](https://www.youtube.com/watch?v=d4EgbgTm0Bg)

If you don't understand this, and feel like your brain is melting, that is ok.  It's an advanced topic that is generally 
only covered in computer graphics and robotics.  We will be using a library to handle the math for it.

## Getting started with the code
For our code, we are going to implement the Pilot interface.  Hit shift twice to bring up a search, then type 'Pilot'.  
Click to open.

It looks like this:
```
interface Pilot {
    fun fly(us: Telemetry, radar: Radar): PilotControl
}
```
