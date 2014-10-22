GravBox LiBGDX Demo 
============
This is a simple demo application that uses a variety of LibGDX tools to create a simple ball-physics scene.

Tilt your Android phone and the balls will roll around the screen in a somewhat realistic fashion.

The application functions on Android (and probably iOS if you can compile for that platform). Although there is no accelerometer in Desktop/Web, it will run if you enter your own gravity values in the world.setRender call inside the GravBox.render method.

The application is a useful starting point to understand the following LibGDX elements:

Scene2d Stages/Actors
---------------------
The app uses a simple demo of the stage/actor relationship. The world is the stage, and the balls extend the Actor class. This Actor encapsulates the sprite, drawing and physics for a ball item, nicely demonstrating how Actors are useful for keeping code tidy. Note you might extend this further and use a full Entity System like Ashley for LibGDX (https://github.com/libgdx/ashley)

Box2d
-----
The app creates a simple Box2d world, the same size as the screen. Edge bodies are created to enclose the screen area to stop balls escaping. Balls are defined as Box2d CircleShapes placed in the world. Note the Box2d ball bodies are created during the Ball Actor constructor. 
The world physics is updated in the GravBox.render method.

Accelerometer
-------------
The app uses the accelerometer on the Android device to set the Box2d world gravity. Note that the world is oriented to be in portrait mode and you might end up with unusual physics in other modes/platforms.

License
-------
This code and application is presented as-is, without warranty or liability, under the Apache 2.0 License: http://www.apache.org/licenses/LICENSE-2.0.html
