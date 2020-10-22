# Combat Flight Simulator
This is a project to practice ai and driving a vehicle.

## How to get set up

### Install Intellij
Intellij is an Integrated Development Environment (IDE).  Much like a word processor will point out issues with spelling
and common grammatical mistakes, an IDE will tell you about the problems with your code.  You can do this whole project
in any text editor, but I highly recommend getting an IDE to catch these mistakes early.

You can grab the community edition [here](https://www.jetbrains.com/idea/download/#section=windows).
The ultimate edition contains more web frameworks, which we won't be using now, so just get the community.

### Install Git
https://git-scm.com/downloads

Choose the default options for the most part.  Once its running, you should make a directory for code and checkout
 the project there.
 
You will need a github account to get access to this repo.  Sign up using the link in the upper right.  
After thats all set, contact the mentors, so we can get your user added to the team1091 organization.


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


## Math
We are going to use some vector math here.  If you have used points on a cartesian plane in algebra this is similar.

[Vector Math](MATH.md)

## Getting started with the code
For our code, we are going to implement the Pilot interface.  Hit shift twice to bring up a search, then type 'Pilot'.  
Click to open.

It looks like this:
```
interface Pilot {
    fun fly(us: Telemetry, radar: Radar): PilotControl
}
```

To use it, we need to implement it.  Make a new class in com/team1091/fighterai/actor/pilot

```kotlin
class YourNamePilot : Pilot {

    override fun fly(us: Telemetry, radar: Radar): PilotControl {

        // This just flies forward
        return PilotControl()
    }
}
```

Then change the line in Config.kt to have your pilot instead of the sample one
```kotlin
val buildPlayerShip = { SampleKotlinPilot() }
```
